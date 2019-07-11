package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block extends Rectangle {
	int x;
	int y;
	Battlefield bf;

	public Block(Battlefield bf, int i, int j) {
		// TODO Auto-generated constructor stub
		super(50, 50);
		this.x = i;
		this.y = j;
		this.bf = bf;
		if (bf.id == 1)// player
			setFill(Color.LIGHTGRAY);
		else// computer
			setFill(Color.WHITE);
		setStroke(Color.BLACK);
	}

}
