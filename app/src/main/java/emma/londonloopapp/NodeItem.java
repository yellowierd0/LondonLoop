package emma.londonloopapp;

/**
 * Created by Emma on 29/04/2015.
 */
public class NodeItem {

    private long nodeID;
    private String name; //name of node
    private double latitude; //latitude of node point
    private double longitude; //longitude of node point

    public NodeItem() {
    }

    public NodeItem(long nodeID, String name, double latitude, double longitude) {
        this.nodeID = nodeID;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public NodeItem(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public long getID(){
        return nodeID;
    }

    public String getName(){
        return name;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setID(int id){
        this.nodeID = id ;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
}
