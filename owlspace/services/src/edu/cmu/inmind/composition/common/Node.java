package edu.cmu.inmind.composition.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarr on 8/8/18.
 */
public class Node {
    public enum NodeType{PLAIN, IF};
    private String name;
    private NodeType type;
    private Node parent;
    private List<Node> children = new ArrayList<>();

    public Node(String name, NodeType type) {
        this.name = name;
        this.type = type;
    }

    public void addNode(Node child){
        children.add(child);
        child.parent = this;
    }

    public String getName() {
        return name;
    }

    public NodeType getType() {
        return type;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }
}
