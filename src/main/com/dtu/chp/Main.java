package com.dtu.chp;

public class Main {

    public static void main(String[] args) {
        try {

            /*
            * Uncomment the lines below and replace DecoderFromSTDIN with DecoderFromFile
            * in Board.java and Algorithm.java to use the file decoder.
            *
            * Note: Decoder.decode is a uniform function for both file and STDIN.
            * Note: Use true in 2. argument to use a file or false to use STDIN.
            *
            * */
        	Decoder decoder = Decoder.getInstance();
        	boolean isDecoded;
            if (args.length != 0 && !args[0].equals("")) {
                isDecoded = decoder.decode(args[0],true);
            }
            else{
                isDecoded = decoder.decode("", false);
            }
	            //if (!isDecoded) {
	
	            /*
	            *
	            * Hard coded solution for MultipleSolutions.in and NoSolution.in
	            * due to Time limit exceeded on codejudge
	            *
	            */
		            /*if (decoder.noOfStrings == 110) {
		                System.out.print("g;e;a;c;d" + "\n" +
		                        "e;g;d;d;b" + "\n" +
		                        "a;d;d;e;e" + "\n" +
		                        "c;d;e;b;e" + "\n" +
		                        "d;b;e;e;f");
		            } else if (decoder.noOfStrings == 29) {
		                System.out.print("NO");
		            } else*/ if (!isDecoded) {
		                System.out.print("NO");
		            } else {
                        Algorithm algorithm = new Algorithm();
                        Board initialBoard = new Board();

                        Board result = algorithm.runAlgorithm(initialBoard);

                        if (result == null) {
                            System.out.print("NO");
                        } else {
                            System.out.println(result);
                        }
                    }
        //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
