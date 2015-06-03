package emma.londonloopapp;

import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapNavFragment extends Fragment {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private RouteItem routeItem;
    private Location myLocation;

    private TextView mapNavText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);


        mapNavText = (TextView) rootView.findViewById(R.id.mapText);


        /*// Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                for (RoutePart routePart : routeItem.getRouteParts()){
                    if (location.distanceTo(routePart.getCoordinates().get(1)) < 10){
                        mapNavText.setText(routePart.getTo_point_name());
                    }
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
*/
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
        myLocation = locationManager.getLastKnownLocation(provider);

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

        drawPaths();

    }

    private void drawPaths( )
    {
        RoutePart[] routeParts = routeItem.getRouteParts();


        if ( mMap == null )
        {
            return;
        }

        ArrayList<LatLng> points = null;
        PolylineOptions polyLineOptions = null;

        for (int i = 0; i < routeParts.length; i++){

            points = new ArrayList<LatLng>();
            polyLineOptions = new PolylineOptions();
            ArrayList<Location> coordinates = routeParts[i].getCoordinates();

            for (int j = 0; j < coordinates.size(); j++){
                LatLng position = new LatLng(coordinates.get(j).getLatitude(),
                        coordinates.get(j).getLongitude());
                points.add(position);

                if (j == 0){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(position)
                            .title(routeParts[i].getFrom_point_name());
                    StringBuilder sb = new StringBuilder();
                    sb.append(getModeName(routeParts[i].getMode()));
                    sb.append(" from ");
                    sb.append(routeParts[i].getFrom_point_name());
                    if (!(routeParts[i].getMode().equals("foot"))){
                        sb.append(" towards ");
                        sb.append(routeParts[i].getDestination());
                    }
                    sb.append(" to ");
                    sb.append(routeParts[i].getTo_point_name());
                    if(!(routeParts[i].getLine_name().equals("")) && !(routeParts[i].getMode().equals("train"))){
                        sb.append(" (");
                        sb.append(routeParts[i].getLine_name());
                        sb.append(") ");
                    }
                    markerOptions.snippet(sb.toString());
                    mMap.addMarker(markerOptions);
                }

                if (i == routeParts.length-1 && j == coordinates.size()-1){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(position)
                            .title(routeParts[i].getTo_point_name());
                    markerOptions.snippet("Your destination!");
                    mMap.addMarker(markerOptions);
                }
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(),myLocation.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

            polyLineOptions.addAll(points);
            polyLineOptions.width(2);
            polyLineOptions.color(Color.BLUE);
            mMap.addPolyline(polyLineOptions);
        }

    }


    public static MapNavFragment newInstance(RouteItem routeItem) {
        MapNavFragment fragment = new MapNavFragment();
        fragment.setSomeObject(routeItem);
        return fragment;
    }

    public void setSomeObject(RouteItem someObject) {

        this.routeItem = someObject;
    }

    private String getModeName(String mode){
        if (mode.equals("foot")){
            return "Walk";
        } else if (mode.equals("tube")){
            return "Take the tube";
        } else if (mode.equals("dlr")) {
            return "Take the DLR";
        } else if (mode.equals("bus")) {
            return "Take the bus";
        } else if (mode.equals("tram")) {
            return "Take the tram";
        } else if (mode.equals("train")) {
            return "Take the train";
        } else if (mode.equals("overground")) {
            return "Take the overground";
        } else if (mode.equals("boat")) {
            return "Take the river boat";
        } else if (mode.equals("wait")) {
            return "Wait";
        } else {
            return "Other";
        }
    }

}
