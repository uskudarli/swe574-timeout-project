package tr.edu.boun.swe574.timeoutclient;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tr.edu.boun.swe574.timeoutclient.utils.JsonRequest;


public class SearchActivity extends ActionBarActivity {

    static int USER_TYPE = 101;
    static int EVENT_TYPE = 201;
    static int GROUP_TYPE = 301;

    ListView searchListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchListview = (ListView) findViewById(R.id.search_listView);

        searchListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchResultItem sri = (searchResultItem) adapterView.getItemAtPosition(i);
                if (sri.getType().intValue() == USER_TYPE) {

                    final String selectedUserId = sri.getId().toString();

                    new AlertDialog.Builder(SearchActivity.this)
                            .setTitle("Add friend")
                            .setMessage("Are you sure you want to sent a friend request?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    inviteFriendTask ift = new inviteFriendTask(getApplicationContext());
                                    ift.execute(selectedUserId);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem mi = menu.findItem(R.id.search_menu_search);
        mi.expandActionView();
        MenuItemCompat.setOnActionExpandListener(mi, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                finish();
                return true;
            }
        });

        SearchView sv = (SearchView) mi.getActionView();
//        sv.performClick();
        sv.setIconified(false);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchContextTask sct = new searchContextTask(SearchActivity.this);
                sct.execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }

        });


        return true;
    }

    /*
    {
    "users": [
        {
            "userId": 60,
            "userEmail": "oguzcam_50@hotmail.com",
            "date": null,
            "password": "0aa0127aae3c911130d58ae733919447",
            "role": {
                "roleId": 1,
                "name": "student"
            },
            "userBasicInfo": {
                "userId": 60,
                "firstName": "OĞUZ",
                "lastName": "ÇAM",
                "gender": null
            },
            "userCommInfo": {
                "userId": 60,
                "mobilePhone": 0,
                "address": null
            },
            "userExtraInfo": {
                "userId": 60,
                "birthDate": null,
                "about": null,
                "interests": null,
                "languages": null
            }
        }
    ],
    "events": [
        {
            "actionId": 41,
            "title": "Oğuz Sevenler",
            "description": "Oğuzu seven insanlar birliğidir, burada buluşacak ve oğuzu gıdıklayacaklardır.",
            "createTime": 1432393947000,
            "privacy": "C",
            "actionType": "G",
            "startTime": 1432393947000,
            "endTime": null
        }
    ],
    "groups": [
        {
            "actionId": 41,
            "title": "Oğuz Sevenler",
            "description": "Oğuzu seven insanlar birliğidir, burada buluşacak ve oğuzu gıdıklayacaklardır.",
            "createTime": 1432393947000,
            "privacy": "C",
            "actionType": "G",
            "startTime": 1432393947000,
            "endTime": null
        }
    ]
}
     */

    public class inviteFriendTask extends AsyncTask<String, Void, Boolean> {

        private Context mContext;
        private ProgressDialog dialog;

        public inviteFriendTask(Context ctx) {
            mContext = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(SearchActivity.this);

            Window window = dialog.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            dialog.setCancelable(false);
            dialog.setMessage(" ");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Searching...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            try {

                String userIds = strings[0];

                List<Integer> retLsit = new ArrayList<>();
                retLsit.add(Integer.parseInt(userIds));

                Gson gson = new Gson();
                String jsonString = gson.toJson(retLsit);

                JsonRequest jr = new JsonRequest(mContext);

                Boolean ret = jr.sendRequestInviteFriend(jsonString);

                return ret;
            } catch (Exception ex) {
                return false;
            }


        }

        @Override
        protected void onPostExecute(Boolean success) {
            dialog.hide();
            dialog.dismiss();

            if (success) {

            }
        }
    }

    public class searchContextTask extends AsyncTask<String, Void, Boolean> {

        private Context mContext;
        private List<searchResultItem> mData;
        private ProgressDialog dialog;

        public searchContextTask(Context ctx) {
            mContext = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(mContext);

            Window window = dialog.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            dialog.setCancelable(false);
            dialog.setMessage(" ");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Searching...");
            dialog.show();
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                String text = params[0];

                mData = new ArrayList<>();

                JsonRequest jr = new JsonRequest(mContext);
//                tagLİst = jr.sendRequestSearchContext(text);

                String res = jr.sendRequestFind(text, "");

                JSONObject mainObj = new JSONObject(res);
                JSONArray userJsonArr = mainObj.getJSONArray("users");
                JSONArray eventJsonArr = mainObj.getJSONArray("events");
                JSONArray groupJsonArr = mainObj.getJSONArray("groups");

                for (int i = 0; i < userJsonArr.length(); i++) {
                    JSONObject o = (JSONObject) userJsonArr.get(i);

                    if (!o.isNull("userBasicInfo")) {
                        JSONObject basicInfo = o.getJSONObject("userBasicInfo");
                        String firstName = basicInfo.getString("firstName");
                        String lastName = basicInfo.getString("lastName");
                        Integer userId = o.getInt("userId");

                        String role = "";
                        if (!o.isNull("role")) {
                            JSONObject roleObj = o.getJSONObject("role");
                            role = roleObj.getString("name");
                        }

                        searchResultItem sri = new searchResultItem();
                        sri.setType(USER_TYPE);
                        sri.setId(userId);
                        sri.setTitle(firstName + " " + lastName);
                        sri.setDescription(role);

                        mData.add(sri);
                    }
                }

                for (int j = 0; j < eventJsonArr.length(); j++) {
                    JSONObject jo = (JSONObject) eventJsonArr.get(j);

                    if (jo.getString("actionType").equals("E")) {
                        String title = jo.getString("title");
                        String desc = jo.getString("description");
                        Integer actionId = jo.getInt("actionId");

                        searchResultItem sri = new searchResultItem();
                        sri.setType(EVENT_TYPE);
                        sri.setId(actionId);
                        sri.setTitle(title);
                        sri.setDescription(desc);

                        mData.add(sri);
                    }
                }

                for (int j = 0; j < groupJsonArr.length(); j++) {
                    JSONObject jo = (JSONObject) groupJsonArr.get(j);

                    if (jo.getString("actionType").equals("G")) {
                        String title = jo.getString("title");
                        String desc = jo.getString("description");
                        Integer actionId = jo.getInt("actionId");

                        searchResultItem sri = new searchResultItem();
                        sri.setType(GROUP_TYPE);
                        sri.setId(actionId);
                        sri.setTitle(title);
                        sri.setDescription(desc);

                        mData.add(sri);
                    }
                }

                Log.d("", mData.toString());

                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {

            dialog.hide();
            dialog.dismiss();

            if (success) {
                // set autocomplete adapter
//                CustomSearchAdapter csa = new CustomSearchAdapter(mContext, tagLİst);
//                csa.setNotifyOnChange(true);
//                searchTextView.setAdapter(csa);
//                csa.notifyDataSetChanged();
                CustomSearchListAdapter csla = new CustomSearchListAdapter(SearchActivity.this, mData);
                searchListview.setAdapter(csla);

            }
        }
    }

    public class searchResultItem {

        Integer type;
        Integer Id;
        String title;
        String description;

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getId() {
            return Id;
        }

        public void setId(Integer id) {
            Id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public class CustomSearchListAdapter extends ArrayAdapter<searchResultItem> {


        List<searchResultItem> mData;

        public CustomSearchListAdapter(Context context, List<searchResultItem> data) {
            super(context, R.layout.activity_search_list_item, data);
            // TODO Auto-generated constructor stub

            mData = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            searchResultItem object = mData.get(position);

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.activity_search_list_item, parent,
                        false);
            }

            /*
                USER_TYPE = 101;
                EVENT_TYPE = 201;
                GROUP_TYPE = 301;
             */
            TextView txt = (TextView) convertView.findViewById(R.id.searchActivity_listitem_type);
            switch (object.getType()) {
                case 101:
                    txt.setText("USER");
                    break;
                case 201:
                    txt.setText("EVENT");
                    break;
                case 301:
                    txt.setText("GROUP");
                    break;
            }

            TextView txt_title = (TextView) convertView.findViewById(R.id.searchActivity_listitem_title);
            txt_title.setText(object.getTitle());

            TextView txt_desc = (TextView) convertView.findViewById(R.id.searchActivity_listitem_desc);
            txt_desc.setText(object.getDescription());

            convertView.setTag(position);
            return convertView;
        }
    }

}
