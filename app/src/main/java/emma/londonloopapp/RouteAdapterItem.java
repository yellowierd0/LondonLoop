package emma.londonloopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
            viewHolder.route_parts = (TextView) convertView.findViewById(R.id.route_parts);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }
            // update the item view
            RouteItem item = getItem(position);
            viewHolder.duration.setText(item.getDuration());
            RoutePart[] routeParts = item.getRouteParts();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < routeParts.length; i++){
                if(i != 0){
                    sb.append(", ");
                }
                sb.append(routeParts[i].getMode());
                sb.append(" (");
                sb.append(routeParts[i].getDuration());
                sb.append(")");
            }
        viewHolder.route_parts.setText(sb.toString());
        return convertView;
    }

        private static class ViewHolder {
            TextView duration;
            TextView route_parts;
        }

}
