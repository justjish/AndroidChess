package com.example.androidchess.model;



/**
 * The Class Square.
 * @author Colin Drucquer, Sujish Patel
 */
public class Square {	
	/** True if White square, false if Black square */
	public boolean whiteSquare;
	
	/** The rank. */
	public int rank;
	
	/** The file. */
	public int file;
	
	/** The piece on this square. */
	public Piece piece;
	
	/**
	 * Instantiates a new square.
	 *
	 * @param whiteSquare square color boolean
	 */
	public Square (boolean whiteSquare, int file, int rank){
		this.whiteSquare = whiteSquare;
		this.file = file;
		this.rank = rank;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		if(this.hasPiece()){
			return this.piece.toString();
		} else if (this.whiteSquare){
			return "  ";
		}
		return "##";
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Square)){
			return false;
		}
		
		Square other = (Square)o;
		
		// Two squares are equal if they have the same rank and file
		if(other.file == this.file && other.rank == this.rank){
			return true;
		}
		
		return false;
	}

	/**
	 * Checks for piece.
	 *
	 * @return true, if successful
	 */
	public boolean hasPiece(){
		if(this.piece == null)
			return false;
		return true;
	}

}
