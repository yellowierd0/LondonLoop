package emma.londonloopapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MyProfileFragment extends Fragment {


	CallbackManager callbackManager;

    private String url = "http://146.169.46.77:55000";
    private String getStatsUrl = url + "/getStats.php";
    private String getUserUrl = url + "/getUsers.php";
    private String setStatsUrl = url + "/setStats.php";
    private String setUserUrl = url + "/setUsers.php";


    private Button postImageBtn;
    private Button updateStatusBtn;

    private static String url_insert_new = "http://146.169.46.77:55000/insertUser.php";


    private int success;//to determine JSON signal insert success/fail
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

    private String walking_time;
    private String last_updated;

    private double miles_walked;
    private int id;
    private int currently_walking = 0;
    private int walks_completed = 0;

    private  TextView userCount;
    private  TextView walksCompleted;
    private  TextView totalTime;
    private  TextView totalMiles;

    private static String message = "Sample status posted from android app";
    private AccessTokenTracker accessTokenTracker;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_myprofile, container, false);

        //get statistics
        new myProfileJSONParse(getStatsUrl, ClassType.STATISTIC).execute();

        userCount = (TextView) rootView.findViewById(R.id.userCount);
        walksCompleted = (TextView) rootView.findViewById(R.id.total_walks);
        totalTime = (TextView) rootView.findViewById(R.id.total_time);
        totalMiles = (TextView) rootView.findViewById(R.id.total_miles);

        //new JSONParse(getUserUrl, ClassType.USER).execute();


        /* FACEBOOK LOGIN
        FacebookSdk.sdkInitialize(getActivity());
		callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

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
                                    }

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
        */

		return rootView;
	}
/*
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
        /*@Override protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Saving the new USER ("+name+")...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        /** * Inserting the new idiom * */
        /*protected String doInBackground(String... args) {
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

        /*protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            //
            pDialog.dismiss();
        }
    }*/


    private class myProfileJSONParse extends AsyncTask<String,String,JSONObject> {

        final String TAG = "JSONParse.java";

        private JSONArray jsonArray = null;

        private static final String TAG_STATS = "statistics";
        private static final String TAG_ID = "id";
        private static final String TAG_CURR = "currently_walking";
        private static final String TAG_WALK_TIME = "walk_time";
        private static final String TAG_MILES = "miles_walked";
        private static final String TAG_WALK_COMP = "walks_completed";
        private static final String TAG_STATS_UPDATE = "'last_updated'";

        private ClassType type;
        private String url;
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        public myProfileJSONParse(String url, ClassType type){
            this.url = url;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Getting your statistics...");
            this.dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser2 jParser = new JSONParser2();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {

                switch (type) {
                    case STATISTIC:
                        jsonArray = json.getJSONArray(TAG_STATS);
                        if (jsonArray != null){
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject c = jsonArray.getJSONObject(i);

                                walking_time = c.getString(TAG_WALK_TIME);
                                //last_updated = c.getString(TAG_STATS_UPDATE);

                                id = Integer.parseInt(c.getString(TAG_ID));
                                currently_walking = Integer.parseInt(c.getString(TAG_CURR));
                                miles_walked = Double.parseDouble(c.getString(TAG_MILES));

                                //walks_completed = Integer.parseInt(c.(TAG_WALK_COMP));

                                // show the values in our logcat
                                Log.e(TAG, "id: " + id
                                                + ", currently walking: " + currently_walking
                                                + ", walking_time: " + walking_time
                                                + ", walked completed: " + walks_completed
                                                + ", miles walked: " + miles_walked
                                                //+ ", last updated: " + last_updated
                                );

                                userCount.setText(String.valueOf(currently_walking) + " users");
                                walksCompleted.setText(String.valueOf(walks_completed) + " walks");
                                totalTime.setText(walking_time + " walking");
                                totalMiles.setText(miles_walked + " miles");

                                if(this.dialog.isShowing())
                                {
                                    this.dialog.dismiss();
                                }


                            }
                        }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
