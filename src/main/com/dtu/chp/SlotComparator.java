package com.dtu.chp;

import java.util.Comparator;

public class SlotComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		
		PuzzleSlot p1 = (PuzzleSlot) o1;
		PuzzleSlot p2 = (PuzzleSlot) o2;
		
		int l1 = p1.getLength();
		int l2 = p2.getLength();
		
		if(l1 > l2) {
			return -1;
		} else if (l1 < l2) {
			return 1;
		} else {
			return 0;
		}
	}
	

}
