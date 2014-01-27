//Cell.java
//Methods used by each individual cell.
//A cell is one of the forty-two spaces playable in Connect 4.
import java.awt.*;   

public class Cell {

	private int myRow, myColumn; // row and column the cell is in; provides info to array regarding cell's location
	private int myX, myY; //x and y coordinates for the top left corner of cell
	private static int myWidth = 100, myHeight = 100;
	private static final int CELL_LENGTH = 100; // width and height of each cell
	private boolean occupied; //cell's state: occupied(true), or empty (false).
	private boolean human; //if cell is occupied, is it a human or AI move?
	//constructors
	public Cell() { //This constructor is for the initial currentCell
	myColumn = 10; //number not equal to anything on the board
	}
	//Initializer for UIBoard cells
	public Cell (int row, int column) {
		myRow = row;
		myColumn = column;
		myX = myColumn * CELL_LENGTH;
		myY = myRow * CELL_LENGTH;
		occupied = false;
		human = true;
	}
	//Initializer for BoardState Cells- DEEP COPY poopie
	public Cell (Cell other){
		this.myRow = other.myRow;
		this.myColumn = other.myColumn;
		this.occupied = other.occupied;
		this.human = other.human;
	}
	public int getColumn() {
		return myColumn;
	}
	public int getRow(){
		return myRow;
	}
	public boolean getPlayer() {
		return human;
	}

	public void AI() {//Turns a cell into an AI move.
		human=false;//Represents AI
		flipState();//When we run AI(), we already know that this cell is empty.
	}
	public void forcePlayer(boolean player){
		 //forces this cell to belong to the player indicated in forcePlayer:
	   //human(true) or AI(false)
	human = player;
	}

	//draw each cell, according to alive or dead
	public void drawCell(Graphics page, Color c) {
		if (occupied == true && human == true){
			page.setColor(c);
			page.fillOval(myX, myY, myWidth, myHeight);
		} else if (occupied == true && human == false) {
			page.setColor(c);
			page.fillOval(myX, myY, myWidth, myHeight);
		}
		
		else {
			page.setColor(Color.white);
			page.fillRect(myX, myY, myWidth, myHeight);
		}
		page.setColor(Color.black);
		page.drawRect(myX, myY, myWidth, myHeight);
	}

	public boolean isCellAlive() {
		return occupied;
	}
	public boolean isHuman(){
		return human;
	}
	public void setState(boolean state) {
		occupied = state;
	}

	//change from dead/alive
	public  void flipState(){
		occupied = !occupied;
	}
	//Force all cells dead (for resetting the game)
	public void forceReset(){
		occupied = false;
		human = true;
	}
	//click point test
	public boolean inHere(Point where){
		return (where.x>myX && where.y >myY && where.x < myX+CELL_LENGTH && where.y<myY + CELL_LENGTH);
	}
} 