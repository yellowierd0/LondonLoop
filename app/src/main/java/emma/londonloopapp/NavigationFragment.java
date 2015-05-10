package emma.londonloopapp;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Emma on 06/05/2015.
 */
public class NavigationFragment extends Fragment {

    MySQLiteHelper db;

    private Activity activity;

    private static final String LOG = "NavHelper";

    private TextView output;

    private static String BASE_URL = "http://transportapi.com/v3/uk/public/journey";

    private static String API_KEY = "377843b343d1e052ac4d024fd9b7c93a";
    private static String APP_ID = "6109f899";

    private static String test_url = "http://transportapi.com/v3/uk/public/journey/from/lonlat:0.191433,51.516886/to/lonlat:-0.1276250,51.503363051.json?api_key=377843b343d1e052ac4d024fd9b7c93a&app_id=6109f899";

    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_navigation, container, false);

        long walkNumber = getArguments().getLong("walkNumber", 0);

        db = new MySQLiteHelper(getActivity());

        final SectionItem sectionItem = db.getSection(walkNumber + 1);

        this.output = (TextView) rootView.findViewById(R.id.output);

        buildGoogleApiClient();

        planJourney(sectionItem, output);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) getActivity())
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) getActivity())
                .addApi(LocationServices.API)
                .build();
    }

    private void planJourney(SectionItem destination, TextView output){

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Get Current Location
        //Location myLocation = locationManager.getLastKnownLocation(provider);
        Location myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (myLocation != null){
            String from = "/from/lonlat:" + myLocation.getLongitude() + "," + myLocation.getLatitude();
            String api_key = "?api_key=377843b343d1e052ac4d024fd9b7c93a&app_id=6109f899";
            String to = "/to/lonlat:" + destination.getStartNode().getLongitude() + "," + destination.getStartNode().getLatitude();
            String endUrl = ".json";

            String getURL = BASE_URL + from + to + endUrl + api_key;

            // call AsynTask to perform network operation on separate thread
            new HttpAsyncTask().execute(getURL);
        } else {
            output.setText("Can't find current Location");
        }

    }

    public static String GET(String url){
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

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity().getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            JSONObject json = null;

            try {
                json = new JSONObject(result);
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                StartWalkFragment wdf = new StartWalkFragment();

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("startFrag")
                        .commit();


                output.setText(json.toString(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public static NavigationFragment newInstance(long walk)
    {
        NavigationFragment f = new NavigationFragment();
        final Bundle bdl = new Bundle(1);
        bdl.putLong("walkNumber", walk);
        f.setArguments(bdl);
        return f;

    }
}
