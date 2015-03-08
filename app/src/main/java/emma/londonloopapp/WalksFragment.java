package emma.londonloopapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class WalksFragment extends ListFragment {

    private List<WalkViewItem> mItems; // ListView items list

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// initialize the items list
        mItems = new ArrayList<WalkViewItem>();
        Resources resources = getResources();
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop1), getString(R.string.loop1_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop2), getString(R.string.loop2_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop3), getString(R.string.loop3_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop4), getString(R.string.loop4_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop5), getString(R.string.loop5_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop6), getString(R.string.loop6_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop7), getString(R.string.loop7_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop8), getString(R.string.loop8_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop9), getString(R.string.loop9_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop10), getString(R.string.loop10_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop11), getString(R.string.loop11_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop12), getString(R.string.loop12_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop13), getString(R.string.loop13_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop14), getString(R.string.loop14_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop15), getString(R.string.loop15_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop16), getString(R.string.loop16_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop17), getString(R.string.loop17_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop18), getString(R.string.loop18_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop19), getString(R.string.loop19_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop20), getString(R.string.loop20_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop21), getString(R.string.loop21_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop22), getString(R.string.loop22_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop23), getString(R.string.loop23_description)));
        mItems.add(new WalkViewItem(resources.getDrawable(R.drawable.loop1), getString(R.string.loop24), getString(R.string.loop24_description)));
// initialize and set the list adapter
        setListAdapter(new ArrayAdapterItem(getActivity(), mItems));
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
// remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
// retrieve theListView item
        WalkViewItem item = mItems.get(position);
// do something
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = new WalkDetailFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
