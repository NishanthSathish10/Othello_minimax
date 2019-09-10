


/**
 * Othello.java
 * A board game, with the following rules.
 * Two black and two white pieces are placed in the middle of the 8 x 8 board 
 * in the form of a square. W and B are placed on the top middle row, and then B, W 
 * is placed on the row below it.  Black plays first and places its piece on a 
 * valid spot on the grid. You must place a disc on a spot that allows you to "flank" 
 * or "capture" at least one of your opponent's discs by bordering them "in a row".
 * You must flip all captured discs to your color.  To capture your opponent's discs
 * you must have 1 of your own pieces at the end of a row and then on your turn place
 * a new piece at the beginning of the row. Everything in between on that row is now 
 * flipped to your color. You can only capture rows of a single color adjacent to each 
 * other; there cannot be any open space or your own discs between them or the combo is 
 * interrupted.  You can flank any number of discs. You may capture discs vertically, 
 * horizontally, diagonally.  You may capture in multiple directions at the same time. 
 * All discs that can be flipped must be flipped. You cannot pick and choose which ones 
 * are captured.  If you cannot make a valid move then your turn is forfeit and your 
 * opponent may go again. If you have a valid move available to you then you must make 
 * that move and are not allowed to forfeit your turn.  When neither player can make 
 * any further play then the game is over.  Each player counts the number of spaces 
 * occupied by their color. Highest count is the winner. Games can end before all 
 * spaces are filled.
 *
 * @author Scott DeRuiter and Harshith Mohan Kumar
 * @version 1.0
 * @since 1/14/2019
 */

import java.awt.Color;
import java.awt.Font;
import java.util.Scanner;

public class Othello
{
	/**    The board object.  This board will be 8 x 8, and filled with OthelloCells.
	 *     The cell may be empty, hold a white game piece, or a black game piece.       */
	private OthelloCell [][] board;
	
	/**    The coordinates of the active piece on the board.                            */
	private int x, y;
	
	/**    Booleans indicating that the mouse is ready to be pressed, that it is     
	 *     black's turn to move (false if white's turn), and that the game is over.     */
	private boolean mousePressReady, blackTurn, gameOver;

	/**
	 *  Creates an Othello object, with a sized graphics canvas, and a 2D (8 x 8) array
	 *  of OthelloCell, setting up initial values.
	 */
	public Othello ( )
	{
		StdDraw.setCanvasSize(500,650);
		StdDraw.setXscale(0,1);
		StdDraw.setYscale(0,1.3);
		StdDraw.enableDoubleBuffering();
		Font font = new Font("Arial", Font.BOLD, 30);
		StdDraw.setFont(font);
		
		startBoard();
	}
	
	/**
	 *  Called by the constructor, or when the player hits the "RESET" button,
	 *  initializing the game board (an 8 x 8 array of OthelloCell).
	 */
	public void startBoard ( )
	{
		mousePressReady = blackTurn = true;
		gameOver = false;
		board = new OthelloCell[8][8];
		for ( int i = 0; i < board.length; i++ )
		{
			for ( int j = 0; j < board[i].length; j++ )
			{
				board[i][j] = new OthelloCell(i,j);
			}
		}
		board[3][3].playIt();
		board[3][3].setBlack(true);
		board[4][4].playIt();
		board[4][4].setBlack(true);
		board[4][3].playIt();
		board[4][3].setBlack(false);
		board[3][4].playIt();
		board[3][4].setBlack(false);
	}
	
	/**
	 *  Sets up and runs the game of Othello.
	 */
	public static void main(String [] args)
	{
		Othello game = new Othello();
		game.run();
	}
	
	/**
	 *  Runs an endless loop to play the game.  Even if the game is over, the
	 *  loop is still ready for the user to press "RESET" to play again.
	 */
	public void run ( )
	{
		do
		{
			drawBoard();
			countScoreAnddrawScoreBoard();
			StdDraw.show();
			StdDraw.pause(30);
			makeChoice();
			gameOver = checkTurnAndGameOver();
		}
		while(true);
	}
	
