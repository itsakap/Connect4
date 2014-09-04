import java.util.*;
public class BoardState  {
	//Object class for Board State.

	//Instantiated column points to which column
	//will be played for this board state.
	private final int MAX_ROWS = 6;
	private final int MAX_COLUMNS = 7;
	private final int SEGMENT_LENGTH = 4;
	private final int NUMBER_OF_SEGMENTS = 69;
	private final int MOVES = 42;
	private Cell [][] myBoard;
	//"segments" is an array of Cell arrays, each representing four adjacent cells
	//horizontal, vertical, or diagonal.  Check these segments for possible wins.
	private Cell [][] segments;
	private Cell[] moves; //to keep track of moves made for this BoardState
	//BoardState constructor for board in UI


	public BoardState(Cell[][]board){
		myBoard = board;
		moves = new Cell[MOVES];
		segments = buildSegments();
	}

	//BoardState constructor.
	public BoardState(BoardState another,int column, boolean player){
		myBoard = new Cell[MAX_ROWS][MAX_COLUMNS];
		moves = new Cell[MOVES];

		for(int i = 0; i<MAX_ROWS;i++){
			for(int j = 0; j<MAX_COLUMNS;j++){
				myBoard[i][j]=new Cell(another.myBoard[i][j]);
			}
		}
		segments = buildSegments();
		transform(column,player);
	}

public Cell[][] buildSegments(){
	Cell[][]toReturn = new Cell[NUMBER_OF_SEGMENTS][SEGMENT_LENGTH];
	return buildDiagSegments(buildVertSegments(buildHorSegments(toReturn)));
}
public Cell[][] buildHorSegments(Cell[][]segs){
	int segNoCount=0;
	for(int i=0;i<MAX_ROWS;i++){//for each row
	for(int j = 0; j<4; j++){//starting cells for all horizontal segments
		for (int jj=0;jj<4;jj++){//cycle through 4 cells in segment
			segs[segNoCount][jj]=myBoard[i][j+jj];//point this cell to a cell on board
		}
		segNoCount++;
	}
	}
return segs;
}
public Cell[][] buildVertSegments(Cell[][]segs){
	int segNoCount = 24;//ending value
	for(int j=0;j<MAX_COLUMNS;j++){//for each column
	for(int i=0;i<3;i++){//starting cells for all vertical segments
		for(int ii=0;ii<4;ii++){//cycle through each of four cells in a segment
			segs[segNoCount][ii]=myBoard[i+ii][j];//point this cell to cell on board
		}
		segNoCount++;
	}
	}
return segs;
}
public Cell[][] buildDiagSegments(Cell[][]segs){

	int segNoCount = 45;
	for (int aa=0;aa<3;aa++){
		for(int bb=0;bb<4;bb++){
			int point = 0;
	for(int a=0;a<4;a++){
		for (int b = 0;b<4;b++){
			if(a==b){
				segs[segNoCount][point]=myBoard[a+aa][b+bb];
				point++;

			}
				}
			}
	segNoCount++;
	point = 0;
	for(int a=0;a<4;a++){
		for(int b=0;b<4;b++){
			if(a+b==3){
				segs[segNoCount][point]=myBoard[a+aa][b+bb];
				point++;

			}
		}
	}
	segNoCount++;
		}
	}
	return segs;
	}

public void addMove(Cell thisMove, int moveNo){

	moves[moveNo]=thisMove;//point to cell in board array
	//moveNo++;//tick move number
}
public void undoMove(int moveNo){
if(moveNo>=0){
	moves[moveNo].forceReset();//turn this move off (ensures no Null Pointer Exceptions)
}
}
//locates segments of which a Cell parameter is a part, and returns if any
//have three activated cells belonging to the player parameter.
public boolean partOfAWin(Cell thisCell, boolean player){
	HashSet<Integer>theseSegments=new HashSet<Integer>();
	for(int i=0;i<NUMBER_OF_SEGMENTS;i++)
		for(int j = 0;j<SEGMENT_LENGTH;j++)
			if(thisCell==segments[i][j])theseSegments.add(i);

	Iterator<Integer> ts = theseSegments.iterator();
	while(ts.hasNext()){
		int winCount=0;
		int i = ts.next();
		for(int j = 0;j<SEGMENT_LENGTH;j++){
			if((segments[i][j].isCellAlive() && segments[i][j].isHuman()==player))
				winCount++;
		if(winCount==3)return true;
		}
	}
	return false;
}
//defines score for a BoardState at the maximum depth
//examines the segments in the board and returns the sum of all scores
public int maxDepthScore(){
	int boardScore = 0;
	for(int i =0;i<NUMBER_OF_SEGMENTS;i++){
		int redChips = 0;
		int blackChips=0;
		for(int j=0;j<SEGMENT_LENGTH;j++)
			if(segments[i][j].isCellAlive()){
			 if (segments[i][j].isHuman()) redChips++; else blackChips++;
			}

		if(redChips!=0&&blackChips==0)boardScore-=Math.pow(10, redChips);
		else if(blackChips!=0&&redChips==0)boardScore+=Math.pow(10, blackChips);
		else boardScore+=0;
	}
	return boardScore;
}

