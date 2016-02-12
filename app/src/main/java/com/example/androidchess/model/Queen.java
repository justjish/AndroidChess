package com.example.androidchess.model;

import java.util.ArrayList;

/* @author Colin Drucquer, Sujish Patel
 */

public class Queen extends Piece {
	
	/**
	 * Instantiates a new queen.
	 *
	 * @param whitePiece the color
	 * @param square the square it's on
	 */
	public Queen (boolean whitePiece, Square square) {
		this.whitePiece = whitePiece;
		this.square = square;
	}

	/* (non-Javadoc)
	 * @see gameInfo.Piece#toString()
	 */
	public String toString() {
		if(this.whitePiece){
			return "wQ";
		}
		return "bQ";
	}

	/* (non-Javadoc)
	 * @see gameInfo.Piece#possibleMoves()
	 */
	public ArrayList<Move> possibleMoves() {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		possibleMoves = this.tryDiagonal();
		
		if(possibleMoves == null)
			possibleMoves = this.tryLateral();
		else {
			if(this.tryLateral() != null)
				possibleMoves.addAll(this.tryLateral());
		}

		
		// All possible moves have been added
		return possibleMoves;
	}

}
