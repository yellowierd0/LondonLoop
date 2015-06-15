package emma.londonloopapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

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

/**
 * Created by Emma on 06/05/2015.
 */
public class EndRouteListFragment extends ListFragment {

    MySQLiteHelper db;

    private Activity activity;

    private static final String LOG = "NavHelper";

    private static String BASE_URL = "http://transportapi.com/v3/uk/public/journey";

    private static String API_KEY = "api_key=377843b343d1e052ac4d024fd9b7c93a";
    private static String APP_ID = "&app_id=6109f899";

    private static String FROM_URL = "/from/lonlat:";
    private static String TO_URL = "/to/lonlat:";
    private static String TO_STOP = "/to/stop:";
    private static String TO_POSTCODE = "/to/postcode:";
    private static String RESPONSE_TYPE = ".json?";

    private static String test_url = "http://transportapi.com/v3/uk/public/journey/from/lonlat:0.191433,51.516886/to/lonlat:-0.1276250,51.503363051.json?api_key=377843b343d1e052ac4d024fd9b7c93a&app_id=6109f899";

    private long walkNumber;
    private String modes;

    private String destination;

    private ArrayList<RouteItem> routeItems;
    private SectionItem sectionItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new MySQLiteHelper(getActivity());

        sectionItem = db.getSection(walkNumber + 1);
        planJourney(new LatLng(sectionItem.getEndNode().getLatitude(),sectionItem.getEndNode().getLongitude()),destination);

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

    private void planJourney(LatLng currentLocation, String destination){

        String from = FROM_URL + currentLocation.longitude + "," + currentLocation.latitude;
        String api_key = API_KEY + APP_ID;
        String to = toUrl(destination);

        String include = "&modes=" + modes;

        String getURL = BASE_URL + from + to + RESPONSE_TYPE + api_key + include;
        Log.e("transport api request", getURL);

        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute(getURL);

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
                setListAdapter(new EndRouteAdapterItem(getActivity(), routeItems, getActivity()));
                if(this.dialog.isShowing())
                {
                    this.dialog.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
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


    public static EndRouteListFragment newInstance(long walk, String modes, String destination)
    {
        EndRouteListFragment f = new EndRouteListFragment();
        f.setSomeObject(walk, modes, destination);
        return f;
    }


    public void setSomeObject(long walk, String modes, String destination) {

        this.walkNumber = walk;
        this.modes= modes;
        this.destination = destination;
    }

    private String toUrl(String loc){
        String out = "";
        String pattern = "(([gG][iI][rR] {0,}0[aA]{2})|((([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y]?[0-9][0-9]?)|(([a-pr-uwyzA-PR-UWYZ][0-9][a-hjkstuwA-HJKSTUW])|([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y][0-9][abehmnprv-yABEHMNPRV-Y]))) {0,}[0-9][abd-hjlnp-uw-zABD-HJLNP-UW-Z]{2}))$";
        if (loc.matches(pattern)){
            out = TO_POSTCODE;
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
            out = TO_STOP + loc.replaceAll("\\s+", "+");
        }
        return out;

    }

}
