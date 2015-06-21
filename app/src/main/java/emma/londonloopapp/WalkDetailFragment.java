package emma.londonloopapp;

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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WalkDetailFragment extends Fragment {
    private final int WALK_NUMBERS = 24;
    private SectionItem sectionItem;
    private MySQLiteHelper db;

    private TextView weatherTextView;

    private String WEATHER_API = "http://api.openweathermap.org/data/2.5/weather?";
    private String METRIC = "&units=metric";
    private String DEGREE  = "\u00b0";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_walk_detail, container, false);
        final long walkNumber = getArguments().getLong("walkNumber", 0);

        db = new MySQLiteHelper(getActivity());

        sectionItem = db.getSection(walkNumber + 1);

        String url = WEATHER_API + "lat=" + sectionItem.getStartNode().getLatitude() + "&lon=" + sectionItem.getStartNode().getLongitude() + METRIC;

        TextView title = (TextView) rootView.findViewById(R.id.walkDetailTitle);
        TextView description = (TextView) rootView.findViewById(R.id.walkDetailDescription);

        weatherTextView = (TextView) rootView.findViewById(R.id.weather);

        title.setText(sectionItem.getId() + ". " + sectionItem.getStartNode().getName() + " to " + sectionItem.getEndNode().getName());
        description.setText(sectionItem.getDescription());
        description.setMovementMethod(new ScrollingMovementMethod());

        Log.e("weather api request", url);

        new HttpAsyncTask().execute(url);

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

        final Button nButton = (Button) rootView.findViewById(R.id.nextWalkDetButton);
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

        final Button pButton = (Button) rootView.findViewById(R.id.preWalkDetButton);
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

    private void getWeather(JSONObject jsonObject) throws JSONException {

        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject mainObject = jsonObject.getJSONObject("main");

        String description = weatherArray.getJSONObject(0).getString("description");
        String main = weatherArray.getJSONObject(0).getString("main");
        int id = Integer.parseInt(weatherArray.getJSONObject(0).getString("id"));

        String weather = "Today's weather: " + description + "\n";
        int current_temp = Math.round(Float.valueOf(mainObject.getString("temp")));
        int min_temp = Math.round(Float.valueOf(mainObject.getString("temp_min")));
        int max_temp = Math.round(Float.valueOf(mainObject.getString("temp_max")));

        if (main.equals("Thunderstorm")){
            weather += "Don't forget a coat and some warm clothes!";
        } else if (main.equals("Drizzle") || main.equals("Rain")){
            weather += "Don't forget a coat!";
        } else if (main.equals("Snow")){
            weather += "Be careful and don't forget a warm coat and boots!";
        } else if (main.equals("Clouds") || main.equals("Clear")){
            weather += "Nothing to worry about today. Enjoy your walk!";
        } else if (main.equals("Additional") && id < 956){
            weather += "Enjoy your walk!";
        } else {
            weather += "Be careful when walking today!";
        }
        weather += "\nTemperature: " + current_temp + DEGREE + "  (min: " + min_temp + DEGREE + ", max: " + max_temp + DEGREE + ")";

        weatherTextView.setText(weather);

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

        protected void onPreExecute()
        {
            weatherTextView.setText("Getting weather data...");
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

                getWeather(json);

            } catch (JSONException e) {
                Log.e("Error", e.toString());
                weatherTextView.setText("Oops! Something seems to have gone wrong!");
            }
        }
    }

}