	/**
	 *  Draws the board, in its current state, to the GUI.
	 */
	public void drawBoard ( )
	{
		StdDraw.setPenColor(new Color(150,150,150));
		StdDraw.filledRectangle(0.5,0.75,0.5,0.75);
		StdDraw.setPenColor(new Color(0,110,0));
		StdDraw.filledSquare(0.5,0.5,0.45);
		StdDraw.setPenColor(new Color(0,0,0));
		StdDraw.filledSquare(0.5,0.5,0.42);
		for ( int i = 0; i < board.length; i++ )
		{
			for ( int j = 0; j < board[i].length; j++ )
			{
				board[i][j].drawCell();
			}
		}
	}
	
	/**
	 *  Waits for the user to make a choice.  The user can make a move
	 *  placing a black piece or the white piece (depending on whose turn
	 *  it is), or click on the "RESET" button to reset the game.
	 */
	public void makeChoice ( )
	{
		boolean moveChosen = false;              //  This method is complete.
		while(!moveChosen)
		{
			if(mousePressReady && StdDraw.isMousePressed())
			{			
				mousePressReady = false;
				double xval = StdDraw.mouseX();
				double yval = StdDraw.mouseY();
				if(xval > 0.655 && xval < 0.865 && yval > 1.15 && yval < 1.23)    //  This if checks for a reset.
				{
					startBoard();
					return;
				}
				if(xval <= 0.1 || xval >= 0.9 || yval <= 0.1 || yval >= 0.9)          //  This if checks for a press off the board.
				{
					return;
				}
				int tempx = (int)(10 * (xval - 0.1));
				int tempy = (int)(10 * (yval - 0.1));
				if(isValidMove(tempx,tempy,blackTurn))                            //  This if checks to see if the move is valid.
				{
					x = tempx;
					y = tempy;
					playAndFlipTiles();
					blackTurn = !blackTurn;
					System.out.println(x + "  " + y); ///eventually want to get rid of this print
				}
			}
			if(!StdDraw.isMousePressed() && !mousePressReady)                      //  This if gives back control when the mouse is released.
			{
				mousePressReady = true;
				return;
			}
			StdDraw.pause(20);
		}
		
	}
/*******************************************************************************************************************************************/
	/**
	 *  Checks to see if a valid move can be made at the indicated OthelloCell,
	 *  for the given player.
	 *  @param  xt      The horizontal coordinate value in the board.
	 *  @param  yt      The vertical coordinate value in the board.
	 *  @param  bTurn   Indicates the current player, true for black, false for white
	 *  @return         Returns true if a valid move can be made for this player at
	 *                  this position, false otherwise.
	 */
	public boolean isValidMove(int xt, int yt, boolean bTurn)
	{
		if (board[xt][yt].hasBeenPlayed()==true) //Cant place on top of a played board
		{
			return false;
		}
		/// check the 8 possible directions.
		for (int i = -1; i <=1; i++)			
		{
			for (int j = -1; j <=1; j++)
			{
				if (!(i==0&&j==0)&&directionValid(xt,yt,i,j,bTurn))
				{
					return true; ///If the direction is valid then return true
				}
			}
		}
		System.out.println();
		return false;
	}
	
	/**
	 *  Checks to see if a valid move can be made at the indicated OthelloCell, in a 
	 *  particular direction (there are 8 possible directions). These are indicated by:
	 *  (1,1) is up and right
	 *  (1,0) is right
	 *  (1,-1) is down and right
	 *  (0,-1) is down
	 *  (-1,-1) is down and left
	 *  (-1,0) is left
	 *  (-1,1) is left and up
	 *  (0,1) is up
	 *  @param  xt      The horizontal coordinate value in the board.
	 *  @param  yt      The vertical coordinate value in the board.
	 *  @param  i       -1 is left, 0 is neutral, 1 is right,
	 *  @param  j       -1 is down, - is neutral, 1 is up.
	 *  @param  bTurn   Indicates the current player, true for black, false for white.
	 *  @return         Returns true if this direction has pieces to be flipped, false otherwise.
	 */
	public boolean directionValid(int xt, int yt, int i, int j, boolean bTurn)
	{
		xt+=i;
		yt+=j;
		if(xt>=0&&xt<=7 && yt>=0&&yt<=7) //loop through the direction
		{
			if (board[xt][yt].hasBeenPlayed()==false) 
			{
				System.out.println("Invalid move");
				return false;
			}
			else if (board[xt][yt].getBlackStatus()==bTurn) 
			{
				System.out.println("Invalid move");
				return false;
			}
			else
			{
				while(xt>=0&&xt<=7 && yt>=0&&yt<=7)
				{
					if (board[xt][yt].hasBeenPlayed()==true&&board[xt][yt].getBlackStatus()==bTurn) 
					{
						System.out.println("\nValid move\n");
						return true;
					}
					else if (board[xt][yt].hasBeenPlayed()==false) 
					{
						return false;
					}
					xt+=i;
					yt+=j;
				}
			}
		}
		return false;
	}

