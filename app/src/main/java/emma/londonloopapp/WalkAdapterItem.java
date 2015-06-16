package emma.londonloopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Emma on 26/01/2015.
 */

public class WalkAdapterItem extends ArrayAdapter<SectionItem> {

    public WalkAdapterItem(Context context, List<SectionItem> items) {
        super(context, R.layout.walk_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.walk_item, parent, false);
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.walkImage = (ImageView) convertView.findViewById(R.id.walkIcon);
            viewHolder.walkTitle = (TextView) convertView.findViewById(R.id.walkTitle);
            viewHolder.walkLength = (TextView) convertView.findViewById(R.id.walkLength);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // update the item view
        SectionItem item = getItem(position);

        if (item.getId() < 11){
            viewHolder.walkImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.south));
        } else if (item.getId() < 17){
            viewHolder.walkImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.northwest));
        } else {
            viewHolder.walkImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.northeast));
        }

        double miles = item.getMiles();
        long m = 0;
        long time = Math.round(miles) * 20;
        if (miles%1==0){
            m = Math.round(miles);
        } else {
            m = (long) miles;
        }

        long hours = Math.round(time / 60);
        long min = Math.round(time % 60);

        viewHolder.walkTitle.setText(item.getId() + ". " + item.getStartNode().getName() + " to " + item.getEndNode().getName());
        if (min==0){
            viewHolder.walkLength.setText(m + " miles (approx " + hours + " hours)");
        } else {
            viewHolder.walkLength.setText(m + " miles (approx " + hours + " hours, " + min + " minutes)");
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView walkImage;
        TextView walkTitle;
        TextView walkLength;
    }
}
