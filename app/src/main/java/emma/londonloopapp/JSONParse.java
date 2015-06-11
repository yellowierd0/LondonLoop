package emma.londonloopapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Emma on 30/04/2015.
 */
public class JSONParse extends AsyncTask<String,String,JSONObject> {

    final String TAG = "JSONParse.java";

    private JSONArray jsonArray = null;
    private static final String TAG_NODE = "nodes";
    private static final String TAG_NODE_ID = "node_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";

    private static final String TAG_SECTION = "sections";
    private static final String TAG_SECTION_ID = "section_id";
    private static final String TAG_START_NODE = "start_node";
    private static final String TAG_END_NODE = "end_node";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_LENGTH = "length";

    private static final String TAG_STATS = "statistics";
    private static final String TAG_ID = "id";
    private static final String TAG_CURR = "currently_walking";
    private static final String TAG_WALK_TIME = "walk_time";
    private static final String TAG_WALK_COMP = "walks_completed";
    private static final String TAG_STATS_UPDATE = "'last_updated'";

    private ClassType type;
    private String url;

    public JSONParse(String url, ClassType type){
        this.url = url;
        this.type = type;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... args) {
        JSONParser2 jParser = new JSONParser2();

        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        try {

            switch (type) {

                case NODE:
                    // Getting JSON Array
                    jsonArray = json.getJSONArray(TAG_NODE);
                    if (jsonArray != null){
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject c = jsonArray.getJSONObject(i);

                            int id = Integer.parseInt(c.getString(TAG_NODE_ID));
                            String name = c.getString(TAG_NAME);
                            double latitude = Double.parseDouble(c.getString(TAG_LATITUDE));
                            double longitude = Double.parseDouble(c.getString(TAG_LONGITUDE));

                            // show the values in our logcat
                            Log.e(TAG, "id: " + id
                                    + ", name: " + name
                                    + ", location: " + latitude + ", " + longitude);


                        }
                    }
                    break;
                case SECTION:
                    jsonArray = json.getJSONArray(TAG_SECTION);
                    if (jsonArray != null){
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject c = jsonArray.getJSONObject(i);

                            int id = Integer.parseInt(c.getString(TAG_SECTION_ID));
                            String start_node = c.getString(TAG_START_NODE);
                            String end_node = c.getString(TAG_END_NODE);
                            String description = c.getString(TAG_DESCRIPTION);
                            double length = Double.parseDouble(c.getString(TAG_LENGTH));

                            // show the values in our logcat
                            Log.e(TAG, "id: " + id
                                    + ", start_node: " + start_node
                                    + ", end_node: " + end_node
                                    + ", description: " + description
                                    + ", miles: " + length);

                        }
                    }
                    break;
                case STATISTIC:
                    jsonArray = json.getJSONArray(TAG_STATS);
                    if (jsonArray != null){
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject c = jsonArray.getJSONObject(i);


                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                            Date walking_time = new Date();
                            Date last_updated = new Date();
                            try{
                                walking_time = format.parse(c.getString(TAG_WALK_TIME));
                                last_updated = format.parse(c.getString(TAG_STATS_UPDATE));
                            } catch (ParseException e){
                                Log.e(TAG, "wrong time format");
                            }


                            int id = Integer.parseInt(c.getString(TAG_ID));
                            int currently_walking = Integer.parseInt(c.getString(TAG_CURR));
                            int walks_completed = Integer.parseInt(c.getString(TAG_WALK_COMP));

                            // show the values in our logcat
                            Log.e(TAG, "id: " + id
                                    + ", currently walking: " + currently_walking
                                    + ", walking_time: " + walking_time.getTime()
                                    + ", walked completed: " + walks_completed
                                    + ", last updated: " + last_updated
                                    );


                        }
                    }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}