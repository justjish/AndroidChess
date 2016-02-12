package com.example.androidchess.model;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/* @author Colin Drucquer, Sujish Patel
 */

/**
 * The Class King.
 */
public class King extends Piece {
	
	/**
	 * Instantiates a new king.
	 *
	 * @param whitePiece the color
	 * @param square the square it's on
	 */
	public King (boolean whitePiece, Square square) {
		this.whitePiece = whitePiece;
		this.square = square;
	}

	/* (non-Javadoc)
	 * @see gameInfo.Piece#toString()
	 */
	public String toString() {
		if(this.whitePiece){
			return "wK";
		}
		return "bK";
	}

	/* (non-Javadoc)
	 * @see gameInfo.Piece#possibleMoves()
	 */
	public ArrayList<Move> possibleMoves() {
		// Get this piece's rank and file
		int startRank = this.square.rank;
		int startFile = this.square.file;
		Move newMove;
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		// First, see if this piece is against the side of the board
		boolean hFile = false;
		boolean aFile = false;
		boolean firstRank = false;
		boolean eighthRank = false;
		if(startFile == 0)
			aFile = true;
		if(startFile == 7)
			hFile = true;
		if(startRank == 0)
			eighthRank = true;
		if(startRank == 7)
			firstRank = true;
		
		/* Moving 1 square in any direction is a possible move
		 */
		if(!aFile){	// King can move toward A-file
			if(!this.checkForFriendlyPiece(Board.squares[startRank][startFile - 1])){	// No friendly piece on destination square
				newMove = this.createMoveFromSquare(Board.squares[startRank][startFile - 1]);
				possibleMoves.add(newMove);
			}
			if(!firstRank){
				if(!this.checkForFriendlyPiece(Board.squares[startRank + 1][startFile - 1])){
					newMove = this.createMoveFromSquare(Board.squares[startRank + 1][startFile - 1]);
					possibleMoves.add(newMove);
				}
			}
			if(!eighthRank){
				if(!this.checkForFriendlyPiece(Board.squares[startRank - 1][startFile - 1])){
					newMove = this.createMoveFromSquare(Board.squares[startRank - 1][startFile - 1]);
					possibleMoves.add(newMove);
				}
			}
		}
		if(!hFile){	// King can move toward H-file
			if(!this.checkForFriendlyPiece(Board.squares[startRank][startFile + 1])){	// No friendly piece on destination square
				newMove = this.createMoveFromSquare(Board.squares[startRank][startFile + 1]);
				possibleMoves.add(newMove);
			}
			if(!firstRank){
				if(!this.checkForFriendlyPiece(Board.squares[startRank + 1][startFile + 1])){
					newMove = this.createMoveFromSquare(Board.squares[startRank + 1][startFile + 1]);
					possibleMoves.add(newMove);
				}
			}
			if(!eighthRank){
				if(!this.checkForFriendlyPiece(Board.squares[startRank - 1][startFile + 1])){
					newMove = this.createMoveFromSquare(Board.squares[startRank - 1][startFile + 1]);
					possibleMoves.add(newMove);
				}
			}
		}
		if(!firstRank){	// King can move toward first rank
			if(!this.checkForFriendlyPiece(Board.squares[startRank + 1][startFile])){	// No friendly piece on destination square
				newMove = this.createMoveFromSquare(Board.squares[startRank + 1][startFile]);
				possibleMoves.add(newMove);
			}
			if(!hFile){
				if(!this.checkForFriendlyPiece(Board.squares[startRank + 1][startFile + 1])){
					newMove = this.createMoveFromSquare(Board.squares[startRank + 1][startFile + 1]);
					possibleMoves.add(newMove);
				}
			}
			if(!aFile){
				if(!this.checkForFriendlyPiece(Board.squares[startRank + 1][startFile - 1])){
					newMove = this.createMoveFromSquare(Board.squares[startRank + 1][startFile - 1]);
					possibleMoves.add(newMove);
				}
			}
		}
		if(!eighthRank){	// King can move toward last rank
			if(!this.checkForFriendlyPiece(Board.squares[startRank - 1][startFile])){	// No friendly piece on destination square
				newMove = this.createMoveFromSquare(Board.squares[startRank - 1][startFile]);
				possibleMoves.add(newMove);
			}
			if(!hFile){
				if(!this.checkForFriendlyPiece(Board.squares[startRank - 1][startFile + 1])){
					newMove = this.createMoveFromSquare(Board.squares[startRank - 1][startFile + 1]);
					possibleMoves.add(newMove);
				}
			}
			if(!aFile){
				if(!this.checkForFriendlyPiece(Board.squares[startRank - 1][startFile - 1])){
					newMove = this.createMoveFromSquare(Board.squares[startRank - 1][startFile - 1]);
					possibleMoves.add(newMove);
				}
			}
		}
		
		/* Castling is a possible move if neither the King nor Rook have moved
		 */
		if(!this.hasMoved){
			// Kingside castling
			if(!this.checkForPiece(Board.squares[startRank][startFile + 1])
					&& !this.checkForPiece(Board.squares[startRank][startFile + 2])
					&& !Board.checkIfSquareIsAttackedForCastling(Board.squares[startRank][startFile + 1], !this.whitePiece)
					&& !Board.checkIfSquareIsAttackedForCastling(Board.squares[startRank][startFile + 2], !this.whitePiece)){
				// No pieces in the way, and squares that King must travel over aren't attacked
				if(Board.squares[startRank][7].hasPiece()){
					if(Board.squares[startRank][7].piece.whitePiece == this.whitePiece
							&& Board.squares[startRank][7].piece instanceof Rook
							&& !Board.squares[startRank][7].piece.hasMoved){
						/* Same color, rook, hasn't moved. Elsewhere, need to check that both squares
						 * passed over by the king are not attacked by enemy pieces
						 */
						newMove = this.createMoveFromSquare(Board.squares[startRank][startFile + 2]);
						newMove.kingSideCastling = true;
						possibleMoves.add(newMove);
					}
				}
			}
			
			// Queenside castling
			if(!this.checkForPiece(Board.squares[startRank][startFile - 1])
					&& !this.checkForPiece(Board.squares[startRank][startFile - 2])
					&& !Board.checkIfSquareIsAttackedForCastling(Board.squares[startRank][startFile - 1], !this.whitePiece)
					&& !Board.checkIfSquareIsAttackedForCastling(Board.squares[startRank][startFile - 2], !this.whitePiece)){
				// No pieces in the way, and squares that King must travel over aren't attacked
				if(Board.squares[startRank][0].hasPiece()){
					if(Board.squares[startRank][0].piece.whitePiece == this.whitePiece
							&& Board.squares[startRank][0].piece instanceof Rook
							&& !Board.squares[startRank][0].piece.hasMoved){
						/* Same color, rook, hasn't moved. Elsewhere, need to check that both squares
						 * passed over by the king are not attacked by enemy pieces
						 */
						newMove = this.createMoveFromSquare(Board.squares[startRank][startFile - 2]);
						newMove.queenSideCastling = true;
						possibleMoves.add(newMove);
					}
				}
			}
			
			
		}

		// All possible moves have been added
		return possibleMoves;
	}

}
