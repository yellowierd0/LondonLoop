package emma.londonloopapp;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emma on 06/05/2015.
 */
public class NavigationFragment extends ListFragment {

    MySQLiteHelper db;

    private Activity activity;

    private static final String LOG = "NavHelper";

    private static String BASE_URL = "http://transportapi.com/v3/uk/public/journey";

    private static String API_KEY = "377843b343d1e052ac4d024fd9b7c93a";
    private static String APP_ID = "6109f899";

    private static String test_url = "http://transportapi.com/v3/uk/public/journey/from/lonlat:0.191433,51.516886/to/lonlat:-0.1276250,51.503363051.json?api_key=377843b343d1e052ac4d024fd9b7c93a&app_id=6109f899";

    private boolean planRoute = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long walkNumber = getArguments().getLong("walkNumber", 0);

        db = new MySQLiteHelper(getActivity());

        final SectionItem sectionItem = db.getSection(walkNumber + 1);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                if (planRoute == false){
                    planJourney(location, sectionItem);
                    planRoute = true;
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setBackgroundColor(getResources().getColor(R.color.primary_100));
        getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        /*SectionItem item = db.getSection(position + 1);
        // do something
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        WalkDetailFragment wdf= WalkDetailFragment.newInstance(item.getId()-1);

        fragmentManager.beginTransaction()
                .add(R.id.container, wdf)
                        // Add this transaction to the back stack
                .addToBackStack("walksFragment")
                .commit();
*/
    }

    private void planJourney(Location currentLocation, SectionItem destination){

        if (currentLocation != null) {

            String from = "/from/lonlat:" + currentLocation.getLongitude() + "," + currentLocation.getLatitude();
            String api_key = "?api_key=377843b343d1e052ac4d024fd9b7c93a&app_id=6109f899";
            String to = "/to/lonlat:" + destination.getStartNode().getLongitude() + "," + destination.getStartNode().getLatitude();
            String endUrl = ".json";

            String getURL = BASE_URL + from + to + endUrl + api_key;

            // call AsynTask to perform network operation on separate thread
            new HttpAsyncTask().execute(getURL);

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
            JSONObject json = null;

            try {
                json = new JSONObject(result);

                List<RouteItem> routeItems = getRouteItems(json);
                setListAdapter(new RouteAdapterItem(getActivity(), routeItems));
                // Perform action on click
                /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                StartWalkFragment wdf = new StartWalkFragment();

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("startFrag")
                        .commit();
*/

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private List<RouteItem> getRouteItems(JSONObject jsonObject) throws JSONException {

        JSONArray routeArray = jsonObject.getJSONArray("routes");
        ArrayList<RouteItem> routeItems = new ArrayList<RouteItem>();

        for (int i = 0; i < routeArray.length(); i++){
            String duration = routeArray.getJSONObject(i).getString("duration");
            JSONArray partArray = routeArray.getJSONObject(i).getJSONArray("route_parts");

            RoutePart[] routeParts = new RoutePart[partArray.length()];

            for (int j = 0; j < partArray.length(); j++){

                String mode = partArray.getJSONObject(i).getString("mode");
                String from_point_name = partArray.getJSONObject(i).getString("from_point_name");
                String to_point_name = partArray.getJSONObject(i).getString("to_point_name");
                String destination = partArray.getJSONObject(i).getString("destination");
                String line_name = partArray.getJSONObject(i).getString("line_name");
                String part_duration = partArray.getJSONObject(i).getString("duration");
                String departure_time = partArray.getJSONObject(i).getString("departure_time");
                String arrival_time = partArray.getJSONObject(i).getString("arrival_time");
                String coordinates = partArray.getJSONObject(i).getString("coordinates");
                ArrayList<Location> locations = convertStringToCoord(coordinates);

                RoutePart rp = new RoutePart(mode, from_point_name, to_point_name, destination, line_name, part_duration, departure_time, arrival_time, locations);
                routeParts[j] = rp;
            }
            routeItems.add(new RouteItem(duration, routeParts));
        }

        return routeItems;

    }

    private ArrayList<Location> convertStringToCoord(String coordinates){

        String delims = "[ \\[\\],]+";
        String[] tokens = coordinates.split(delims);
        ArrayList<Location> locations = new ArrayList<Location>();
        for (int i = 1; i < tokens.length; i++){
            if (i % 2 != 0){
                Location l = new Location("TfL journey planning API");
                l.setLongitude(Double.parseDouble(tokens[i]));
                l.setLatitude(Double.parseDouble(tokens[i+1]));
                locations.add(l);
            }
        }
        return locations;
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
