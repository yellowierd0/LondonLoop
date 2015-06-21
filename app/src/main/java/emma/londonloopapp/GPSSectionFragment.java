package emma.londonloopapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Emma on 14/05/2015.
 */
public class GPSSectionFragment extends Fragment {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MySQLiteHelper db;
    private Map<Integer, GPSItem> gpsItemList;
    private List<MarkerItem> markerItemList;

    private static String POST_URL = "http://146.169.46.77:55000/putStat.php";

    private long walkNumber;
    private Location mLastLocation;
    private boolean hasLocation;

    private int global_time;
    private double global_miles;
    private int currently_walking = 0;
    private int global_walks = 0;
    private int id = 0;

    private GPSItem currentItem;

    private Date startDate;
    private DateFormat dateFormat;
    private StatItem statItem;
    private StatItem globalStat;

    private List<NameValuePair> params;

    private TextView mapNavText;
    private Button gpsButton;
    private Button pButton;
    private Button nButton;

    private int currentNo;
    private SectionItem sectionItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gps_maps, container, false);

        //initialize layout
        walkNumber = getArguments().getLong("walkNumber", 0);

        db = new MySQLiteHelper(getActivity());

        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        startDate = new Date();

        statItem = db.getStatItem(walkNumber);
        globalStat = db.getStatItem(0);

        sectionItem = db.getSection(walkNumber + 1);

        gpsItemList = db.getGPSItemFromSection(sectionItem);
        markerItemList = db.getMarkerItemFromSection(sectionItem);

        //set current item to first gpsItem
        currentItem = gpsItemList.get(1);
        currentNo = currentItem.getIncr();

        //get buttons and textview from xml
        mapNavText = (TextView) rootView.findViewById(R.id.gpsMapText);

        gpsButton = (Button) rootView.findViewById(R.id.gpsButton);
        pButton = (Button) rootView.findViewById(R.id.preGPSButton);
        nButton = (Button) rootView.findViewById(R.id.nextGPSButton);

        //initialise text
        mapNavText.setText(currentItem.getNote());

        //set on click listeners for pre/next buttons
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem.getIncr()==1){
                    //do nothing
                } else{
                    setText(prevItemWithNote(gpsItemList.get(currentNo)));
                }

            }
        });

        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem.getIncr()==gpsItemList.size()){
                    //do nothing
                } else{
                    setText(nextItemWithNote(gpsItemList.get(currentNo)));
                }
            }
        });


        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                mLastLocation = location;

                hasLocation = true;

                setMapText();

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        setUpMapIfNeeded();

        drawPaths();

        drawMarkers();

        params = new ArrayList<NameValuePair>();

        return rootView;
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (myLocation!= null){
            // Get latitude of the current location
            double latitude = myLocation.getLatitude();

            // Get longitude of the current location
            double longitude = myLocation.getLongitude();

            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Show the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));
        }

    }

    private void drawPaths( )
    {
        if ( mMap == null )
        {
            return;
        }

        ArrayList<LatLng> points = new ArrayList<LatLng>();
        PolylineOptions polyLineOptions = new PolylineOptions();

        for (int i = 1; i <= gpsItemList.size(); i++){

            LatLng latLng = gpsItemList.get(i).getLatLng();
            points.add(latLng);
            if (!(gpsItemList.get(i).getNote().equals(""))){
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                if(i==gpsItemList.size()){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.push_pin_blue));
                }


                mMap.addMarker(markerOptions);

            }
        }

        //DrawArrowHead(mMap, points.get(0), points.get(1));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(points.get(0)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        polyLineOptions.addAll(points);
        polyLineOptions.width(3);
        polyLineOptions.color(Color.BLUE);
        mMap.addPolyline(polyLineOptions);

    }

    private void setMapText(){

        mapNavText.setText(currentItem.getNote());

        for (int i = 2; i <= gpsItemList.size(); i++){
            Location l = new Location("gpsCoord");
            l.setLatitude(gpsItemList.get(i).getLatLng().latitude);
            l.setLongitude(gpsItemList.get(i).getLatLng().longitude);
            if (gpsItemList.get(i) != currentItem && mLastLocation.distanceTo(l) < 100){
                setText(gpsItemList.get(i));
            }
        }
    }

    private void setText(GPSItem gpsItem){

        if (gpsItem.getNote().equals("")){

        } else{
            mapNavText.setText(gpsItem.getNote());
            currentItem = gpsItem;
            currentNo = gpsItem.getIncr();
            //if final gpsItem
            if (gpsItem.getIncr() == gpsItemList.size()){

                gpsButton.setText("What do you want to do now?");
                gpsButton.setVisibility(View.VISIBLE);
                gpsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Date date = new Date();
                        long diff = date.getTime() - startDate.getTime();
                        long minutes = diff / (60 * 1000) % 60;

                        //save stats internally
                        statItem.setMiles(statItem.getMiles() + sectionItem.getMiles());
                        statItem.setTime(statItem.getTime() + minutes);
                        statItem.setCompleted(statItem.getCompleted() + 1);
                        globalStat.setMiles(globalStat.getMiles() + sectionItem.getMiles());
                        globalStat.setTime(globalStat.getTime() + minutes);
                        globalStat.setCompleted(globalStat.getCompleted() + 1);
                        db.updateStatItem(statItem);
                        db.updateStatItem(globalStat);

                        //save stats online
                        //new putDataTask().execute();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                        EndWalkFragment wdf = EndWalkFragment.newInstance(walkNumber);

                        fragmentManager.beginTransaction()
                                .add(R.id.container, wdf)
                                        // Add this transaction to the back stack
                                .addToBackStack("gpsFrag")
                                .commit();
                    }
                });
            }

        }
    }


    private GPSItem nextItemWithNote(GPSItem gpsItem){
        GPSItem curr = gpsItemList.get(gpsItem.getIncr()+1);
        while (curr.getNote().equals("")){
            //last item will always have a note
            curr = gpsItemList.get(curr.getIncr()+1);
        }
        currentNo = curr.getIncr();
        return curr;
    }

    private GPSItem prevItemWithNote(GPSItem gpsItem){
        GPSItem curr = gpsItemList.get(gpsItem.getIncr()-1);
        while (curr.getNote().equals("")){
            //first item will always have a note
            curr = gpsItemList.get(curr.getIncr()-1);
        }
        currentNo = curr.getIncr();
        return curr;
    }

    private void drawMarkers(){
        for (MarkerItem m : markerItemList){
            MarkerOptions options = new MarkerOptions().position(m.getLocation())
                    .title(m.getName()).snippet(m.getText())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.push_pin));

            mMap.addMarker(options);
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                int pos = getMarkerPos(marker.getTitle());

                if (markerItemList.get(pos).getUrl().equals("")) {
                    //do nothing
                } else {
                    Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(markerItemList.get(pos).getUrl()));
                    startActivity(browser);
                }
            }
        });

    }

    private int getMarkerPos(String s){
        Matcher matcher = Pattern.compile("\\d+").matcher(s);
        matcher.find();
        return Integer.valueOf(matcher.group()) - 1;
    }

    public static GPSSectionFragment newInstance(long walk)
    {
        GPSSectionFragment f = new GPSSectionFragment();
        final Bundle bdl = new Bundle(1);
        bdl.putLong("walkNumber", walk);
        f.setArguments(bdl);
        return f;

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

    private void setGlobalStats(JSONObject jsonObject) throws JSONException {


        JSONArray jsonArray = jsonObject.getJSONArray("statistics");
        int i = 0;
        while (i < jsonArray.length()){
            JSONObject global = jsonArray.getJSONObject(i);
            if (global.getString("id").equals(String.valueOf(id))){

                currently_walking = Integer.valueOf(global.getString("currently_walking")) + 1;
                global_miles = Integer.valueOf(global.getString("miles_walked"));
                global_time = Integer.valueOf(global.getString("walk_time"));
                global_walks = Integer.valueOf(global.getString("walks_completed"));

                Log.e("getStats request",
                        "current: " + currently_walking +
                                "walked: " + global_miles +
                                "time: " + global_time +
                                " completed: " + global_walks);


                break;
            }
            i++;

        }
    }
/*
    private class insertDATA extends AsyncTask<String, String, String> {

        InputStream is = null;
        @Override
        protected String doInBackground(String... arg0) {
            ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();

            values.add(new BasicNameValuePair("id", String.valueOf(id)));
            values.add(new BasicNameValuePair("currently_walking", String.valueOf(currently_walking)));
            values.add(new BasicNameValuePair("miles_walked", String.valueOf(global_miles)));
            values.add(new BasicNameValuePair("walk_time",String.valueOf(global_time)));
            values.add(new BasicNameValuePair("walks_completed",String.valueOf(global_walks)));


            try {
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(POST_URL);
                httppost.setEntity(new UrlEncodedFormEntity(values));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.i("TAG", "Connection Successful");
            } catch (Exception e) {
                Log.i("TAG", e.toString());
                //Invalid Address
            }

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.i(“TAG”, “Result Retrieved”);
            } catch (Exception e) {
                Log.i(“TAG”, e.toString());
            }

            try {
                JSONObject json = new JSONObject(result);
                code = (json.getInt(“code”));
                if (code == 1) {
                    Log.i(“msg”, “Data Successfully Inserted”);
                    //Data Successfully Inserted
                } else {
                    //Data Not Inserted
                }
            } catch (Exception e) {
                Log.i(“TAG”, e.toString());
            }
            return null;
        }

    }
*/
}
