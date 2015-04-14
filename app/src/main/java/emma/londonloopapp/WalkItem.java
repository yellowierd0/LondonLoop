package emma.londonloopapp;

import android.graphics.drawable.Drawable;

/**
 * Created by Emma on 07/03/2015.
 */
public class WalkItem {
    public final int num;
    public final Drawable icon; // the drawable for the ListView item ImageView
    public final String title; // the text for the ListView item title
    public final String description; // the text for the ListView item description
    public final double miles; //distance for walk in miles
    public final double latitude; //latitude of start point
    public final double longitude; //longitude of start point

    public WalkItem(int num, Drawable icon, String title, String description, double miles, double latitude, double longitude) {
        this.num = num;
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.miles = miles;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getNum(){
        return num;
    }

    public Drawable getIcon(){
        return icon;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public double getMiles(){
        return miles;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

}
