package emma.londonloopapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MyProfileFragment extends Fragment {


	CallbackManager callbackManager;

    private Button postImageBtn;
    private Button updateStatusBtn;

    private TextView userName;

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

    private static String message = "Sample status posted from android app";
    private AccessTokenTracker accessTokenTracker;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getActivity());
		callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

		View rootView = inflater.inflate(R.layout.fragment_myprofile, container, false);

        LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("public_profile", "user_friends");

        //loginButton.setOnClickListener(loginbut);
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT).show();
                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    String id = me.optString("id");
                                    String name = me.optString("name");
                                    Log.e("Facebook", name);
                                    Log.e("Facebook", id);
                                    // send name and id to your web server

                                    

                                }
                            }
                        }).executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
				// App code
                Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
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
