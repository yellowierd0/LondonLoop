package emma.londonloopapp;

import android.graphics.drawable.Drawable;

/**
 * Created by Emma on 07/03/2015.
 */
public class WalkItem {
    public final Drawable icon; // the drawable for the ListView item ImageView
    public final String title; // the text for the ListView item title
    public final String description; // the text for the ListView item description
    /*public final String miles; //distance for walk in miles
    public final double longitude; //longitude of start point
    public final double latitude; //latitude of start point*/
    public WalkItem(Drawable icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        /*this.miles = miles;
        this.longitude = longitude;
        this.latitude = latitude;*/
    }
}
