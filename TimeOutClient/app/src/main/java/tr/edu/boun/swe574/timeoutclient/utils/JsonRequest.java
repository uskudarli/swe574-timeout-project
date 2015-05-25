package tr.edu.boun.swe574.timeoutclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import tr.edu.boun.swe574.timeoutclient.jsonObjects.CommonReturnObject;
import tr.edu.boun.swe574.timeoutclient.jsonObjects.Tag;

/**
 * Created by haluks on 19/04/15.
 */
public class JsonRequest {

    private HttpClient service;
    private Context mContext = null;

    private String ipadServiceUri = "http://timeout5742.appspot.com/";

    /*
        Constructor

        - gets Context and set local var by param
        - init connection parameters and HttpClient.
     */
    public JsonRequest(Context ctx) {
        mContext = ctx;

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 30 * 1000; // 30 seconds.
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        int timeoutSocket = 1 * 60 * 1000; // 1 minute.
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        service = new DefaultHttpClient(httpParameters);
    }

    /*
        sendRequest is the main request function, all other methods will use this basic method.
        simply, send a request to an URI which is given by parameter.
        get response from server and return the json string.
     */
    public String sendRequest(String uri) {
        try {

            HttpGet request = new HttpGet();
            request.setURI(new URI(uri));
            HttpResponse response = service.execute(request);

            InputStream ips = response.getEntity().getContent();
            BufferedReader buf = new BufferedReader(new InputStreamReader(ips,
                    "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String s;
            while (true) {
                s = buf.readLine();
                if (s == null || s.length() == 0)
                    break;
                sb.append(s);

            }
            buf.close();
            ips.close();
            String jsonString = sb.toString();

            return jsonString;
        } catch (Exception e) {
            return "{ 'Error':[{ 'Message' : '', 'No': -404}]}";

        }
    }

    public String md5(String s) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(s.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return "";
    }

    /*
        http://timeoutswe5743.appspot.com/login?userName=hakan&password=hakan

        {"type":"Success","message":"It is successfully done."}
        {"type":"Fail","message":"Specified information is wrong!"}
     */
    public boolean sendRequestLogin(String username, String password) {
        try {
            password = md5(password);

            String loginUri = ipadServiceUri + "login?userEmail=" + username
                    + "&password=" + password;

            String loginJson = sendRequest(loginUri);

            Gson gson = new Gson();

            CommonReturnObject obj = gson.fromJson(loginJson, CommonReturnObject.class);

            if (obj != null && obj.getType().equals("Success")) {

                SharedPreferences mPrefs = mContext.getApplicationContext().getSharedPreferences(
                        mContext.getApplicationContext().getClass().getName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.clear();
                prefsEditor.putString("sessionId", obj.getSessionId());
                prefsEditor.commit();


                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /*
        http://timeoutswe5743.appspot.com/register?userName=haluks@boun.edu.tr&password=haluk

        {"type":"Success","message":"It is successfully done."}
     */
    public boolean sendRequestRegister(String username, String password) {
        try {
            String loginUri = ipadServiceUri + "register?userEmail=" + username
                    + "&password=" + password;

            String loginJson = sendRequest(loginUri);

            Gson gson = new Gson();

            CommonReturnObject obj = gson.fromJson(loginJson, CommonReturnObject.class);

            if (obj != null && obj.getType().equals("Success")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /*
                @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "contextId", required = false) String contextId,
     */

    public String sendRequestGetNewsFeed() {

        String response_str = "";

        try {
            SharedPreferences mPrefs = mContext.getApplicationContext()
                    .getSharedPreferences(
                            mContext.getClass().getName(),
                            Context.MODE_PRIVATE);
            String sessionId = mPrefs.getString("sessionId", "");

            String uri = ipadServiceUri + "newsFeed?sessionId=" + sessionId;

            String jsonString = sendRequest(uri);

            Log.d("", jsonString);

            return jsonString;

        } catch (Exception ex) {
            return "";
        }
    }

    public String sendRequestFind(String keyword, String contextId) {

        String response_str = "";

        try {
            SharedPreferences mPrefs = mContext.getApplicationContext()
                    .getSharedPreferences(
                            mContext.getClass().getName(),
                            Context.MODE_PRIVATE);
            String sessionId = mPrefs.getString("sessionId", "");

            String uri = ipadServiceUri + "find?keyword=" + keyword;

            String jsonString = sendRequest(uri);

            Log.d("", jsonString);

            return jsonString;

        } catch (Exception ex) {
            return "";
        }

    }

    /*
        {"id":"Q1660056","url":"//www.wikidata.org/wiki/Q1660056","description":"male given name","label":"Alican"}
     */
    public List<Tag> sendRequestSearchContext(String tag) {

        List<Tag> retList = new ArrayList<>();

        try {

            SharedPreferences mPrefs = mContext.getApplicationContext()
                    .getSharedPreferences(
                            mContext.getClass().getName(),
                            Context.MODE_PRIVATE);
            String sessionId = mPrefs.getString("sessionId", "");

            String uri = ipadServiceUri + "searchContext?tag=" + tag;

            String jsonString = sendRequest(uri);

            Log.d("", jsonString);

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Tag>>() {}.getType();
            retList = gson.fromJson(jsonString, listType);

            return retList;
        } catch (Exception e) {
            return retList;
        }
    }


    /*

            @RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "groupName") String groupName,
			@RequestParam(value = "groupDescription", required = false) String groupDescription,
			@RequestParam(value = "invitedPeople", required = false) String invitedPeople,
			@RequestParam(value = "tag", required = false) String tagString,
			@RequestParam(value = "privacy", required = false) String privacy,
     */
    public boolean sendRequestCreateGroup(String name, String desc, String invitedPeople, String tags, String privacy) {

        /*
        invitedPeople = json string of (List<Integer> of userIDs)
         */

        try {

            SharedPreferences mPrefs = mContext.getApplicationContext()
                    .getSharedPreferences(
                            mContext.getClass().getName(),
                            Context.MODE_PRIVATE);
            String sessionId = mPrefs.getString("sessionId", "");

            String uri = ipadServiceUri + "group/create?sessionId=" + sessionId
                    + "&groupName=" + URLEncoder.encode(name, "utf-8")
                    + "&groupDescription=" + URLEncoder.encode(desc, "utf-8");

            if (invitedPeople.length() > 0) {
                uri += "&invitedPeople=" + invitedPeople;
            }
            if (tags.length() > 0) {
                tags = URLEncoder.encode(tags, "UTF-8");
                uri += "&tag=" + tags;
            }
            uri += "&privacy=" + privacy;

            String jsonString = sendRequest(uri);

            Log.d("crate people", jsonString);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public String sendRequestMineFriends() {

        String ret = "";

        try {
            SharedPreferences mPrefs = mContext.getApplicationContext()
                    .getSharedPreferences(
                            mContext.getClass().getName(),
                            Context.MODE_PRIVATE);
            String sessionId = mPrefs.getString("sessionId", "");

            String uri = ipadServiceUri + "friends/my?sessionId=" + sessionId;

            String jsonString = sendRequest(uri);

            Log.d("friends/my", jsonString);

            return jsonString;
        } catch (Exception ex) {
            return "";
        }
    }

    public String sendRequestGetCreatedGroups() {

        String ret = "";

        try {
            SharedPreferences mPrefs = mContext.getApplicationContext()
                    .getSharedPreferences(
                            mContext.getClass().getName(),
                            Context.MODE_PRIVATE);
            String sessionId = mPrefs.getString("sessionId", "");

            String uri = ipadServiceUri + "group/my?sessionId=" + sessionId;

            String jsonString = sendRequest(uri);

            Log.d("group/my", jsonString);

            return jsonString;
        } catch (Exception ex) {
            return "";
        }
    }

    /*
        "/friends/invite"
            @RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "userIds") String friendsString,//json List<Integer> olarak user idleri
     */

    public Boolean sendRequestInviteFriend(String userIDs) {
        try {

            SharedPreferences mPrefs = mContext.getApplicationContext()
                    .getSharedPreferences(
                            mContext.getClass().getName(),
                            Context.MODE_PRIVATE);
            String sessionId = mPrefs.getString("sessionId", "");

            String uri = ipadServiceUri + "friends/invite?sessionId=" + sessionId
                    + "&userIds=" + userIDs;

            String jsonString = sendRequest(uri);

            Log.d("invite friends", jsonString);

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
