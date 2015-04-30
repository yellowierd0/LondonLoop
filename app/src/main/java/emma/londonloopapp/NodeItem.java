package emma.londonloopapp;

/**
 * Created by Emma on 29/04/2015.
 */
public class NodeItem {

    private final int nodeID;
    private final String name; //name of node
    private final double latitude; //latitude of node point
    private final double longitude; //longitude of node point

    public NodeItem(int nodeID, String name, double latitude, double longitude) {

        this.nodeID = nodeID;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public int getID(){
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
}
