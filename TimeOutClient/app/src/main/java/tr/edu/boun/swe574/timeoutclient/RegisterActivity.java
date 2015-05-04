package tr.edu.boun.swe574.timeoutclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tr.edu.boun.swe574.timeoutclient.utils.JsonRequest;


public class RegisterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        Context mContext;

        EditText mEmail;
        EditText mReEmail;
        EditText mPass;
        EditText mRePass;

        public PlaceholderFragment() {
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            mContext = activity;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_register, container, false);

            mEmail = (EditText) rootView.findViewById(R.id.edt_signup_email);
            mReEmail = (EditText) rootView.findViewById(R.id.edt_signup_re_email);
            mPass = (EditText) rootView.findViewById(R.id.edt_signup_pass);
            mRePass = (EditText) rootView.findViewById(R.id.edt_signup_re_pass);

            Button btn_signup = (Button) rootView.findViewById(R.id.btn_signup);
            btn_signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // controls
                    String email = mEmail.getText().toString();
                    String remail = mReEmail.getText().toString();

                    if (email.contains("@") == true && remail.contains("@") == true && email.equals(remail) == true) {
                        // valid.
                    } else {
                        mEmail.setError("Girdiğiniz emailler eşleşmedi.");
                        return;
                    }

                    String pass = mPass.getText().toString();
                    String rpass = mRePass.getText().toString();

                    if (pass.equals(rpass) == true) {
                        // valid.
                    } else {
                        mPass.setError("Girdiğiniz parolalar eşleşmedi.");
                        return;
                    }

                    UserRegisterTask urt = new UserRegisterTask();
                    urt.execute(email, pass);

                }
            });

            return rootView;
        }

        public class UserRegisterTask extends AsyncTask<String, Void, Boolean> {

            @Override
            protected Boolean doInBackground(String... params) {

                String email = params[0];
                String pass = params[1];

                JsonRequest jr = new JsonRequest(mContext);
                if (jr.sendRequestRegister(email, pass)) {

                    return true;
                } else {
                    return false;
                }

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                if (aBoolean) {
                    Toast.makeText(mContext, "Başarıyla kayıt yaptınız.", Toast.LENGTH_SHORT).show();

//                    // intent to main
//                    Intent i = new Intent(mContext, MainActivity.class);
//                    startActivity(i);

                    Intent returnIntent = new Intent();
                    getActivity().setResult(RESULT_OK, returnIntent);
                    getActivity().finish();
                } else {
                    Toast.makeText(mContext, "Kayıt başarısız.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
