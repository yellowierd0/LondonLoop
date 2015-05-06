package emma.londonloopapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Emma on 06/05/2015.
 */
public class StartWalkFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        final Button navButton = (Button) rootView.findViewById(R.id.navButton);
        navButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                StartWalkFragment wdf = new StartWalkFragment();

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("startFrag")
                        .commit();
            }
        });

        final Button noNavButton = (Button) rootView.findViewById(R.id.noNavButton);
        noNavButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                StartWalkFragment wdf = new StartWalkFragment();

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("startFrag")
                        .commit();
            }
        });

        return rootView;
    }


}
