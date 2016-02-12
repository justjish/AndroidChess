package com.example.androidchess;

import com.example.androidchess.model.Data;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class RecordGames extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_games);
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
	
	public void onToggleClicked(View view) {
	    boolean on = ((Switch)view).isChecked();
	    Home.recordGames = on;
	    
	    if (on) {
	    	Toast
	    	.makeText(this, "Games will be recorded", Toast.LENGTH_SHORT)
	    	.show();
	    } else {
	    	Toast
	    	.makeText(this, "Games will not be recorded", Toast.LENGTH_SHORT)
	    	.show();
	    }
	}
	
	public void done(View view){	// Go back to main menu
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
		finish();	// Finish this activity
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record_games, menu);
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
