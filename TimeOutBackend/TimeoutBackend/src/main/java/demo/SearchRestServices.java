package demo;

import entity.Action;
import entity.User;
import helpers.ServiceHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import recommendation.RecommendationEngine;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class SearchRestServices {

    @RequestMapping("/searchContext")
    public Object searchTag(
            @RequestParam(value = "tag") String tag, HttpServletResponse resp) {
        ServiceHelper.setResponseHeaders(resp);
        return RecommendationEngine.findRelatedTags(tag);
    }

    @RequestMapping("/find")
    public Object find(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "contextId", required = false) String contextId,
            HttpServletResponse resp) {
        ServiceHelper.setResponseHeaders(resp);

        List<Action> recommendedEvents;
        List<Action> recommendedGroups;
        List<User> recommendedUsers;

        if(keyword != null){
            recommendedEvents = RecommendationEngine.searchEventsWithGivenKeyword(keyword);
            recommendedGroups = RecommendationEngine.searchGroupWithGivenKeyword(keyword);
            recommendedUsers = RecommendationEngine.searchUsersWithGivenKeyword(keyword);
        } else {
            recommendedEvents = RecommendationEngine.searchSemanticEventsWithGivenTagId(contextId);
            recommendedGroups = RecommendationEngine.searchSemanticGroupsWithGivenTagId(contextId);
            recommendedUsers = RecommendationEngine.searchSemanticUsersWithGivenTagIds(contextId);
        }

        HashMap<String, Object > searchResults = new HashMap<>();
        searchResults.put("events", recommendedEvents);
        searchResults.put("groups", recommendedGroups);
        searchResults.put("users", recommendedUsers);

        return searchResults;
    }

    @RequestMapping("/semanticSearch")
    public Object semanticSearch(
            @RequestParam(value = "tag") String tag, HttpServletResponse resp) {
        ServiceHelper.setResponseHeaders(resp);
        return findSemanticTag(tag);
    }

    @RequestMapping("/findRelatedGroupsforTag")
    public List<Action> findRelatedGroupsforTag(
            @RequestParam(value = "sessionId") String sessionId,
            @RequestParam(value = "tag") String tag, HttpServletResponse resp) {

        ServiceHelper.setResponseHeaders(resp);

//        return RecommendationEngine.;
        return null;
    }

    @RequestMapping("/findSemanticTag")
    private ArrayList<String> findSemanticTag(String tag) {
        return RecommendationEngine.findSemanticTag(tag);
    }








}