	/**
	 *  Places a game piece on the current cell for the current player.  Also flips the
	 *  appropriate neighboring game pieces, checking the 8 possible directions from the
	 *  current cell.
	 */
	public void playAndFlipTiles ( )
	{
		board[x][y].setBlack(blackTurn);
		board[x][y].playIt();		
		
		for (int i=-1; i<=1; i++) 
		{
			for (int j=-1; j<=1; j++) 
			{
				if (!(i==0&&j==0)&& directionValid(x,y,i,j,blackTurn))
				{ 
					System.out.println("Flip");
					flipAllInThatDirection(x,y,i,j);
				}
			}
		}
	}

	/**
	 *  A helper method for playAndFlipTiles.  Flips pieces in a given direction.  The
	 *  directions are as follows:
	 *  (1,1) is up and right
	 *  (1,0) is right
	 *  (1,-1) is down and right
	 *  (0,-1) is down
	 *  (-1,-1) is down and left
	 *  (-1,0) is left
	 *  (-1,1) is left and up
	 *  (0,1) is up
	 *  @param  xt      The horizontal coordinate value in the board.
	 *  @param  yt      The vertical coordinate value in the board.
	 *  @param  i       -1 is left, 0 is neutral, 1 is right,
	 *  @param  j       -1 is down, - is neutral, 1 is up.
	 */
	public void flipAllInThatDirection(int xt, int yt, int i, int j)
	{
		xt+=i;
		yt+=j;
		while(board[xt][yt].getBlackStatus()!=blackTurn) //as long as the tile is not equal to the players loop through and flip it
		{
			board[xt][yt].setBlack(blackTurn);
			board[xt][yt].playIt();
			System.out.println("\nfliped\n");
			xt+=i;
			yt+=j;
		}
		
	}

	/**
	 *  Counts the white pieces on the board, and the black pieces on the board.
	 *  Displays these numbers toward the top of the board, for the current state
	 *  of the board.  Also prints whether it is "BLACK'S TURN" or "WHITE'S TURN"
	 *  or "GAME OVER".
	 */
	public void countScoreAnddrawScoreBoard ( )
	{
		int whiteCount = 0, blackCount = 0;

		for(int x = 0; x<8; x++)
		{
			for(int y = 0; y<8; y++)
			{
				if (board[x][y].hasBeenPlayed()==true)
				{
					if (board[x][y].getBlackStatus()==true)
					{
						blackCount++;
					}
					else
					{
						whiteCount++;
					}
				}
			}
		}
		
		drawScoresAndMessages(whiteCount,blackCount);
	}

	/**
	 *  A helper method for countScoreAnddrawScoreBoard.  Draws the scores
	 *  and messages.
	 *  @param  whiteCount      The current count of the white pieces on the board.
	 *  @param  blackCount      The current count of the black pieces on the board.
	 */
	public void drawScoresAndMessages(int whiteCount, int blackCount)
	{
		StdDraw.setPenColor(new Color(0,0,0));
		StdDraw.filledRectangle(0.41,1.05,0.055,0.045);
		StdDraw.filledRectangle(0.80,1.05,0.055,0.045);
		StdDraw.filledRectangle(0.76,1.19,0.11,0.045);
		StdDraw.setPenColor(new Color(255,255,255));
		StdDraw.filledRectangle(0.41,1.05,0.05,0.04);
		StdDraw.filledRectangle(0.80,1.05,0.05,0.04);
		StdDraw.filledRectangle(0.76,1.19,0.105,0.04);
		StdDraw.setPenColor(new Color(0,0,0));
		StdDraw.text(0.24,1.04,"BLACK");
		StdDraw.text(0.41,1.04,"" + blackCount);
		StdDraw.text(0.63,1.04,"WHITE");
		StdDraw.text(0.80,1.04,"" + whiteCount);
		StdDraw.text(0.76,1.18,"RESET");
		if(gameOver)
		{
			StdDraw.text(0.34,1.18,"GAME OVER");
		}
		else if(blackTurn)
		{
			StdDraw.text(0.34,1.18,"BLACK'S TURN");
		}
		else
		{
			StdDraw.text(0.34,1.18,"WHITE'S TURN");
		}
	}

