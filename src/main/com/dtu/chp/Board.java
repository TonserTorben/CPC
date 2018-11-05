package com.dtu.chp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Board {

	private List<String> words;
	private List<PuzzleSlot> vertical;
	private List<PuzzleSlot> horizontal;
	private List<PuzzleSlot> all;
	private char[][] currEntries;
	private Decoder decoder;

	public Board() {
		this.decoder = Decoder.getInstance();
		this.words = this.decoder.getStrings();
		this.currEntries = initEntries();
		this.horizontal = new ArrayList<PuzzleSlot>();
		this.vertical = new ArrayList<PuzzleSlot>();
		determineSlots();
		collectAllSlots();
	
	}
	
	public Board(Board board) {
		this.decoder = Decoder.getInstance();
		this.words = this.decoder.getStrings();
		this.currEntries = new char[this.decoder.getEntries().length][this.decoder.getEntries().length];
		
		for(int i=0; i<this.currEntries.length;i++) {
			for(int j=0; j<this.currEntries.length;j++) {
				this.currEntries[i][j] = board.getCurrEntries()[i][j];
			}
		}
		
		this.horizontal = new ArrayList<PuzzleSlot>(board.getHorizontalSlots());
		this.vertical = new ArrayList<PuzzleSlot>(board.getVerticalSlots());
		this.all = new ArrayList<PuzzleSlot>(board.getAllSlots());
		
	}

	/*
	 * Used to combine all slots that need to be filled in one list
	 */
	private void collectAllSlots() {

		this.all = new ArrayList<PuzzleSlot>();
		for (PuzzleSlot p : horizontal) {
			this.all.add(p);
		}
		for (PuzzleSlot p : vertical) {
			this.all.add(p);
		}
		Collections.sort(this.all, new SlotComparator());

	}

	/*
	* Used to set a word in a slot if possible
	*
	* @param slot A free slot to be filled in the current board
	* @param word A word to be filled in the provided slot
	*
	* @returns boolean If a given word can be fitted in a given board slot
	* */
	public boolean fillIn(PuzzleSlot slot, String word) {
		
		List<Integer> changed = new ArrayList<Integer>();
		
		if (slot.getDirection()) {
		
			for (int i = 0; i < word.length(); i++) {
				char current = this.currEntries[slot.getLine()][slot
						.getStartInd() + i];
			
				if (current == '_') {
				
					changed.add(slot.getStartInd() + i);
					this.currEntries[slot.getLine()][slot.getStartInd() + i] = word
							.charAt(i);
					
					if (!checkWordAt(slot.getStartInd() + i, slot.getLine(),
							false)) {
					
						for (Integer j : changed) {
							this.currEntries[slot.getLine()][j] = '_';
						}
						
						return false;
					}
				} else if (current != word.charAt(i)) {
				
					for (Integer j : changed) {
						this.currEntries[slot.getLine()][j] = '_';
					}
					
					return false;
				}
			}
		} else {
			for (int i = 0; i < word.length(); i++) {
				
				char current = this.currEntries[slot.getStartInd() + i][slot
						.getLine()];
				
				if (current == '_') {
				
					changed.add(slot.getStartInd() + i);
					this.currEntries[slot.getStartInd() + i][slot.getLine()] = word
							.charAt(i);
					
					if (!checkWordAt(slot.getLine(), slot.getStartInd() + i,
							true)) {
					
						for (Integer j : changed) {
							this.currEntries[j][slot.getLine()] = '_';
						}
						
						return false;
					}
				} else if (current != word.charAt(i)) {
					
					for (Integer j : changed) {
						this.currEntries[j][slot.getLine()] = '_';
					}
			
					return false;
				}
			}
		}
		
		int currentIndex = all.indexOf(slot);
		all.remove(currentIndex);
		
		return true;
	}

	private boolean checkWordAt(int x, int y, boolean direction) {
		
		if (direction) {
		
			PuzzleSlot predecessor = null;
			
			for (PuzzleSlot slot : horizontal) {
			
				if (slot.getLine() == y && slot.getStartInd() <= x)
					if (predecessor == null)
						predecessor = slot;
					else if (predecessor.getStartInd() < slot.getStartInd())
						predecessor = slot;
			}

			// check if predecessor is a filled world
			for (int i = 0; i < predecessor.getLength(); i++) {
				
				if (this.currEntries[y][predecessor.getStartInd() + i] == '_')
					return true;
			
			}

			// check if created word exists

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < predecessor.getLength(); i++) {
				sb.append(this.currEntries[y][predecessor.getStartInd() + i]);
			}
			String createdWord = sb.toString();

			for (String word : this.words) {
				if (word.equals(createdWord))
					return true;
			}
		} else {
			
			PuzzleSlot predecessor = null;
			
			for (PuzzleSlot slot : vertical) {
			
				if (slot.getLine() == x && slot.getStartInd() <= y)
					if (predecessor == null)
						predecessor = slot;
					else if (predecessor.getStartInd() < slot.getStartInd())
						predecessor = slot;
			}

			// check if predecessor is a filled world
			for (int i = 0; i < predecessor.getLength(); i++) {
				
				if (this.currEntries[predecessor.getStartInd() + i][x] == '_')
					return true;
			}

			// check if created word exists

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < predecessor.getLength(); i++) {
				sb.append(this.currEntries[predecessor.getStartInd() + i][x]);
			}
			String createdWord = sb.toString();
			
			for (String word : this.words) {
				if (word.equals(createdWord))
					return true;
			}
		
		}

		return false;
	}
	// Determine slots for both horizontal and vertical puzzleslots.
	private void determineSlots(){
		int horizontalIndex = 0;
		int verticalIndex = 0;
		int length = currEntries.length;
		for (int i = 0; i < length; i++){
			int horizontalCount = 0;
			int verticalCount = 0;

			for (int j = 0; j < length; j++){
				if(currEntries[i][j] != '#')
					horizontalCount ++;
				else{
					if (horizontalCount != 0){
						if(horizontalIndex < length)
							horizontal.add(new PuzzleSlot(i, horizontalCount, horizontalIndex, true));
					}
					horizontalCount = 0;
					horizontalIndex = j + 1;
				}
				if(currEntries[j][i] != '#')
					verticalCount ++;
				else{
					if (verticalCount != 0){
						if (verticalIndex < length)
							vertical.add(new PuzzleSlot(i, verticalCount, verticalIndex, false));
					}
					verticalCount = 0;
					verticalIndex = j + 1;
				}
			}

			if (horizontalCount != 0)
				horizontal.add(new PuzzleSlot(i, horizontalCount, horizontalIndex, true));
			
			if (verticalCount != 0)
				vertical.add(new PuzzleSlot(i, verticalCount, verticalIndex, false));
			
			horizontalIndex = 0;
			verticalIndex = 0;

		}
	}

	private char[][] initEntries() {
		this.decoder = Decoder.getInstance();
		int puzzleSize = this.decoder.getPuzzleSize();
		char[][] result = new char[puzzleSize][puzzleSize];
		char[][] decodedEntries = this.decoder.getEntries();

		for (int i = 0; i < decodedEntries.length; i++) {
			for (int j = 0; j < decodedEntries.length; j++) {
				result[i][j] = decodedEntries[i][j];
			}
		}

		return result;
	}

	private List<PuzzleSlot> getVerticalSlots() {
		return vertical;
	}

	private List<PuzzleSlot> getHorizontalSlots() {
		return horizontal;
	}

	private char[][] getCurrEntries() {
		return currEntries;
	}

	public List<PuzzleSlot> getAllSlots() {
		return all;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for(int i=0; i<currEntries.length; i++) {
			for(int j=0; j<currEntries.length; j++) {
				if (j!=currEntries.length-1) {
					sb.append(currEntries[i][j] + ";");
				}else {
					sb.append(currEntries[i][j]);
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
