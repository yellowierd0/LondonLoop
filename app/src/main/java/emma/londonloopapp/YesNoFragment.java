package emma.londonloopapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Emma on 16/06/2015.
 */
public class YesNoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_start_walk, container, false);

        final long walkNumber = getArguments().getLong("walkNumber", 0);

        final Button yesButton = (Button) rootView.findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                GPSSectionFragment wdf = GPSSectionFragment.newInstance(walkNumber);

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("yesNoFrag")
                        .commit();
            }
        });

        final Button noButton = (Button) rootView.findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                WalksListFragment wdf = new WalksListFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("yesNoFrag")
                        .commit();
            }
        });

        return rootView;
    }

    public static YesNoFragment newInstance(long walk)
    {
        YesNoFragment f = new YesNoFragment();
        final Bundle bdl = new Bundle(1);
        bdl.putLong("walkNumber", walk);
        f.setArguments(bdl);
        return f;

    }

}
