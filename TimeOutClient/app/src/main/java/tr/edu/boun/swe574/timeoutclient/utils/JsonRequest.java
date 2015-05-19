package tr.edu.boun.swe574.timeoutclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

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
import java.net.URI;
import java.net.URLEncoder;

import tr.edu.boun.swe574.timeoutclient.jsonObjects.CommonReturnObject;

/**
 * Created by haluks on 19/04/15.
 */
public class JsonRequest {

    /*
        http://timeoutswe5743.appspot.com/register?userName=hakan&password=hakan
        http://timeoutswe5743.appspot.com/login?userName=hakan&password=hakan
     */

    private HttpClient service;
    private Context mContext = null;

    private String ipadServiceUri = "http://timeoutswe5743.appspot.com/"; // "http://timeoutswe5743.appspot.com/";

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

    /*
        http://timeoutswe5743.appspot.com/login?userName=hakan&password=hakan

        {"type":"Success","message":"It is successfully done."}
        {"type":"Fail","message":"Specified information is wrong!"}
     */
    public boolean sendRequestLogin(String username, String password) {
        try {
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
            @RequestParam(value = "sessionId") String sessionId,
            @RequestParam(value = "groupName") String groupName,
            @RequestParam(value = "groupDescription", required = false) String groupDescription,
            @RequestParam(value = "invitedPeople", required = false) List<User> invitedPeople,
            @RequestParam(value = "tag", required = false) String tagString,
            @RequestParam(value = "privacy", required = false) String privacy
     */
    public boolean sendRequestCreateGroup(String name, String desc, String invitedPeople, String tags, String privacy) {

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
                uri += "&tag=" + tags;
            }
            uri += "&privacy=" + privacy;

            String jsonString = sendRequest(uri);

            Log.d("", jsonString);

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
