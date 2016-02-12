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

public class ReplayGame extends Activity {
	
	public static ArrayList<String> savedMoves;	// The moves to replay
	public static int currentMoveIndex;	// The index of the current move
	public static boolean whoseMove;	// True if it's white's move, false if it's black's move
	public String input;	// The String move, taken from savedMoves
	public Board board;	// The game board
	public Move move;	// Current move

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_replay_game);
		
		// Get the list of saved moves
		Intent intent = getIntent();
		if (intent == null) {
	    	Toast
	    	.makeText(this, "Error: activity called without passing saved moves.", Toast.LENGTH_SHORT)
	    	.show();
	    	return;
		}
		
		savedMoves = intent.getStringArrayListExtra(ViewSavedGames.SAVED_MOVES_KEY);
		
		if (savedMoves == null) {
	    	Toast
	    	.makeText(this, "Error: saved moves is null.", Toast.LENGTH_SHORT)
	    	.show();
	    	return;
		}
		
		startGame();
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
	
	public void startGame(){
		setUpBoard();	// Put images on the correct squares to start
		
		whoseMove = true;	// True if white's move, false if black's move
		currentMoveIndex = 0;	// First move
		input = null;
		board = new Board();	// The game board
		move = null;	// The current move
	}
	
	/*
	 * Button click listener for Next Move
	 */
	public void nextMove(View view){
		if(currentMoveIndex >= savedMoves.size()){
			// Go back to ViewSavedGames
	    	Toast
	    	.makeText(this, "Game over.", Toast.LENGTH_SHORT)
	    	.show();
			Intent intent = new Intent(this, ViewSavedGames.class);
			startActivity(intent);
			finish();
		}
		input = savedMoves.get(currentMoveIndex);
		move = Move.parseMove(input, whoseMove);
		
		board.updateBoard(move);	// Make the move
		board.cleanUpOldPieces();	// Get rid of pieces that no longer reside on squares
		board.setEnPassantToFalse(move.piece);	// Update en Passant tags for all pawns
		
		// Update GUI board
		updateBoard();
		
		whoseMove = !whoseMove;	// Switch whose turn it is
		currentMoveIndex++;	// Prepare to get the next move
		
		if(currentMoveIndex >= savedMoves.size()){
			// Go back to ViewSavedGames
	    	Toast
	    	.makeText(this, "Game over.", Toast.LENGTH_SHORT)
	    	.show();
			Intent intent = new Intent(this, ViewSavedGames.class);
			startActivity(intent);
			finish();
		}
	}
	
	public void squareClicked(View view) {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.replay_game, menu);
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
}