package emma.londonloopapp;

/**
 * Created by Emma on 07/03/2015.
 */
public class SectionItem {
    private long id;
    private NodeItem startNode; // the text for the ListView item title
    private NodeItem endNode; // the text for the ListView item title
    private String description; // the text for the ListView item description
    private double miles; //distance for walk in miles

    public SectionItem() {

    }

    public SectionItem(long id, NodeItem start_node, NodeItem end_node, String description, double miles) {
        this.id = id;
        this.startNode = start_node;
        this.endNode = end_node;
        this.description = description;
        this.miles = miles;
    }

    public SectionItem(NodeItem start_node, NodeItem end_node, String description, double miles) {
        this.startNode = start_node;
        this.endNode = end_node;
        this.description = description;
        this.miles = miles;
    }

    public long getId(){
        return id;
    }

    public NodeItem getStartNode(){
        return startNode;
    }

    public NodeItem getEndNode(){
        return endNode;
    }

    public String getDescription(){
        return description;
    }

    public double getMiles(){
        return miles;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setStartNode(NodeItem startNode){
        this.startNode = startNode;
    }

    public void setEndNode(NodeItem endNode){
        this.endNode = endNode;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setMiles(double miles){
        this.miles = miles;
    }
}
