package app;

import entity.Action;
import helpers.ServiceHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import recommendation.RecommendationEngine;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchRestServices {

    @RequestMapping("/searchContext")
    public Object searchTag(
            @RequestParam(value = "tag") String tag, HttpServletResponse resp) {
        ServiceHelper.setResponseHeaders(resp);
        return RecommendationEngine.findRelatedTags(tag);
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
