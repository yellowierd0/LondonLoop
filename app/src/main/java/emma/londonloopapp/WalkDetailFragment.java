package emma.londonloopapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WalkDetailFragment extends Fragment {
    private final int WALK_NUMBERS = 24;
    private SectionItem sectionItem;
    private View walkDetailView;
    private MySQLiteHelper db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_walk_detail, container, false);
        final long walkNumber = getArguments().getLong("walkNumber", 0);

        db = new MySQLiteHelper(getActivity());

        final SectionItem sectionItem = db.getSection(walkNumber + 1);

        TextView title = (TextView) rootView.findViewById(R.id.walkDetailTitle);
        TextView description = (TextView) rootView.findViewById(R.id.walkDetailDescription);

        title.setText(sectionItem.getId() + ". " + sectionItem.getStartNode().getName() + " to " + sectionItem.getEndNode().getName());
        description.setText(sectionItem.getDescription());
        description.setMovementMethod(new ScrollingMovementMethod());

        final Button startButton = (Button) rootView.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                StartWalkFragment wdf = StartWalkFragment.newInstance(walkNumber);

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("walkDetailFrag")
                        .commit();
            }
        });

        final Button nButton = (Button) rootView.findViewById(R.id.nextGPSButton);
        nButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                WalkDetailFragment wdf;
                if(sectionItem.getId() < WALK_NUMBERS) {
                    wdf = WalkDetailFragment.newInstance(sectionItem.getId());
                } else {
                    wdf = WalkDetailFragment.newInstance(0);
                }

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("walkDetailFrag")
                        .commit();

            }
        });

        final Button pButton = (Button) rootView.findViewById(R.id.preGPSButton);
        pButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                WalkDetailFragment wdf;
                if(sectionItem.getId() > 1) {
                    wdf = WalkDetailFragment.newInstance(sectionItem.getId() - 2);
                } else {
                    wdf = WalkDetailFragment.newInstance(23);
                }
                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("walkDetailFrag")
                        .commit();
            }
        });


        return rootView;
	}

    public static WalkDetailFragment newInstance(long walk)
    {
        WalkDetailFragment f = new WalkDetailFragment();
        final Bundle bdl = new Bundle(1);
        bdl.putLong("walkNumber", walk);
        f.setArguments(bdl);
        return f;

    }

}