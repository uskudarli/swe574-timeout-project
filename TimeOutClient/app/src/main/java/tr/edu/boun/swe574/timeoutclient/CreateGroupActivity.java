package tr.edu.boun.swe574.timeoutclient;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import tr.edu.boun.swe574.timeoutclient.jsonObjects.Tag;
import tr.edu.boun.swe574.timeoutclient.utils.JsonRequest;


public class CreateGroupActivity extends ActionBarActivity {

    AutoCompleteTextView searchTextView;

    EditText edt_title;
    EditText edt_desc;
    Boolean isPublic = false;

    Button btn_create;

    List<Tag> selectedTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        selectedTags = new ArrayList<>();

        searchTextView = (AutoCompleteTextView) findViewById(R.id.edt_cg_tags);
        searchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.length() >= 3) {
                        if (s.toString().lastIndexOf(",") > 0) {
                            s = s.subSequence(s.toString().lastIndexOf(",") + 1, s.length());
                        }

                        if (s.length() >= 3) {
                            searchContextTask sct = new searchContextTask(CreateGroupActivity.this);
                            sct.execute(s.toString());
                        }
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    selectedTags.clear();
                }
            }
        });
        searchTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Tag selectedTag = (Tag) adapterView.getAdapter().getItem(i);
                Log.d("", "");
                String oldText = searchTextView.getText().toString();
                if (oldText.contains("@")) {
                    oldText = "";
                }

                for (Tag t : selectedTags) {
                    oldText += t.getLabel() + ",";
                }

                searchTextView.setText(oldText + selectedTag.getLabel() + ",");
                searchTextView.setSelection(searchTextView.getText().length());

                selectedTags.add(selectedTag);
            }
        });

        edt_title = (EditText) findViewById(R.id.edt_cg_title);
        edt_desc = (EditText) findViewById(R.id.edt_cg_description);
        CheckBox cb_privacy = (CheckBox) findViewById(R.id.cb_cg_privacy);
        cb_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPublic = !isPublic;
            }
        });

        btn_create = (Button) findViewById(R.id.btn_cg_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = edt_title.getText().toString();
                String desc = edt_desc.getText().toString();

                if (title.length() <= 0 || desc.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Missing required fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<MyTag> mySelectedTags = new ArrayList<MyTag>();
                for (Tag st : selectedTags) {
                    MyTag mt = new MyTag();
                    mt.setContextId(st.getId());
                    mt.setAlias(st.getLabel());
                    mt.setDescription(st.getDescription());
                    mt.setTagName(st.getLabel());
                    mt.setUrl(st.getUrl());
                    mt.setLabel(st.getLabel());

                    mySelectedTags.add(mt);
                }

                Gson gson = new Gson();
                String tagStr = gson.toJson(mySelectedTags);

                createGroupTask cgt = new createGroupTask(getApplicationContext());
                cgt.execute(title, desc, "", tagStr, "public");
            }
        });
    }

    public class MyTag {
        /*
            /*
                tag.tagName = selectedTags[i].searchString;
            tag.contextId = selectedTags[i].originalObject.id;
            tag.url = selectedTags[i].originalObject.url;
            if (selectedTags[i].originalObject.aliases != null) {
                tag.alias = selectedTags[i].originalObject.aliases[0];
            }
            tag.description = selectedTags[i].originalObject.description;
            tag.label = selectedTags[i].originalObject.label;
     */

        String tagName;
        String contextId;
        String url;
        String alias;
        String description;
        String label;

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public String getContextId() {
            return contextId;
        }

        public void setContextId(String contextId) {
            this.contextId = contextId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class searchContextTask extends AsyncTask<String, Void, Boolean> {

        private Context mContext;
        private List<Tag> tagLİst;

        public searchContextTask(Context ctx) {
            mContext = ctx;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                String text = params[0];

                text = URLEncoder.encode(text, "UTF-8");

                JsonRequest jr = new JsonRequest(mContext);
                tagLİst = jr.sendRequestSearchContext(text);

                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                // set autocomplete adapter
                CustomSearchAdapter csa = new CustomSearchAdapter(mContext, tagLİst);
                csa.setNotifyOnChange(true);
                searchTextView.setAdapter(csa);
                csa.notifyDataSetChanged();
            }
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

    public void resetScreen() {
        edt_title.setText("");
        edt_desc.setText("");
        searchTextView.setText("");

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
            Boolean ret = jr.sendRequestCreateGroup(groupName, groupDesc, people, tags, privacy);

            return ret;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(getApplicationContext(), "Group has been created.", Toast.LENGTH_SHORT).show();
                resetScreen();
            } else {
                Toast.makeText(getApplicationContext(), "Problem occurred. Group has not been created.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
