package emma.londonloopapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.FacebookSdk;

public class MyProfileFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getActivity());

		View rootView = inflater.inflate(R.layout.fragment_myprofile, container, false);
        return rootView;
	}
}
