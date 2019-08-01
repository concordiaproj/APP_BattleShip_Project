package application.Models;
/**
 * 
 * @author SAHIL
 * getting update(Char) of block weather it is occupied or not
 */
public class Block {
private char charOccupiedFor;
private int x;
private int y;
private Ship2 ship;
public Block(int x,int y,char charOccupiedFor) {
	this.charOccupiedFor=charOccupiedFor;
	this.x=x;
	this.y=y;
}
public void setShip(Ship2 ship) {
	this.ship=ship;
}
public Ship2 getShip() {
	return this.ship;
}
public void setCharOccupiedFor(char c) {
	this.charOccupiedFor=c;
}
public char getCharOccupiedFor() {
	return this.charOccupiedFor;
}
}
