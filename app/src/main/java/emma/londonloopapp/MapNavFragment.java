package emma.londonloopapp;

import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapNavFragment extends Fragment {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private RouteItem routeItem;
    private Location mLastLocation;
    private long walkNumber;
    private int type;

    private Button pButton;
    private Button nButton;
    private Button gpsButton;
    private TextView mapNavText;

    private RoutePart[] routeParts;
    private RoutePart currentRoute;
    private int currentRouteNo = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gps_maps, container, false);

        routeParts = routeItem.getRouteParts();
        currentRoute = routeParts[0];

        mapNavText = (TextView) rootView.findViewById(R.id.gpsMapText);
        mapNavText.setText(buildJourneyText(currentRoute));
        gpsButton = (Button) rootView.findViewById(R.id.gpsButton);
        nButton = (Button) rootView.findViewById(R.id.nextGPSButton);
        pButton = (Button) rootView.findViewById(R.id.preGPSButton);


        //set on click listeners for buttons
        gpsButton.setOnClickListener(new View.OnClickListener(){
                 @Override
                 public void onClick(View v) {
                     FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                     YesNoFragment wdf = YesNoFragment.newInstance(walkNumber);
                     fragmentManager.beginTransaction()
                             .add(R.id.container, wdf)
                                     // Add this transaction to the back stack
                             .addToBackStack("mapnavFrag")
                             .commit();

                 }
             }
        );

        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentRouteNo == 0) {
                    //do nothing
                } else {
                    currentRouteNo--;
                    mapNavText.setText(buildJourneyText(routeParts[currentRouteNo]));
                }

            }
        });

        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentRouteNo == routeParts.length-1) {
                    //do nothing
                    currentRouteNo++;
                    if (type==0){
                        mapNavText.setText("You have now reached the London Loop.");
                        gpsButton.setText("What do you want to do now?");
                        gpsButton.setVisibility(View.VISIBLE);
                    } else {
                        mapNavText.setText("You have now reached your destination!");
                    }

                } else if (currentRouteNo < routeParts.length-1) {
                    currentRouteNo++;
                    mapNavText.setText(buildJourneyText(routeParts[currentRouteNo]));
                } else {
                    // do nothing
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
        mLastLocation = locationManager.getLastKnownLocation(provider);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        double latitude = mLastLocation.getLatitude();

        // Get longitude of the current location
        double longitude = mLastLocation.getLongitude();

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
                            .title(routeParts[i].getFrom_point_name())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.push_pin_blue));

                    markerOptions.snippet(buildJourneyText(routeParts[i]));
                    mMap.addMarker(markerOptions);
                }

                if (i == routeParts.length-1 && j == coordinates.size()-1){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(position)
                            .title(routeParts[i].getTo_point_name())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
                    markerOptions.snippet("Your destination!");
                    mMap.addMarker(markerOptions);
                }
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

            polyLineOptions.addAll(points);
            polyLineOptions.width(2);
            polyLineOptions.color(Color.BLUE);
            mMap.addPolyline(polyLineOptions);
        }

    }


    public static MapNavFragment newInstance(RouteItem routeItem, long walkNumber, int type) {
        MapNavFragment fragment = new MapNavFragment();
        fragment.setSomeObject(routeItem, walkNumber, type);
        return fragment;
    }

    public void setSomeObject(RouteItem someObject, long walkNumber, int type) {

        this.type = type;
        this.routeItem = someObject;
        this.walkNumber = walkNumber;
    }

    private String buildJourneyText(RoutePart r){
        StringBuilder sb = new StringBuilder();
        sb.append(getModeName(r.getMode()));
        sb.append(" from ");
        sb.append(r.getFrom_point_name());
        if (!(r.getMode().equals("foot"))){
            sb.append(" towards ");
            sb.append(r.getDestination());
        }
        sb.append(" to ");
        sb.append(r.getTo_point_name());
        if(!(r.getLine_name().equals("")) && !(r.getMode().equals("train"))){
            sb.append(" (");
            sb.append(r.getLine_name());
            sb.append(") ");
        }
        return sb.toString();
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


    private void setMapText(){


        for (int i = 0; i < routeParts.length; i++){
            if (routeParts[i] != currentRoute && mLastLocation.distanceTo(routeParts[i].getCoordinates().get(0)) < 100){
                if (routeParts[i] != routeParts[routeParts.length-1]){
                    mapNavText.setText(buildJourneyText(routeParts[i]));
                    currentRoute = routeParts[i];
                    currentRouteNo = i;
                } else {
                    if (type==0){
                        mapNavText.setText("You have now reached the London Loop.");
                        gpsButton.setText("What do you want to do now?");
                        gpsButton.setVisibility(View.VISIBLE);
                    } else {
                        mapNavText.setText("You have now reached your destination!");
                    }
                }

            }
        }




    }
}
