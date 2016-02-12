package com.example.androidchess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The Class Move.
 * @author Colin Drucquer, Sujish Patel
 */
public class Move {
	public Square startSquare;
	public Square endSquare;
	public Piece piece;
	public Piece capturedEnPassant;
	public Square enPassantLocation;
	public String promotion;
	public boolean twoSquarePawnMove;
	public boolean drawAccepted;
	public boolean drawOffer;
	public boolean kingSideCastling;
	public boolean queenSideCastling;

	public static List<String> legitFiles =
			Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
	public static List<String> legitRanks =
			Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
	
	/**
	 * Instantiates a new move.
	 *
	 * @param startSquare the start square
	 * @param endSquare the end square
	 * @param piece the piece
	 */
	public Move(Square startSquare, Square endSquare, Piece piece){
		this.startSquare = startSquare;
		this.endSquare = endSquare;
		this.piece = piece;
	}
	
	/**
	 * Instantiates a new move.
	 *
	 * @param startSquare the start square
	 * @param endSquare the end square
	 * @param piece the piece
	 * @param promotion the promotion
	 */
	public Move(Square startSquare, Square endSquare, Piece piece, String promotion){
		this.startSquare = startSquare;
		this.endSquare = endSquare;
		this.piece = piece;
		this.promotion = promotion;
	}
	
	/**
	 * Instantiates a new move.
	 *
	 * @param startSquare the start square
	 * @param endSquare the end square
	 * @param piece the piece
	 * @param drawOffer the draw offer
	 */
	public Move(Square startSquare, Square endSquare, Piece piece, boolean drawOffer){
		this.startSquare = startSquare;
		this.endSquare = endSquare;
		this.piece = piece;
		this.drawOffer = drawOffer;
	}
	
	/**
	 * Instantiates a new move.
	 *
	 * @param drawAccepted the draw accepted
	 */
	public Move(boolean drawAccepted){
		this.drawAccepted = drawAccepted;
	}
	
	/**
	 * Parses the move from command line input.
	 *
	 * @param input the input (something like "e2 e4" or "draws")
	 * @param whiteToMove whose move it is
	 * @return the move object
	 */
	public static Move parseMove(String input, boolean whiteToMove){
		if(input.equals("draws") || input.equals("draw")){
			if(Board.drawOffer)	// Draw accepted
				return new Move(true);
			else	// No draw was offered
				return null;
		}
		
		String start = "";	// Must be start square
		String end = "";	// Must be end square
		String additional = "";	// Third token can be draw offer or promotion
		
		Scanner sc = new Scanner(input);
		while(sc.hasNext()){
			if(start.equals("")){
				start = sc.next();
			} else if (end.equals("")){
				end = sc.next();
			} else if (additional.equals("")){
				additional = sc.next();
			} else {	// Too many tokens: improper input
				sc.close();
				return null;
			}
		}
		sc.close();

		// Get starting and ending squares, check for nullity before continuing
		Square startSquare = parseSquare(start);
		Square endSquare = parseSquare(end);
		if(startSquare == null || endSquare == null){	// The square inputs could not be parsed
			return null;
		}
		
		Piece piece = startSquare.piece;
		if(piece == null){	// This square has no piece on it
			return null;
		} else if (piece.whitePiece != whiteToMove){	// Trying to move opponent's piece
			return null;
		}
		
		boolean kingsideCastling = false;
		boolean queensideCastling = false;
		if(piece instanceof King && Math.abs(startSquare.file - endSquare.file) == 2){	// This is a castling attempt
			if(piece.whitePiece){
				if(endSquare.equals(Board.squares[7][6]))
					kingsideCastling = true;
				else if(endSquare.equals(Board.squares[7][2]))
					queensideCastling = true;
			} else {
				if(endSquare.equals(Board.squares[0][6]))
					kingsideCastling = true;
				else if(endSquare.equals(Board.squares[0][2]))
					queensideCastling = true;
			}
		}
		
		if(additional.equals("")){	// All checks for normal moves have been passed
			Move move = new Move(startSquare, endSquare, piece);
			move.setCastling(kingsideCastling, queensideCastling);
			move.setEnPassant();
			return move;
		}
		
		if(additional.equals("draw?")){	// Draw offer has been made. Should be handled elsewhere
			Move move = new Move(startSquare, endSquare, piece, true);
			move.setCastling(kingsideCastling, queensideCastling);
			move.setEnPassant();
			return move;
		}
		
		if(additional.equals("N") || additional.equals("Q") 
				|| additional.equals("B") || additional.equals("R")){	// Promotion
			return new Move(startSquare, endSquare, piece, additional);
		}
		
		// Improper input: additional token after move was incorrect
		return null;
	}
	
