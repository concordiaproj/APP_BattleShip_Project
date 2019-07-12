package application.Models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author dhvaniagola This Class create a block.
 */
public class Block extends Rectangle {
	int x;
	int y;
	Battlefield bf;

	/**
	 * 
	 * @param bf Battlefield
	 * @param i  x-coordinate of a block
	 * @param j  y-coordinate of a block It sets (x,y) coordinates of a block with
	 *           associated color.
	 */
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
