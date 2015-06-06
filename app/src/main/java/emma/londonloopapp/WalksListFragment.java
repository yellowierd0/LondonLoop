package emma.londonloopapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class WalksListFragment extends ListFragment {

    MySQLiteHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the items list

        db = new MySQLiteHelper(getActivity());

        setListAdapter(new WalkAdapterItem(getActivity(), db.getAllSections()));

        // initialize and set the list adapter

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setBackgroundColor(getResources().getColor(R.color.primary_500));
        getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        SectionItem item = db.getSection(position + 1);
        // do something
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        WalkDetailFragment wdf= WalkDetailFragment.newInstance(item.getId()-1);

        fragmentManager.beginTransaction()
                .add(R.id.container, wdf)
                // Add this transaction to the back stack
                .addToBackStack("walksFragment")
                .commit();

    }

}
