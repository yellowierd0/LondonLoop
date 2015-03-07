package emma.londonloopapp;

import android.graphics.drawable.Drawable;

/**
 * Created by Emma on 07/03/2015.
 */
public class WalkViewItem {
    public final Drawable icon; // the drawable for the ListView item ImageView
    public final String title; // the text for the ListView item title
    public final String description; // the text for the ListView item description
    public WalkViewItem(Drawable icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }
}
