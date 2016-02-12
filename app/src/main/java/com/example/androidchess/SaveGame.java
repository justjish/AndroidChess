package com.example.androidchess;

import java.util.ArrayList;

import com.example.androidchess.model.Data;
import com.example.androidchess.model.SavedGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SaveGame extends Activity {
	
	public static ArrayList<String> savedMoves;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_game);
		
		// Get the list of moves to save
		Intent intent = getIntent();
		if (intent == null) {
	    	Toast
	    	.makeText(this, "Error: activity called without passing saved moves", Toast.LENGTH_SHORT)
	    	.show();
	    	return;
		}
		
		savedMoves = intent.getStringArrayListExtra(ViewSavedGames.SAVED_MOVES_KEY);
		
		if (savedMoves == null) {
	    	Toast
	    	.makeText(this, "Error: saved moves is null", Toast.LENGTH_SHORT)
	    	.show();
	    	return;
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
	
	public void confirm(View view){
		EditText text = (EditText)findViewById(R.id.editText1);
		String title = text.getText().toString();
		
		if(title == null){
	    	Toast
	    	.makeText(this, "Please enter a title!", Toast.LENGTH_SHORT)
	    	.show();
	    	return;
		}
		
		// Store this game with the title in Edit Text
		boolean stored = storeGame(title);
		
		if (!stored){
	    	Toast
	    	.makeText(this, "Error: game was not saved", Toast.LENGTH_SHORT)
	    	.show();
	    	return;
		}
		
    	Toast
    	.makeText(this, "Game saved with title: " + title, Toast.LENGTH_SHORT)
    	.show();
    	
    	SavedGame game = new SavedGame(savedMoves,title);
    	Data.games_list.add(game);
    	Data.saveData();
    	// Finish this activity and go back to home screen
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
		finish();
	}
	public void cancel(View view){	// Go back to main menu
    	Toast
    	.makeText(this, "Save cancelled", Toast.LENGTH_SHORT)
    	.show();
    	
    	// Finish this activity and go back to home screen
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
		finish();
	}
	
	/*
	 * Method to store the game (ArrayList<String> savedMoves), with this title
	 */
	private boolean storeGame(String title){
		// TODO Complete this method
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.save_game, menu);
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
}
