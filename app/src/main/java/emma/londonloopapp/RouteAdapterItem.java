package emma.londonloopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Emma on 13/05/2015.
 */
public class RouteAdapterItem extends ArrayAdapter<RouteItem> {

    public RouteAdapterItem(Context context, List <RouteItem> items) {

        super(context, R.layout.route_item, items);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.route_item, parent, false);
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.duration = (TextView) convertView.findViewById(R.id.duration);
            viewHolder.route_parts = (LinearLayout) convertView.findViewById(R.id.transportModes);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        RouteItem item = getItem(position);
        viewHolder.duration.setText(getDuration(item.getDuration()));
        RoutePart[] routeParts = item.getRouteParts();

        for (int i = 0; i < routeParts.length; i++){
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(getModeView(routeParts[i].getMode()));
            viewHolder.route_parts.addView(imageView);
        }

        return convertView;

    }

    public int getModeView(String mode){
        int image;

        if (mode.equals("foot")){
            image = R.drawable.foot;
        } else if (mode.equals("tube")){
            image = R.drawable.tube;
        } else if (mode.equals("dlr")) {
            image = R.drawable.dlr;
        } else if (mode.equals("bus")) {
            image = R.drawable.bus;
        } else if (mode.equals("tram")) {
            image = R.drawable.tram;
        } else if (mode.equals("train")) {
            image = R.drawable.rail;
        } else if (mode.equals("overground")) {
            image = R.drawable.overground;
        } else if (mode.equals("boat")) {
            image = R.drawable.boat;
        } else if (mode.equals("wait")) {
            image = R.drawable.wait;
        } else {
            image = R.drawable.other;
        }
        return image;
    }

    private String getDuration(String duration){
        String delims = "[:]+";
        String[] tokens = duration.split(delims);

        String d = tokens[0].replaceFirst("^0+(?!$)", "") + " hours";
        if (!(tokens[1].equals("00"))){
            d += ", " + tokens[1].replaceFirst("^0+(?!$)", "") + " minutes";
        }

        return d;
    }

        private static class ViewHolder {
            TextView duration;
            LinearLayout route_parts;
        }

}
