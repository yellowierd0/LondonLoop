package emma.londonloopapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Calendar;

/**
 * Created by Emma on 06/05/2015.
 */
public class RouteListFragment extends ListFragment {

    MySQLiteHelper db;

    private Activity activity;

    private static final String LOG = "NavHelper";

    private static String BASE_URL = "http://transportapi.com/v3/uk/public/journey";

    private static String API_KEY = "api_key=377843b343d1e052ac4d024fd9b7c93a";
    private static String APP_ID = "&app_id=6109f899";

    private static String FROM_LL = "/from/lonlat:";
    private static String TO_LL = "/to/lonlat:";
    private static String FROM_STOP = "/from/stop:";
    private static String FROM_POSTCODE = "/from/postcode:";
    private static String TO_STOP = "/to/stop:";
    private static String TO_POSTCODE = "/to/postcode:";
    private static String RESPONSE_TYPE = ".json?";

    //private static String test_url = "http://transportapi.com/v3/uk/public/journey/from/lonlat:0.191433,51.516886/to/lonlat:-0.1276250,51.503363051.json?api_key=377843b343d1e052ac4d024fd9b7c93a&app_id=6109f899";

    private long walkNumber;
    private String modes;

    //editable location
    private String location;

    //type: 0=to walk, 1=from walk
    int type;

    private Location mLastLocation;

    boolean hasLocation = false;

    private ArrayList<RouteItem> routeItems;
    private SectionItem sectionItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new MySQLiteHelper(getActivity());

        sectionItem = db.getSection(walkNumber + 1);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                mLastLocation = location;

