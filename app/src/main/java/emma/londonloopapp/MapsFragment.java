package emma.londonloopapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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


        mMap.setMyLocationEnabled(true);

        // add locations of start/end points
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.483144, 0.177975)).title("Erith"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.441233, 0.148956)).title("Old Bexley"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.393209,0.069081)).title("Petts Wood"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.370944,0.004860)).title("West Wickham Common"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.319035,-0.063420)).title("Hamsey Green"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.315728,-0.136744)).title("Coulsdon South"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.332148,-0.209290)).title("Banstead Downs"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.351650,-0.250176)).title("Ewell"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.411854,-0.308274)).title("Kingston Bridge"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.469927,-0.409793)).title("Hatton Cross"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.505117,-0.418654)).title("Hayes"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.550933,-0.483414)).title("Uxbridge"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.610477,-0.498761)).title("Harefield"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.623932,-0.427529)).title("Moor Park"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.610702,-0.380326)).title("Hatch End"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.653365,-0.281950)).title("Elstree"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.652244,-0.148998)).title("Cockfosters"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.668264,-0.028316)).title("Enfield Lock"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.634306,0.012118)).title("Chingford"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.621468,0.078004)).title("Chigwell"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.616860,0.183245)).title("Havering-atte-Bower"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.593421,0.234098)).title("Harold Wood"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.559197,0.236748)).title("Upminster Bridge"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(51.516886,0.191433)).title("Rainham"));

    }

}
