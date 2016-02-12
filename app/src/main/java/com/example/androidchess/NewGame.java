package com.example.androidchess;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidchess.model.Board;
import com.example.androidchess.model.Data;
import com.example.androidchess.model.Move;
import com.example.androidchess.model.Piece;
import com.example.androidchess.model.SavedGame;

public class NewGame extends Activity {
	
	public static ArrayList<String> savedMoves;	// Moves that have been made in this game so far
	public static String firstSquare;	// Square that was clicked on first
	public static String secondSquare;	// Square that was clicked on second
	public static boolean whoseMove, drawOffered, clickedUndo;
	public Piece lastPieceCaptured;	// Keep track of last piece captured in case user hits undo
	public String input;	// The String move, taken from the squares the user clicks on
	public Board board;	// The game board
	public Move move, lastMove;	// Current move and previous move
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		startGame();
	}

	public void startGame(){
		setUpBoard();	// Put images on the correct squares to start
		savedMoves = new ArrayList<String>();	// Initialize ArrayList of String moves for this game
		
		whoseMove = true;	// True if white's move, false if black's move
		board = new Board();	// The game board
		move = null;	// The current move
	}
	
	public void newMove(){
		/*
		 * This method should only be called if there are legitimate
		 * values for firstSquare and secondSquare. Otherwise, it will
		 * simply treat the move as an illegal move.
		 */
		input = firstSquare + " " + secondSquare;
		move = Move.parseMove(input, whoseMove);
		
		if(move == null){	// Move could not be parsed
	    	Toast
	    	.makeText(this, "Illegal move, try again.", Toast.LENGTH_SHORT)
	    	.show();
			resetMove();
			return;
		}
		
		if(!move.isLegal(board)){	// Illegal move
	    	Toast
	    	.makeText(this, "Illegal move, try again.", Toast.LENGTH_SHORT)
	    	.show();
			resetMove();
			return;
		}
		
		/*
		 * This move is legal...
		 */
		
		// Information for undo move:
		lastMove = move;
		lastPieceCaptured = null;
		if(move.endSquare.hasPiece()){
			lastPieceCaptured = move.endSquare.piece;
		}
		
		drawOffered = false;	// Successful move: cancel any previous draw offers
		clickedUndo = false;	// Allow user to use their single undo option again
		savedMoves.add(input);	// Add this move to saved moves
		board.updateBoard(move);	// Move is legal: make the move
		board.cleanUpOldPieces();	// Get rid of pieces that no longer reside on squares
		board.setEnPassantToFalse(move.piece);	// Update en Passant tags for all pawns
		
		// Update GUI board
		updateBoard();
		
		if(board.checkmate(!whoseMove)){	// Opposing player is in checkmate		
			if(whoseMove){	// White delivered checkmate
		    	Toast
		    	.makeText(this, "Checkmate, White wins.", Toast.LENGTH_LONG)
		    	.show();
			} else {	// Black delivered checkmate
		    	Toast
		    	.makeText(this, "Checkmate, Black wins.", Toast.LENGTH_LONG)
		    	.show();				
			}
			gameOver();
			return;
		}
		
		if(board.check() && savedMoves.size() > 1){	// Opposing player is in check
	    	Toast
	    	.makeText(this, "Check!", Toast.LENGTH_SHORT)
	    	.show();
		}
		
		whoseMove = !whoseMove;	// Switch whose turn it is
		resetMove();	// Move processing is done: wait for next move
	}
	
	public void gameOver(){
		if(Home.recordGames){	// If saving is enabled, open the save dialog
			Intent intent = new Intent(this, SaveGame.class);
			intent.putExtra(ViewSavedGames.SAVED_MOVES_KEY, savedMoves);
			startActivity(intent);
			finish();	// Finish this New Game activity
		} else {	// If saving is not enabled, return to Home view
			Intent intent = new Intent(this, Home.class);
			startActivity(intent);
			finish();	// Finish this New Game activity
		}
	}
	
	/**
	 * Button Click Methods
	 */
	public void undo(View view){
		// Make sure this isn't the first move of the game
		if(savedMoves != null){
			if(savedMoves.isEmpty()){
		    	Toast
		    	.makeText(this, "Need to make a move first!", Toast.LENGTH_SHORT)
		    	.show();
		    	return;
			}
		} else {
	    	Toast
	    	.makeText(this, "Need to make a move first!", Toast.LENGTH_SHORT)
	    	.show();
	    	return;
		}
		
		// Do not allow multiple undo clicks
		if(clickedUndo){
	    	Toast
	    	.makeText(this, "Only one undo is allowed.", Toast.LENGTH_SHORT)
	    	.show();
	    	return;
		}
		
		// This is a legitimate click: undo last move
		clickedUndo = true;
		board.revertBoard(lastMove, lastPieceCaptured);	// Step the board back one move
		savedMoves.remove(savedMoves.size()-1);	// Remove the last move from saved moves
		updateBoard();	// Update GUI
		whoseMove = lastMove.piece.whitePiece;	// This player gets another turn
		resetMove();	// Wait for new input
	}

	public void ai(View view){
		Move newMove = board.randomMove(whoseMove);	// Get a legitimate random move
		parseRandomMove(newMove);	// Assign it to firstSquare and secondSquare
		newMove();	// Make the move
	}
	
	public void draw(View view){
		if(!drawOffered){
			drawOffered = true;
			if(whoseMove){	// Currently white's move, so black has offered a draw
		    	Toast
		    	.makeText(this, "Black has offered a draw. Click draw to accept, or make a move to deny.", Toast.LENGTH_SHORT)
		    	.show();
			} else {
		    	Toast
		    	.makeText(this, "White has offered a draw. Click draw to accept, or make a move to deny.", Toast.LENGTH_SHORT)
		    	.show();
			}
		} else {	// Draw accepted
	    	Toast
	    	.makeText(this, "Draw accepted. Game drawn.", Toast.LENGTH_SHORT)
	    	.show();
			gameOver();
		}
	}
	
	public void resign(View view){
		if(whoseMove){
	    	Toast
	    	.makeText(this, "White resigns. Black wins.", Toast.LENGTH_SHORT)
	    	.show();
		} else {
	    	Toast
	    	.makeText(this, "Black resigns. White wins.", Toast.LENGTH_SHORT)
	    	.show();
		}
		gameOver();
	}
	
	/**
	 * Square Click Method
	 * This method should identify which square the user clicked on
	 * and carry out the appropriate action
	 */
	public void squareClicked(View view){
		// Get the name of the square that was clicked on
		String squareName = view
				.getResources()
				.getResourceEntryName(view.getId());
		if (firstSquare == null) {
			firstSquare = squareName;
		} else if (secondSquare == null) {
			secondSquare = squareName;
			// Two squares have been clicked: try to make this move
			newMove();
		} else {
			/*
			 *  If First and Second square have been previously selected,
			 *  treat this as a first square click
			 */
			firstSquare = squareName;
			secondSquare = null;
		}
	}
	
	/*
	 * Overriding onPause, onResume, and onSaveInstanceState
	 * methods to do persistence
	 */
	@Override
	protected void onPause() {
		super.onPause();
		Data.saveData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/* ====================
	 * BEGIN HELPER METHODS
	 * ====================
	 */
	public void resetMove(){	// Empty square-click-holders for new inputs
		firstSquare = null;
		secondSquare = null;
	}
	
	public void updateBoard(){
		String squareString = null, squareID = null;
		boolean squareColor;
		int i, j;
		for(i = 0; i < 8; i++){
			for(j = 0; j < 8; j++){
				squareString = Board.squares[7-j][i].toString();
				squareColor = Board.squares[7-j][i].whiteSquare;
				squareID = getSquareFromNumbers(i,j);
				Log.i("updateBoard", "squareID: " + squareID);
				int resID = getResources().getIdentifier(squareID, "id", getPackageName());
				Log.i("updateBoard", "resID: " + resID);
				ImageView tempView = (ImageView)findViewById(resID);
				
				// Update the image displayed on this square based on the piece
				switch(squareString){
				case "##":
					tempView.setImageResource(R.drawable.bsq);
					break;
				case "  ":
					tempView.setImageResource(R.drawable.wsq);
					break;
				case "wp":
					if(squareColor)
						tempView.setImageResource(R.drawable.wwp);
					else
						tempView.setImageResource(R.drawable.bwp);
					break;
				case "bp":
					if(squareColor)
						tempView.setImageResource(R.drawable.wbp);
					else
						tempView.setImageResource(R.drawable.bbp);
					break;
				case "wN":
					if(squareColor)
						tempView.setImageResource(R.drawable.wwn);
					else
						tempView.setImageResource(R.drawable.bwn);
					break;
				case "bN":
					if(squareColor)
						tempView.setImageResource(R.drawable.wbn);
					else
						tempView.setImageResource(R.drawable.bbn);
					break;
				case "wB":
					if(squareColor)
						tempView.setImageResource(R.drawable.wwb);
					else
						tempView.setImageResource(R.drawable.bwb);
					break;
				case "bB":
					if(squareColor)
						tempView.setImageResource(R.drawable.wbb);
					else
						tempView.setImageResource(R.drawable.bbb);
					break;
				case "wR":
					if(squareColor)
						tempView.setImageResource(R.drawable.wwr);
					else
						tempView.setImageResource(R.drawable.bwr);
					break;
				case "bR":
					if(squareColor)
						tempView.setImageResource(R.drawable.wbr);
					else
						tempView.setImageResource(R.drawable.bbr);
					break;
				case "wQ":
					if(squareColor)
						tempView.setImageResource(R.drawable.wwq);
					else
						tempView.setImageResource(R.drawable.bwq);
					break;
				case "bQ":
					if(squareColor)
						tempView.setImageResource(R.drawable.wbq);
					else
						tempView.setImageResource(R.drawable.bbq);
					break;
				case "wK":
					if(squareColor)
						tempView.setImageResource(R.drawable.wwk);
					else
						tempView.setImageResource(R.drawable.bwk);
					break;
				case "bK":
					if(squareColor)
						tempView.setImageResource(R.drawable.wbk);
					else
						tempView.setImageResource(R.drawable.bbk);
					break;
				default:	// Default to white square
					tempView.setImageResource(R.drawable.wsq);
				}
				
			}
		}
	}
	
	public String getSquareFromNumbers(int i, int j){
		String returnString = " ";
		
		switch(i){
		case 0:
			returnString = "a";
			break;
		case 1:
			returnString = "b";
			break;
		case 2:
			returnString = "c";
			break;
		case 3:
			returnString = "d";
			break;
		case 4:
			returnString = "e";
			break;
		case 5:
			returnString = "f";
			break;
		case 6:
			returnString = "g";
			break;
		case 7:
			returnString = "h";
			break;
		default:	// Default to h
			returnString = "h";
		}
		
		/*
		 *  Return rank letter concatenated with file index plus one
		 *  (Example: (7,0) becomes "h1")
		 */
		return returnString += j+1;
	}
	
	private void parseRandomMove(Move move){
		int startRank = move.startSquare.rank;
		int startFile = move.startSquare.file;
		int endRank = move.endSquare.rank;
		int endFile = move.endSquare.file;
		
		firstSquare = getSquareFromNumbers(startFile, 7-startRank);
		secondSquare = getSquareFromNumbers(endFile, 7-endRank);
	}
	
	private void setUpBoard(){
		ImageView tempView;
		/* FIRST ROW */
		tempView = (ImageView)findViewById(R.id.a1);
		tempView.setImageResource(R.drawable.bwr);
		tempView = (ImageView)findViewById(R.id.b1);
		tempView.setImageResource(R.drawable.wwn);
		tempView = (ImageView)findViewById(R.id.c1);
		tempView.setImageResource(R.drawable.bwb);
		tempView = (ImageView)findViewById(R.id.d1);
		tempView.setImageResource(R.drawable.wwq);
		tempView = (ImageView)findViewById(R.id.e1);
		tempView.setImageResource(R.drawable.bwk);
		tempView = (ImageView)findViewById(R.id.f1);
		tempView.setImageResource(R.drawable.wwb);
		tempView = (ImageView)findViewById(R.id.g1);
		tempView.setImageResource(R.drawable.bwn);
		tempView = (ImageView)findViewById(R.id.h1);
		tempView.setImageResource(R.drawable.wwr);
		/* SECOND ROW */
		tempView = (ImageView)findViewById(R.id.a2);
		tempView.setImageResource(R.drawable.wwp);
		tempView = (ImageView)findViewById(R.id.b2);
		tempView.setImageResource(R.drawable.bwp);
		tempView = (ImageView)findViewById(R.id.c2);
		tempView.setImageResource(R.drawable.wwp);
		tempView = (ImageView)findViewById(R.id.d2);
		tempView.setImageResource(R.drawable.bwp);
		tempView = (ImageView)findViewById(R.id.e2);
		tempView.setImageResource(R.drawable.wwp);
		tempView = (ImageView)findViewById(R.id.f2);
		tempView.setImageResource(R.drawable.bwp);
		tempView = (ImageView)findViewById(R.id.g2);
		tempView.setImageResource(R.drawable.wwp);
		tempView = (ImageView)findViewById(R.id.h2);
		tempView.setImageResource(R.drawable.bwp);
		/* EIGHTH ROW */
		tempView = (ImageView)findViewById(R.id.a8);
		tempView.setImageResource(R.drawable.wbr);
		tempView = (ImageView)findViewById(R.id.b8);
		tempView.setImageResource(R.drawable.bbn);
		tempView = (ImageView)findViewById(R.id.c8);
		tempView.setImageResource(R.drawable.wbb);
		tempView = (ImageView)findViewById(R.id.d8);
		tempView.setImageResource(R.drawable.bbq);
		tempView = (ImageView)findViewById(R.id.e8);
		tempView.setImageResource(R.drawable.wbk);
		tempView = (ImageView)findViewById(R.id.f8);
		tempView.setImageResource(R.drawable.bbb);
		tempView = (ImageView)findViewById(R.id.g8);
		tempView.setImageResource(R.drawable.wbn);
		tempView = (ImageView)findViewById(R.id.h8);
		tempView.setImageResource(R.drawable.bbr);
		/* SEVENTH ROW */
		tempView = (ImageView)findViewById(R.id.a7);
		tempView.setImageResource(R.drawable.bbp);
		tempView = (ImageView)findViewById(R.id.b7);
		tempView.setImageResource(R.drawable.wbp);
		tempView = (ImageView)findViewById(R.id.c7);
		tempView.setImageResource(R.drawable.bbp);
		tempView = (ImageView)findViewById(R.id.d7);
		tempView.setImageResource(R.drawable.wbp);
		tempView = (ImageView)findViewById(R.id.e7);
		tempView.setImageResource(R.drawable.bbp);
		tempView = (ImageView)findViewById(R.id.f7);
		tempView.setImageResource(R.drawable.wbp);
		tempView = (ImageView)findViewById(R.id.g7);
		tempView.setImageResource(R.drawable.bbp);
		tempView = (ImageView)findViewById(R.id.h7);
		tempView.setImageResource(R.drawable.wbp);
		/* SQUARES IN ROW 3 THROUGH 6 */
		tempView = (ImageView)findViewById(R.id.a3);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.b3);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.c3);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.d3);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.e3);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.f3);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.g3);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.h3);
		tempView.setImageResource(R.drawable.wsq);
		
		tempView = (ImageView)findViewById(R.id.a4);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.b4);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.c4);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.d4);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.e4);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.f4);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.g4);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.h4);
		tempView.setImageResource(R.drawable.bsq);
		
		tempView = (ImageView)findViewById(R.id.a5);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.b5);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.c5);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.d5);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.e5);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.f5);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.g5);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.h5);
		tempView.setImageResource(R.drawable.wsq);

		tempView = (ImageView)findViewById(R.id.a6);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.b6);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.c6);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.d6);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.e6);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.f6);
		tempView.setImageResource(R.drawable.bsq);
		tempView = (ImageView)findViewById(R.id.g6);
		tempView.setImageResource(R.drawable.wsq);
		tempView = (ImageView)findViewById(R.id.h6);
		tempView.setImageResource(R.drawable.bsq);

	}
	
	// Keys for persistence
	/*public static final String
		BOARD_KEY = "board",
		INPUT_KEY = "input",
		FIRST_SQUARE_KEY = "firstSquare",
		SECOND_SQUARE_KEY = "secondSquare",
		WHOSE_MOVE_KEY = "whoseMove",
		DRAW_OFFERED_KEY = "drawOffered",
		CLICKED_UNDO_KEY = "clickedUndo",
		MOVE_KEY = "move",
		LAST_MOVE_KEY = "lastMove",
		LAST_PIECE_CAPTURED_KEY = "lastPieceCaptured";*/

}