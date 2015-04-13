package emma.londonloopapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WalkDetailFragment extends Fragment {

    private WalkItem walkItem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_walk_detail, container, false);
        int walkNumber = getArguments().getInt("walkNumber", 0);

        WalksFragment wf = new WalksFragment();
        //WalkItem walkItem = wf.getWalk(walkNumber);

        return rootView;
	}

    public static final WalkDetailFragment newInstance(int walk)
    {
        WalkDetailFragment f = new WalkDetailFragment();
        final Bundle bdl = new Bundle(1);
        bdl.putInt("walkNumber", walk);
        f.setArguments(bdl);
        return f;
    }


}