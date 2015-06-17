package emma.londonloopapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WalkDetailFragment extends Fragment {
    private final int WALK_NUMBERS = 24;
    private SectionItem sectionItem;
    private View walkDetailView;
    private MySQLiteHelper db;

    private String weatherUrl = "api.openweathermap.org/data/2.5/weather?";

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

        new HttpAsyncTask().execute(weatherUrl);

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

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        protected void onPreExecute()
        {
            this.dialog.setMessage("Getting data...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            JSONObject json = null;

            try {

                json = new JSONObject(result);

                if(this.dialog.isShowing())
                {
                    this.dialog.dismiss();
                }

            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Error retrieving weather data", Toast.LENGTH_SHORT).show();
                if(this.dialog.isShowing())
                {
                    this.dialog.dismiss();
                }
            }
        }
    }

}