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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

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

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));


        //locationList
        String[] locationList = {getString(R.string.loop1),getString(R.string.loop2),
                getString(R.string.loop3),getString(R.string.loop4),getString(R.string.loop5),
                getString(R.string.loop6),getString(R.string.loop7),getString(R.string.loop8),
                getString(R.string.loop9),getString(R.string.loop10),getString(R.string.loop11),
                getString(R.string.loop12),getString(R.string.loop13),getString(R.string.loop14),
                getString(R.string.loop15),getString(R.string.loop16),getString(R.string.loop17),
                getString(R.string.loop18),getString(R.string.loop19),getString(R.string.loop20),
                getString(R.string.loop21),getString(R.string.loop22),getString(R.string.loop23),
                getString(R.string.loop24)};

        //markerList
        Double[][] markerList = {
                {51.483144, 0.177975}, {51.441233, 0.148956}, {51.393209,0.069081},
                {51.370944,0.004860}, {51.319035,-0.063420}, {51.315728,-0.136744},
                {51.332148,-0.209290}, {51.351650,-0.250176}, {51.411854,-0.308274},
                {51.469927,-0.409793}, {51.505117,-0.418654}, {51.550933,-0.483414},
                {51.610477,-0.498761}, {51.623932,-0.427529}, {51.610702,-0.380326},
                {51.653365,-0.281950}, {51.652244,-0.148998}, {51.668264,-0.028316},
                {51.634306,0.012118}, {51.621468,0.078004}, {51.616860,0.183245},
                {51.593421,0.234098}, {51.559197,0.236748}, {51.516886,0.191433}};

        // add locations of start/end points
        addMarkers(locationList, markerList);

    }

    private void addMarkers(String[] locationList, Double[][] markerList){

        MarkerOptions markers[] = new MarkerOptions[24];

        for (int i = 0; i < locationList.length ; i++){

            markers[i] = new MarkerOptions().position(new LatLng(markerList[i][0], markerList[i][1])).title(i+". "+locationList[i]);

            mMap.addMarker(markers[i]);

            mMap.setOnInfoWindowClickListener(
                    new GoogleMap.OnInfoWindowClickListener(){
                        public void onInfoWindowClick(Marker marker){

                            // retrieve theListView item
                            //WalkViewItem item = mItems.get(position);
                            // do something
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            Fragment fragment = new WalkDetailFragment();
                            fragmentManager.beginTransaction()
                                    .add(R.id.container, fragment)
                                            // Add this transaction to the back stack
                                    .addToBackStack("fragBack")
                                    .commit();
                        }
                    }
            );
        }



    }

}
