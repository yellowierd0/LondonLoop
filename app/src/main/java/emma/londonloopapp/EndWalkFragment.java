package emma.londonloopapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Emma on 06/05/2015.
 */
public class EndWalkFragment extends Fragment {


    private static String POST_URL = "http://146.169.46.77:55000/putStat.php";
    private static String GET_URL = "http://146.169.46.77:55000/getStats.php";

    private long minutes;
    private double new_miles;

    private MySQLiteHelper db;
    private SectionItem sectionItem;

    private long walkNumber;

    private String modes = "bus-tube-train-boat";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_end, container, false);

        CheckBox bus = (CheckBox) rootView.findViewById(R.id.end_checkbox_bus);
        CheckBox tube = (CheckBox) rootView.findViewById(R.id.end_checkbox_tube);
        CheckBox train = (CheckBox) rootView.findViewById(R.id.end_checkbox_train);
        CheckBox boat = (CheckBox) rootView.findViewById(R.id.end_checkbox_boat);

        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });

        tube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });

        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });

        boat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });


        bus.setChecked(true);
        tube.setChecked(true);
        train.setChecked(true);
        boat.setChecked(true);

        db = new MySQLiteHelper(getActivity());
        sectionItem = db.getSection(walkNumber+1);
        new_miles = sectionItem.getMiles();

        final Button navButton = (Button) rootView.findViewById(R.id.nextWalkButton);
        navButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                // update db
                new StatisticsAsyncTask().execute(GET_URL);

                // next walk
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                GPSSectionFragment wdf;
                if (sectionItem.getId() < 24){
                    wdf = GPSSectionFragment.newInstance(walkNumber+1);
                } else {
                    wdf = GPSSectionFragment.newInstance(0);
                }


                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("endFrag")
                        .commit();
            }
        });

        final EditText destination = (EditText) rootView.findViewById(R.id.leaveWalkText);

        final Button leaveButton = (Button) rootView.findViewById(R.id.leaveWalkButton);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                if (destination.getText().toString().trim().length()>0){
                    // update db
                    new StatisticsAsyncTask().execute(GET_URL);

                    // plan new journey
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    EndRouteListFragment wdf = EndRouteListFragment.newInstance(walkNumber, modes, replaceAtTheEnd(destination.getText().toString()));
                    System.out.println(destination.getText().toString());

                    fragmentManager.beginTransaction()
                            .add(R.id.container, wdf)
                                    // Add this transaction to the back stack
                            .addToBackStack("endFrag")
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "Please enter a destination", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_bus:
                if (checked){
                    if (modes.equals("")){
                        modes = "bus";
                    } else if(modes.contains("bus")) {
                        //do nothing
                    } else {
                        modes += "-bus";
                    }
                } else{
                    if (modes.contains("-bus")){
                        modes =  modes.replace("-bus", "");
                    } else if (modes.contains("bus")){
                        modes =  modes.replace("bus","");
                    }
                }
                break;
            case R.id.checkbox_tube:
                if (checked) {
                    if (modes.equals("")){
                        modes = "tube";
                    } else if(modes.contains("tube")) {
                        //do nothing
                    }else {
                        modes += "-tube";
                    }
                }else {
                    if (modes.contains("-tube")){
                        modes = modes.replace("-tube", "");
                    } else if (modes.contains("tube")){
                        modes = modes.replace("tube","");
                    }
                }
                break;
            case R.id.checkbox_train:
                if (checked) {
                    if (modes.equals("")){
                        modes = "train";
                    } else if (modes.contains("train")){
                        //do nothing
                    } else{
                        modes += "-train";
                    }
                }else {
                    if (modes.contains("-train")){
                        modes = modes.replace("-train", "");
                    } else if (modes.contains("train")){
                        modes = modes.replace("train","");
                    }
                }
                break;
            case R.id.checkbox_boat:
                if (checked) {
                    if (modes.equals("")){
                        modes = "boat";
                    } else if(modes.contains("boat")) {
                        //do nothing
                    } else{
                        modes += "-boat";
                    }
                }else {
                    if (modes.contains("-boat")){
                        modes = modes.replace("-boat", "");
                    } else if (modes.contains("boat")){
                        modes = modes.replace("boat","");
                    }
                }break;
        }
    }

    public static String replaceAtTheEnd(String input){
        input = input.replaceAll("\\s+$", "");
        return input;
    }

    public static EndWalkFragment newInstance(long walk)
    {
        EndWalkFragment f = new EndWalkFragment();
        final Bundle bdl = new Bundle(1);
        bdl.putLong("walkNumber", walk);
        f.setArguments(bdl);
        return f;

    }


    public static EndWalkFragment newInstance(long walkNumber, long minutes) {
        EndWalkFragment fragment = new EndWalkFragment();
        fragment.setSomeObject(walkNumber, minutes);
        return fragment;
    }

    public void setSomeObject(long walkNumber, long minutes) {

        this.minutes = minutes;
        this.walkNumber = walkNumber;
    }


    private static String GET(String url){
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

    private class StatisticsAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject json = null;

            try {
                json = new JSONObject(result);

                setStats(json, 0);
                setStats(json, walkNumber + 1);

            } catch (JSONException e) {
                Log.e("JSONException: ", e.toString());
            }

        }
    }

    private void setStats(JSONObject jsonObject, long walkId) throws JSONException {


        JSONArray jsonArray = jsonObject.getJSONArray("statistics");
        int i = 0;
        while (i < jsonArray.length()){
            JSONObject global = jsonArray.getJSONObject(i);
            if (global.getString("id").equals(String.valueOf(walkId))){

                int curr_walking = Integer.valueOf(global.getString("currently_walking")) - 1;
                double miles = Double.valueOf(global.getString("miles_walked")) + new_miles;
                int time = Integer.valueOf(global.getString("walk_time") + minutes);
                int walks = Integer.valueOf(global.getString("walks_completed")) + 1;

                Log.e("Walk Completed", walkId + ": " + walks);

                ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();

                values.add(new BasicNameValuePair("WalkId", String.valueOf(walkId)));
                values.add(new BasicNameValuePair("CurrentlyWalking", String.valueOf(curr_walking)));
                values.add(new BasicNameValuePair("MilesWalked", String.valueOf(miles)));
                values.add(new BasicNameValuePair("WalkTime",String.valueOf(time)));
                values.add(new BasicNameValuePair("WalksCompleted", String.valueOf(walks)));

                new insertDATA(values).execute();
                break;
            }
            i++;

        }
    }

    private class insertDATA extends AsyncTask<String, String, String> {

        InputStream is = null;
        String line;
        String result;
        int code;

        ArrayList<NameValuePair> values ;

        public insertDATA(ArrayList<NameValuePair> values){
            this.values = values;
        }


        @Override
        protected String doInBackground(String... arg0) {

            try {
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(POST_URL);
                httppost.setEntity(new UrlEncodedFormEntity(values));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.i("TAG", "Connection Successful");
            } catch (Exception e) {
                Log.i("TAG", e.toString());
                //Invalid Address
            }

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.i("TAG", "Result Retrieved");
            } catch (Exception e) {
                Log.i("TAG", e.toString());
            }

            try {

                Log.i("result", result);

                JSONObject json = new JSONObject(result);
                code = (json.getInt("code"));

                if (code == 0) {
                    Log.i("msg", "Data Successfully Inserted");
                    //Data Successfully Inserted
                } else {
                    //Data Not Inserted
                    Log.i("msg", "Data Not Inserted");
                }
            } catch (Exception e) {
                Log.i("TAG", e.toString());
            }
            return null;
        }

    }

}
