package tr.edu.boun.swe574.timeoutclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tr.edu.boun.swe574.timeoutclient.jsonObjects.Tag;
import tr.edu.boun.swe574.timeoutclient.utils.JsonRequest;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    final int[] ICONS = new int[] {
            R.drawable.ic_action_home,
            R.drawable.ic_action_friends,
            R.drawable.ic_action_social_group,
            R.drawable.ic_action_notification,
            R.drawable.ic_action_profile
    };

    final int[] TITLES = new int[] {
            R.string.title_section1,
            R.string.title_section2,
            R.string.title_section3,
            R.string.title_section4,
            R.string.title_section5
    };

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

//    AutoCompleteTextView searchTextView;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.main_menu_search) {
            // intent to search activity
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(i);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

//        MenuItem mi = menu.findItem(R.id.main_menu_search);
//        MenuItemCompat.setOnActionExpandListener(mi, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem menuItem) {
//                Log.d("", "expanded");
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//                return true;
//            }
//        });

//        SearchView sv = (SearchView) menu.findItem(R.id.main_menu_search)
//                .getActionView();
//
//        sv.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                Log.d("","close");
//                return false;
//            }
//        });
//        sv.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("", "search");
//            }
//        });
//        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // Filter("", newText);
////                searchQuery = newText;
////                handler.removeMessages(TRIGGER_SERACH);
////                handler.sendEmptyMessageDelayed(TRIGGER_SERACH,
////                        SEARCH_TRIGGER_DELAY_IN_MS);
//                return false;
//            }
//
//        });

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowCustomEnabled(true);

//        LayoutInflater inflator = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflator.inflate(R.layout.actionbar_search, null);
//
//        actionBar.setCustomView(v);

//        searchTextView = (AutoCompleteTextView) v
//                .findViewById(R.id.editText1);
//        searchTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                try {
//                    if (s.length() >= 3) {
//                        searchContextTask sct = new searchContextTask(MainActivity.this);
//                        sct.execute(s.toString());
//                    }
//
//                } catch (Exception e) {
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.

            ActionBar.Tab tab = actionBar.newTab();
            tab.setCustomView(R.layout.home_tab_layout);

            ImageView cv = (ImageView)tab.getCustomView().findViewById(R.id.tabImage);
            cv.setImageResource(ICONS[i]);
            TextView tv = (TextView)tab.getCustomView().findViewById(R.id.tabText);
            tv.setText(TITLES[i]);

            tab.setTabListener(this);

            actionBar.addTab(tab);
        }
    }

    public class CustomSearchAdapter extends ArrayAdapter<Tag> {

        private Filter filter;
        private List<Tag> mData;

        public CustomSearchAdapter(Context context, List<Tag> data) {
            super(context, R.layout.actionbar_search_item, data);
            // TODO Auto-generated constructor stub
            mData = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Tag item = mData.get(position);

            if (convertView == null) {
                LayoutInflater inflater = ((Activity) getContext())
                        .getLayoutInflater();
                convertView = inflater.inflate(R.layout.actionbar_search_item, parent,
                        false);
            }

            TextView tv_label = (TextView) convertView.findViewById(R.id.search_item_label);
            tv_label.setText(item.getLabel());

            TextView tv_desc = (TextView) convertView.findViewById(R.id.search_item_description);
            tv_desc.setText(item.getDescription());

            convertView.setTag(position);

            return convertView;
        }

//        @Override
//        public Filter getFilter() {
//            if (filter == null) {
//                TagFilter myFilter = new TagFilter();
//                myFilter.setSearchAdapter(this);
//                filter = myFilter;
//            }
//            return filter;
//        }
    }

//    public class TagFilter extends Filter {
//
//        CustomSearchAdapter searchAdapter;
//
//        public void setSearchAdapter(CustomSearchAdapter adapter) {
//            searchAdapter = adapter;
//        }
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//
//            List<Tag> list = new ArrayList<>();
//            FilterResults result = new FilterResults();
//            String substr = constraint.toString().toLowerCase();
//
//            if (substr.length() == 0) {
//                result.values = list;
//                result.count = list.size();
//            } else {
//                // iterate over the list of venues and find if the venue matches the constraint. if it does, add to the result list
//                final ArrayList<Tag> retList = new ArrayList<Tag>();
//                for (Tag filterTag : list) {
//                    try {
//
//                        if (filterTag.getLabel().toLowerCase().contains(constraint)
//                                || filterTag.getDescription().toLowerCase().contains(constraint)) {
//                            retList.add(filterTag);
//                        }
//                    } catch (Exception e) {
//                        Log.d("TagFilter:", e.getMessage());
//                    }
//                }
//                result.values = retList;
//                result.count = retList.size();
//            }
//            return result;
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
//
//            searchAdapter.clear();
//            if (filterResults.count > 0) {
//                for (Tag o : (ArrayList<Tag>) filterResults.values) {
//                    searchAdapter.add(o);
//                }
//            }
//        }
//    }