	/**
	 *  Checks to see if black can play.  Checks to see if white can play.
	 *  If neither can play, the game is over.  If black can't go, then set
	 *  blackTurn to false.  If white can't go, set blackTurn to true.
	 *  @return         Returns true if the game is over, false otherwise.
	 */
	public boolean checkTurnAndGameOver ( )
	{
		boolean whiteCanGo = false, blackCanGo = false;

		for (int i=0; i<8; i++) 
		{
			for (int j=0; j<8; j++) 
			{
				if (isValidMove(i,j,blackTurn)==true) 
				{
					return false;
				}
			}
		}
		
		for (int i=0; i<8; i++) 
		{
			for (int j=0; j<8; j++) 
			{
				if (isValidMove(i,j,!blackTurn)==true) 
				{
					blackTurn=false;
					return false;
				}
			}
		}
		return true;
	}
}

/**
 * Represents a single cell in the game of Othello.  By default, a cell is black, and
 * has not been played.  When a game piece is "placed" on the board, the boolean played
 * is set to true.  If the game piece is black, then the boolean black is true, and if
 * the game piece is white, then the boolean black is false.  The ints x and y 
 * represent the coordinate values of the cell within the game board, with the lower
 * left at (0,0) and the upper right at (7,7).
 */
class OthelloCell         
{
	/**    The coordinates of the active piece on the board.                              */
	private int x, y;

	/**    Booleans indicating if a piece has been played (or is empty), and indicating
	 *     if the piece is black (or white)                                               */
	private boolean played, black;

	/**
	 *  Creates an OthelloCell object, at the given coordinate pair.
	 *  @param  i      The horizontal coordinate value for the cell on the board.
	 *  @param  j      The vertical coordinate value for the cell on the board.
	 */
	public OthelloCell(int i, int j)
	{
		played = false;
		x = i;
		y = j;
		black = true;
	}

	/**
	 *  Draws the cell on the board, in its current state.
	 */
	public void drawCell ( )   
	{
		StdDraw.setPenColor(new Color(0,0,0));
		StdDraw.filledSquare(0.15 + 0.1 * x, 0.15 + 0.1 * y, 0.05);
		StdDraw.setPenColor(new Color(0,110,0));
		StdDraw.filledSquare(0.15 + 0.1 * x, 0.15 + 0.1 * y, 0.048);

		if(played)
		{
			for(int i = 0; i <= 20; i++)
			{
				if(black)
				{
					StdDraw.setPenColor(new Color(5+8*i,5+8*i,5+8*i));
				}
				else
				{
					StdDraw.setPenColor(new Color(255-8*i,255-8*i,255-8*i));
				}
				StdDraw.filledCircle(0.15 + 0.1 * x - i*0.001, 0.15 + 0.1 * y + i*0.001, 0.043-i*0.002);
			}
		}
	}
	
	/**
	 *  Sets the piece to black (black true) or white (black false).
	 *  @param  bool      The value to be assigned to the game piece.
	 */
	public void setBlack(boolean bool)
	{
		black = bool;
	}
	
	/**
	 *  Return the status of black; true for a black piece, false for a white piece.
	 *  @return            Returns true for a black piece, false for a white piece.
	 */
	public boolean getBlackStatus ( )
	{
		return black;
	}

	/**
	 *  Sets the value of played to true, to indicate that a piece has been placed on this cell.
	 */
	public void playIt ( )   
	{
		played = true;
	}
	
	/**
	 *  Return the status of played, indicating whether or not there is a game piece on this cell.
	 *  @return            Returns true if a game piece is on this cell, false otherwise.
	 */
	public boolean hasBeenPlayed ( )
	{
		return played;
	}
}
