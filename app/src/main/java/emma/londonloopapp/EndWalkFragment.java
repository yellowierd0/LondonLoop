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
public class EndWalkFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_end, container, false);

        final long walkNumber = getArguments().getLong("walkNumber", 0);

        final Button navButton = (Button) rootView.findViewById(R.id.nextWalkButton);
        navButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                GPSSectionFragment wdf = GPSSectionFragment.newInstance(walkNumber+1);

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("endFrag")
                        .commit();
            }
        });

        final Button leaveButton = (Button) rootView.findViewById(R.id.leaveWalkButton);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                GPSSectionFragment wdf = GPSSectionFragment.newInstance(walkNumber+1);

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("endFrag")
                        .commit();
            }
        });

        return rootView;
    }


    public static EndWalkFragment newInstance(long walk)
    {
        EndWalkFragment f = new EndWalkFragment();
        final Bundle bdl = new Bundle(1);
        bdl.putLong("walkNumber", walk);
        f.setArguments(bdl);
        return f;

    }

}
