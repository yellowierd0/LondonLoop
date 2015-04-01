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

public class ArrayAdapterItem extends ArrayAdapter<WalkViewItem> {

    public ArrayAdapterItem(Context context, List<WalkViewItem> items) {
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
            viewHolder.walkImage = (ImageView) convertView.findViewById(R.id.ivIcon);
            viewHolder.walkTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.walkDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // update the item view
        WalkViewItem item = getItem(position);
        viewHolder.walkImage.setImageDrawable(item.icon);
        viewHolder.walkTitle.setText(item.title);
        viewHolder.walkDescription.setText(item.description);
        return convertView;
    }

    private static class ViewHolder {
        ImageView walkImage;
        TextView walkTitle;
        TextView walkDescription;
    }

}
