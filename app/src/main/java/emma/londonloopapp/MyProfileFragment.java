package emma.londonloopapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MyProfileFragment extends Fragment {


	CallbackManager callbackManager;

    private String url = "http://146.169.46.77:55000";
    private String getStatsUrl = url + "/getStats.php";
    private String setStatsUrl = url + "/setStats.php";

    private int global_time;

    private double global_miles;

    private int currently_walking = 0;

    private int walks_completed = 0;
    private int global_walks = 0;

    private TextView userCount;
    private TextView walksCompleted;
    private TextView totalTime;
    private TextView totalMiles;
    private TextView globalTime;
    private TextView globalWalks;
    private TextView globalMiles;


    private static String message = "Sample status posted from android app";
    private AccessTokenTracker accessTokenTracker;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_myprofile, container, false);

        //get statistics
        new HttpAsyncTask().execute(getStatsUrl);

        userCount = (TextView) rootView.findViewById(R.id.userCount);

        walksCompleted = (TextView) rootView.findViewById(R.id.total_walks);
        totalTime = (TextView) rootView.findViewById(R.id.total_time);
        totalMiles = (TextView) rootView.findViewById(R.id.total_miles);

        globalMiles = (TextView) rootView.findViewById(R.id.global_miles);
        globalTime = (TextView) rootView.findViewById(R.id.global_time);
        globalWalks = (TextView) rootView.findViewById(R.id.global_walks);

        MySQLiteHelper db = new MySQLiteHelper(getActivity());
        List<StatItem> statItems = db.getAllStats();

        if (statItems.get(0).getCompleted()==1){
            walksCompleted.setText(String.valueOf(statItems.get(0).getCompleted()) + " walk");
        } else {
            walksCompleted.setText(String.valueOf(statItems.get(0).getCompleted()) + " walks");
        }

        if (statItems.get(0).getTime() >= 60){
            totalTime.setText(String.valueOf(statItems.get(0).getTime()/60) + " hrs, " +String.valueOf(statItems.get(0).getTime()%60) + " mins");
        } else {
            totalTime.setText(String.valueOf(statItems.get(0).getTime()) + " mins");
        }


        totalMiles.setText(String.valueOf(statItems.get(0).getMiles()) + " miles");


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

    private void setGlobalStats(JSONObject jsonObject) throws JSONException {


        JSONArray jsonArray = jsonObject.getJSONArray("statistics");
        int i = 0;
        while (i < jsonArray.length()){
            JSONObject global = jsonArray.getJSONObject(i);
            if (global.getString("id").equals("0")){

                currently_walking = Integer.valueOf(global.getString("currently_walking"));
                global_miles = Integer.valueOf(global.getString("miles_walked"));
                global_time = Integer.valueOf(global.getString("walk_time"));
                global_walks = Integer.valueOf(global.getString("walks_completed"));

                Log.e("getStats request",
                        "current: " + currently_walking +
                                "walked: " + global_miles +
                                "time: " + global_time +
                                " completed: " + global_walks);

                userCount.setText(String.valueOf(currently_walking) + " users");
                globalMiles.setText(String.valueOf(global_miles) + " miles");
                globalWalks.setText(String.valueOf(global_walks) + " walks");

                if (global_time >= 60){
                    globalTime.setText(global_time/60 + " hrs, " + global_time%60 + " mins");
                } else {
                    globalTime.setText(global_time + " mins");
                }

                break;
            }
            i++;

        }
    }

    private static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Getting your statistics...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject json = null;

            try {
                json = new JSONObject(result);

                Log.e("json result", result);

                setGlobalStats(json);

                if(this.dialog.isShowing())
                {
                    this.dialog.dismiss();
                }

            } catch (JSONException e) {
                Log.e("JSONException: ", e.toString());
            }

        }
    }

}
