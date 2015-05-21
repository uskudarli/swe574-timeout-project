package recommendation;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RecommendationEngine {

//    public void insertRecommendations(
//            String userId) {
//
//        ServiceHelper.setResponseHeaders(resp);
//
//        EntityManager em = DBUtility.startTransaction();
//
//        ArrayList<String> tagsOfUser = new ArrayList<>();
//
//
//
//        Object relatedTags = findRelatedTags(tag);
//
//        ArrayList<Action> relatedGroups = new ArrayList<>();
//
//        Query query = em
//                .createQuery(
//                        "FROM Action A INNER JOIN A.actionTags tags WHERE tags.tag.tagName IN (:relatedTags) AND A.actionType = :actionType")
//                .setParameter("actionType", ActionType.GROUP.toString())
//                .setParameter("relatedTags", relatedTags);
//
//        List<Object[]> queryResult = query.getResultList();
//        for (Object[] o : queryResult) {
//            relatedGroups.add((Action) o[0]);
//        }
//    }



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

    public static ArrayList<Integer> executeSemanticQuery(String itemId){
        RestTemplate restTemplate = new RestTemplate();

        String searchTagQueryUrl = "https://wdq.wmflabs.org/api?q=tree["
                + itemId.substring(1) + "][279,366]";
        Object searchTagQueryResponse = restTemplate.getForObject(
                searchTagQueryUrl, Object.class);
        ArrayList<Integer> searchTagResultList = (ArrayList<Integer>) ((HashMap)
                searchTagQueryResponse)
                .get("items");

        return searchTagResultList;

    }

    private static ArrayList<String> findItemNamesofIds(ArrayList<Integer> searchTagResultList){
        ArrayList<String> searchTagResults = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        for (Integer searchTagResult : searchTagResultList) {
            try {
                String getItemNameUrl =
                        "https://www.wikidata.org/w/api.php?action=wbgetentities&ids=Q"
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
