package emma.londonloopapp;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapsFragment extends Fragment {

    private MySQLiteHelper db;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private final int WALK_NUMBERS = 24;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        db = new MySQLiteHelper(getActivity());


        TextView mapInfo = (TextView) rootView.findViewById(R.id.mapText);

        mapInfo.setText("Please choose a walk - blue for South London, green for North-West London, and yellow for North-East London");

        setUpMapIfNeeded();
        return rootView;
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
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

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(51.505665, -0.138428);

        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        // add locations of start/end points
        addMarkers();

    }

    private void addMarkers(){

        final MarkerOptions markers = new MarkerOptions();

        for  (int i = 1; i <= WALK_NUMBERS; i++){

            SectionItem w = db.getSection(Long.valueOf(i));

            MarkerOptions m = new MarkerOptions().position(new LatLng(w.getStartNode().getLatitude(),
                    w.getStartNode().getLongitude())).title(w.getId() + ". " +
                    w.getStartNode().getName() + " to " + w.getEndNode().getName());

            if (w.getId() < 11){
                m.icon(BitmapDescriptorFactory.fromResource(R.drawable.loop_marker_blue));
            } else if (w.getId() < 17){
                m.icon(BitmapDescriptorFactory.fromResource(R.drawable.loop_marker_green));
            } else {
                m.icon(BitmapDescriptorFactory.fromResource(R.drawable.loop_marker_yellow));
            }


            mMap.addMarker(m);

        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                WalkDetailFragment wdf= WalkDetailFragment.newInstance(getMarkerPos(marker.getTitle()));
                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                        // Add this transaction to the back stack
                        .addToBackStack("mapFrag")
                        .commit();

            }});

    }

    private long getMarkerPos(String s){
        Matcher matcher = Pattern.compile("\\d+").matcher(s);
        matcher.find();
        return Long.valueOf(matcher.group()) - 1;
    }


}
