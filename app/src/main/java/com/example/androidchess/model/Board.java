package com.example.androidchess.model;

import java.util.ArrayList;
import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class Board.
 * @author Colin Drucquer, Sujish Patel
 */
public class Board {
	/* XXX Convention, to avoid confusion
	 * When iterating through the board, always go from:
	 * top left square to bottom right square
	 * A8, B8... to ...G1, H1
	 * squares[0][0], [0][1]... to ...[7][6], squares[7][7]
	 * 
	 * Moves are entered as [file][rank] instead of [rank][file]
	 * So E2-E4 becomes [_2][E] to [_4][E]
	 */
	
	// Color names
	private static final boolean white = true;
	private static final boolean black = false;
	
	// File names
	private static final int A = 0;
	private static final int B = 1;
	private static final int C = 2;
	private static final int D = 3;
	private static final int E = 4;
	private static final int F = 5;
	private static final int G = 6;
	private static final int H = 7;
	
	// Rank names
	private static final int _1 = 7;
	private static final int _2 = 6;
	@SuppressWarnings("unused")
	private static final int _3 = 5;
	@SuppressWarnings("unused")
	private static final int _4 = 4;
	@SuppressWarnings("unused")
	private static final int _5 = 3;
	@SuppressWarnings("unused")
	private static final int _6 = 2;
	private static final int _7 = 1;
	private static final int _8 = 0;
	
	public static Square[][] squares = new Square[8][8];
	public static ArrayList<Piece> whitePieces = new ArrayList<Piece>();
	public static ArrayList<Piece> blackPieces = new ArrayList<Piece>();
	
	public static boolean drawOffer = false;
	public static boolean stalemate = false;
	public static boolean check = false;
	public static boolean checkmate = false;
	public static boolean resigns = false;
	public static boolean draw = false;
	
