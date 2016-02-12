package com.example.androidchess.model;

import java.util.ArrayList;

/* @author Colin Drucquer, Sujish Patel
 */

public class Rook extends Piece {

	/**
	 * Instantiates a new rook.
	 *
	 * @param whitePiece the color
	 * @param square the square it's on
	 */
	public Rook (boolean whitePiece, Square square) {
		this.whitePiece = whitePiece;
		this.square = square;
	}

	/* (non-Javadoc)
	 * @see gameInfo.Piece#toString()
	 */
	public String toString() {
		if(this.whitePiece){
			return "wR";
		}
		return "bR";
	}

	/* (non-Javadoc)
	 * @see gameInfo.Piece#possibleMoves()
	 */
	public ArrayList<Move> possibleMoves() {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		possibleMoves = this.tryLateral();
		
		// All possible moves have been added
		return possibleMoves;
	}

}
