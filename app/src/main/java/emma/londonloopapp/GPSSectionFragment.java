package emma.londonloopapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
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

    private long walkNumber;
    private Location mLastLocation;
    private boolean hasLocation;

    private GPSItem currentItem;

    private TextView mapNavText;
    private Button gpsButton;
    private Button pButton;
    private Button nButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gps_maps, container, false);

        walkNumber = getArguments().getLong("walkNumber", 0);

        db = new MySQLiteHelper(getActivity());

        final SectionItem sectionItem = db.getSection(walkNumber + 1);

        gpsItemList = db.getGPSItemFromSection(sectionItem);
        markerItemList = db.getMarkerItemFromSection(sectionItem);

        //set current item to first gpsItem
        currentItem = gpsItemList.get(1);

        //get buttons and textview from xml
        mapNavText = (TextView) rootView.findViewById(R.id.gpsMapText);

        gpsButton = (Button) rootView.findViewById(R.id.gpsButton);
        pButton = (Button) rootView.findViewById(R.id.preGPSButton);
        nButton = (Button) rootView.findViewById(R.id.nextGPSButton);

        //set on click listeners for pre/next buttons
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem.getIncr()==1){
                    //do nothing
                } else{
                    setText(prevItemWithNote(gpsItemList.get(currentItem.getIncr())));
                }

            }
        });

        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem.getIncr()==gpsItemList.size()){
                    //do nothing
                } else{
                    setText(nextItemWithNote(gpsItemList.get(currentItem.getIncr())));
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
            /*MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            mMap.addMarker(markerOptions);*/
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

            if (gpsItem.getIncr() == gpsItemList.size()){

                gpsButton.setText("What do you want to do now?");
                gpsButton.setVisibility(View.VISIBLE);
                gpsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
        return curr;
    }

    private GPSItem prevItemWithNote(GPSItem gpsItem){
        GPSItem curr = gpsItemList.get(gpsItem.getIncr()-1);
        while (curr.getNote().equals("")){
            //first item will always have a note
            curr = gpsItemList.get(curr.getIncr()-1);
        }
        return curr;
    }

    private void drawMarkers(){
        for (MarkerItem m : markerItemList){
            MarkerOptions options = new MarkerOptions().position(m.getLocation())
                    .title(m.getName()).snippet(m.getText());

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
}