	/**
	 * Instantiates a new board.
	 */
	public Board(){		
		// Initialize squares with colors
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if((i + j) % 2 == 0){	// White square
					squares[i][j] = new Square(white, j, i);
				} else {	// Black square
					squares[i][j] = new Square(black, j, i);
				}
			}
		}
		
		// Add white pieces to list
		whitePieces.add(new King(white, squares[_1][E]));
		whitePieces.add(new Queen(white, squares[_1][D]));
		whitePieces.add(new Rook(white, squares[_1][H]));
		whitePieces.add(new Rook(white, squares[_1][A]));
		whitePieces.add(new Bishop(white, squares[_1][F]));
		whitePieces.add(new Bishop(white, squares[_1][C]));
		whitePieces.add(new Knight(white, squares[_1][G]));
		whitePieces.add(new Knight(white, squares[_1][B]));
		for(int i = 0; i < 8; i++){
			whitePieces.add(new Pawn(white, squares[_2][i]));
		}
		
		// Add black pieces to list
		blackPieces.add(new King(black, squares[_8][E]));
		blackPieces.add(new Queen(black, squares[_8][D]));
		blackPieces.add(new Rook(black, squares[_8][H]));
		blackPieces.add(new Rook(black, squares[_8][A]));
		blackPieces.add(new Bishop(black, squares[_8][F]));
		blackPieces.add(new Bishop(black, squares[_8][C]));
		blackPieces.add(new Knight(black, squares[_8][G]));
		blackPieces.add(new Knight(black, squares[_8][B]));
		for(int i = 0; i < 8; i++){
			blackPieces.add(new Pawn(black, squares[_7][i]));
		}

		// Move white pieces to their initial squares
		for(Piece piece : whitePieces){
			piece.square.piece = piece;
		}
		
		// Move black pieces to their initial squares
		for(Piece piece : blackPieces){
			piece.square.piece = piece;
		}
		
	}
	
	/**
	 * Game over.
	 *
	 * @return true, if game is drawn or one person has won.
	 */
	public boolean gameOver(){
		if(draw || stalemate || checkmate || resigns)
			return true;
		return false;
	}
	
	/**
	 * Prints the board.
	 * Must have a blank line above and below board, as per instructions.
	 */
	public void printBoard(){
		int i, j;
		for(i = 0; i < 8; i++){
			for(j = 0; j < 8; j++){
				System.out.print(squares[i][j] + " ");
			}
			System.out.println(8-i);
		}
		System.out.println(" a  b  c  d  e  f  g  h \n");
	}
	
	/**
	 * Updates the board to reflect the given move.
	 * Also updates any boolean tags set by this move.
	 *
	 * @param move the move
	 */
	public Piece updateBoard(Move move){
		Piece captured = null;
		
		/* Update the Board's boolean tags from this move
		 * Resignation and accepting draws are handled in Main, don't need to update them
		 */
		// Draw offer
		if(move.drawOffer)
			drawOffer = true;
		else
			drawOffer = false;
		
		// Is enemy King in check after this move?
		if(kingAttackedAfterMove(!move.piece.whitePiece, move))
			check = true;
		else
			check = false;
		
		if(move.endSquare.hasPiece()){
			/* Capture this piece by removing it from
			 * ArrayList of pieces, then from the square
			 */
			captured = move.endSquare.piece;
			
			if(captured.whitePiece){
				whitePieces.remove(captured);
			} else {
				blackPieces.remove(captured);
			}
			
			move.endSquare.piece = null;
			captured.square = null;
		}
		
		
		/*
		 * En Passant
		 */
		if(move.capturedEnPassant != null){
			captured = move.capturedEnPassant;
			
			if(captured.whitePiece){
				tryToRemove(whitePieces, move.capturedEnPassant);
			} else {
				tryToRemove(blackPieces, move.capturedEnPassant);
			}
			
			clearSquare(move.enPassantLocation);
			move.capturedEnPassant.square = null;
		}
		
		if(move.twoSquarePawnMove && move.piece instanceof Pawn){
			// This move opens up this pawn for an en Passant capture for 1 move
			Pawn temp = (Pawn)move.piece;
			temp.hasMoved = true;
			temp.justMoved = true;
		}
		
		/*
		 * Pawn Promotion
		 * Note: isPawnPromotion will set move.promotion to "Q" if it is null
		 */
		if(move.promotion != null || isPawnPromotion(move)){
			Boolean color = move.piece.whitePiece;
			Piece promotedPiece = null;
			
			String promotion = move.promotion;
			switch(promotion){
			case "Q":
				promotedPiece = new Queen(color, move.endSquare);
				if(color){
					whitePieces.add(promotedPiece);
				} else {
					blackPieces.add(promotedPiece);
				}
				break;
			case "R":
				promotedPiece = new Rook(color, move.endSquare);
				if(color){
					whitePieces.add(promotedPiece);
				} else {
					blackPieces.add(promotedPiece);
				}
				break;
			case "B":
				promotedPiece = new Bishop(color, move.endSquare);
				if(color){
					whitePieces.add(promotedPiece);
				} else {
					blackPieces.add(promotedPiece);
				}
				break;
			case "N":
				promotedPiece = new Knight(color, move.endSquare);
				if(color){
					whitePieces.add(promotedPiece);
				} else {
					blackPieces.add(promotedPiece);
				}
				break;
			default:	// Default to Queen promotion
				promotedPiece = new Queen(color, move.endSquare);
				if(color){
					whitePieces.add(promotedPiece);
				} else {
					blackPieces.add(promotedPiece);
				}
				break;
			}
			
			// Put promoted piece on the square
			move.endSquare.piece = promotedPiece;
			
			// Remove the pawn that was promoted
			if(move.piece.whitePiece){
				whitePieces.remove(move.piece);
			} else {
				blackPieces.remove(move.piece);
			}
			clearSquare(move.startSquare);
			move.piece.square = null;
			
			
			// Don't run code below
			return captured;
		}
		
		// Move the piece from startSquare to endSquare
		move.endSquare.piece = move.piece;
		move.startSquare.piece = null;
		
		// Update the piece's coordinates
		move.piece.square = move.endSquare;
		
		// Set hasMoved to true for this piece, increment its number of moves
		move.piece.hasMoved = true;
		move.piece.numMoves++;
		
		// If this move was castling, move the appropriate rook as well
		if(move.kingSideCastling){
			if(move.piece.whitePiece){	// White 0-0
				squares[_1][H].piece.hasMoved = true;
				squares[_1][H].piece.numMoves++;
				squares[_1][H].piece.square = squares[_1][F];
				squares[_1][F].piece = squares[_1][H].piece;
				squares[_1][H].piece = null;
			} else {	// Black 0-0
				squares[_8][H].piece.hasMoved = true;
				squares[_8][H].piece.numMoves++;
				squares[_8][H].piece.square = squares[_8][F];
				squares[_8][F].piece = squares[_8][H].piece;
				squares[_8][H].piece = null;
			}
		} else if(move.queenSideCastling){
			if(move.piece.whitePiece){	// White 0-0-0
				squares[_1][A].piece.hasMoved = true;
				squares[_1][A].piece.numMoves++;
				squares[_1][A].piece.square = squares[_1][D];
				squares[_1][D].piece = squares[_1][A].piece;
				squares[_1][A].piece = null;
			} else {	// Black 0-0-0
				squares[_8][A].piece.hasMoved = true;
				squares[_8][A].piece.numMoves++;
				squares[_8][A].piece.square = squares[_8][D];
				squares[_8][D].piece = squares[_8][A].piece;
				squares[_8][A].piece = null;
			}
		}
		
		// Return captured piece, if any
		return captured;
	}
	
	/**
	 * Updates the board to reflect the given move.
	 * Does not set any tags (ie: draw offer, checkmate) that would then need
	 * to be reverted.
	 *
	 * @param move the move
	 */
	public Piece tryMove(Move move){
		Piece captured = null;
		if(move.endSquare.hasPiece()){
			/* Capture this piece by removing it from
			 * ArrayList of pieces, then from the square
			 */
			captured = move.endSquare.piece;
			
			if(captured.whitePiece){
				whitePieces.remove(captured);
			} else {
				blackPieces.remove(captured);
			}
			
			move.endSquare.piece = null;
			captured.square = null;
		}
		
		/*
		 * En Passant
		 */
		if(move.capturedEnPassant != null){
			captured = move.capturedEnPassant;
			
			if(captured.whitePiece){
				tryToRemove(whitePieces, move.capturedEnPassant);
			} else {
				tryToRemove(blackPieces, move.capturedEnPassant);
			}
			
			clearSquare(move.enPassantLocation);
			move.capturedEnPassant.square = null;
		}
		
/* XXX old en passant code
		if(move.capturedEnPassant != null){
			captured = move.capturedEnPassant;
			
			if(captured.whitePiece){
				tryToRemove(whitePieces, captured);
			} else {
				tryToRemove(blackPieces, captured);
			}
			
			clearSquare(captured.square);
			captured.square = null;
			
		}
*/
		
		if(move.twoSquarePawnMove && move.piece instanceof Pawn){
			// This move opens up this pawn for an en Passant capture for 1 move
			Pawn temp = (Pawn)move.piece;
			temp.hasMoved = true;
			temp.justMoved = true;
		}
				
		/*
		 * Pawn Promotion
		 * Note: isPawnPromotion will set move.promotion to "Q" if it is null
		 */
		if(move.promotion != null || isPawnPromotion(move)){
			Boolean color = move.piece.whitePiece;
			Piece promotedPiece = null;
			
			String promotion = move.promotion;
			switch(promotion){
			case "Q":
				promotedPiece = new Queen(color, move.endSquare);
				if(color){
					whitePieces.add(promotedPiece);
				} else {
					blackPieces.add(promotedPiece);
				}
				break;
			case "R":
				promotedPiece = new Rook(color, move.endSquare);
				if(color){
					whitePieces.add(promotedPiece);
				} else {
					blackPieces.add(promotedPiece);
				}
				break;
			case "B":
				promotedPiece = new Bishop(color, move.endSquare);
				if(color){
					whitePieces.add(promotedPiece);
				} else {
					blackPieces.add(promotedPiece);
				}
				break;
			case "N":
				promotedPiece = new Knight(color, move.endSquare);
				if(color){
					whitePieces.add(promotedPiece);
				} else {
					blackPieces.add(promotedPiece);
				}
				break;
			default:	// Default to Queen promotion
				promotedPiece = new Queen(color, move.endSquare);
				if(color){
					whitePieces.add(promotedPiece);
				} else {
					blackPieces.add(promotedPiece);
				}
				break;
			}
			
			// Put promoted piece on the square
			move.endSquare.piece = promotedPiece;
			
			// Remove the pawn that was promoted
			if(move.piece.whitePiece){
				whitePieces.remove(move.piece);
			} else {
				blackPieces.remove(move.piece);
			}
			clearSquare(move.startSquare);
			move.piece.square = null;
			
			// Don't run code below
			return captured;
		}
		
		
		// Move the piece from startSquare to endSquare
		move.endSquare.piece = move.piece;
		move.startSquare.piece = null;
		
		// Update the piece's coordinates
		move.piece.square = move.endSquare;
		
		// Set hasMoved to true for this piece, increment its number of moves
		move.piece.hasMoved = true;
		move.piece.numMoves++;
		
		/*
		 * Castling
		 * If this move was castling, move the appropriate rook as well
		 */
		if(move.kingSideCastling){
			if(move.piece.whitePiece){	// White 0-0
				squares[_1][H].piece.hasMoved = true;
				squares[_1][H].piece.numMoves++;
				squares[_1][H].piece.square = squares[_1][F];
				squares[_1][F].piece = squares[_1][H].piece;
				squares[_1][H].piece = null;
			} else {	// Black 0-0
				squares[_8][H].piece.hasMoved = true;
				squares[_8][H].piece.numMoves++;
				squares[_8][H].piece.square = squares[_8][F];
				squares[_8][F].piece = squares[_8][H].piece;
				squares[_8][H].piece = null;
			}
		} else if(move.queenSideCastling){
			if(move.piece.whitePiece){	// White 0-0-0
				squares[_1][A].piece.hasMoved = true;
				squares[_1][A].piece.numMoves++;
				squares[_1][A].piece.square = squares[_1][D];
				squares[_1][D].piece = squares[_1][A].piece;
				squares[_1][A].piece = null;
			} else {	// Black 0-0-0
				squares[_8][A].piece.hasMoved = true;
				squares[_8][A].piece.numMoves++;
				squares[_8][A].piece.square = squares[_8][D];
				squares[_8][D].piece = squares[_8][A].piece;
				squares[_8][A].piece = null;
			}
		}
		
		// Return captured piece, if any
		return captured;
	}
	
	
	/**
	 * Reverts board to the way it was before the last move.
	 *
	 * @param move The move to undo
	 * @param piece The piece captured by the last move (if any)
	 */
	public void revertBoard(Move move, Piece captured){
		Piece moved = move.piece;
		Square startSquare = move.startSquare;
		Square endSquare = move.endSquare;
		
		/*
		 * Undo pawn Promotion
		 * Note: isPawnPromotion will set move.promotion to "Q" if it is null
		 */
		if(move.promotion != null || isPawnPromotion(move)){
			Boolean color = move.piece.whitePiece;
			
			// Add back the old pawn, give it some moves
			Pawn oldPawn = new Pawn(color, startSquare);
			startSquare.piece = oldPawn;
			oldPawn.hasMoved = true;
			oldPawn.numMoves = 3;
			
			/*
			 * Remove the promoted piece.
			 * Add back the pawn that was promoted.
			 */
			Piece promotedPiece = endSquare.piece;	// This should never be null if this was a legit promotion
			endSquare.piece = captured;	// This will be null for a promotion on an empty square, or the captured piece
			if(color){
				whitePieces.remove(promotedPiece);
				whitePieces.add(oldPawn);
			} else {
				blackPieces.remove(promotedPiece);
				blackPieces.add(oldPawn);
			}
			
			// If there was a captured piece, put it back on its square and add it back to the arrayList of pieces
			if(captured != null){
				if(captured.whitePiece){
					whitePieces.add(captured);
					captured.square = endSquare;
					endSquare.piece = captured;
				} else {
					blackPieces.add(captured);
					captured.square = endSquare;
					endSquare.piece = captured;
				}
			}
			
			// Don't run code below
			return;
		}
		
		
		moved.numMoves--;
		if(moved.numMoves == 0)			// If piece moved for the first time, set it to unmoved
			moved.hasMoved = false;
		
		// Move piece back to its starting square and update its coordinates
		if(move.capturedEnPassant == null){
			startSquare.piece = moved;
			endSquare.piece = captured;	// (This will set endSquare to null if there was no captured piece)
			moved.square = startSquare;
		} else {	// Move was an en Passant capture
			startSquare.piece = moved;
			moved.square = startSquare;

			// Link the captured pawn back up with its square
			move.enPassantLocation.piece = move.capturedEnPassant;
			move.capturedEnPassant.square = move.enPassantLocation;
		}
		
		if(move.twoSquarePawnMove && move.piece instanceof Pawn){
			// This pawn should no longer be open to en Passant capture
			Pawn temp = (Pawn)move.piece;
			temp.hasMoved = false;
		}
		
		// If there was a captured piece, put it back on its square and add it back to the arrayList of pieces
		if(captured != null){
			if(captured.whitePiece){
				whitePieces.add(captured);
				captured.square = endSquare;
				endSquare.piece = captured;
			} else {
				blackPieces.add(captured);
				captured.square = endSquare;
				endSquare.piece = captured;
			}
		}
		
		/*
		 * Castling
		 * If this move was castling, move the appropriate rook as well
		 */
		if(move.kingSideCastling){
			if(move.piece.whitePiece){
				squares[_1][F].piece.hasMoved = false;
				squares[_1][F].piece.numMoves--;
				squares[_1][F].piece.square = squares[_1][H];
				squares[_1][H].piece = squares[_1][F].piece;
				squares[_1][F].piece = null;
			} else {
				squares[_8][F].piece.hasMoved = false;
				squares[_8][F].piece.numMoves--;
				squares[_8][F].piece.square = squares[_8][H];
				squares[_8][H].piece = squares[_8][F].piece;
				squares[_8][F].piece = null;
			}
		} else if(move.queenSideCastling){
			if(move.piece.whitePiece){
				squares[_1][D].piece.hasMoved = false;
				squares[_1][D].piece.numMoves--;
				squares[_1][D].piece.square = squares[_1][A];
				squares[_1][A].piece = squares[_1][D].piece;
				squares[_1][D].piece = null;
			} else {
				squares[_8][D].piece.hasMoved = false;
				squares[_8][D].piece.numMoves--;
				squares[_8][D].piece.square = squares[_8][A];
				squares[_8][A].piece = squares[_8][D].piece;
				squares[_8][D].piece = null;
			}
		}
		
		
	}
	
	/**
	 * Checks for stalemate.
	 *
	 * @param whiteToMove true if it's white's move, false if it's black's
	 * @return true, if the player of the input color is in stalemate
	 */
	public boolean stalemate(boolean whiteToMove){
		if(check) return false;	// There cannot be stalemate if there is check
		
		ArrayList<Move> allMovesByColor = new ArrayList<Move>();
		for(Piece piece : whiteToMove ? whitePieces : blackPieces){	// Iterate over all pieces of this player's color
			if(piece.possibleMoves() != null){	// Add this piece's possible moves
				if(allMovesByColor == null)
					allMovesByColor = piece.possibleMoves();
				else
					allMovesByColor.addAll(piece.possibleMoves());
			}
		}	
		
		for(Move move : allMovesByColor){
			if(move.isLegal(this))
				// This is a legal move for this player
				return false;
		}
		
		// There are no legal moves for this player, so they are in stalemate
		return true;
	}
	
	/**
	 * Checks for checkmate.
	 *
	 * @param whiteToMove true if it's white's move, false if it's black's
	 * @return true, if the player of the input color is in checkmate
	 */
	public boolean checkmate(boolean whiteToMove){
		if(!check) return false;	// There must be check for there to be checkmate
		
		ArrayList<Move> allMovesByColor = new ArrayList<Move>();
		for(Piece piece : whiteToMove ? whitePieces : blackPieces){	// Iterate over all pieces of this player's color
			if(piece.possibleMoves() != null){	// Add this piece's possible moves
				if(allMovesByColor == null)
					allMovesByColor = piece.possibleMoves();
				else
					allMovesByColor.addAll(piece.possibleMoves());
			}
		}	
		
		for(Move move : allMovesByColor){
			if(!kingAttackedAfterMove(whiteToMove, move)){
				// This is a move for this player which gets out of check
				if(move.kingSideCastling || move.queenSideCastling){	// Caveat: cannot castle out of check
					continue;
				}
				return false;
			}
		}
		
		// There are no moves for this player which don't leave their king under attack
		return true;
	}
	
	/**
	 * Check if square is attacked by any pieces of the given color.
	 * This is a static method that's performed on the main game board.
	 *
	 * @param square the square
	 * @param color the color of the potential attacking piece
	 * @return true, if successful
	 */
	public static boolean checkIfSquareIsAttacked(Square square, boolean color){
		ArrayList<Move> allMovesByColor = new ArrayList<Move>();
		for(Piece piece : color ? whitePieces : blackPieces){	// Iterate over all pieces of the given color
			if(piece.possibleMoves() != null){	// Add this piece's possible moves
				if(allMovesByColor == null)
					allMovesByColor = piece.possibleMoves();
				else
					allMovesByColor.addAll(piece.possibleMoves());
			}
		}	
		
		for(Move move : allMovesByColor){
			if(move.endSquare.equals(square))
				// This move attacks this square
				return true;
		}
		
		return false;
		
		
/* XXX Old code for this method which attempts to exclude castling
		for(Piece piece : color ? whitePieces : blackPieces){	// Iterate over all pieces of the given color
	ArrayList<Move> possibleMoves = piece.possibleMoves();
	
	if(piece instanceof King && possibleMoves != null){
		// Before adding these moves, remove any castling moves
		for(Move move : possibleMoves){
			if(move.kingSideCastling || move.queenSideCastling)
				possibleMoves.remove(move);
		}
	}
		
	if(possibleMoves != null){	// Add this piece's possible moves
		if(allMovesByColor == null)
			allMovesByColor = possibleMoves;
		else 
			allMovesByColor.addAll(piece.possibleMoves());
	}
}
*/
		
	}
	
	/**
	 * Check if square is attacked by any pieces of the given color.
	 * This is a static method that's performed on the main game board.
	 * It is used by the King class to determine if the King can castle
	 * over this square.
	 * 
	 * @param square the square
	 * @param color the color of the potential attacking piece
	 * @return true, if successful
	 */
	public static boolean checkIfSquareIsAttackedForCastling(Square square, boolean color){
		ArrayList<Move> allMovesByColor = new ArrayList<Move>();
		for(Piece piece : color ? whitePieces : blackPieces){	// Iterate over all pieces of the given color
			/* Crude attempt to exclude castling loop
			 * Allows some very minor fringe cases, should be ok
			 * Example: White can castle with enemy king on h2
			 */
			if(piece instanceof King)
				continue;
			
			if(piece.possibleMoves() != null){	// Add this piece's possible moves
				if(allMovesByColor == null)
					allMovesByColor = piece.possibleMoves();
				else
					allMovesByColor.addAll(piece.possibleMoves());
			}
		}
		
		for(Move move : allMovesByColor){
			if(move.endSquare.equals(square))
				// This move attacks this square
				return true;
		}
		
		return false;
	}

	/**
	 * Try square -- returns null if the square is out of bounds.
	 * Returns the square object from the game board if it's in bounds.
	 *
	 * @param rank the rank
	 * @param file the file
	 * @return the square
	 */
	public static Square trySquare(int rank, int file){
		if(rank >= 0 && rank < 8 && file >= 0 && file < 8){
			return squares[rank][file];
		}
		return null;
	}
	
	/**
	 * Checks if king is attacked after the given move is made on the board.
	 *
	 * @param whitePiece the piece color
	 * @param move the move
	 * @return true, if king is attacked
	 */
	public boolean kingAttackedAfterMove(boolean whitePiece, Move move){
		Piece captured = this.tryMove(move);
		Piece king = getKing(whitePiece);
		Square square = king.square;
		
		if(move.piece instanceof King)	// This move is a King move: update the square to check
			square = move.endSquare;
		
		if(checkIfSquareIsAttacked(square, !whitePiece)){
			revertBoard(move, captured);
			return true;
		}
		revertBoard(move, captured);
		return false;
	}
	
	/**
	 * Gets the king.
	 *
	 * @param whitePiece the color
	 * @return the king
	 */
	public static Piece getKing(boolean whitePiece){
		if(whitePiece){
			for(Piece piece : whitePieces){
				if(piece instanceof King){
					return piece;
				}
			}
		} else {
			for(Piece piece : blackPieces){
				if(piece instanceof King){
					return piece;
				}
			}
		}
		
		// Should never get here
		return null;
	}
	
	/**
	 * Check.
	 *
	 * @return true, if successful
	 */
	public boolean check(){
		return check;
	}
	
	public boolean isPawnPromotion(Move move){
		Piece piece = move.piece;
		if(piece instanceof Pawn){
			if(piece.whitePiece && move.endSquare.rank == 0){
				if(move.promotion == null){ move.promotion = "Q"; }
				return true;
			} else if(move.endSquare.rank == 7) {
				if(move.promotion == null){ move.promotion = "Q"; }
				return true;
			}
		}
		return false;
	}
	
	public void clearSquare(Square square){
		if(square != null){
			if(square.hasPiece()){
				square.piece = null;
			}
		}
	}
	
	/**
	 * Sets en passant to false for all pawns except this one, if it is a pawn.
	 *
	 * @param pawn the excluded pawn
	 */
	public void setEnPassantToFalse(Piece inputPiece){
		Pawn pawn = null;
		if(inputPiece instanceof Pawn)
			pawn = (Pawn)inputPiece;
		
		for(Piece piece : whitePieces){
			if(piece instanceof Pawn){
				Pawn thePawn = (Pawn)piece;
				if(!thePawn.equals(pawn))
					thePawn.justMoved = false;
			}
		}
		
		for(Piece piece : blackPieces){
			if(piece instanceof Pawn){
				Pawn thePawn = (Pawn)piece;
				if(!thePawn.equals(pawn))
					thePawn.justMoved = false;
			}
		}
		
	}
	
	public void tryToRemove(ArrayList<Piece> pieces, Piece piece){
		if(pieces.contains(piece)){
			pieces.remove(piece);
		}
	}
	
	/**
	 * Clean up old pieces that aren't on any squares
	 */
	public void cleanUpOldPieces(){
		ArrayList<Piece> toDeleteWhite = new ArrayList<Piece>();
		for(Piece piece : whitePieces){
			// Check if there are any squares that have this piece
			boolean old = true;
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					if(squares[i][j].hasPiece()){
						if(squares[i][j].piece.equals(piece)){
							old = false;
							break;
						}
					}
				}
			}
			if(old){
				toDeleteWhite.add(piece);
			}
		}
		
		if(toDeleteWhite != null){
			whitePieces.removeAll(toDeleteWhite);
		}
		
		ArrayList<Piece> toDeleteBlack = new ArrayList<Piece>();
		for(Piece piece : blackPieces){
			// Check if there are any squares that have this piece
			boolean old = true;
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					if(squares[i][j].hasPiece()){
						if(squares[i][j].piece.equals(piece)){
							old = false;
							break;
						}
					}
				}
			}
			if(old){
				toDeleteBlack.add(piece);
			}
		}
		
		if(toDeleteBlack != null){
			blackPieces.removeAll(toDeleteBlack);
		}
	}
	
	/* =======================
	 * NEW METHODS FOR ANDROID
	 * =======================
	 */
	
	/*
	 * Returns a random legal move for this color, in String form
	 */
	public Move randomMove(boolean whiteToMove){
		ArrayList<Move> allMovesByColor = new ArrayList<Move>();
		for(Piece piece : whiteToMove ? whitePieces : blackPieces){	// Iterate over all pieces of this player's color
			if(piece.possibleMoves() != null){	// Add this piece's possible moves
				if(allMovesByColor == null)
					allMovesByColor = piece.possibleMoves();
				else
					allMovesByColor.addAll(piece.possibleMoves());
			}
		}
		
		int numMoves = allMovesByColor.size()-1;
		int randomIndex = randInt(0, numMoves);
		Move randomMove = allMovesByColor.get(randomIndex);
		
		for(int i = 0; !randomMove.isLegal(this) && i<150; i++){
			// Try 150 times to get a legal random move
			randomIndex = randInt(0, numMoves);
			randomMove = allMovesByColor.get(randomIndex);
		}
		
		// Random move is legal
		return randomMove;
	}
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
}
