package com.dtu.chp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Algorithm {

	private List<String> strings;
	private List<Integer> vertical;
	private List<Integer> horizontal;
	private List<PuzzleSlot> slots;
	private List<Integer> lengths;
	private List<List<String>> segregatedWords;
	private Decoder decoder;

	public Algorithm() {
		this.decoder = Decoder.getInstance();
		this.strings = decoder.getStrings();
		this.vertical = new ArrayList<Integer>();
		this.horizontal = new ArrayList<Integer>();
		this.slots = new ArrayList<PuzzleSlot>();
		segregatedWords = segregateStringsByLength(strings);
	}

	public Board runAlgorithm(Board currentBoard) {

		if (strings != null && !strings.isEmpty()) {
			
			if(currentBoard.getAllSlots().isEmpty()) return currentBoard;
			PuzzleSlot p = currentBoard.getAllSlots().get(0);

			List<String> certainList = null;

			for (List<String> l : segregatedWords) {
				if (l.get(0).length() == p.getLength()) {
					certainList = l;
					break;
				}
			}

			if (certainList != null) {

				for (String s : certainList) {
					Board newBoard = new Board(currentBoard);
					if (newBoard.fillIn(p, s)) {
						Board result = runAlgorithm(newBoard);
						if (result != null)
							return result;
					}

				}
			}
		}
		return null;
	}

	private List<List<String>> segregateStringsByLength(List<String> strings) {

		lengths = new ArrayList<Integer>();

		for (String s : strings) {
			if (!lengths.contains(s.length()))
				lengths.add(s.length());
		}

		Collections.sort(lengths);

		List<List<String>> lists = new ArrayList<List<String>>();

		for (int i = 0; i < lengths.size(); i++) {
			lists.add(new ArrayList<String>());
		}

		for (String s : strings) {
			for (int i = 0; i < lengths.size(); i++) {
				if (s.length() == lengths.get(i)) {
					lists.get(i).add(s);
				}
			}
		}

		return lists;

	}
}
