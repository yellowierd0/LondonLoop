package emma.londonloopapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyProfileFragment extends Fragment {


	CallbackManager callbackManager;

    private Button postImageBtn;
    private Button updateStatusBtn;

    private static String url_insert_new = "http://146.169.46.77:55000/insertUser.php";


    private JSONParser jsonParser;
    private int success;//to determine JSON signal insert success/fail
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");


    private static String message = "Sample status posted from android app";
    private AccessTokenTracker accessTokenTracker;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getActivity());
		callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

		View rootView = inflater.inflate(R.layout.fragment_myprofile, container, false);

        jsonParser = new JSONParser();
        LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("public_profile", "user_friends");

        //loginButton.setOnClickListener(loginbut);
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT).show();
                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    String id = me.optString("id");
                                    String name = me.optString("name");
                                    Log.e("Facebook", name);
                                    Log.e("Facebook", id);
                                    // send name and id to your web server
                                    /*new AddNewUser(name, id).execute();
                                    //insertFb(name, id);

                                    if (success==1){
                                        Toast.makeText(getActivity(), "New user saved...", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(getActivity(), "New idiom FAILED to saved...", Toast.LENGTH_LONG).show();
                                    }*/

                                }
                            }
                        }).executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
				// App code
                Toast.makeText(getActivity(),"Error logging into Facebook", Toast.LENGTH_SHORT).show();
			}
		});


		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

	}

    private class AddNewUser extends AsyncTask<String, String, String> {
        //capture values from EditText

        ProgressDialog pDialog;
        String name;
        String fb_id;

        AddNewUser(String name, String fb_id){
            this.name = name;
            this.fb_id = fb_id;
        }

        /** * Before starting background thread Show Progress Dialog * */
        @Override protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Saving the new USER ("+name+")...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        /** * Inserting the new idiom * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("fb_Id", fb_id));
            // getting JSON Object

            // Note that create product url accepts GET method
            JSONObject json = jsonParser.makeHttpRequest(url_insert_new, "GET", params);

            // check log cat from response
            Log.d("Insert New User", json.toString());
            // check for success tag
            try {
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully save new idiom
                } else {
                    // failed to add new idiom
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //return null;
            return null;
        }

        /** * After completing background task Dismiss the progress dialog * **/

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            //
            pDialog.dismiss();
        }
    }

}
