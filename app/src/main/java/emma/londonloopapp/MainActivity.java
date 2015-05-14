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
        db.clearDB(db.getWritableDatabase());
        db.onCreate(db.getWritableDatabase());
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
                listFragment = new WalksFragment();
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
                fragment = new StatsFragment();
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
                mTitle = getString(R.string.title_stats);
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

        NodeItem n1 = new NodeItem(1, "Erith", 51.483144, 0.177975);
        NodeItem n2 = new NodeItem(2, "Old Bexley", 51.441233, 0.148956);
        NodeItem n3 = new NodeItem(3, "Jubilee Park", 51.393209, 0.069081);
        NodeItem n4 = new NodeItem(4, "West Wickham Common", 51.370944, 0.004860);
        NodeItem n5 = new NodeItem(5, "Hamsey Green", 51.319035, -0.063420);
        NodeItem n6 = new NodeItem(6, "Coulsdon South", 51.315728, -0.136744);
        NodeItem n7 = new NodeItem(7, "Banstead Downs", 51.332148, -0.209290);
        NodeItem n8 = new NodeItem(8, "Ewell", 51.351650, -0.250176);
        NodeItem n9 = new NodeItem(9, "Kingston Bridge", 51.411854, -0.308274);
        NodeItem n10 = new NodeItem(10, "Hatton Cross", 51.469927, -0.409793);
        NodeItem n11 = new NodeItem(11, "Hayes and Harlington", 51.505117, -0.418654);
        NodeItem n12 = new NodeItem(12, "Uxbridge Lock", 51.550933, -0.483414);
        NodeItem n13 = new NodeItem(13, "Harefield West", 51.610477, -0.498761);
        NodeItem n14 = new NodeItem(14, "Moor Park", 51.623932, -0.427529);
        NodeItem n15 = new NodeItem(15, "Hatch End", 51.610702, -0.380326);
        NodeItem n16 = new NodeItem(16, "Elstree", 51.653365, -0.281950);
        NodeItem n17 = new NodeItem(17, "Cockfosters", 51.652244, -0.148998);
        NodeItem n18 = new NodeItem(18, "Enfield Lock", 51.668264, -0.028316);
        NodeItem n19 = new NodeItem(19, "Chingford", 51.634306, 0.012118);
        NodeItem n20 = new NodeItem(20, "Chigwell", 51.621468, 0.078004);
        NodeItem n21 = new NodeItem(21, "Havering-atte-Bower", 51.616860, 0.183245);
        NodeItem n22 = new NodeItem(22, "Harold Wood", 51.593421, 0.234098);
        NodeItem n23 = new NodeItem(23, "Upminster Bridge", 51.559197, 0.236748);
        NodeItem n24 = new NodeItem(24, "Rainham", 51.516886, 0.191433);
        NodeItem n25 = new NodeItem(25, "Purfleet", 51.480946, 0.236477);

        db.createNode(n1);
        db.createNode(n2);
        db.createNode(n3);
        db.createNode(n4);
        db.createNode(n5);
        db.createNode(n6);
        db.createNode(n7);
        db.createNode(n8);
        db.createNode(n9);
        db.createNode(n10);
        db.createNode(n11);
        db.createNode(n12);
        db.createNode(n13);
        db.createNode(n14);
        db.createNode(n15);
        db.createNode(n16);
        db.createNode(n17);
        db.createNode(n18);
        db.createNode(n19);
        db.createNode(n20);
        db.createNode(n21);
        db.createNode(n22);
        db.createNode(n23);
        db.createNode(n24);
        db.createNode(n25);

        Resources resources = getResources();

        int dummy1 =  R.drawable.dummy;
        int dummy2 = R.drawable.dummy2;

        SectionItem[] sectionItems = new SectionItem[24];


        sectionItems[0] = new SectionItem(1, n1, n2, resources.getString(R.string.loop1_description), 8.5, dummy1);
        sectionItems[1] = new SectionItem(2, n2, n3, resources.getString(R.string.loop2_description), 7, dummy2);
        sectionItems[2] = new SectionItem(3, n3, n4, resources.getString(R.string.loop3_description), 9, dummy1);
        sectionItems[3] = new SectionItem(4, n4, n5, resources.getString(R.string.loop4_description), 10, dummy2);
        sectionItems[4] = new SectionItem(5, n5, n6, resources.getString(R.string.loop5_description), 6, dummy1);
        sectionItems[5] = new SectionItem(6, n6, n7, resources.getString(R.string.loop6_description), 4.5, dummy2);
        sectionItems[6] = new SectionItem(7, n7, n8, resources.getString(R.string.loop7_description), 3.5, dummy1);
        sectionItems[7] = new SectionItem(8, n8, n9, resources.getString(R.string.loop8_description), 7.3, dummy2);
        sectionItems[8] = new SectionItem(9, n9, n10, resources.getString(R.string.loop9_description), 8.5, dummy1);
        sectionItems[9] = new SectionItem(10, n10, n11, resources.getString(R.string.loop10_description), 3.5, dummy2);
        sectionItems[10] = new SectionItem(11, n11, n12, resources.getString(R.string.loop11_description), 7.5, dummy1);
        sectionItems[11] = new SectionItem(12, n12, n13, resources.getString(R.string.loop12_description), 4.5, dummy2);
        sectionItems[12] = new SectionItem(13, n13, n14, resources.getString(R.string.loop13_description), 5, dummy1);
        sectionItems[13] = new SectionItem(14, n14, n15, resources.getString(R.string.loop14_description), 3.8, dummy2);
        sectionItems[14] = new SectionItem(15, n15, n16, resources.getString(R.string.loop15_description), 10, dummy1);
        sectionItems[15] = new SectionItem(16, n16, n17, resources.getString(R.string.loop16_description), 10, dummy2);
        sectionItems[16] = new SectionItem(17, n17, n18, resources.getString(R.string.loop17_description), 9.5, dummy1);
        sectionItems[17] = new SectionItem(18, n18, n19, resources.getString(R.string.loop18_description), 4, dummy2);
        sectionItems[18] = new SectionItem(19, n19, n20, resources.getString(R.string.loop19_description), 4, dummy1);
        sectionItems[19] = new SectionItem(20, n20, n21, resources.getString(R.string.loop20_description), 6, dummy2);
        sectionItems[20] = new SectionItem(21, n21, n22, resources.getString(R.string.loop21_description), 4.3, dummy1);
        sectionItems[21] = new SectionItem(22, n22, n23, resources.getString(R.string.loop22_description), 4, dummy2);
        sectionItems[22] = new SectionItem(23, n23, n24, resources.getString(R.string.loop23_description), 4, dummy1);
        sectionItems[23] = new SectionItem(24, n24, n25, resources.getString(R.string.loop24_description), 5, dummy2);

        for (int i = 0; i < 24; i++){
            db.createSection(sectionItems[i]);
        }

        InputStream inputStream = getResources().openRawResource(R.raw.gps);

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] gpsString = line.split(cvsSplitBy);

                GPSItem gpsItem = new GPSItem(Long.parseLong(gpsString[0]),
                        Integer.parseInt(gpsString[1]),
                        new LatLng(Double.parseDouble(gpsString[2]), Double.parseDouble(gpsString[3])),
                        sectionItems[Integer.parseInt(gpsString[4])-1]);
                db.createGPSItem(gpsItem);
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