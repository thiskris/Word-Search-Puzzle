// Author: Kris McFarland
// This program creates a word search puzzle
// it prompts the user for words to enter and then
// creates the puzzle with those words
// the user can also view the solution

import java.util.*;

public class WordSearch{
    public static void main(String[] args) {
        Random rand = new Random();
        Scanner console = new Scanner(System.in);
        
        // created two arrays. one for the generated puzzle, the other is solution
        int puzzleSize = 0;
        char[][] puzzle = generateBlankPuzzle(puzzleSize);
        char[][] solution = generateBlankPuzzle(puzzleSize);
        System.out.println(" \n Welcome to my word search generator \n");
        
        // while loop runs the program
        boolean runWordSearch = true;
        while(runWordSearch) {
            // array list holds prompted words
            ArrayList<String> words = new ArrayList<String>();
            printIntro();
            String menuOpt = console.nextLine();
            switch (menuOpt) {

                            // generate a puzzle and solution
                case "g" :  System.out.print("\n type 'done' when your finsished entering words \n");
                            getWords(console, words);
                            puzzleSize = calcPuzzleSize(words);
                            solution = generateBlankPuzzle(puzzleSize);
                            solution = generate(solution, words, rand);
                            puzzle = generatePuzzle(solution, rand);
                            break;

                            // display puzzle. puzzle size is 0 if no words have been added
                case "p" :  if(puzzleSize == 0) {
                                System.out.println("\n A word search must be generated in order to print");
                            } else {
                                showSolution(puzzle);
                            }
                            break;
                            
                            // display solution
                case "s" :  showSolution(solution);
                            break;

                            // quit
                case "q" :  runWordSearch = false;
                            break;

                            // reprompt menu in case of invalid menu input
                default  :  System.out.println("Invalid Option: Please select a valid option");
            }
        }
    } // end of main

    // main menu intro 
    public static void printIntro() {
        System.out.println("Select an option: \n" +
        "g - Generate a new word search  \n" +
        "p - Print out your word search \n" +
        "s - Show the solution to your word search \n" +
        "q - Quit the program");                 
    }

    // method to get words from the user and add to arrayList
    public static ArrayList<String> getWords(Scanner console, ArrayList<String> words) {
        boolean stopCollecting = false;
        while(!stopCollecting) {
            System.out.print("Enter word: ");
            String nextWord = console.nextLine().toLowerCase();
            // stop collecting words 
            if (nextWord.equals("done")) {
                stopCollecting = true;
            } else {
            words.add(nextWord);
            }
        }
        return words;
    }

    // calculates the puzzle size based on longest word and qty of words
    public static int calcPuzzleSize(ArrayList<String> words) {
        int longestWord = 0;
        for (int i = 0; i < words.size(); i++) {
            if (longestWord < words.get(i).length()) {
                longestWord = words.get(i).length();
            }
        }
        return longestWord + 1 + words.size() / 2;
    }

    // generates a skeloton puzzle based on the given size
    public static char[][] generateBlankPuzzle(int puzzleSize) {
        char[][] puzzle = new char[puzzleSize][puzzleSize];
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize ; j++) {
                puzzle[i][j] = '-';
                }
            }
            return puzzle;
        }

    // generates puzzle solution. words are sent as parameter -- > see get words method
    public static char[][] generate(char[][] puzzle, ArrayList<String> words, Random rand) {
        int diagonals = 0;
            for(int i = 0; i < words.size(); i++) {
                int num = rand.nextInt(3);
                if (num == 0 && diagonals < 2) {
                   // randomized word placement is based on the direction parameter String
                   exploreWordPlacement(puzzle, words.get(i), "Diagonal", rand);
                   diagonals++;
                } else if (num == 2) {
                   exploreWordPlacement(puzzle, words.get(i), "Horizontal", rand);
                } else {
                   exploreWordPlacement(puzzle, words.get(i), "Vertical", rand);
                }    
            }  
        return puzzle;
    }

    // generates puzzle with randomized letters
    public static char[][] generatePuzzle(char[][] solution, Random rand) {
        // created copy so the solution array is not changed
        char[][] arrCopy = new char[solution.length][solution.length];
        for (int i = 0; i < solution.length; i++) {
            arrCopy[i] = solution[i].clone();
        }
        for(int i = 0; i < solution.length; i++) {
            for(int j = 0; j < solution.length; j++) {
                if(arrCopy[i][j] == '-') {
                        arrCopy[i][j] = (char)(rand.nextInt(26) + 'a');
                }
            }
        }
        return arrCopy;
    }

    // puzzle is passed as paramter to print out charcters
    public static void showSolution(char[][] puzzle) {
        System.out.println();
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle.length; j++ ) {
            System.out.print(puzzle[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    // method to check if the word can in these cells
    public static boolean checkWord(char[][] puzzle, String word, String direction, int rowNum, int colNum) {
        boolean noConflict = true;
        for (int i = 0; i < word.length(); i++) {
            char cell = puzzle[rowNum][colNum];
                // if statement allows words to intersect 
                // if the next charcter is the same as the 
                // the character to be placed at location
                if(cell != word.charAt(i) && cell != '-') {
                    noConflict = false;
                }
                colNum = incrementCol(colNum, direction);
                rowNum = incrementRow(rowNum, direction);
            }
            return noConflict;
    }

    // places a word at location. collumn and row is incremented based on direction 
    public static void placeWord(char[][] puzzle, String word, String direction, int rowNum, int colNum) {
        for (int i = 0; i < word.length(); i++) {
            puzzle[rowNum][colNum] = word.charAt(i);
            colNum = incrementCol(colNum, direction);
            rowNum = incrementRow(rowNum, direction);
        }
    }

    // increments the collumn to place the next character
    public static int incrementCol(int colNum, String direction) {
        if (direction.equals("Horizontal") || direction.equals("Diagonal")) {
            colNum++;
        }
        return colNum;  
    }

    // increments the row to place the next character
    public static int incrementRow(int rowNum, String direction) {
        if (direction.equals("Vertical") || direction.equals("Diagonal")) {
            rowNum++;
        }
        return rowNum;
    }

    // recursivelly check if the chars can be placed at the collumn and row
    // until a solution is found. keeps exploring if no solution is found.
    // the direction is passed as a paramter so I can reuse this same method
    // for all word placements
    public static void exploreWordPlacement(char[][] puzzle, String word, String direction, Random rand) {
       int rowNum = randomRow(word, direction, rand, puzzle.length);
       int colNum = randomCol(word, direction, rand, puzzle.length);
        if (checkWord(puzzle, word, direction, rowNum, colNum)) {
            placeWord(puzzle, word, direction, rowNum, colNum);
        } else {
            exploreWordPlacement(puzzle, word, direction, rand);
        }
    } 

    // randomized column selction.
    public static int randomCol(String word, String direction, Random rand, int pSize) {
        int colNum = rand.nextInt(pSize);
        if (direction.equals("Horizontal") || direction.equals("Diagonal")) {
            colNum = rand.nextInt(pSize - word.length());
        }
        return colNum;
    }

    // randomized row selction
    public static int randomRow(String word, String direction, Random rand, int pSize) {
        int rowNum = rand.nextInt(pSize);
        if (direction.equals("Vertical") || direction.equals("Diagonal")) {
            rowNum = rand.nextInt(pSize - word.length());
        }
        return rowNum;
    }

} // end of WordSerach class