package emma.londonloopapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        WalksFragment wf = new WalksFragment();

        WalkItem walkItem = walkList.getWalk(walkNumber);

        TextView title = (TextView) rootView.findViewById(R.id.walkDetailTitle);
        TextView description = (TextView) rootView.findViewById(R.id.walkDetailDescription);

        title.setText(walkItem.getTitle());
        description.setText(walkItem.getDescription());
        description.setMovementMethod(new ScrollingMovementMethod());


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