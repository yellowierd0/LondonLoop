package emma.londonloopapp;

import android.graphics.drawable.Drawable;

/**
 * Created by Emma on 07/03/2015.
 */
public class SectionItem {
    private long id;
    private long startNode; // the text for the ListView item title
    private long endNode; // the text for the ListView item title
    private String description; // the text for the ListView item description
    private double miles; //distance for walk in miles
    private Drawable icon; // the drawable for the ListView item ImageView

    public SectionItem() {

    }

    public SectionItem(long id, long startNode, long endNode, String description, double miles, Drawable icon) {
        this.id = id;
        this.startNode = startNode;
        this.endNode = endNode;
        this.description = description;
        this.miles = miles;
        this.icon = icon;
    }

    public SectionItem(long startNode, long endNode, String description, double miles, Drawable icon) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.description = description;
        this.miles = miles;
        this.icon = icon;
    }

    public SectionItem(String description, double miles, Drawable icon) {
        this.description = description;
        this.miles = miles;
        this.icon = icon;
    }

    public long getId(){
        return id;
    }

    public long getStartNode(){
        return startNode;
    }

    public long getEndNode(){
        return endNode;
    }

    public String getDescription(){
        return description;
    }

    public double getMiles(){
        return miles;
    }

    public Drawable getIcon(){
        return icon;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setStartNode(int startNode){
        this.startNode = startNode;
    }

    public void setEndNode(int endNode){
        this.endNode = endNode;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setMiles(double miles){
        this.miles = miles;
    }

    public void setIcon(Drawable icon){
        this.icon = icon;
    }
}
