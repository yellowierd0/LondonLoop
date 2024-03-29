package emma.londonloopapp;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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

    private FragmentActivity listFragment;

    private int route_no = 0;
    private int route_max;

    //avoid repeats
    private int current_position = 0;

    private long walkNumber;
    private int type;

    public RouteAdapterItem(Context context, List <RouteItem> items, FragmentActivity listFragment, long walkNumber, int type) {

        super(context, R.layout.route_item, items);
        route_max = items.size();
        this.listFragment = listFragment;
        this.walkNumber = walkNumber;
        this.type = type;
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
            viewHolder.time= (TextView) convertView.findViewById(R.id.time);
            viewHolder.duration = (TextView) convertView.findViewById(R.id.duration);
            viewHolder.route_parts = (LinearLayout) convertView.findViewById(R.id.transportModes);
            viewHolder.route_detail = (LinearLayout) convertView.findViewById(R.id.transportRoute);

            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view

        if (route_no < route_max && position==current_position){
            // Stop duplicate icons
            final RouteItem item = getItem(current_position);
            RoutePart[] routeParts = item.getRouteParts();
            viewHolder.time.setText("Depart: " + routeParts[0].getDeparture_time() + ", Arrive: " + routeParts[routeParts.length-1].getArrival_time());
            viewHolder.duration.setText("(" + getDuration(item.getDuration()) + ")");

            for (int i = 0; i < routeParts.length; i++){

                if ((i == 0) || (getModeView(routeParts[i-1].getMode()) != getModeView(routeParts[i].getMode()))) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setBackgroundResource(getModeView(routeParts[i].getMode()));
                    viewHolder.route_parts.addView(imageView);
                }

                TextView textView = new TextView(getContext());
                StringBuilder sb = new StringBuilder();
                sb.append(routeParts[i].getDeparture_time());
                sb.append(": ");
                sb.append(getModeName(routeParts[i].getMode()));
                if(!(routeParts[i].getLine_name().equals("")) && !(routeParts[i].getMode().equals("train"))){
                    sb.append(" (");
                    sb.append(routeParts[i].getLine_name());
                    sb.append(") ");
                }
                sb.append(" from ");
                sb.append(routeParts[i].getFrom_point_name());
                if (!(routeParts[i].getMode().equals("foot"))){
                    sb.append(" towards ");
                    sb.append(routeParts[i].getDestination());
                }
                sb.append(" to ");
                sb.append(routeParts[i].getTo_point_name());
                textView.setText(sb.toString());
                textView.setPadding(5, 5, 5, 5);
                viewHolder.route_detail.addView(textView);
            }

            setClickListeners(viewHolder, item);
            route_no++;
            current_position++;
        }

        return convertView;

    }

    private void setClickListeners(ViewHolder viewHolder, final RouteItem item){
        viewHolder.time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                FragmentManager fragmentManager = listFragment.getSupportFragmentManager();
                MapNavFragment wdf = MapNavFragment.newInstance(item, walkNumber, type);

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("routeList")
                        .commit();

            }
        });

        viewHolder.duration.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                FragmentManager fragmentManager = listFragment.getSupportFragmentManager();
                MapNavFragment wdf = MapNavFragment.newInstance(item, walkNumber, type);

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("routeList")
                        .commit();

            }
        });

        viewHolder.route_parts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                FragmentManager fragmentManager = listFragment.getSupportFragmentManager();
                MapNavFragment wdf = MapNavFragment.newInstance(item, walkNumber, type);

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("routeList")
                        .commit();

            }
        });

        viewHolder.route_detail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                FragmentManager fragmentManager = listFragment.getSupportFragmentManager();
                MapNavFragment wdf = MapNavFragment.newInstance(item, walkNumber, type);

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("routeList")
                        .commit();

            }
        });
    }

    private String getModeName(String mode){
        if (mode.equals("foot")){
            return "Walk";
        } else if (mode.equals("tube")){
            return "Take the tube";
        } else if (mode.equals("dlr")) {
            return "Take the DLR";
        } else if (mode.equals("bus")) {
            return "Take the bus";
        } else if (mode.equals("tram")) {
            return "Take the tram";
        } else if (mode.equals("train")) {
            return "Take the train";
        } else if (mode.equals("overground")) {
            return "Take the overground";
        } else if (mode.equals("boat")) {
            return "Take the river boat";
        } else if (mode.equals("wait")) {
            return "Wait";
        } else {
            return "Other";
        }
    }

    private int getModeView(String mode){
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
            TextView time;
            TextView duration;
            LinearLayout route_parts;
            LinearLayout route_detail;
        }

}
