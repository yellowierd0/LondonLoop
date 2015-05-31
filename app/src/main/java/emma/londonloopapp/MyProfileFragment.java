package emma.londonloopapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MyProfileFragment extends Fragment {


	CallbackManager callbackManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getActivity());
		callbackManager = CallbackManager.Factory.create();

		View rootView = inflater.inflate(R.layout.fragment_myprofile, container, false);

		LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.fb_login_button);
		loginButton.setReadPermissions("public_profile", "user_friends");

		// If using in a fragment
		loginButton.setFragment(this);
		// Other app specific specialization

		// Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
				// App code
			}
		});

		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

	}


}
