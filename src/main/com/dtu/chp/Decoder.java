package com.dtu.chp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Decoder {

    private int alphabetSize = 0;
    public int noOfStrings = 0;
    private int puzzleSize = 0;
    private List<Character> alphabet;
    private char[][] entries;
    private List<String> words;
    private static Decoder decoder = null;

    private Decoder() {
        words = new ArrayList<String>();
        alphabet = new ArrayList<Character>();
    }

    public static Decoder getInstance() {
        if (decoder == null) {
            decoder = new Decoder();
        }
        return decoder;
    }
    
    //Uniform Decoder function for both file input and STDIN 
	public boolean decode(String filename, boolean useFile) {
		try {
			String strLine;
			if (useFile){ //Using file for decoding
				File initialFile = new File(filename);
				InputStream is =  new FileInputStream(initialFile);
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				int counter = 0;
				int lineCounter = 0;
				
				while ((strLine = br.readLine()) != null) {
					// Read the first line, which should contain the following <alphabet size>;<no. of strings>;<puzzle size>
					switch(counter){
					case 0:
						this.entries = initialize(strLine);
						if(this.entries == null)
							return false;
						break;
					case 1: 
						if(!initializeAlphabet(strLine))
							return false;
						break;
					default: 
						if(counter > 1 && counter < this.puzzleSize + 2){
							if (lineCounter == this.puzzleSize || !initializePuzzleBoard(strLine, lineCounter)){
								return false;
							}
							lineCounter++;
						}
						if (counter >= this.puzzleSize + 2 && counter < this.puzzleSize + this.noOfStrings + 2) {
							if(!initializeWords(strLine))
								return false;
						}
						break;
					}				
					counter++;
				}
				is.close();
				br.close();	
			}
			else{ //Using STDIN
				Scanner scanner = new Scanner(System.in);
				strLine = scanner.nextLine();
				this.entries = initialize(strLine);
				if(this.entries == null)
					return false;
				strLine = scanner.nextLine();
				initializeAlphabet(strLine);
				for(int i = 0; i < this.puzzleSize; i++){
					strLine = scanner.nextLine();
					initializePuzzleBoard(strLine, i);
				}
				for(int i = 0; i < this.noOfStrings; i++){
					if(!scanner.hasNext())
						return false;
					strLine = scanner.nextLine();
					initializeWords(strLine);
				}
				if(scanner.hasNext())
					return false;
				scanner.close();
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}

	}
	
    // Initializing parameters for CPC
	private char[][] initialize(String strLine){
		String[] inputs = strLine.split(";");

		if (inputs.length != 3 || inputs[0].equals("") || inputs[1].equals("") || inputs[2].equals("")) {
			return null;
		}
		
		this.alphabetSize = Integer.parseInt(inputs[0]);
		this.noOfStrings = Integer.parseInt(inputs[1]);
		this.puzzleSize = Integer.parseInt(inputs[2]);

		if (this.alphabetSize <= 0 || this.noOfStrings <= 0 || this.puzzleSize <= 0){
			return null;
		}

		return new char[puzzleSize][puzzleSize];
	}
	
	// Initialize Alphabet 
	private boolean initializeAlphabet(String strLine){
		String[] letters = strLine.split(";");

		if (letters.length != this.alphabetSize){
			return false;
		}

		for (int i = 0; i < letters.length; i++) {
			this.alphabet.add(letters[i].toCharArray()[0]);
		}
		return true;
	}
	
	//Initialize puzzleboard
	private boolean initializePuzzleBoard(String strLine, int lineCounter){

		String[] chars = strLine.split(";");

		if (chars.length != puzzleSize){
			return false;
		}

		for (int i = 0; i < chars.length; i++) {
			if (chars[i].equals("#") || chars[i].equals("_")) {
				this.entries[lineCounter][i] = chars[i].charAt(0);
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	//Initialize words
	private boolean initializeWords(String strLine){

		if (checkCharacterInAlphabet(strLine)) {
			if(strLine.length() <= this.puzzleSize) {
				this.words.add(strLine);
			} else {
				return false;
			}
		}
		return true;
	}
	
    /*
     * Checks whether a given strings has the character from the provided alphabet
     *
     * param s character that must be in alphabet
     * */
    private boolean checkCharacterInAlphabet(String s) {
    	int count = 0;
    	
        for (int i = 0; i < s.length(); i++) {
        	if (alphabet.contains(s.charAt(i)))
        		count++;
        }
        return count == s.length();
    }


    public int getPuzzleSize() {
        return puzzleSize;
    }

    public char[][] getEntries() {
        return entries;
    }

    public List<String> getStrings() {
        return words;
    }

}
