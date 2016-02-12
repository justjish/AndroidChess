package com.example.androidchess.model;

import java.util.ArrayList;

/* @author Colin Drucquer, Sujish Patel
 */

public class Pawn extends Piece {
	
	public boolean justMoved;

	/**
	 * Instantiates a new pawn.
	 *
	 * @param whitePiece the color
	 * @param square the square it's on
	 */
	public Pawn (boolean whitePiece, Square square) {
		this.whitePiece = whitePiece;
		this.square = square;
	}

	/* (non-Javadoc)
	 * @see gameInfo.Piece#toString()
	 */
	public String toString() {
		if(this.whitePiece){
			return "wp";
		}
		return "bp";
	}

	/* (non-Javadoc)
	 * @see gameInfo.Piece#possibleMoves()
	 */
	public ArrayList<Move> possibleMoves() {
		// Get this piece's rank and file
		int startRank = this.square.rank;
		int startFile = this.square.file;
		int endRank;
		int endFile;
		Square endSquare;
		Move newMove;
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		/* Moving 2 squares forward could be a possible move
		 */
		if(!this.hasMoved){
			if(this.whitePiece){	// White piece
				// Check if there are any pieces in the way
				if(!Board.squares[this.square.rank - 1][startFile].hasPiece()
						&& !Board.squares[this.square.rank - 2][startFile].hasPiece()){
					// Add this move
					endRank = this.square.rank - 2;
					endSquare = Board.squares[endRank][startFile];
					newMove = new Move(this.square, endSquare, this);
					newMove.twoSquarePawnMove = true;
					possibleMoves.add(newMove);
				}
			} else {	// Black piece
				// Check if there are any pieces in the way
				if(!Board.squares[this.square.rank + 1][startFile].hasPiece()
						&& !Board.squares[this.square.rank + 2][startFile].hasPiece()){
					// Add this move
					endRank = this.square.rank + 2;
					endSquare = Board.squares[endRank][startFile];
					newMove = new Move(this.square, endSquare, this);
					newMove.twoSquarePawnMove = true;
					possibleMoves.add(newMove);
				}
			}
		}
		
		/* Moving 1 square forward is a possible move: check for promotion
		 */
		if(this.whitePiece){	// White piece
			// Check if there is a piece in the way
			if(Board.trySquare(this.square.rank - 1, startFile) != null){
				if(!Board.squares[this.square.rank - 1][startFile].hasPiece()){
					endRank = this.square.rank - 1;
					endSquare = Board.squares[endRank][startFile];
					if(startRank == 1){	// Promotion
						addAllPromotions(possibleMoves, this, endSquare);
					} else {
						newMove = new Move(this.square, endSquare, this);
						possibleMoves.add(newMove);
					}
				}
			}
		} else {	// Black piece
			// Check if there is a piece in the way
			if(Board.trySquare(this.square.rank + 1, startFile) != null){
				if(!Board.squares[this.square.rank + 1][startFile].hasPiece()){
					endRank = this.square.rank + 1;
					endSquare = Board.squares[endRank][startFile];
					if(startRank == 6){	// Promotion
						addAllPromotions(possibleMoves, this, endSquare);
					} else {
						newMove = new Move(this.square, endSquare, this);
						possibleMoves.add(newMove);
					}
				}
			}
		}
		
		/*	Capturing diagonally is a possible move if there is an enemy piece there
		 * 	It's also possible if the piece next to the pawn is a pawn that just moved 2 spaces
		 */
		// Capture diagonally left (towards A-file)
		if(this.whitePiece){	// White piece
			if(Board.trySquare(this.square.rank - 1, startFile - 1) != null){
				if(Board.squares[this.square.rank - 1][startFile - 1].hasPiece()){	// There is a piece here
					if(!Board.squares[this.square.rank - 1][startFile - 1].piece.whitePiece){	// The piece is black
						endRank = this.square.rank - 1;
						endFile = this.square.file - 1;
						endSquare = Board.squares[endRank][endFile];
						if(startRank == 1){	// Promotion
							addAllPromotions(possibleMoves, this, endSquare);
						} else {
							newMove = new Move(this.square, endSquare, this);
							possibleMoves.add(newMove);
						}
					}
				} else if(Board.trySquare(this.square.rank, startFile - 1) != null){ 
					if(Board.squares[this.square.rank][startFile - 1].hasPiece()){	// There is a piece next to the pawn
						if(Board.squares[this.square.rank][startFile - 1].piece instanceof Pawn){	// It's a pawn
							Pawn temp = (Pawn)Board.squares[this.square.rank][startFile - 1].piece;
							if(temp.justMoved && !temp.whitePiece){	// It can be captured en Passant
								endRank = this.square.rank - 1;
								endFile = this.square.file - 1;
								endSquare = Board.squares[endRank][endFile];
								newMove = new Move(this.square, endSquare, this);
								
								// Store the pawn that was captured en Passant, plus the square it was on
								newMove.capturedEnPassant = temp;
								newMove.enPassantLocation = temp.square;
								possibleMoves.add(newMove);
							}
						}
					}
				}
			}
			// Capture diagonally right (towards H-file)
			if(Board.trySquare(this.square.rank - 1, startFile + 1) != null){
				if(Board.squares[this.square.rank - 1][startFile + 1].hasPiece()){	// There is a piece here
					if(!Board.squares[this.square.rank - 1][startFile + 1].piece.whitePiece){	// The piece is black
						endRank = this.square.rank - 1;
						endFile = this.square.file + 1;
						endSquare = Board.squares[endRank][endFile];
						if(startRank == 1){	// Promotion
							addAllPromotions(possibleMoves, this, endSquare);
						} else {
							newMove = new Move(this.square, endSquare, this);
							possibleMoves.add(newMove);
						}
					}
				} else if(Board.trySquare(this.square.rank, startFile + 1) != null){
					if(Board.squares[this.square.rank][startFile + 1].hasPiece()){	// There is a piece next to the pawn
						if(Board.squares[this.square.rank][startFile + 1].piece instanceof Pawn){	// It's a pawn
							Pawn temp = (Pawn)Board.squares[this.square.rank][startFile + 1].piece;
							if(temp.justMoved && !temp.whitePiece){	// It can be captured en Passant
								endRank = this.square.rank - 1;
								endFile = this.square.file + 1;
								endSquare = Board.squares[endRank][endFile];
								newMove = new Move(this.square, endSquare, this);
								
								// Store the pawn that was captured en Passant, plus the square it was on
								newMove.capturedEnPassant = temp;
								newMove.enPassantLocation = temp.square;
								possibleMoves.add(newMove);
							}
						}
					}
				}
			}
			
		} else {	// Black piece
			// Capture diagonally right (towards A-file)
			if(Board.trySquare(this.square.rank + 1, startFile - 1) != null){
				if(Board.squares[this.square.rank + 1][startFile - 1].hasPiece()){	// There is a piece here
					if(Board.squares[this.square.rank + 1][startFile - 1].piece.whitePiece){	// The piece is white
						endRank = this.square.rank + 1;
						endFile = this.square.file - 1;
						endSquare = Board.squares[endRank][endFile];
						if(startRank == 6){	// Promotion
							addAllPromotions(possibleMoves, this, endSquare);
						} else {
							newMove = new Move(this.square, endSquare, this);
							possibleMoves.add(newMove);
						}
					}
				} else if(Board.trySquare(this.square.rank, startFile - 1) != null){
					if(Board.squares[this.square.rank][startFile - 1].hasPiece()){	// There is a piece next to the pawn
						if(Board.squares[this.square.rank][startFile - 1].piece instanceof Pawn){	// It's a pawn
							Pawn temp = (Pawn)Board.squares[this.square.rank][startFile - 1].piece;
							if(temp.justMoved && temp.whitePiece){	// It can be captured en Passant
								endRank = this.square.rank + 1;
								endFile = this.square.file - 1;
								endSquare = Board.squares[endRank][endFile];
								newMove = new Move(this.square, endSquare, this);
								
								// Store the pawn that was captured en Passant, plus the square it was on
								newMove.capturedEnPassant = temp;
								newMove.enPassantLocation = temp.square;
								possibleMoves.add(newMove);
							}
						}
					}
				}
			}
			// Capture diagonally left (towards H-file)
			if(Board.trySquare(this.square.rank + 1, startFile + 1) != null){	
				if(Board.squares[this.square.rank + 1][startFile + 1].hasPiece()){	// There is a piece here
					if(Board.squares[this.square.rank + 1][startFile + 1].piece.whitePiece){	// The piece is white
						endRank = this.square.rank + 1;
						endFile = this.square.file + 1;
						endSquare = Board.squares[endRank][endFile];
						if(startRank == 6){	// Promotion
							addAllPromotions(possibleMoves, this, endSquare);
						} else {
							newMove = new Move(this.square, endSquare, this);
							possibleMoves.add(newMove);
						}
					}
				} else if(Board.trySquare(this.square.rank, startFile + 1) != null){
					if(Board.squares[this.square.rank][startFile + 1].hasPiece()){	// There is a piece next to the pawn
						if(Board.squares[this.square.rank][startFile + 1].piece instanceof Pawn){	// It's a pawn
							Pawn temp = (Pawn)Board.squares[this.square.rank][startFile + 1].piece;
							if(temp.justMoved && temp.whitePiece){	// It can be captured en Passant
								endRank = this.square.rank + 1;
								endFile = this.square.file + 1;
								endSquare = Board.squares[endRank][endFile];
								newMove = new Move(this.square, endSquare, this);
								
								// Store the pawn that was captured en Passant, plus the square it was on
								newMove.capturedEnPassant = temp;
								newMove.enPassantLocation = temp.square;
								possibleMoves.add(newMove);
							}
						}
					}
				}
			}
		}

		// All possible moves have been added
		return possibleMoves;
	}
	
	public void addAllPromotions(ArrayList<Move> possibleMoves, Piece piece, Square endSquare){
		ArrayList<Move> promotions = new ArrayList<Move>();
		
		promotions.add(new Move(piece.square, endSquare, piece, "Q"));
		promotions.add(new Move(piece.square, endSquare, piece, "R"));
		promotions.add(new Move(piece.square, endSquare, piece, "B"));
		promotions.add(new Move(piece.square, endSquare, piece, "N"));
		
		if(possibleMoves == null)
			possibleMoves = promotions;
		else
			possibleMoves.addAll(promotions);
	}

}
