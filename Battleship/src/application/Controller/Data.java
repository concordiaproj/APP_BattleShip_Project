package application.Controller;

import javafx.scene.Node;

public class Data {
private Node node;
public Data() {
	node=null;
}
public void setNode(Node node) {
	this.node=node;
}
public Node getNode() {
	System.out.println("node in data:"+node.getId());
	return this.node;
}
}
