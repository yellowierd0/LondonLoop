package emma.londonloopapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Emma on 30/04/2015.
 */
public class JSONParse extends AsyncTask<String,String,JSONObject> {

    private ProgressDialog pDialog;

    //List of Walks
    public NodeList nodeList;

    final String TAG = "JSONParse.java";

    private JSONArray nodes = null;
    private static final String TAG_NODE = "nodes";
    private static final String TAG_NODE_ID = "node_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";

    private JSONArray sections = null;
    private static final String TAG_SECTION = "sections";
    private static final String TAG_SECTION_ID = "section_id";
    private static final String TAG_START_NODE = "start_node";
    private static final String TAG_END_NODE = "end_node";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_LENGTH = "length";
    private static final String TAG_IMAGE= "image";

    private String url;
    private MainActivity activity;

    public JSONParse(String url, MainActivity activity){
        this.url = url;
        this.activity = activity;
        nodeList = new NodeList(activity.getResources());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Getting Data ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

    }

    @Override
    protected JSONObject doInBackground(String... args) {
        JSONParser jParser = new JSONParser();

        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);
        return json;
    }
    @Override
    protected void onPostExecute(JSONObject json) {
        pDialog.dismiss();
        try {
            // Getting JSON Array
            nodes = json.getJSONArray(TAG_NODE);
            if (nodes != null){
                for (int i = 0; i < nodes.length(); i++){
                    JSONObject c = nodes.getJSONObject(i);

                    int id = Integer.parseInt(c.getString(TAG_NODE_ID));
                    String name = c.getString(TAG_NAME);
                    double latitude = Double.parseDouble(c.getString(TAG_LATITUDE));
                    double longitude = Double.parseDouble(c.getString(TAG_LONGITUDE));

                    // show the values in our logcat
                    Log.e(TAG, "id: " + id
                            + ", name: " + name);


                }
            }

            sections = json.getJSONArray(TAG_SECTION);
            if (sections != null){
                for (int i = 0; i < nodes.length(); i++){
                    JSONObject c = nodes.getJSONObject(i);

                    int id = Integer.parseInt(c.getString(TAG_SECTION_ID));
                    String start_node = c.getString(TAG_START_NODE);
                    String end_node = c.getString(TAG_END_NODE);
                    String description = c.getString(TAG_DESCRIPTION);
                    double length = Double.parseDouble(c.getString(TAG_LENGTH));

                    // show the values in our logcat
                    Log.e(TAG, "id: " + id
                            + ", start_node: " + start_node
                            + ", end_node: " + end_node);

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}