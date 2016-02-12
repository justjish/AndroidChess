package com.example.androidchess.model;

import java.util.ArrayList;

/* @author Colin Drucquer, Sujish Patel
 */

public class Bishop extends Piece {
	
	/**
	 * Instantiates a new bishop.
	 *
	 * @param whitePiece the color
	 * @param square the square it's on
	 */
	public Bishop (boolean whitePiece, Square square) {
		this.whitePiece = whitePiece;
		this.square = square;
	}

	/* (non-Javadoc)
	 * @see gameInfo.Piece#toString()
	 */
	public String toString() {
		if(this.whitePiece){
			return "wB";
		}
		return "bB";
	}

	/* (non-Javadoc)
	 * @see gameInfo.Piece#possibleMoves()
	 */
	public ArrayList<Move> possibleMoves() {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		possibleMoves = this.tryDiagonal();
		
		// All possible moves have been added
		
		/*if(possibleMoves.isEmpty()){
			return null;
		}*/
		
		return possibleMoves;
	}

}
