package com.example.androidchess.model;

import java.util.Scanner;

/**
 * The Class Chess.
 * 
 * @author Colin Drucquer, Sujish Patel
 */
public class Chess {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args){
		boolean whoseMove = true;	// True if white's move, false if black's move
		String input;	// User input from command line
		Board board = new Board();	// The game board
		Move move = null;	// The current move
		
		board.printBoard();	// Print board on launch

		Scanner sc = new Scanner(System.in);
		while(!board.gameOver()){	// Main game loop
			if(board.stalemate(whoseMove)){	// This player is in stalemate: draw, quit
				System.out.println("Stalemate");
				System.out.println("Draw");
				return;
			}
			
			if(board.checkmate(whoseMove)){	// This player is in checkmate: print result, quit
				System.out.println("Checkmate");
				
				if(whoseMove){	// White delivered checkmate
					System.out.println("White wins");
				} else {	// Black delivered checkmate
					System.out.println("Black wins");					
				}
				return;
			}
			
			// Print whose move it is
			if(whoseMove){
				System.out.print("White's move: ");
			} else {
				System.out.print("Black's move: ");				
			}
			
			// Prompt user for input
			input = sc.nextLine();
			
			// Check for resignation
			if(input.equals("resign")){
				if(whoseMove){	// White resigns
					System.out.println("Black wins");
				} else {	// Black resigns
					System.out.println("White wins");					
				}
				return;
			}
			
			// Update current move based on user input
			move = Move.parseMove(input, whoseMove);
			
			if(move == null){	// Move could not be parsed, restart loop
				System.out.println("Illegal move, try again");
				continue;
			}
			
			if(!move.isLegal(board)){	// Illegal move, restart loop
				System.out.println("Illegal move, try again");
				continue;
			}
			
			if(move.drawAccepted){	// Move was a successful acceptance of a draw offer (isLegal() checks this)
				System.out.println("Draw");
				return;
			}
			
			board.updateBoard(move);	// Move is legal: make the move
			board.cleanUpOldPieces();	// Get rid of pieces that no longer reside on squares
			board.setEnPassantToFalse(move.piece);	// Update en Passant tags for all pawns
			
			// Print a blank line followed by resulting board
			System.out.println();
			board.printBoard();
			
			if(board.checkmate(!whoseMove)){	// Opposing player is in checkmate: print result, quit
				System.out.println("Checkmate");
				
				if(whoseMove){	// White delivered checkmate
					System.out.println("White wins");
				} else {	// Black delivered checkmate
					System.out.println("Black wins");					
				}
				return;
			}
			
			if(board.check()){	// Print check before asking for next player's move
				System.out.println("Check");
			}
			
			whoseMove = nextPlayerTurn(whoseMove);	// Switch whose turn it is
		}
		sc.close();
		
	}

	/**
	 * Switch whose turn it is
	 *
	 * @param boolean whiteToMove
	 * @return true if it is now White's move, false if it's now Black's move
	 */
	public static boolean nextPlayerTurn(boolean whiteToMove){
		if(whiteToMove){	// Change to false (Black's move)
			return false;
		}
		return true;	// Change to true (White's move)
	}
	
}