	/**
	 * Parses a square from command line input.
	 *
	 * @param input the input (should be something like "e1")
	 * @return the square from the board, or null
	 */
	public static Square parseSquare(String input){
		if(input.length() != 2){	// Must be in file/rank format
			return null;
		}
		
		String stringFile = input.substring(0, 1);
		String stringRank = input.substring(1);
		
		// Check that these are legitimate values
		if (!legitFiles.contains(stringFile)){
			return null;
		} else if (!legitRanks.contains(stringRank)){
			return null;
		}
		
		// Values are legit: convert inputs to integers
		int file = legitFiles.indexOf(stringFile);
		int rank = 8-(int)Integer.parseInt(stringRank);

		// Return this square from the board
		return Board.squares[rank][file];
	}
	
	/**
	 * Checks if this move is legal on the current board.
	 * To be legal, the move must be:
	 * 1. Possible for this piece
	 * 2. Does not leave the player's king in check
	 * 3. Not a draw offer in response to a draw offer
	 *
	 * @return true, if is legal
	 */
	public boolean isLegal(Board board){
		// Need to check for draw accepted first, since the move will have no other fields
		if(this.drawAccepted){
			if(!board.drawOffer)	// Accepting draw, but no draw offer was given
				return false;
			else	// This is a successful draw
				return true;
		}
		
		Square start = this.startSquare;
		Square end = this.endSquare;
		Piece piece = this.piece;
		ArrayList<Move> possibleMoves = piece.possibleMoves();
		
		// Check if this move is possible for this piece
		if(possibleMoves == null)	// This piece has no possible moves
			return false;
		
		boolean possible = false;
		for(Move move : possibleMoves){
			if(move.startSquare == start && move.endSquare == end){	// This move is possible
				possible = true;
				break;
			}
		}

		// Check that this move doesn't leave this player's King in check
		if(!possible || board.kingAttackedAfterMove(piece.whitePiece, this))
			return false;
		
		// Check that this move isn't a draw offer in response to a draw offer (must be draw ACCEPTED)
		if(this.drawOffer && board.drawOffer)
			return false;
		
		// All tests for legality have been passed
		return true;
	}
	
	/**
	 * Sets the castling.
	 *
	 * @param kingsideCastling the kingside castling
	 * @param queensideCastling the queenside castling
	 */
	public void setCastling(boolean kingsideCastling, boolean queensideCastling){
		this.kingSideCastling = kingsideCastling;
		this.queenSideCastling = queensideCastling;
	}
	
	/**
	 * Sets en Passant possibility for this move.
	 */
	public void setEnPassant(){
		if(!(this.piece instanceof Pawn)){ return; }
		
		Pawn temp = (Pawn)this.piece;
		
		if(!temp.hasMoved && Math.abs(this.endSquare.rank - this.startSquare.rank) == 2){
			// This is a two square pawn move which opens up the pawn to be captured en Passant
			this.twoSquarePawnMove = true;
			return;
		}
		
		
		if(endSquare.file != piece.square.file && !endSquare.hasPiece()){
			// This is an en Passant capture
			Square enPassantSquare = null;
			int file = piece.square.file;
			int rank = piece.square.rank;
			
			if(endSquare.file > piece.square.file){	// Pawn towards H-file was captured
				enPassantSquare = Board.trySquare(rank, file+1);
			} else {	// Pawn towards A-file was captured
				enPassantSquare = Board.trySquare(rank, file-1);					
			}
			
			this.capturedEnPassant = enPassantSquare.piece;
			this.enPassantLocation = enPassantSquare;
		}
		
	}
}
