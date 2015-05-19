package tr.edu.boun.swe574.timeoutclient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

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
                    break;
                case 3:
                    // group
                    LinearLayout ll_group = (LinearLayout) rootView.findViewById(R.id.main_layout);

                    Button btn_addGroup = new Button(getActivity());
                    btn_addGroup.setText("Create Group");
                    btn_addGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            List<Integer> people = new ArrayList<Integer>();
                            people.add(1);
                            people.add(2);
                            people.add(333);

                            Gson gson = new Gson();
                            String peopleString = gson.toJson(people);

                            createGroupTask cgt = new createGroupTask(getActivity().getApplicationContext());
                            cgt.execute("süper grup", "dünyanın en süper grubu", peopleString, "", "public");
                        }
                    });


                    ll_group.addView(btn_addGroup);


                    break;
            }
            return rootView;
        }

        public class createGroupTask extends AsyncTask<String, Void, Boolean> {

            private Context mContext;

            public createGroupTask(Context ctx) {
                mContext = ctx;
            }

            @Override
            protected Boolean doInBackground(String... params) {

                String groupName = params[0];
                String groupDesc = params[1];
                String people = params[2];
                String tags = params[3];
                String privacy = params[4];


                JsonRequest jr = new JsonRequest(mContext);
                jr.sendRequestCreateGroup(groupName, groupDesc, people, tags, privacy);


                return true;
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
