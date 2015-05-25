package repository;

import helpers.ServiceHelper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;

import common.BusinessException;
import common.ErrorMessages;
import common.ResponseHeader;
import dto.ActionDTO;
import dto.ActionMemberDTO;
import dto.NewsFeedDTO;
import entity.Action;
import entity.Comment;
import entity.CustomType;
import entity.Friendship;
import entity.Post;
import entity.Role;
import entity.Session;
import entity.User;
import enums.ActionType;
import enums.ActionUserStatus;
import enums.FeedType;

public class UserRepository {

	EntityManager em;

	public UserRepository(EntityManager em) {
		this.em = em;
	}

	public int getUserByUserNumberEmail(String userEmail) {
		List<User> users = em
				.createQuery("FROM User U WHERE U.userEmail = :userEmail")
				.setParameter("userEmail", userEmail).getResultList();

		if (users == null)
			return 0;
		return users.size();
	}

	public ResponseHeader login(String userEmail, String password)
			throws BusinessException {
		List<User> result = em
				.createQuery(
						"FROM User U WHERE U.userEmail = :userEmail AND U.password = :password")
				.setParameter("userEmail", userEmail)
				.setParameter("password", password).getResultList();
		if (result != null && result.size() > 0) {

			String sessionId = new BigInteger(130, new Random()).toString(32)
					.toUpperCase();

			em.persist(new Session(result.get(0), sessionId));

			return new ResponseHeader(sessionId);
		} else {
			ServiceHelper.businessError(ErrorMessages.invalidLoginCode,
					ErrorMessages.invalidLogin);
			return new ResponseHeader();
		}
	}

	public Role getRoleByName(String role) {
		if (role.isEmpty())
			return null;
		Role roleDb;
		try {
			roleDb = (Role) em.createQuery("FROM Role R WHERE R.name = :name")
					.setParameter("name", role).getSingleResult();
		} catch (Exception e) {
			return null;
		}
		return roleDb;
	}

	public void insertUser(User user) {
		// User user1 = em.merge(user);
		// em.merge(user1.getUserBasicInfo());
		// em.merge(user1.getUserCommInfo());
		// em.merge(user1.getUserExtraInfo());
		// em.merge(user1.getRole());
		em.persist(user);
	}

	public void insertRole(Role role) {
		em.persist(role);
	}

	public User getUserById(Long userId) {
		User user = (User) em
				.createQuery("FROM User U WHERE U.userId = :userId")
				.setParameter("userId", userId).getSingleResult();
		return user;
	}

	public User getUserByEmail(String userEmail) {
		User user = (User) em
				.createQuery("FROM User U WHERE U.userName = :userName")
				.setParameter("userName", userEmail).getSingleResult();
		return user;
	}

	public List<NewsFeedDTO> getNewsFeed(User user) {
		List<NewsFeedDTO> feeds = new ArrayList<NewsFeedDTO>();
		if (user == null)
			return null;
		
		FriendshipRepository fr = new FriendshipRepository(em);
		List<Friendship> friendships = fr.prepareFriendsForUser(user);
		
		ActionRepository ar = new ActionRepository(em);
		List<ActionMemberDTO> newGroups = ar.getNewActions(ActionType.GROUP.toString());
		List<ActionMemberDTO> newEvents = ar.getNewActions(ActionType.EVENT.toString());
		
		//get one of friends become member of a group or event
		for (Friendship friendship : friendships){
			User friend = friendship.getFriend();
			getNewsFeedByAction(feeds, newGroups, friend);
			getNewsFeedByAction(feeds, newEvents, friend);
		}
		
		//get if a user adds a post in my group or event
		List<ActionDTO> myCreatedGroups = ar.prepareCreatedActionForUser(user, ActionType.GROUP.toString());
		List<ActionDTO> myCreatedEvents = ar.prepareCreatedActionForUser(user, ActionType.EVENT.toString());
		createNewsFeedForPosts(feeds, ar, myCreatedGroups, ActionType.GROUP.toString());
		createNewsFeedForPosts(feeds, ar, myCreatedEvents, ActionType.EVENT.toString());
		
		//get if a user adds a comment to my post
		CustomTypeRepository cr = new CustomTypeRepository(em);
		List<Post> posts = cr.getPostListByUser(user);
		
		for (Post post : posts){
			List<Comment> newComments = cr.getNewCommentsByPost(post);
			for(Comment comment : newComments){
				NewsFeedDTO newsFeedDTO = new NewsFeedDTO();
				newsFeedDTO.setUser(comment.getUser());
				newsFeedDTO.setAction(comment.getPost().getCustomType().getAction());
				newsFeedDTO.setFeedType(FeedType.COMMENT);
				newsFeedDTO.setFeedId(comment.getCommentId());
				feeds.add(newsFeedDTO);
			}
		}
		
		
		return feeds;
	}

	private void createNewsFeedForPosts(List<NewsFeedDTO> feeds,
			ActionRepository ar, List<ActionDTO> myCreatedGroups, String actionType) {
		CustomTypeRepository ctr = new CustomTypeRepository(em);
		for (ActionDTO actionDTO : myCreatedGroups){
			Action action = ar.getActionById(actionDTO.getActionId(), actionType); 
			List<CustomType> customTypes = new ArrayList<CustomType>();
			customTypes.addAll(action.getCustomTypes());
			for (CustomType customType : customTypes){
				List<Post> posts =  new ArrayList<Post>();
				posts.addAll(customType.getPosts());
				List<Post> newPosts = ctr.getNewPostsByCustomTypeId(customType.getCustomTypeId());;
				for (Post post : posts){
					if (newPosts.contains(post)){
						NewsFeedDTO newsFeedDTO = new NewsFeedDTO();
						newsFeedDTO.setUser(post.getUser());
						newsFeedDTO.setAction(action);
						newsFeedDTO.setFeedType(FeedType.POST);
						newsFeedDTO.setFeedId(post.getPostId());
						feeds.add(newsFeedDTO);
					}
				}
			}
			
		}
	}

	private void getNewsFeedByAction(List<NewsFeedDTO> feeds,
			List<ActionMemberDTO> newGroups, User friend) {
		for (ActionMemberDTO actionMemberDTO : newGroups){
			if (actionMemberDTO.getUsers().contains(friend)){
				MembersRepository mr = new MembersRepository(em);
				Action action = actionMemberDTO.getAction();
				if (mr.checkUserIsMemberOfAction(action, friend, ActionUserStatus.ADMIN) ||
					mr.checkUserIsMemberOfAction(action, friend, ActionUserStatus.CREATED) ||	
					mr.checkUserIsMemberOfAction(action, friend, ActionUserStatus.MEMBER)){ 
					createNewsFeed(feeds, friend, actionMemberDTO);
				}
			}
		}
	}

	private void createNewsFeed(List<NewsFeedDTO> feeds, User friend,
			ActionMemberDTO actionMemberDTO) {
		NewsFeedDTO newsFeedDTO = new NewsFeedDTO();
		newsFeedDTO.setUser(friend);
		newsFeedDTO.setAction(actionMemberDTO.getAction());
		newsFeedDTO.setFeedType(FeedType.MEMBER);
		feeds.add(newsFeedDTO);
	}
}
