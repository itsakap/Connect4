//GamePanel.java
//Methods for implementing Connect4 Game,
//Both visually and functionally.

//Credits go to Artificial Intelligence: A Modern Approach,
//by Stuart Russell and Peter Norvig, for their explanation of Minimax

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements MouseListener{
	//constants for cells and grid
	private final int WIDTH = 701;
	private final int HEIGHT = 601;
	private final int MAX_ROWS = 6;
	private final int MAX_COLUMNS = 7;
	
	private BoardState UIBoard;

//"cells" is the connect 4 board used for the board shown in the UI.  I use
//UIBoard and cells interchangably in this file to do different things.
	private Cell [][] cells;
	private Cell currentCell; //Keeps track of the last cell user clicked
	private Cell defaultCell; //currentCell to use at beginning of turn.
	private JPanel panel = this;
	
//constructor and create cells
	public GamePanel(){
		cells = new Cell [MAX_ROWS][MAX_COLUMNS];
		defaultCell = new Cell();
		currentCell = defaultCell;
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setBackground(Color.white);
		panel.setFont(new Font("Arial",Font.BOLD,72));
		for(int i=0; i<MAX_ROWS; i++){
			for(int j = 0; j < MAX_COLUMNS; j++){
				cells[i][j] = new Cell(i, j);
			}
		}
		UIBoard = new BoardState(cells);
		panel.addMouseListener(this);

}

	public void paintComponent(Graphics page) {
		super.paintComponent (page);
		drawColony(page);
		printWin(true, page);
		printWin(false, page);
		printDraw(page);
	}
	public Color defaultColor(Cell c){
		if(c.isHuman())return Color.red;
		else return Color.black;
	}
	//cycle through and draw each cell
	public void drawColony (Graphics page) {
		Color c;
		Cell dis;
		for (int i = 0; i < MAX_ROWS; i++) {
			for (int j = 0; j < MAX_COLUMNS; j++) {
				dis = cells[i][j];
				Color d = defaultColor(dis);
				c=d;
				if((dis==currentCell)){
					c = Color.green;
				}
				else {
					if(UIBoard.win(true)||UIBoard.win(false)){
					if(UIBoard.inWinningSegment(dis)) c = Color.cyan;
					else c=d;
				}
				
				}
				dis.drawCell(page,c);
			}
		}
	}
	//Resets the game by emptying all Cells.
	public void reset () {
		for (int i = 0; i < MAX_ROWS; i++) {
			for (int j = 0; j<MAX_COLUMNS; j++) {
				cells[i][j].forceReset();
			}
		}
		currentCell=defaultCell;
	}
	public void undo (){
		if(currentCell!=defaultCell){
			currentCell.forceReset();
			currentCell=defaultCell;
		}
		else {
		UIBoard.undoMove(UIBoard.numberOfCellsOnBoard());
		UIBoard.undoMove(UIBoard.numberOfCellsOnBoard());
		}
	}
	//METHODS FOR WIN CRITERIA
	public void printWin(boolean player,Graphics page){
		
		if(win(player,UIBoard)) {
			page.setColor(Color.blue);
			page.drawString(thisPlayer(player)+" wins!", 200, 300);
		
		}
	}
	public boolean win(boolean player,BoardState b) {
		return (b.win(player));
	}
	
	//returns a string with the with either "human" or AI, depending on boolean passed in
	public String thisPlayer(boolean player) {
		if(player==true) return "Red";
		else return "Black";
	}
	
	public void printDraw(Graphics page) {
		//if board is full and neither player has won, print draw.
		if(UIBoard.isFull()&&!(win(true,UIBoard)||win(false,UIBoard)))
		{
			page.setColor(Color.blue);
			page.drawString("Draw!", 200, 300);
	}
	}

	//Algorithm for AI player's moves.
	//returns the best move's column.


	//method for placing the AI's chip.  Takes an int representing a column.
	//The int will be obtained from running the algorithm and finding the best
	//AI move.
	public void placeAI(int j) {
		for (int k=MAX_ROWS-1; k>=0;k--) {
		//if this cell is dead, then we place the cell there.
		//otherwise, we check the next row up.
		if(!cells[k][j].isCellAlive()) {
		cells[k][j].AI();//Turns human false, and occupied true.
		break; // we only want to flip the state of THIS cell and none following it.
		}
		}	
	}
	
	 
	
	//All of the events that occur after human clicks on "Submit"
	public void compTurn(){
	
	
	if(currentBoardIsNotTerminalState(UIBoard,currentCell,defaultCell)){
		int column = columnToPlay(UIBoard, false);
		int nocb = UIBoard.numberOfCellsOnBoard();
		UIBoard.addMove(UIBoard.getTransformedCell(column),nocb+1);
		placeAI(column);
	}
		
	
	currentCell=defaultCell;//reset currentCell before letting human move.
	}

//do not let computer move if current board is a terminal state!!!
public boolean currentBoardIsNotTerminalState(BoardState b, Cell c, Cell d){
	return (!(b.win(true)||b.win(false))&&(c!=d)&&!b.isFull());
}
//traverses a "tree" of potential Board States and eliminates moves most likely to
//result in a loss. Takes a player, to represent whose turn it is; the Board State
//being evaluated; the worst possible outcome of a move; the best possible outcome
//of a move; and a ticker to represent which move is being evaluated (respectively).
	
//each iteration of alphaBeta represents one move that the AI is evaluating.
public int alphaBeta(boolean player, BoardState s, int a, int b, int moveCount, int depth){

	int score;
	//if AI wins, return best score for this move.
	if(s.win(false))return 100000;
	//if human wins, return worst score
	if(s.win(true))return -100000;
	//if tie, return a neutral score
	if(s.isFull())return 0;
	//if this iteration is at the maximum depth, return the max depth score.
	if(moveCount==depth) return s.maxDepthScore();
	//gather all legal moves, put in a hash set, create an iterator
	Iterator<Integer> i = s.columns().iterator();

//depending on which player is moving, this iteration will behave one of two ways
	if(player==true){//human turn
		
		while(i.hasNext()){
		int column=i.next();
		s.addMove(s.getTransformedCell(column), moveCount);
		moveCount++;
		
		s.transform(column, false);//prepare board for next iteration.
			
			score = alphaBeta(false,s,a,b, moveCount, depth);
			moveCount--;
		s.undoMove(moveCount);
		
		if(score>a)a=score;//better best move
		if(a>=b)return a;//cut off
		
		}
		return a;
		
	}
	else {//ai turn
		while(i.hasNext()){
			int column=i.next();
			s.addMove(s.getTransformedCell(column),moveCount);
			moveCount++;
			
			s.transform(column,true);//prepare the board for next iteration.
			score = alphaBeta(true,s,a,b, moveCount,depth);
			moveCount--;
			s.undoMove(moveCount);
			if(score<b)b=score;//better worse move
			if(a>=b)return b;
			
		}
		return b;
	}
	
}

public int columnToPlay(BoardState b, boolean turn){
Random r = new Random();
//I don't want you to figure out my pattern so Imma add some unpredictability, beeyotch
int randomDepth = r.nextInt(2)+6;
int solution=0;
int bestScore=Integer.MIN_VALUE;
Iterator<Integer> i = b.columns().iterator();
while(i.hasNext()){
	
	int iter=i.next();
	if(gameSavePossible(b, iter)) return iter;
	BoardState attempt=new BoardState(b,iter,turn);
	if(attempt.isFull())return iter;
	if(choosable(attempt,iter,!turn,!i.hasNext(),bestScore)){
	int score = alphaBeta(!turn,attempt,-100000,100000,0,randomDepth);
	if(score==bestScore){
	 solution=columnWithMorePaths(b, solution, iter, !turn, randomDepth);//true
	}
	if(score>bestScore){
		solution=iter;
		bestScore=score;
	}
	}
}
return solution;
}
public boolean choosable(BoardState b, int i, boolean t, boolean h, int bs){
	return okayToProceed(b,i,t)||noChoiceLastColumn(h,bs);
}
public boolean okayToProceed(BoardState b, int i, boolean t){
	return (!b.partOfAWin(b.getTransformedCell(i),t));
}
public boolean noChoiceLastColumn(boolean a, int bs){
	return (a && bs==Integer.MIN_VALUE);
}
public int columnWithMorePaths(BoardState c, int a, int b, boolean f, int r){
	int npa = numPaths(c,a,f,r);
	int npb = numPaths(c,b,f,r);
	if( Math.max(npa,npb)==npa)return a;
	else return b;
}
public int numPaths(BoardState b, int a, boolean f,int r){
	BoardState c = new BoardState(b, a, !f);
	int score = alphaBeta(false,c,-100000,100000,0,r);
	return score;
	}
public boolean gameSavePossible(BoardState b, int i){
	return b.partOfAWin(b.getTransformedCell(i), true)
			||b.partOfAWin(b.getTransformedCell(i),false);
}
	//Select the first empty cell in the column from the board's bottom.
	public void mouseClicked(MouseEvent event){
	if(!UIBoard.isTerminalState()){
		Point where = event.getPoint();
		for(int i = 0; i<MAX_ROWS; i++){
			for(int j = 0; j<MAX_COLUMNS; j++){
				//determine which cell we're clicking on, and if that cell is alive
				if(cells[i][j].inHere(where)){
					currentCell.flipState();
					currentCell=UIBoard.getTransformedCell(j);
					currentCell.flipState();
				}
			}
		}
		repaint();
	}
	int nocb = UIBoard.numberOfCellsOnBoard();
	UIBoard.addMove(currentCell,nocb);
	}
	//unused mouse events
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
