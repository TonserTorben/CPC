package com.dtu.chp;

public class PuzzleSlot {

	private int line = -1;
	private int length = 0;
	private int startIndex = -1;	
	private boolean horizontal;
	
	protected PuzzleSlot(int line, int length, int startIndex, boolean horizontal){
		this.line = line;
		this.length = length;
		this.startIndex = startIndex;
		this.horizontal = horizontal;
	}

	public int getLine() {
		return line;
	}

	public int getLength() {
		return length;
	}
	
	public boolean getDirection() {
		return horizontal;
	}

	public int getStartInd() {
		return startIndex;
	}

}
