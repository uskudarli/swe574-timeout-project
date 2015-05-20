package demo;

import helpers.ServiceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import common.DBUtility;

import entity.Action;
import enums.ActionType;

@RestController
public class SearchRestServices {
	@RequestMapping("/searchContext")
	public Object searchTag(
			@RequestParam(value = "tag") String tag, HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		return findRelatedTags(tag);
	}

	@RequestMapping("/findRelatedGroupsforTag")
	public List<Action> findRelatedGroupsforTag(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "tag") String tag, HttpServletResponse resp) {

		ServiceHelper.setResponseHeaders(resp);

		EntityManager em = DBUtility.startTransaction();

		Object relatedTags = findRelatedTags(tag);

		ArrayList<Action> relatedGroups = new ArrayList<>();

		Query query = em
				.createQuery(
						"FROM Action A INNER JOIN A.actionTags tags WHERE tags.tag.tagName IN (:relatedTags) AND A.actionType = :actionType")
				.setParameter("actionType", ActionType.GROUP.toString())
				.setParameter("relatedTags", relatedTags);

		List<Object[]> queryResult = query.getResultList();
		for (Object[] o : queryResult) {
			relatedGroups.add((Action) o[0]);
		}
		return relatedGroups;
	}

	// private ArrayList<String> findRelatedTags(String tag) {
	// RestTemplate restTemplate = new RestTemplate();
	//
	// String getItemIdUrl =
	// "https://www.wikidata.org/w/api.php?action=wbgetentities&sites=enwiki&titles="
	// + tag + "&normalize=&format=json";
	// Object getItemIdResponse = restTemplate.getForObject(getItemIdUrl,
	// Object.class);
	// String itemId = ((HashMap) Arrays
	// .asList(((HashMap) ((HashMap) getItemIdResponse)
	// .get("entities")).values()).get(0).iterator().next())
	// .get("id").toString();
	//
	// String searchTagQueryUrl = "https://wdq.wmflabs.org/api?q=tree["
	// + itemId.substring(1) + "][31] OR tree[" + itemId.substring(1)
	// + "][279]";
	// Object searchTagQueryResponse = restTemplate.getForObject(
	// searchTagQueryUrl, Object.class);
	// ArrayList<Integer> searchTagResultList = (ArrayList<Integer>) ((HashMap)
	// searchTagQueryResponse)
	// .get("items");
	//
	// ArrayList<String> searchTagResults = new ArrayList<>();
	//
	// for (Integer searchTagResult : searchTagResultList) {
	// String getItemNameUrl =
	// "https://www.wikidata.org/w/api.php?action=wbgetentities&ids=Q"
	// + searchTagResult
	// + "&props=labels&languages=en&format=json";
	// Object getItemNameResponse = restTemplate.getForObject(
	// getItemNameUrl, Object.class);
	// String item = ((HashMap) (Arrays
	// .asList(((HashMap) ((HashMap) Arrays
	// .asList(((HashMap) ((HashMap) getItemNameResponse)
	// .get("entities")).values()).get(0)
	// .iterator().next()).get("labels")).values()).get(0)
	// .iterator().next())).get("value").toString();
	// searchTagResults.add(item);
	// }
	// return searchTagResults;
	// }

	private Object findRelatedTags(String tag) {
		RestTemplate restTemplate = new RestTemplate();

		String getItemIdUrl = "https://www.wikidata.org/w/api.php?action=wbsearchentities&search="
				+ tag + "&format=json&language=en";

		Object getResult = restTemplate
				.getForObject(getItemIdUrl, Object.class);
		Object obj = ((HashMap) getResult).get("search");

		return obj;
	}
}
