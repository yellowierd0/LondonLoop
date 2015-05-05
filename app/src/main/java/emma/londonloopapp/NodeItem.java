package emma.londonloopapp;

/**
 * Created by Emma on 29/04/2015.
 */
public class NodeItem {

    private long nodeId;
    private String name; //name of node
    private double latitude; //latitude of node point
    private double longitude; //longitude of node point

    public NodeItem() {
    }

    public NodeItem(long nodeId, String name, double latitude, double longitude) {
        this.nodeId = nodeId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public NodeItem(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public long getId(){
        return nodeId;
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

    public void setId(long id){
        this.nodeId = id ;
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
