package com.example.androidchess;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.example.androidchess.model.Data;
import com.example.androidchess.model.SavedGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Home extends Activity {
	
	public static boolean recordGames = false;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Data.ctx = getApplicationContext();
        Data.loadData();
        setContentView(R.layout.activity_home);
    }
	

    /**
     * Called when New Game button is pressed
     */
    public void newGame(View view){
		Intent intent = new Intent(this, NewGame.class);
		startActivity(intent);
    }
    
    /**
     * Called when View Saved Games button is pressed
     */
    public void viewSavedGames(View view){
		Intent intent = new Intent(this, ViewSavedGames.class);
		startActivity(intent);
    }
    
    /**
     * Called when Record Games button is pressed
     */
    public void recordGames(View view){
		Intent intent = new Intent(this, RecordGames.class);
		startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
