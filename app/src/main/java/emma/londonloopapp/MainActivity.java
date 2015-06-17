package emma.londonloopapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private String url = "http://146.169.46.77:55000";
    private String nodeUrl = url + "/getNodes.php";
    private String sectionUrl = url + "/getSections.php";

    // Database Helper
    MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //new JSONParse(nodeUrl, MainActivity.this).execute();
        //new JSONParse(sectionUrl, MainActivity.this).execute();

        db = new MySQLiteHelper(getApplicationContext());

        //db.onUpgrade(db.getWritableDatabase(), 0, 1);
        //db.onCreate(db.getWritableDatabase());
        createWalks();

        db.closeDB();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        ListFragment listFragment;
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

        //switch on fragments
        switch(position) {
            default:
            case 0:
                listFragment = new WalksListFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.container, listFragment)
                        // Add this transaction to the back stack
                        .addToBackStack("fragBack")
                        .commit();
                break;
            case 1:
                fragment = new MapsFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.container, fragment)
                        // Add this transaction to the back stack
                        .addToBackStack("fragBack")
                        .commit();
                break;
            case 2:
                fragment = new MyProfileFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.container, fragment)
                        // Add this transaction to the back stack
                        .addToBackStack("fragBack")
                        .commit();
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_walks);
                break;
            case 2:
                mTitle = getString(R.string.title_maps);
                break;
            case 3:
                mTitle = getString(R.string.title_myprofile);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_us) {
            Toast.makeText(getApplicationContext(), "Written by Emma Hulme :)", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag("fragBack") != null) {

        } else {
            super.onBackPressed();
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
            Fragment frag = getSupportFragmentManager().findFragmentByTag("fragBack");
            FragmentTransaction transac = getSupportFragmentManager().beginTransaction().remove(frag);
            transac.commit();
        }
    }
        /**
         * A placeholder fragment containing a simple view.
         */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    private void createWalks(){

        Resources resources = getResources();

        NodeItem[] nodeItems = setUpNodes();

        SectionItem[] sectionItems = setUpSections(nodeItems);

        setUpMarkers(sectionItems);

        if(db.hasTableCount(db.getReadableDatabase(), "Statistics") == false){

            long i = 0;
            while (i < 25){
                //index i-0 for all walks collectively, i=1-24 for individual walks
                db.createStatItem(new StatItem(i, 0, 0, 0));
                i++;
            }

        }

        if(db.hasTableCount(db.getReadableDatabase(), "Gps") == false){
            InputStream inputStream = resources.openRawResource(R.raw.gps);

            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = "\\+";

            try {

                br = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = br.readLine()) != null) {

                    // use comma as separator
                    String[] gpsString = line.split(cvsSplitBy);

                    if (gpsString.length == 5){
                        GPSItem gpsItem = new GPSItem(Long.parseLong(gpsString[0]),
                                Integer.parseInt(gpsString[1]),
                                new LatLng(Double.parseDouble(gpsString[2]),
                                        Double.parseDouble(gpsString[3])),
                                sectionItems[Integer.parseInt(gpsString[4])-1], "");
                        db.createGPSItem(gpsItem);
                    } else {
                        GPSItem gpsItem = new GPSItem(Long.parseLong(gpsString[0]),
                                Integer.parseInt(gpsString[1]),
                                new LatLng(Double.parseDouble(gpsString[2]),
                                        Double.parseDouble(gpsString[3])),
                                sectionItems[Integer.parseInt(gpsString[4])-1], gpsString[5]);
                        db.createGPSItem(gpsItem);
                    }


                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private NodeItem[] setUpNodes(){
        Resources resources = getResources();
        NodeItem nodeItems[] = new NodeItem[25];

        InputStream inputNodeStream = resources.openRawResource(R.raw.nodes);

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new InputStreamReader(inputNodeStream));

            int j = 0;
            long k = 1;

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] nodeString = line.split(cvsSplitBy);

                nodeItems[j] = new NodeItem(k, nodeString[0], Double.parseDouble(nodeString[1]), Double.parseDouble(nodeString[2]));
                j++;
                k++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(db.hasTableCount(db.getReadableDatabase(), "Node") == false){
            for (int i = 0; i < 25; i++){
                db.createNode(nodeItems[i]);
            }
        }

        return nodeItems;
    }

    private SectionItem[] setUpSections(NodeItem[] nodeItems){

        Resources resources = getResources();

        SectionItem[] sectionItems = new SectionItem[24];


        InputStream inputSectionStream = resources.openRawResource(R.raw.sections);

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = "\\+";

        try {

            br = new BufferedReader(new InputStreamReader(inputSectionStream));

            int j = 0;
            long k = 1;

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] sectionString = line.split(cvsSplitBy);

                sectionItems[j] = new SectionItem(k, nodeItems[j], nodeItems[j+1], sectionString[1], Double.parseDouble(sectionString[0]));
                j++;
                k++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (db.hasTableCount(db.getReadableDatabase(), "Section") == false){

            for (int i = 0; i < 24; i++){
                db.createSection(sectionItems[i]);
            }
        }

        return sectionItems;
    }

    private void setUpMarkers(SectionItem[] sectionItems){

        Resources resources = getResources();
        InputStream inputSectionStream = resources.openRawResource(R.raw.markers);

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        ArrayList<MarkerItem> markerItems = new ArrayList<MarkerItem>();

        try {

            br = new BufferedReader(new InputStreamReader(inputSectionStream));

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] markerString = line.split(cvsSplitBy);

                int sectionNo = Integer.parseInt(markerString[1])-1;

                SectionItem s = sectionItems[sectionNo];
                LatLng l = new LatLng(Double.parseDouble(markerString[2]),Double.parseDouble(markerString[3]));
                String name = markerString[4];
                Long id = Long.parseLong(markerString[0]);

                MarkerItem markerItem;

                if (markerString.length == 5){
                    markerItem = new MarkerItem(id, s, l, name);
                } else {
                    String text = markerString[5];
                    String url = markerString[6];
                    markerItem = new MarkerItem(id, s, l, name, text, url);
                }

                markerItems.add(markerItem);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (db.hasTableCount(db.getReadableDatabase(), "Marker") == false){

            for (MarkerItem m : markerItems){
                db.createMarkerItem(m);
            }
        }

    }
}