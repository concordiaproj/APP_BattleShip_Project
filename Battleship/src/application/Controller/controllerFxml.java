package application.Controller;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class controllerFxml {

	@FXML
	private Button btn;

	@FXML
	private Rectangle size2;
	private Rectangle rect;
	private Rectangle rectWhereShipLeft;
	public Node nd;
	public Node nd2;
	static boolean flag=false;
	public Data data=new Data();
	@FXML
	private Pane pane;

	@FXML
	private GridPane gp;

	@FXML
	public void initialize() {
//		Label lbl = new Label("DhvaniS");
//		gp.add(lbl, 1, 1);
		gpListener();
	}

	private void gpListener() {
		// TODO Auto-generated method stub
		System.out.println("in gpListener+");
//		Node source=(Node)rect2;
//		System.out.println("id in gpListener : "+source.getId());
pane.setOnMouseReleased(new EventHandler<MouseEvent>() {

	@Override
	public void handle(MouseEvent event) {
		// TODO Auto-generated method stub
		System.out.println("in gpListener -- here");
		
	}
});
		
	}

	@FXML
	public void pressed(ActionEvent event) {
		System.out.println("Button Presssed");
	}

	@FXML
	public void onDragShip(MouseEvent event) {
//		System.out.println("dragged detected");

	}

	@FXML
	public void clickOnBlock(MouseEvent event) {
		System.out.println("clicked on block");
		Node source = (Node) event.getSource();
		System.out.println("source id:"+source.getId());
		if(source==gp)
			System.out.println("yes Parent");
		Integer colIndex = GridPane.getColumnIndex(source);
	    Integer rowIndex = GridPane.getRowIndex(source);
	    System.out.println("column-row"+colIndex+","+rowIndex);
	}
	@FXML
	public void dropMouse(MouseEvent event) {
		flag=false;
		System.out.println("mouse dropped");
		Node node=(Node) event.getSource();
		System.out.println("id is : "+node.getId());
		nd=node;
		if(nd==null)
			System.out.println("nd is null");
		
		data.setNode(node);
		Node temp=data.getNode();
		System.out.println("id::::"+temp.getId());
		flag=true;
		if(flag==true) {
		pane.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				System.out.println("released in xyz");
			}
		});
		}
	}
	
	@FXML
	public void mouseReleased(MouseEvent event) {
		System.out.println("mouse is released");
//		Node nodeEvent=(Node)event.getSource();
//		System.out.println("###:"+nodeEvent.getId());
//		
//		long time=System.currentTimeMillis();
//		
//		Node nodetemp=data.getNode();
//		System.out.println("&&&&&:"+nodetemp.getId());
//		Rectangle r=(Rectangle)nodetemp;
//		r.setFill(Color.GREEN);
//		for(Node n:gp.getChildren()) {
//			if(gp.getColumnIndex(n)==1 && gp.getRowIndex(n)==1) {
//				r=(Rectangle)n;
//				r.setFill(Color.GREEN);
//			}
//		}
//		pane.getChildren().remove(size2);
//		
	}
}
