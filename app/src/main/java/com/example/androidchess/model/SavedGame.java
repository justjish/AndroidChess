package com.example.androidchess.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class SavedGame implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String title;
	public ArrayList<String> moves;
	public Calendar datePlayed;
	
	public SavedGame(ArrayList<String> givenMoves, String givenTitle){
		
		this.title=givenTitle;
		this.moves=givenMoves;
		this.datePlayed = Calendar.getInstance();
	}
	
	public String toString() {
		return title +" - "+ datePlayed.getTime().toString();
	}

}