	//Method for retrieving cell grid from this BoardState
	public Cell[][] getMyBoard(){
		return myBoard;
	}

	//print cell info-- for testing
	public void printCellStuff(){
		System.out.println("HI! \n\n\n");
			for(int j = 0; j<SEGMENT_LENGTH;j++){
				for(int i = 0;i<NUMBER_OF_SEGMENTS;i++){
				System.out.println("ROW: "+i+"--COLUMN: "+j+"--ALIVE: "
				+myBoard[i][j].isCellAlive()+"--PLAYER: "+myBoard[i][j].getPlayer());
			}
		}
	}
	//Method for changing the myBoard of a BoardState.
	//Used to either create a new BoardState, or
	//to change this boardState.
	public void transform(int c,boolean p) {
		for(int i = MAX_ROWS-1; i>=0;i--){
			if(!this.myBoard[i][c].isCellAlive()){
				this.myBoard[i][c].forcePlayer(p);
				this.myBoard[i][c].flipState();
				return;
			}
		}
	}
	//given a column, returns the cell where a new chip would fall.
	public Cell getTransformedCell(int column){
		//no need to check for columnFull; columns being passed in have already
		//passed a test!
		for(int i=MAX_ROWS-1;i>=0;i--){
			if(!myBoard[i][column].isCellAlive()){
				return myBoard[i][column];
				}
		}
		//this return occurs if testing the top row of board.
		return new Cell();
	}
	public boolean inWinningSegment(Cell c){

		for(int j = 0;j<SEGMENT_LENGTH;j++){
			if(segments[winningSeg()][j]==c) return true;
		}
		return false;
	}
	public int winningSeg(){
		for(int i = 0; i<NUMBER_OF_SEGMENTS;i++){
			int winCount=0;
			boolean player = segments[i][0].isHuman();
			for(int j = 0; j<SEGMENT_LENGTH;j++){
				if((segments[i][j].isCellAlive())&&segments[i][j].isHuman()==player)winCount++;
			}
			if(winCount==4) return i;
		}
		return 0;
	}
	public boolean win(boolean player) {
		for(int i = 0;i<NUMBER_OF_SEGMENTS;i++){
			int winCount=0;
			for(int j = 0;j<SEGMENT_LENGTH;j++){
				if((segments[i][j].isCellAlive())&&((segments[i][j].isHuman())==player))winCount++;
			}
			if(winCount==4){
				return true;
			}
		}
		return false;
	}
	//used in searching for a cell not belonging to the passed player.
	//used to refute the presence of a win
	public boolean encounterNonPlayerCell(boolean player,int i,int j){
		return(myBoard[i][j].getPlayer()!=player||!myBoard[i][j].isCellAlive());
	}

	//Check to see if board is full by evaluating top row
	//Used in evaluating if a game has ended in a draw.
	public boolean isFull() {
		for (int j = 0; j<MAX_COLUMNS; j++)
			if(!myBoard[0][j].isCellAlive()) return false;

		return true;
	}
	//generates an array of Ints specifying columns this BoardState can act upon
	public Set<Integer> columns(){
		Set<Integer> legalMoves=new HashSet<Integer>();
		for(int i = 0; i<MAX_COLUMNS;i++)
			if(!columnFull(i)) legalMoves.add(i);
		return legalMoves;
	}
	//returns whether or not a specified column has filled.
	//Used in evaluating the legality of an impending move.
	public boolean columnFull(int c){
		int topRow=0;
		return myBoard[topRow][c].isCellAlive();
	}
	//returns whether or not the game has ended
	public boolean isTerminalState(){
		return(isFull()||win(true)||win(false));
	}
	public int numberOfCellsOnBoard(){
		int cellCount = 0;
		for(int i = 0;i<MAX_ROWS;i++)
			for(int j = 0;j<MAX_COLUMNS;j++)
				if(myBoard[i][j].isCellAlive()) cellCount++;
		return cellCount-1;
			}

}
