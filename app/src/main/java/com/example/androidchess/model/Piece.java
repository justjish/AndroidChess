package com.example.androidchess.model;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Piece.
 * @author Colin Drucquer, Sujish Patel
 */
public abstract class Piece {
	
	/**  True if this is a White piece, false if it's a Black piece. */
	public boolean whitePiece;
	
	/** True if this piece has moved, false otherwise. */
	public boolean hasMoved;
	
	/** The square this piece is on. */
	public Square square;
	
	/** The number of moves this piece has made. */
	public int numMoves;
	
	/**  This piece's unique ID (derived from count). */
	public int ID;
	
	/**  The total number of pieces. */
	public static int count;
	
	/**
	 * Instantiates a new piece.
	 */
	public Piece(){
		this.ID = count++;
		this.numMoves = 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
	
	/**
	 * Legal moves.
	 *
	 * @return Array list of possible moves this piece could make
	 */
	public abstract ArrayList<Move> possibleMoves();
	
	/**
	 * Check for friendly piece.
	 *
	 * @param square the square
	 * @return true, if successful
	 */
	public boolean checkForFriendlyPiece(Square square){
		if(square.hasPiece()){
			if(square.piece.whitePiece == this.whitePiece){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check for piece.
	 *
	 * @param square the square
	 * @return true, if successful
	 */
	public boolean checkForPiece(Square square){
		if(square.hasPiece()){
			return true;
		}
		return false;
	}
	
	/**
	 * Creates the move from square.
	 *
	 * @param end the end
	 * @return the move
	 */
	public Move createMoveFromSquare(Square end){
		return new Move(this.square, end, this);
	}
	
	/**
	 * Checks for moved.
	 *
	 * @return true, if successful
	 */
	public boolean hasMoved(){
		return this.hasMoved;
	}
	
	/**
	 * Try diagonal.
	 *
	 * @return the array list
	 */
	public ArrayList<Move> tryDiagonal(){
		// Get this piece's rank and file
		int startRank = this.square.rank;
		int startFile = this.square.file;
		Square endSquare;
		Move newMove;
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		// Try all 4 directions to which a Bishop could move, add the legal ones
		
		// Toward H1
		for(int i = startRank+1, j = startFile+1; i < 8 || j < 8; i++, j++){
			if(Board.trySquare(i, j) != null){
				endSquare = Board.trySquare(i, j);
				if(endSquare.hasPiece()){
					if(endSquare.piece.whitePiece != this.whitePiece){
						// Enemy piece: this is legal
						newMove = this.createMoveFromSquare(endSquare);
						possibleMoves.add(newMove);
					}
					// Additional moves along this direction are not legal
					break;
				} else {	// No piece, this is legal
					newMove = this.createMoveFromSquare(endSquare);
					possibleMoves.add(newMove);						
				}
			} else {	// Out of bounds -- no need to check further
				break;
			}
		}
		
		// Toward A1
		for(int i = startRank+1, j = startFile-1; i < 8 || j >= 0; i++, j--){
			if(Board.trySquare(i, j) != null){
				endSquare = Board.trySquare(i, j);
				if(endSquare.hasPiece()){
					if(endSquare.piece.whitePiece != this.whitePiece){
						// Enemy piece: this is legal
						newMove = this.createMoveFromSquare(endSquare);
						possibleMoves.add(newMove);
					}
					// Additional moves along this direction are not legal
					break;
				} else {	// No piece, this is legal
					newMove = this.createMoveFromSquare(endSquare);
					possibleMoves.add(newMove);						
				}
			} else {	// Out of bounds -- no need to check further
				break;
			}
		}
		
		// Toward A8
		for(int i = startRank-1, j = startFile-1; i >= 0 || j >= 0; i--, j--){
			if(Board.trySquare(i, j) != null){
				endSquare = Board.trySquare(i, j);
				if(endSquare.hasPiece()){
					if(endSquare.piece.whitePiece != this.whitePiece){
						// Enemy piece: this is legal
						newMove = this.createMoveFromSquare(endSquare);
						possibleMoves.add(newMove);
					}
					// Additional moves along this direction are not legal
					break;
				} else {	// No piece, this is legal
					newMove = this.createMoveFromSquare(endSquare);
					possibleMoves.add(newMove);						
				}
			} else {	// Out of bounds -- no need to check further
				break;
			}
		}
		
		// Toward H8
		for(int i = startRank-1, j = startFile+1; i >= 0 || j < 8; i--, j++){
			if(Board.trySquare(i, j) != null){
				endSquare = Board.trySquare(i, j);
				if(endSquare.hasPiece()){
					if(endSquare.piece.whitePiece != this.whitePiece){
						// Enemy piece: this is legal
						newMove = this.createMoveFromSquare(endSquare);
						possibleMoves.add(newMove);
					}
					// Additional moves along this direction are not legal
					break;
				} else {	// No piece, this is legal
					newMove = this.createMoveFromSquare(endSquare);
					possibleMoves.add(newMove);						
				}
			} else {	// Out of bounds -- no need to check further
				break;
			}
		}
		
		// All possible moves have been added
		if(possibleMoves.isEmpty()){
			return null;
		}
		return possibleMoves;
	}
	
	/**
	 * Try lateral.
	 *
	 * @return the array list
	 */
	public ArrayList<Move> tryLateral(){
		// Get this piece's rank and file
		int startRank = this.square.rank;
		int startFile = this.square.file;
		Square endSquare;
		Move newMove;
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		// Try all 4 directions to which a Rook could move, add the legal ones
		
		// Toward first rank
		for(int i = startRank+1, j = startFile; i < 8; i++){
			if(Board.trySquare(i, j) != null){
				endSquare = Board.trySquare(i, j);
				if(endSquare.hasPiece()){
					if(endSquare.piece.whitePiece != this.whitePiece){
						// Enemy piece: this is legal
						newMove = this.createMoveFromSquare(endSquare);
						possibleMoves.add(newMove);
					}
					// Additional moves along this direction are not legal
					break;
				} else {	// No piece, this is legal
					newMove = this.createMoveFromSquare(endSquare);
					possibleMoves.add(newMove);						
				}
			} else {	// Out of bounds -- no need to check further
				break;
			}
		}
		
		// Toward eighth rank
		for(int i = startRank-1, j = startFile; i >= 0; i--){
			if(Board.trySquare(i, j) != null){
				endSquare = Board.trySquare(i, j);
				if(endSquare.hasPiece()){
					if(endSquare.piece.whitePiece != this.whitePiece){
						// Enemy piece: this is legal
						newMove = this.createMoveFromSquare(endSquare);
						possibleMoves.add(newMove);
					}
					// Additional moves along this direction are not legal
					break;
				} else {	// No piece, this is legal
					newMove = this.createMoveFromSquare(endSquare);
					possibleMoves.add(newMove);						
				}
			} else {	// Out of bounds -- no need to check further
				break;
			}
		}
		
		// Toward H-file
		for(int i = startRank, j = startFile+1; j < 8; j++){
			if(Board.trySquare(i, j) != null){
				endSquare = Board.trySquare(i, j);
				if(endSquare.hasPiece()){
					if(endSquare.piece.whitePiece != this.whitePiece){
						// Enemy piece: this is legal
						newMove = this.createMoveFromSquare(endSquare);
						possibleMoves.add(newMove);
					}
					// Additional moves along this direction are not legal
					break;
				} else {	// No piece, this is legal
					newMove = this.createMoveFromSquare(endSquare);
					possibleMoves.add(newMove);						
				}
			} else {	// Out of bounds -- no need to check further
				break;
			}
		}
		
		// Toward A-file
		for(int i = startRank, j = startFile-1; j >= 0; j--){
			if(Board.trySquare(i, j) != null){
				endSquare = Board.trySquare(i, j);
				if(endSquare.hasPiece()){
					if(endSquare.piece.whitePiece != this.whitePiece){
						// Enemy piece: this is legal
						newMove = this.createMoveFromSquare(endSquare);
						possibleMoves.add(newMove);
					}
					// Additional moves along this direction are not legal
					break;
				} else {	// No piece, this is legal
					newMove = this.createMoveFromSquare(endSquare);
					possibleMoves.add(newMove);						
				}
			} else {	// Out of bounds -- no need to check further
				break;
			}
		}
		
		// All possible moves have been added
		if(possibleMoves.isEmpty()){
			return null;
		}
		return possibleMoves;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Piece)){
			return false;
		}
		
		Piece other = (Piece)o;
		
		// Two squares are equal if they have the same ID
		if(other.ID == this.ID){
			return true;
		}
		
		return false;
	}
}
