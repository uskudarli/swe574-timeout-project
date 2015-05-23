package recommendation;

import common.DBUtility;
import entity.Action;
import entity.ActionRecommendation;
import entity.User;
import entity.UserRecommendation;
import enums.ActionType;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RecommendationEngine {

    public static void insertRecommendationsforUser(
            User user) {

        deleteCurrentRecommendationsOfUser(user);
        EntityManager em = DBUtility.startTransaction();

        List<String> semanticTagsofUser = findAllSemanticTagsOfUser(user);
        if(semanticTagsofUser != null && semanticTagsofUser.size() > 0) {
            List<Action> recommendedEvents = findSemanticEventsWithGivenTagId(semanticTagsofUser, user);
            List<Action> recommendedGroups = findSemanticGroupsWithGivenTagId(semanticTagsofUser, user);
            List<User> recommendedUsers = findSemanticUsersWithGivenTagIds(semanticTagsofUser, user);

            for (Action event : recommendedEvents) {
                ActionRecommendation eventRecommendation = new ActionRecommendation();
//            Action action = ActionRepository.getActionById(event.getActionId(), ActionType.EVENT.toString());
                eventRecommendation.setAction(event);
                eventRecommendation.setUser(user);
                em.persist(eventRecommendation);
            }
            for (Action group : recommendedGroups) {
                ActionRecommendation groupRecommendation = new ActionRecommendation();
//            Action action = ActionRepository.getActionById(group.getActionId(), ActionType.EVENT.toString());
                groupRecommendation.setAction(group);
                groupRecommendation.setUser(user);
                em.persist(groupRecommendation);
            }
            for (User recommendedUser : recommendedUsers) {
                UserRecommendation userRecommendation = new UserRecommendation();
                userRecommendation.setUserRecommended1(user);
                userRecommendation.setUserRecommended2(recommendedUser);
                em.persist(userRecommendation);
            }
        }

        DBUtility.commitTransaction(em);
    }

    public static void deleteCurrentRecommendationsOfUser (User user){
        EntityManager em = DBUtility.startTransaction();
        em.createQuery("DELETE  FROM ActionRecommendation WHERE user = :user")
                .setParameter("user", user).executeUpdate();
        em.createQuery("DELETE FROM UserRecommendation WHERE userRecommended1 = :user")
                .setParameter("user", user).executeUpdate();
        DBUtility.commitTransaction(em);

    }
    public static List<String> findAllSemanticTagsOfUser(User user) {
        List<String> tagsOfUser = getActionTagsofUser(user);
        List<String> semanticTagsofUser = new ArrayList<>();
        for (String tag : tagsOfUser) {
            ArrayList<String> semanticTagResults = executeSemanticQuery(tag);
            semanticTagsofUser.addAll(semanticTagResults);
        }
        return semanticTagsofUser;
    }

    public static List<String> getActionTagsofUser(User user) {
        EntityManager em = DBUtility.startTransaction();

        Query query = em.createQuery("SELECT At.tag.contextId FROM ActionTag At WHERE At.action IN " +
                "(SELECT  A FROM Action A WHERE A IN" +
                "(SELECT  Au.action FROM ActionUser Au WHERE Au.user = :user))")
                .setParameter("user", user);
        List<String> queryResult = query.getResultList();

        DBUtility.commitTransaction(em);

        return queryResult;
    }
    

    public static ArrayList<Action> findSemanticEventsWithGivenTagId(List<String> semanticTagIds, User user) {
        return findSemanticActionsWithGivenTagIds(semanticTagIds, user, ActionType.EVENT);
    }

    public static ArrayList<Action> findSemanticGroupsWithGivenTagId(List<String> semanticTagIds, User user) {
        return findSemanticActionsWithGivenTagIds(semanticTagIds, user, ActionType.GROUP);
    }

    public static ArrayList<Action> searchSemanticEventsWithGivenTagId(String semanticTagId) {
        return searchSemanticActionsWithGivenTagIds(semanticTagId, ActionType.EVENT);
    }

    public static ArrayList<Action> searchSemanticGroupsWithGivenTagId(String semanticTagId) {
        return searchSemanticActionsWithGivenTagIds(semanticTagId, ActionType.GROUP);
    }

    public static ArrayList<Action> searchEventsWithGivenKeyword(String keyword) {
        return searchActionsWithGivenKeyword(keyword, ActionType.EVENT);
    }

    public static ArrayList<Action> searchGroupWithGivenKeyword(String keyword) {
        return searchActionsWithGivenKeyword(keyword, ActionType.GROUP);
    }

    public static ArrayList<Action> findSemanticActionsWithGivenTagIds(List<String> semanticTagIds,  User user, ActionType actionType) {
        EntityManager em = DBUtility.startTransaction();

        Query query = em
                .createQuery(
                        "SELECT A FROM Action A INNER JOIN A.actionTags tags WHERE tags.tag.contextId " +
                                "IN (:relatedTags) AND A NOT IN (SELECT Au.action FROM ActionUser Au WHERE Au.user = :user)AND A.actionType = :actionType ")
                .setParameter("actionType", actionType.toString())
                .setParameter("relatedTags", semanticTagIds)
                .setParameter("user", user);

        ArrayList<Action> relatedActions = (ArrayList<Action>)query.getResultList();

        DBUtility.commitTransaction(em);

        return relatedActions;
    }

    public static ArrayList<Action> searchSemanticActionsWithGivenTagIds(String semanticTagId,  ActionType actionType) {
        EntityManager em = DBUtility.startTransaction();

        Query query = em
                .createQuery(
                        "SELECT A FROM Action A INNER JOIN A.actionTags tags WHERE tags.tag.contextId " +
                                "IN (:relatedTags) AND A.actionType = :actionType ")
                .setParameter("actionType", actionType.toString())
                .setParameter("relatedTags", semanticTagId);

        ArrayList<Action> relatedActions = (ArrayList<Action>)query.getResultList();

        DBUtility.commitTransaction(em);

        return relatedActions;
    }

    public static ArrayList<User> findSemanticUsersWithGivenTagIds(List<String> semanticTagIds, User user) {
        EntityManager em = DBUtility.startTransaction();

        Query query = em
                .createQuery(
                        "SELECT U FROM User U WHERE U IN" +
                                "(SELECT Au.user FROM ActionUser Au WHERE Au.action IN" +
                                "(SELECT A FROM Action A INNER JOIN A.actionTags tags " +
                                "WHERE tags.tag.contextId IN (:relatedTags))) AND U NOT IN " +
                                "(SELECT U2 FROM User U2 WHERE U2 IN " +
                                "(SELECT F.friend FROM Friendship F WHERE F.person = (:user))) AND U NOT IN (:user)")
                .setParameter("relatedTags", semanticTagIds)
                .setParameter("user", user);

        ArrayList<User> relatedActions = (ArrayList<User>)query.getResultList();

        DBUtility.commitTransaction(em);

        return relatedActions;
    }

    public static ArrayList<User> searchSemanticUsersWithGivenTagIds(String semanticTagId) {
        EntityManager em = DBUtility.startTransaction();

        Query query = em
                .createQuery(
                        "SELECT U FROM User U WHERE U IN" +
                                "(SELECT Au.user FROM ActionUser Au WHERE Au.action IN" +
                                "(SELECT A FROM Action A INNER JOIN A.actionTags tags " +
                                "WHERE tags.tag.contextId IN (:relatedTags)))")
                .setParameter("relatedTags", semanticTagId);

        ArrayList<User> relatedActions = (ArrayList<User>)query.getResultList();

        DBUtility.commitTransaction(em);

        return relatedActions;
    }

    public static ArrayList<User> searchUsersWithGivenKeyword(String keyword) {
        EntityManager em = DBUtility.startTransaction();

        Query query = em
                .createQuery(
                        "SELECT U FROM User U WHERE U.userEmail LIKE ('%' || (:keyword) || '%')")
                .setParameter("keyword", keyword);

        ArrayList<User> relatedActions = (ArrayList<User>)query.getResultList();

        DBUtility.commitTransaction(em);

        return relatedActions;
    }

    public static ArrayList<Action> searchActionsWithGivenKeyword(String keyword, ActionType actionType) {
        EntityManager em = DBUtility.startTransaction();

        Query query = em
                .createQuery(
                        "SELECT A FROM Action A WHERE A.title LIKE ('%' || (:keyword) || '%') " +
                                "OR A.description LIKE ('%' || (:keyword) || '%') AND A.actionType = :actionType")
                .setParameter("actionType", actionType.toString())
                .setParameter("keyword",  keyword );

        ArrayList<Action> relatedActions = (ArrayList<Action>)query.getResultList();

        DBUtility.commitTransaction(em);

        return relatedActions;
    }


    public static Object findRelatedTags(String tag) {
        RestTemplate restTemplate = new RestTemplate();

        String getItemIdUrl = "https://www.wikidata.org/w/api.php?action=wbsearchentities&search="
                + tag + "&format=json&language=en";

        Object getResult = restTemplate
                .getForObject(getItemIdUrl, Object.class);
        Object obj = ((HashMap) getResult).get("search");

        return obj;
    }

    public static ArrayList<String> findSemanticTag(String tag) {
        return findItemNamesofIds(executeSemanticQuery(findTagId(tag)));
    }


    private static String findTagId(String tag) {
        RestTemplate restTemplate = new RestTemplate();

        String itemId = null;
        try {
            String getItemIdUrl =
                    "https://www.wikidata.org/w/api.php?action=wbgetentities&sites=enwiki&titles="
                            + tag + "&normalize=&format=json";
            Object getItemIdResponse = restTemplate.getForObject(getItemIdUrl,
                    Object.class);
            itemId = ((HashMap) Arrays
                    .asList(((HashMap) ((HashMap) getItemIdResponse)
                            .get("entities")).values()).get(0).iterator().next())
                    .get("id").toString();
        } catch (Exception e) {
        }

        return itemId;
    }

    public static ArrayList<String> executeSemanticQuery(String itemId) {
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<String> searchTagResultListinString = new ArrayList<>();

        String searchTagQueryUrl = "https://wdq.wmflabs.org/api?q=tree["
                + itemId.substring(1) + "][279,366]";
        Object searchTagQueryResponse = restTemplate.getForObject(
                searchTagQueryUrl, Object.class);
        ArrayList<Integer> searchTagResultList = (ArrayList<Integer>) ((HashMap)
                searchTagQueryResponse)
                .get("items");

        for (Integer s : searchTagResultList) {
            searchTagResultListinString.add("Q" + s.toString());
        }

        return searchTagResultListinString;
    }

    private static ArrayList<String> findItemNamesofIds(ArrayList<String> searchTagResultList) {
        ArrayList<String> searchTagResults = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        for (String searchTagResult : searchTagResultList) {
            try {
                String getItemNameUrl =
                        "https://www.wikidata.org/w/api.php?action=wbgetentities&ids="
                                + searchTagResult
                                + "&props=labels&languages=en&format=json";
                Object getItemNameResponse = restTemplate.getForObject(
                        getItemNameUrl, Object.class);
                String item = ((HashMap) (Arrays
                        .asList(((HashMap) ((HashMap) Arrays
                                .asList(((HashMap) ((HashMap) getItemNameResponse)
                                        .get("entities")).values()).get(0)
                                .iterator().next()).get("labels")).values()).get(0)
                        .iterator().next())).get("value").toString();
                searchTagResults.add(item);
            } catch (Exception e) {
            }
        }
        return searchTagResults;
    }


}