                hasLocation = true;

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        if (type==0){
            if (location.equals("")){
                new LocationControl().execute();
            } else {
                planCustomJourneyToWalk(location, sectionItem);
            }
        } else {
            planJourneyFromWalk(sectionItem, location);
        }

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
        getListView().setBackgroundColor(getResources().getColor(R.color.primary_500));
        getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item

    }

    private void planJourneyToWalk(Location currentLocation, SectionItem destination){

        String from = FROM_LL + currentLocation.getLongitude() + "," + currentLocation.getLatitude();
        String api_key = API_KEY + APP_ID;
        String to = TO_LL + destination.getStartNode().getLongitude() + "," + destination.getStartNode().getLatitude();

        String include = "&modes=" + modes;


        String getURL = BASE_URL + from + to + RESPONSE_TYPE + api_key + include;

        Log.e(LOG, getURL);

        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute(getURL);

    }

    private void planCustomJourneyToWalk(String currentLocation, SectionItem destination){

        String api_key = API_KEY + APP_ID;
        String to = TO_LL + destination.getStartNode().getLongitude() + "," + destination.getStartNode().getLatitude();

        String include = "&modes=" + modes;

        String getURL = BASE_URL + getLocation(currentLocation) + to + RESPONSE_TYPE + api_key + include;

        Log.e(LOG, getURL);

        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute(getURL);

    }

    private void planJourneyFromWalk(SectionItem startLocation, String destination){

        String from = FROM_LL + startLocation.getEndNode().getLongitude() + "," + startLocation.getEndNode().getLatitude();
        String api_key = API_KEY + APP_ID;

        String to = getLocation(destination);

        String include = "&modes=" + modes;

        String getURL = BASE_URL + from + to + RESPONSE_TYPE + api_key + include;

        Log.e(LOG, getURL);

        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute(getURL);

    }

    private String getLocation(String loc){

        String out = "";
        String pattern = "(([gG][iI][rR] {0,}0[aA]{2})|((([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y]?[0-9][0-9]?)|(([a-pr-uwyzA-PR-UWYZ][0-9][a-hjkstuwA-HJKSTUW])|([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y][0-9][abehmnprv-yABEHMNPRV-Y]))) {0,}[0-9][abd-hjlnp-uw-zABD-HJLNP-UW-Z]{2}))$";
        if (loc.matches(pattern)){
            if (type==0){
                out += FROM_POSTCODE;
            } else {
                out += TO_POSTCODE;
            }
            if (loc.contains(" ")){
                out += loc.replaceAll("\\s+", "+");
            } else {
                if (loc.length() == 5){
                    out += loc.substring(0, 2) + "+" + loc.substring(2,5);
                } else if (loc.length() == 6) {
                    out += loc.substring(0, 3) + "+" + loc.substring(3,6);
                } else {
                    out += loc.substring(0, 4) + "+" + loc.substring(4,7);
                }
            }
        } else {
            if (type==0){
                out += FROM_STOP;
            } else {
                out += TO_STOP;
            }
            out += loc.replaceAll("\\s+", "+");
        }
        return out;

    }


    public static RouteListFragment newInstance(long walk, String modes, String location, int type)
    {
        RouteListFragment f = new RouteListFragment();
        f.setSomeObject(walk, modes, location, type);
        return f;
    }


    public void setSomeObject(long walk, String modes, String location, int type) {

        this.walkNumber = walk;
        this.modes= modes;
        this.location = location;
        this.type = type;
    }

    private class LocationControl extends AsyncTask<Context, Void, Void>
    {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        protected void onPreExecute()
        {
            this.dialog.setMessage("Determining your location...");
            this.dialog.show();
        }

        protected Void doInBackground(Context... params)
        {

            Long t = Calendar.getInstance().getTimeInMillis();
            while (!hasLocation && Calendar.getInstance().getTimeInMillis() - t < 30000) {
                try {
                    Thread.sleep(Long.valueOf(1000));
                } catch (InterruptedException e) {
                    Toast.makeText(getActivity(), "Location not found, please check internet connection and try again", Toast.LENGTH_SHORT).show();
                    if(this.dialog.isShowing())
                    {
                        this.dialog.dismiss();
                    }
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            };
            return null;
        }

        protected void onPostExecute(final Void unused)
        {
            if(this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }

            if (mLastLocation!=null){
                //does the stuff that requires current location
                planJourneyToWalk(mLastLocation, sectionItem);
            } else {
                Toast.makeText(getActivity(), "Location not found, please check internet connection and try again", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }

    }

    private ArrayList<RouteItem> getRouteItems(JSONObject jsonObject) throws JSONException {

        JSONArray routeArray = jsonObject.getJSONArray("routes");
        routeItems = new ArrayList<RouteItem>();

        for (int i = 0; i < routeArray.length(); i++){
            String duration = routeArray.getJSONObject(i).getString("duration");
            JSONArray partArray = routeArray.getJSONObject(i).getJSONArray("route_parts");

            RoutePart[] routeParts = new RoutePart[partArray.length()];

            for (int j = 0; j < partArray.length(); j++){

                String mode = partArray.getJSONObject(j).getString("mode");
                String from_point_name = partArray.getJSONObject(j).getString("from_point_name");
                String to_point_name = partArray.getJSONObject(j).getString("to_point_name");
                String destination = partArray.getJSONObject(j).getString("destination");
                String line_name = partArray.getJSONObject(j).getString("line_name");
                String part_duration = partArray.getJSONObject(j).getString("duration");
                String departure_time = partArray.getJSONObject(j).getString("departure_time");
                String arrival_time = partArray.getJSONObject(j).getString("arrival_time");
                String coordinates = partArray.getJSONObject(j).getString("coordinates");
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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        protected void onPreExecute()
        {
            this.dialog.setMessage("Planning routes...");
            this.dialog.show();
        }

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

                routeItems = getRouteItems(json);
                setListAdapter(new RouteAdapterItem(getActivity(), routeItems, getActivity(), sectionItem.getId(), type));
                if(this.dialog.isShowing())
                {
                    this.dialog.dismiss();
                }

            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Unable to retrieve routes, please check internet connection and try again", Toast.LENGTH_SHORT).show();
                if(this.dialog.isShowing())
                {
                    this.dialog.dismiss();
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }
}
