package emma.londonloopapp;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Emma on 06/05/2015.
 */
public class NavigationFragment extends Fragment {

    MySQLiteHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_navigation, container, false);

        long walkNumber = getArguments().getLong("walkNumber", 0);

        db = new MySQLiteHelper(getActivity());

        final SectionItem sectionItem = db.getSection(walkNumber + 1);


        planJourney(sectionItem, (TextView) rootView.findViewById(R.id.output));

        return rootView;
    }

    private void planJourney(SectionItem destination, TextView output){
        try {
            // Get LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            // Create a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Get the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Get Current Location
            Location myLocation = locationManager.getLastKnownLocation(provider);

            HttpClient client = new DefaultHttpClient();
            String startUrl = "http://transportapi.com/v3/uk/public/journey/"; //API URL
            String from = "from/lonlat:" + myLocation.getLongitude() + "," + myLocation.getLatitude();
            String to = "to/lonlat:" + destination.getStartNode().getLongitude() + "," + destination.getStartNode().getLatitude();
            String endUrl = ".json";
            String getURL = startUrl + from + to + endUrl; //The API service URL
            HttpGet get = new HttpGet(getURL);
            HttpResponse responseGet = client.execute(get);
            HttpEntity resEntityGet = responseGet.getEntity();
            if (resEntityGet != null) {
                //do something with the response
                Log.i("GET RESPONSE", EntityUtils.toString(resEntityGet));
                output.setText(EntityUtils.toString(resEntityGet)); //This is a TextView
            }
            else {
                output.setText("null reponse");
            }
        } catch(Exception e) {
            output.setText("exception");
        }

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
