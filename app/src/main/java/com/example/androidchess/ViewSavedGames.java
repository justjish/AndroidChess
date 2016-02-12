package com.example.androidchess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.example.androidchess.model.Data;
import com.example.androidchess.model.SavedGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ViewSavedGames extends Activity {
	
	// Key for passing around the ArrayList<String> of saved moves
	public static final String SAVED_MOVES_KEY = "saved moves";
	
	/**
	 * Code for launching replay game, given an ArrayList of moves to play over:
	 * 		Intent intent = new Intent(this, ReplayGame.class);
			intent.putExtra(SAVED_MOVES_KEY, savedMoves);
			startActivity(intent);
			finish();
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_saved_games);
		populateSavedGames();
		selectedGame();
	}
	
	public void selectedGame() {
		ListView list  = (ListView) findViewById(R.id.saved_games_list);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> paret, View viewClicked, int position, long id) {
				SavedGame p = Data.games_list.get(position);
				Intent intent = new Intent(ViewSavedGames.this, ReplayGame.class);
				intent.putExtra(SAVED_MOVES_KEY, p.moves);
				startActivity(intent);
				finish();
			}
			{
		}
		});
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
	
	private void populateSavedGames(){
		int totalSavedGames = Data.games_list.size();
		String[] games = new String[totalSavedGames];
		
		for (int i = 0; i<totalSavedGames; i++) {
			games[i] = Data.games_list.get(i).toString();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.games,
				games);
		
		ListView list  = (ListView) findViewById(R.id.saved_games_list);
		list.setAdapter(adapter);
		
	}
	
	public void sortByDate(View view) {
		int totalSavedGames = Data.games_list.size();
		
		Collections.sort(Data.games_list, new Comparator<SavedGame>() {
			  public int compare(SavedGame o1, SavedGame o2) {
			      return o1.datePlayed.compareTo(o2.datePlayed);
			  }
			});
		
		String[] games = new String[totalSavedGames];
		
		for (int i = 0; i<totalSavedGames; i++) {
			games[i] = Data.games_list.get(i).toString();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.games,
				games);
		ListView list  = (ListView) findViewById(R.id.saved_games_list);
		list.setAdapter(adapter);
	}
	
	public void sortByTitle(View view) {
		int totalSavedGames = Data.games_list.size();
		
		Collections.sort(Data.games_list, new Comparator<SavedGame>() {
			  public int compare(SavedGame o1, SavedGame o2) {
				  if(o1 != null && o2 != null)
				  {
					  return o1.title.compareToIgnoreCase(o2.title);
					  }
				  return 0;
			  }
			});
		
		String[] games = new String[totalSavedGames];
		
		for (int i = 0; i<totalSavedGames; i++) {
			games[i] = Data.games_list.get(i).toString();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.games,
				games);
		ListView list  = (ListView) findViewById(R.id.saved_games_list);
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_saved_games, menu);
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