//    public class searchContextTask extends AsyncTask<String, Void, Boolean> {
//
//        private Context mContext;
//        private List<Tag> tagLİst;
//
//        public searchContextTask(Context ctx) {
//            mContext = ctx;
//        }
//
//        @Override
//        protected Boolean doInBackground(String... params) {
//
//            try {
//                String text = params[0];
//
//                JsonRequest jr = new JsonRequest(mContext);
////                tagLİst = jr.sendRequestSearchContext(text);
//
//                String res = jr.sendRequestFind(text, "");
//                Log.d("", res);
//
//                return true;
//            } catch (Exception ex) {
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean success) {
//
//            if (success) {
//                // set autocomplete adapter
//                CustomSearchAdapter csa = new CustomSearchAdapter(mContext, tagLİst);
//                csa.setNotifyOnChange(true);
//                searchTextView.setAdapter(csa);
//                csa.notifyDataSetChanged();
//            }
//        }
//    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section5).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            int section = getArguments().getInt(ARG_SECTION_NUMBER);

            switch (section) {
                case 1:
                    // home
                    LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.main_layout);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    RecyclerView recyclerView = new RecyclerView(getActivity());
                    recyclerView.setLayoutParams(params);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setVerticalScrollBarEnabled(true);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    newsFeedTask nft = new newsFeedTask(getActivity().getApplicationContext());
                    nft.execute();

                    List<basicItem> basicItems = new ArrayList<>();

                    basicItems.add(new basicItem(R.drawable.aa, "<b>Hasan</b> joined the group 'Math funs'"));
                    basicItems.add(new basicItem(R.drawable.ad, "<b>Sara</b> subscribe the group 'Sematic Searching'"));
                    basicItems.add(new basicItem(R.drawable.ac, "<b>Sara</b> has become friend with 'Robert'"));

                    basicItems.add(new basicItem(R.drawable.aa, "<b>Ali</b> joined the group 'Star Wars'"));
                    basicItems.add(new basicItem(R.drawable.ad, "<b>Suzan</b> subscribe the group 'Dungeon & Dragons'"));
                    basicItems.add(new basicItem(R.drawable.ac, "<b>John</b> joined the group 'Math funs'"));

                    homeAdapter adapter = new homeAdapter(basicItems);
                    recyclerView.setAdapter(adapter);

                    ll.addView(recyclerView);

                    break;
                case 2:
                    // friends
                    LinearLayout ll_friend = (LinearLayout) rootView.findViewById(R.id.main_layout);

                    LinearLayout.LayoutParams params_friend = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    RecyclerView recyclerView_friend = new RecyclerView(getActivity());
                    recyclerView_friend.setLayoutParams(params_friend);
                    recyclerView_friend.setHasFixedSize(true);
                    recyclerView_friend.setVerticalScrollBarEnabled(true);

                    LinearLayoutManager layoutManager_friend = new LinearLayoutManager(getActivity());
                    recyclerView_friend.setLayoutManager(layoutManager_friend);
                    recyclerView_friend.setItemAnimator(new DefaultItemAnimator());

                    getMyFriends gmf = new getMyFriends(getActivity().getApplicationContext());
                    gmf.setRecyclerView(recyclerView_friend);
                    gmf.execute();

                    ll_friend.addView(recyclerView_friend);

                    break;
                case 3:
                    // group
                    LinearLayout ll_group = (LinearLayout) rootView.findViewById(R.id.main_layout);

                    Button btn_addGroup = new Button(getActivity());
                    btn_addGroup.setText("Create Group");
                    btn_addGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

//                            List<Integer> people = new ArrayList<Integer>();
//                            people.add(1);
//                            people.add(2);
//                            people.add(333);
//
//                            Gson gson = new Gson();
//                            String peopleString = gson.toJson(people);
//
//                            createGroupTask cgt = new createGroupTask(getActivity().getApplicationContext());
//                            cgt.execute("süper grup", "dünyanın en süper grubu", peopleString, "", "public");

                            Intent i = new Intent(getActivity(), CreateGroupActivity.class);
                            startActivity(i);
                        }
                    });


                    ll_group.addView(btn_addGroup);

                    LinearLayout.LayoutParams params_group = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    RecyclerView recyclerView_group = new RecyclerView(getActivity());
                    recyclerView_group.setLayoutParams(params_group);
                    recyclerView_group.setHasFixedSize(true);
                    recyclerView_group.setVerticalScrollBarEnabled(true);

                    LinearLayoutManager layoutManager_group = new LinearLayoutManager(getActivity());
                    recyclerView_group.setLayoutManager(layoutManager_group);
                    recyclerView_group.setItemAnimator(new DefaultItemAnimator());

                    getCreatedGroupsTask cgt = new getCreatedGroupsTask(getActivity().getApplicationContext());
                    cgt.setRecyclerView(recyclerView_group);
                    cgt.execute();

                    ll_group.addView(recyclerView_group);


                    break;
            }
            return rootView;
        }

        public class getMyFriends extends AsyncTask<String, Void, Boolean> {

            private Context mContext;
            RecyclerView list;
            List<String> friendList;

            public void setRecyclerView(RecyclerView rview) {
                list = rview;
            }

            public getMyFriends(Context ctx) {
                mContext = ctx;
            }

            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    friendList = new ArrayList<>();

                    JsonRequest jr = new JsonRequest(mContext);

                    String res = jr.sendRequestMineFriends();
                    Log.d("myFriends", res);

                    // commonReturnObject dönüyor data vs. olmadığında

                    JSONArray mainArray = new JSONArray(res);

                    for (int i=0; i < mainArray.length(); i++) {
                        JSONObject jo = (JSONObject)mainArray.get(i);

                        if (jo.getString("status").equals("AO") || jo.getString("status").equals("AS")) {
                            JSONObject jsonFrined = jo.getJSONObject("friend");

                            String name = jsonFrined.getString("userEmail");
                            if (!jsonFrined.isNull("userBasicInfo")) {
                                JSONObject info = jsonFrined.getJSONObject("userBasicInfo");
                                name = info.getString("firstName") + " " + info.getString("lastName");
                            }
                            friendList.add(name);
                        }


                    }

                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {

                    List<basicItem> friend_basicItems = new ArrayList<>();

                    for (String res : friendList) {
                        friend_basicItems.add(new basicItem(R.drawable.user_group, res));
                    }

                    homeAdapter adapter_friend = new homeAdapter(friend_basicItems);
                    list.setAdapter(adapter_friend);
                }
            }
        }

        public class getCreatedGroupsTask extends AsyncTask<String, Void, Boolean> {

            private Context mContext;
            List<myGroupResponse> mData;
            RecyclerView list;

            public void setRecyclerView(RecyclerView rview) {
                list = rview;
            }

            public getCreatedGroupsTask(Context ctx) {
                mContext = ctx;
            }

            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    mData = new ArrayList<>();

                    JsonRequest jr = new JsonRequest(mContext);

                    String res = jr.sendRequestGetCreatedGroups();
                    Log.d("myGroups", res);

                    // commonReturnObject dönüyor data vs. olmadığında

                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<myGroupResponse>>() {}.getType();
                    mData = gson.fromJson(res, listType);

                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {

                    List<basicItem> group_basicItems = new ArrayList<>();

                    for (myGroupResponse res : mData) {
                        group_basicItems.add(new basicItem(R.drawable.user_group, res.getName() + " - " + res.getCount().toString() + " member(s)"));
                    }

                    homeAdapter adapter_group = new homeAdapter(group_basicItems);
                    list.setAdapter(adapter_group);

                }
            }
        }

        public class myFriendResponse {
            /*
                {
        "friendshipId": 1,
        "friend": {
            "userId": 32,
            "userEmail": "morteza.bandi@gmail.com",
            "date": null,
            "password": "202cb962ac59075b964b07152d234b70",
            "role": null,
            "userBasicInfo": null,
            "userCommInfo": null,
            "userExtraInfo": null
        },
        "status": "IS"
    },
             */



        }

        public class myGroupResponse {
            /*
                {
        "actionId": 10,
        "name": "süper grup",
        "count": 1
    },
             */

            Integer actionId;
            String name;
            Integer count;

            public Integer getActionId() {
                return actionId;
            }

            public void setActionId(Integer actionId) {
                this.actionId = actionId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Integer getCount() {
                return count;
            }

            public void setCount(Integer count) {
                this.count = count;
            }
        }

        public class newsFeedTask extends AsyncTask<String, Void, Boolean> {

            private Context mContext;
            private List<Tag> tagLİst;

            public newsFeedTask(Context ctx) {
                mContext = ctx;
            }

            @Override
            protected Boolean doInBackground(String... params) {

                try {

                    JsonRequest jr = new JsonRequest(mContext);
//                tagLİst = jr.sendRequestSearchContext(text);

                    String res = jr.sendRequestGetNewsFeed();
                    Log.d("news feed", res);

                    // commonReturnObject dönüyor data vs. olmadığında

                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {

                }
            }
        }





        public class basicItem {
            int drawable;
            String text;

            public basicItem(int img, String txt) {
                drawable = img;
                text = txt;
            }
        }

        public class homeAdapter extends RecyclerView.Adapter<homeAdapter.viewHolder> {

            List<basicItem> values;

            public homeAdapter(List<basicItem> items) {
                values = items;
            }

            public class viewHolder extends RecyclerView.ViewHolder {

                ImageView img;
                TextView txt;

                public viewHolder(View itemView) {
                    super(itemView);
                    this.img = (ImageView) itemView.findViewById(R.id.home_item_img);
                    this.txt = (TextView) itemView.findViewById(R.id.home_item_txt);
                }
            }

            @Override
            public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_card_layout, viewGroup, false);

                viewHolder myViewHolder = new viewHolder(view);

                return myViewHolder;
            }

            @Override
            public void onBindViewHolder(homeAdapter.viewHolder holder, int i) {
                TextView _txt = holder.txt;
                ImageView _img = holder.img;

                _txt.setText(Html.fromHtml(values.get(i).text));
                _img.setImageResource(values.get(i).drawable);
            }

            @Override
            public int getItemCount() {
                return values.size();
            }
        }

    }

}
