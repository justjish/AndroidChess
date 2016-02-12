package com.example.androidchess.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import android.content.Context;

public class Data implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 93082451920156397L;
	public static ArrayList<SavedGame> games_list;
	public static Context ctx;
	
	public static void loadData() {
    	File f = new File(ctx.getFilesDir(), "sav.dat");
		if (f.exists()) {
			try {
				FileInputStream saveFile = ctx.openFileInput("sav.dat");
				ObjectInputStream save = new ObjectInputStream(saveFile);
				games_list = (ArrayList<SavedGame>) save.readObject();
				save.close();
			} catch(Exception e){ 
				e.printStackTrace();
			}
		} else {
			Data.games_list = new ArrayList<SavedGame>();
		}
    }
    
    public static void saveData() {
    	
		try {
			FileOutputStream saveFile = ctx.openFileOutput("sav.dat", 0);
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(games_list);
			save.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
