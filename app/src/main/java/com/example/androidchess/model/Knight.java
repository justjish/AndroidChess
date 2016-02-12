package com.example.androidchess.model;

import java.util.ArrayList;

/* @author Colin Drucquer, Sujish Patel
 */

public class Knight extends Piece {
	
	/**
	 * Instantiates a new knight.
	 *
	 * @param whitePiece the color
	 * @param square the square it's on
	 */
	public Knight (boolean whitePiece, Square square) {
		this.whitePiece = whitePiece;
		this.square = square;
	}

	/* (non-Javadoc)
	 * @see gameInfo.Piece#toString()
	 */
	public String toString() {
		if(this.whitePiece){
			return "wN";
		}
		return "bN";
	}

	/* (non-Javadoc)
	 * @see gameInfo.Piece#possibleMoves()
	 */
	public ArrayList<Move> possibleMoves() {
		// Get this piece's rank and file
		int startRank = this.square.rank;
		int startFile = this.square.file;
		Square endSquare;
		Move newMove;
		ArrayList<Move> possibleMoves = new ArrayList<Move>();

		// Try all 8 squares to which a Knight could move, add the legal ones
		// Toward A8
		for(int i = 1; i < 3; i++){
			for(int j = 1; j < 3; j++){
				if(i != j && Board.trySquare(startRank - i, startFile - j) != null){
					endSquare = Board.trySquare(startRank - i, startFile - j);
					if(endSquare.hasPiece()){
						if(endSquare.piece.whitePiece != this.whitePiece){	// Enemy piece, this is legal
							newMove = this.createMoveFromSquare(endSquare);
							possibleMoves.add(newMove);
						}
					} else {	// No piece, this is legal
						newMove = this.createMoveFromSquare(endSquare);
						possibleMoves.add(newMove);						
					}
				}
			}
		}
		
		// Toward A1
		for(int i = 1; i < 3; i++){
			for(int j = 1; j < 3; j++){
				if(i != j && Board.trySquare(startRank + i, startFile - j) != null){
					endSquare = Board.trySquare(startRank + i, startFile - j);
					if(endSquare.hasPiece()){
						if(endSquare.piece.whitePiece != this.whitePiece){	// Enemy piece, this is legal
							newMove = this.createMoveFromSquare(endSquare);
							possibleMoves.add(newMove);
						}
					} else {	// No piece, this is legal
						newMove = this.createMoveFromSquare(endSquare);
						possibleMoves.add(newMove);						
					}
				}
			}
		}
		
		// Toward H8
		for(int i = 1; i < 3; i++){
			for(int j = 1; j < 3; j++){
				if(i != j && Board.trySquare(startRank - i, startFile + j) != null){
					endSquare = Board.trySquare(startRank - i, startFile + j);
					if(endSquare.hasPiece()){
						if(endSquare.piece.whitePiece != this.whitePiece){	// Enemy piece, this is legal
							newMove = this.createMoveFromSquare(endSquare);
							possibleMoves.add(newMove);
						}
					} else {	// No piece, this is legal
						newMove = this.createMoveFromSquare(endSquare);
						possibleMoves.add(newMove);						
					}
				}
			}
		}
		
		// Toward H1
		for(int i = 1; i < 3; i++){
			for(int j = 1; j < 3; j++){
				if(i != j && Board.trySquare(startRank + i, startFile + j) != null){
					endSquare = Board.trySquare(startRank + i, startFile + j);
					if(endSquare.hasPiece()){
						if(endSquare.piece.whitePiece != this.whitePiece){	// Enemy piece, this is legal
							newMove = this.createMoveFromSquare(endSquare);
							possibleMoves.add(newMove);
						}
					} else {	// No piece, this is legal
						newMove = this.createMoveFromSquare(endSquare);
						possibleMoves.add(newMove);						
					}
				}
			}
		}
		
		
		// All possible moves have been added
		return possibleMoves;
	}

}
