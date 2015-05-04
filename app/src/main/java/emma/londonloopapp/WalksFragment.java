package emma.londonloopapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

public class WalksFragment extends ListFragment {

    private WalkList walkList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the items list

        Resources resources = getResources();
        walkList = new WalkList(resources);

        setListAdapter(new ArrayAdapterItem(getActivity(), walkList.getWalks()));

        // initialize and set the list adapter

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }

    /*@Override/*
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        SectionItem item = walkList.getWalks().get(position);
        // do something
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //WalkDetailFragment wdf= WalkDetailFragment.newInstance(item.getId()-1);

        fragmentManager.beginTransaction()
                .add(R.id.container, wdf)
                // Add this transaction to the back stack
                .addToBackStack("walksFragment")
                .commit();

    }*/

}
