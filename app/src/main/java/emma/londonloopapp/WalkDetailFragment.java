package emma.londonloopapp;

import android.content.res.Resources;
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

    private WalkItem walkItem;
    private WalkList walkList;
    private View walkDetailView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_walk_detail, container, false);
        int walkNumber = getArguments().getInt("walkNumber", 0);

        Resources resources = getResources();
        walkList = new WalkList(resources);

        final WalkItem walkItem = walkList.getWalk(walkNumber);

        TextView title = (TextView) rootView.findViewById(R.id.walkDetailTitle);
        TextView description = (TextView) rootView.findViewById(R.id.walkDetailDescription);

        title.setText(walkItem.getTitle());
        description.setText(walkItem.getDescription());
        description.setMovementMethod(new ScrollingMovementMethod());

        final Button nButton = (Button) rootView.findViewById(R.id.nextButton);
        nButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                WalkDetailFragment wdf;
                if(walkItem.getNum()<walkList.getSize()) {
                    wdf = WalkDetailFragment.newInstance(walkItem.getNum());
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

        final Button pButton = (Button) rootView.findViewById(R.id.previousButton);
        pButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                WalkDetailFragment wdf;
                if(walkItem.getNum() > 1) {
                    wdf = WalkDetailFragment.newInstance(walkItem.getNum() - 2);
                } else {
                    wdf = WalkDetailFragment.newInstance(walkList.getSize()-1);
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

    public static WalkDetailFragment newInstance(int walk)
    {
        WalkDetailFragment f = new WalkDetailFragment();
        final Bundle bdl = new Bundle(1);
        bdl.putInt("walkNumber", walk);
        f.setArguments(bdl);
        return f;

    }

}