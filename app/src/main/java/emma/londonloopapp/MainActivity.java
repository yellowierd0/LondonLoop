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
        NodeItem n1 = new NodeItem("Erith", 51.483144, 0.177975);
        NodeItem n2 = new NodeItem("Old Bexley", 51.441233, 0.148956);
        NodeItem n3 = new NodeItem("Jubilee Park", 51.393209, 0.069081);
        NodeItem n4 = new NodeItem("West Wickham Common", 51.370944, 0.004860);
        NodeItem n5 = new NodeItem("Hamsey Green", 51.319035, -0.063420);
        NodeItem n6 = new NodeItem("Coulsdon South", 51.315728, -0.136744);
        NodeItem n7 = new NodeItem("Banstead Downs", 51.332148, -0.209290);
        NodeItem n8 = new NodeItem("Ewell", 51.351650, -0.250176);
        NodeItem n9 = new NodeItem("Kingston Bridge", 51.411854, -0.308274);
        NodeItem n10 = new NodeItem("Hatton Cross", 51.469927, -0.409793);
        NodeItem n11 = new NodeItem("Hayes and Harlington", 51.505117, -0.418654);
        NodeItem n12 = new NodeItem("Uxbridge Lock", 51.550933, -0.483414);
        NodeItem n13 = new NodeItem("Harefield West", 51.610477, -0.498761);
        NodeItem n14 = new NodeItem("Moor Park", 51.623932, -0.427529);
        NodeItem n15 = new NodeItem("Hatch End", 51.610702, -0.380326);
        NodeItem n16 = new NodeItem("Elstree", 51.653365, -0.281950);
        NodeItem n17 = new NodeItem("Cockfosters", 51.652244, -0.148998);
        NodeItem n18 = new NodeItem("Enfield Lock", 51.668264, -0.028316);
        NodeItem n19 = new NodeItem("Chingford", 51.634306, 0.012118);
        NodeItem n20 = new NodeItem("Chigwell", 51.621468, 0.078004);
        NodeItem n21 = new NodeItem("Havering-atte-Bower", 51.616860, 0.183245);
        NodeItem n22 = new NodeItem("Harold Wood", 51.593421, 0.234098);
        NodeItem n23 = new NodeItem("Upminster Bridge", 51.559197, 0.236748);
        NodeItem n24 = new NodeItem("Rainham", 51.516886, 0.191433);
        NodeItem n25 = new NodeItem("Purfleet", 51.480946, 0.236477);

        long node1_id = db.createNode(n1);
        long node2_id = db.createNode(n2);
        long node3_id = db.createNode(n3);
        long node4_id = db.createNode(n4);
        long node5_id = db.createNode(n5);
        long node6_id = db.createNode(n6);
        long node7_id = db.createNode(n7);
        long node8_id = db.createNode(n8);
        long node9_id = db.createNode(n9);
        long node10_id = db.createNode(n10);
        long node11_id = db.createNode(n11);
        long node12_id = db.createNode(n12);
        long node13_id = db.createNode(n13);
        long node14_id = db.createNode(n14);
        long node15_id = db.createNode(n15);
        long node16_id = db.createNode(n16);
        long node17_id = db.createNode(n17);
        long node18_id = db.createNode(n18);
        long node19_id = db.createNode(n19);
        long node20_id = db.createNode(n20);
        long node21_id = db.createNode(n21);
        long node22_id = db.createNode(n22);
        long node23_id = db.createNode(n23);
        long node24_id = db.createNode(n24);
        long node25_id = db.createNode(n25);

        Resources resources = getResources();

        SectionItem s1 = new SectionItem(node1_id, node2_id, resources.getString(R.string.loop1_description), 8.5, null);
        SectionItem s2 = new SectionItem(node2_id, node3_id, resources.getString(R.string.loop2_description), 7, null);
        SectionItem s3 = new SectionItem(node3_id, node4_id, resources.getString(R.string.loop3_description), 9, null);
        SectionItem s4 = new SectionItem(node4_id, node5_id, resources.getString(R.string.loop4_description), 10, null);
        SectionItem s5 = new SectionItem(node5_id, node6_id, resources.getString(R.string.loop5_description), 6, null);
        SectionItem s6 = new SectionItem(node6_id, node7_id, resources.getString(R.string.loop6_description), 4.5, null);
        SectionItem s7 = new SectionItem(node7_id, node8_id, resources.getString(R.string.loop7_description), 3.5, null);
        SectionItem s8 = new SectionItem(node8_id, node9_id, resources.getString(R.string.loop8_description), 7.3, null);
        SectionItem s9 = new SectionItem(node9_id, node10_id, resources.getString(R.string.loop9_description), 8.5, null);
        SectionItem s10 = new SectionItem(node10_id, node11_id, resources.getString(R.string.loop10_description), 3.5, null);
        SectionItem s11 = new SectionItem(node11_id, node12_id, resources.getString(R.string.loop11_description), 7.5, null);
        SectionItem s12 = new SectionItem(node12_id, node13_id, resources.getString(R.string.loop12_description), 4.5, null);
        SectionItem s13 = new SectionItem(node13_id, node14_id, resources.getString(R.string.loop13_description), 5, null);
        SectionItem s14 = new SectionItem(node14_id, node15_id, resources.getString(R.string.loop14_description), 3.8, null);
        SectionItem s15 = new SectionItem(node15_id, node16_id, resources.getString(R.string.loop15_description), 10, null);
        SectionItem s16 = new SectionItem(node16_id, node17_id, resources.getString(R.string.loop16_description), 10, null);
        SectionItem s17 = new SectionItem(node17_id, node18_id, resources.getString(R.string.loop17_description), 9.5, null);
        SectionItem s18 = new SectionItem(node18_id, node19_id, resources.getString(R.string.loop18_description), 4, null);
        SectionItem s19 = new SectionItem(node19_id, node20_id, resources.getString(R.string.loop19_description), 4, null);
        SectionItem s20 = new SectionItem(node20_id, node21_id, resources.getString(R.string.loop20_description), 6, null);
        SectionItem s21 = new SectionItem(node21_id, node22_id, resources.getString(R.string.loop21_description), 4.3, null);
        SectionItem s22 = new SectionItem(node22_id, node23_id, resources.getString(R.string.loop22_description), 4, null);
        SectionItem s23 = new SectionItem(node23_id, node24_id, resources.getString(R.string.loop23_description), 4, null);
        SectionItem s24 = new SectionItem(node24_id, node25_id, resources.getString(R.string.loop24_description), 5, null);

        long section1_id = db.createSection(s1);
        long section2_id = db.createSection(s2);
        long section3_id = db.createSection(s3);
        long section4_id = db.createSection(s4);
        long section5_id = db.createSection(s5);
        long section6_id = db.createSection(s6);
        long section7_id = db.createSection(s7);
        long section8_id = db.createSection(s8);
        long section9_id = db.createSection(s9);
        long section10_id = db.createSection(s10);
        long section11_id = db.createSection(s11);
        long section12_id = db.createSection(s12);
        long section13_id = db.createSection(s13);
        long section14_id = db.createSection(s14);
        long section15_id = db.createSection(s15);
        long section16_id = db.createSection(s16);
        long section17_id = db.createSection(s17);
        long section18_id = db.createSection(s18);
        long section19_id = db.createSection(s19);
        long section20_id = db.createSection(s20);
        long section21_id = db.createSection(s21);
        long section22_id = db.createSection(s22);
        long section23_id = db.createSection(s23);
        long section24_id = db.createSection(s24);
    }
}