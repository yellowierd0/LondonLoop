package emma.londonloopapp;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emma on 29/04/2015.
 */
public class NodeList {

    private List<NodeItem> nodes; // WalkItem items list

    public NodeList(Resources resources){
        nodes = new ArrayList<>();

    }

    public void addNode(NodeItem n){
        System.out.println(n.getName());
        nodes.add(n);
    }

}
