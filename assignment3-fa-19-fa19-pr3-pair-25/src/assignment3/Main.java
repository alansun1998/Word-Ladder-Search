/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * Alan Sun
 * as79972
 * 16180
 * William Gu
 * wg4792
 *
 * Slip days used: <0>
 * Git URL:
 * Fall 2019
 */


package assignment3;

import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	private static Set<String> wordDictionary;
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();

        ArrayList<String> input = parse(kb);
        int count = 0;
		while(input.size() != 0){
		    if(count %2 == 0){
                printLadder(getWordLadderDFS(input.get(0), input.get(1)));
            }else{
                printLadder(getWordLadderDFS(input.get(0), input.get(1)));
            }
		    input = parse(kb);

		    count ++;
        }

		/////////////
		//ArrayList<String> parse = parse(kb);
		//printLadder(getWordLadderBFS(parse.get(0), parse.get(1)));
		//printLadder(getWordLadderDFS("cells", "stone"));
		/////////////
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		wordDictionary = makeDictionary();

	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word.
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
        ArrayList<String> parse = new ArrayList<>();
        String command = keyboard.nextLine();
        if(!command.equals("/quit")) {
            //split the string into two substrings, ignoring all white spaces between the words
            String[] res = command.split("\\s+");
            parse.add(res[0]);
            parse.add(res[1]);
        }
        else{
			keyboard.close();
		}
		return parse;
	}

	// finds a word ladder between "start" and "end" using DFS searching, initializes all necessary variables
	// and calls the recursive function findWordLadder() and returns an ArrayList of the word ladder.
	// Returns the "start" and "end" word if no word ladder is found.
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		start = start.toUpperCase();
		end = end.toUpperCase();

		ArrayList<String> wordLadder = new ArrayList<>();

		Stack<String> stack = new Stack<String>();
		Set<String> tempDictionary = new HashSet<>();
		tempDictionary.addAll(wordDictionary);

		if(!tempDictionary.contains(end) || !tempDictionary.contains(start) ){       //start/end not in dictionary
			wordLadder.add(start);
			wordLadder.add(end);
			return wordLadder;
		}
		tempDictionary.remove(start);

		stack.add(start);
		stack = findWordLadderDFS(end, stack, tempDictionary);

		while(!stack.isEmpty()) {
			wordLadder.add(stack.pop());
		}
		if(wordLadder.size() == 1) {
			wordLadder.add(end);
		}
		Collections.reverse(wordLadder);
		return wordLadder;
	}

	//Recursive function that creates the word ladder and is used to end the search when the "end" is found.
	private static Stack<String> findWordLadderDFS(String end, Stack<String> stack, Set<String> tempDictionary) {
		if(stack.isEmpty() || stack.peek().equals(end)) {
			return stack;
		}
		else {
			String s = nextAdjacentWord(stack.peek(),tempDictionary,end);
			if(s == null) {
				stack.pop();
			}
			else {
				stack.add(s);
			}
			return findWordLadderDFS(end, stack, tempDictionary);
		}
	}

	// Returns the next valid word that is one letter different and removes it from the tempDictionary to prevent looping
	private static String nextAdjacentWord(String word, Set<String> tempDictionary, String end) {
		char[] currWord = word.toCharArray();
		boolean first = true;
		for (int i = 0; i < word.length(); i++) {
			char[] newWord = Arrays.copyOf(currWord, currWord.length);
			for (char c = 'Z'; c >= 'A'; c--) {
				if(c == 'Z' && first){
					newWord[i] = end.charAt(i);
					first = false;
					c++;
					String s = String.valueOf(newWord);
					if (tempDictionary.contains(s)) {
						tempDictionary.remove(s);
						return s;
					}
				}
				else if(c != currWord[i]) {
					newWord[i] = c;
					String s = String.valueOf(newWord);
					if (tempDictionary.contains(s)) {
						tempDictionary.remove(s);
						return s;
					}
				}
			}
		}
		return null;
	}

	// finds a word ladder between "start" and "end" using BFS searching,returns the "start" and "end" word if no word
	// ladder is found
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		start = start.toUpperCase();
		end = end.toUpperCase();

		ArrayList<String> wordLadder = new ArrayList<>();
		wordLadder.add(start);
		wordLadder.add(end);

		Queue<QNode> queue = new LinkedList<QNode>();
        ArrayList<String> tempDictionary = new ArrayList<>();
        tempDictionary.addAll(wordDictionary);
		if(!tempDictionary.contains(end) || !tempDictionary.contains(start) ){    //start/end not in dictionary
			return wordLadder;
		}

        queue.add(new QNode(start));
        if(!tempDictionary.contains(end)){ tempDictionary.add(end); }
		tempDictionary.remove(start);

        while(!queue.isEmpty()) {
        	QNode qn = queue.remove();

			char[] currWord = qn.getWord().toCharArray();
			for(int i = 0; i < start.length(); i++) {
				char[] newWord = Arrays.copyOf(currWord, currWord.length);
				for(char c = 'Z'; c >= 'A'; c--) {
					if(c != currWord[i]) {
						newWord[i] = c;
						String s = String.valueOf(newWord);
						if(tempDictionary.contains(s)) {
							QNode newNode = new QNode(s, qn);
							queue.add(newNode);
							tempDictionary.remove(s);

							if(s.equals(end)) {
								return getLadder(newNode);
							}
						}
					}
				}
			}
		}

		return wordLadder;
	}

	// Prints out the word ladder with the requested stylization.
	public static void printLadder(ArrayList<String> ladder) {
		if(ladder.size() == 2){
			System.out.println("no word ladder can be found between " + ladder.get(0).toLowerCase() + " and " + ladder.get(1).toLowerCase());
		}
		else{
			System.out.println("a " + ladder.size() + "-rung word ladder exists between " + ladder.get(0).toLowerCase() + " and " + ladder.get(ladder.size()-1).toLowerCase());
			for(String s : ladder) {
				System.out.println(s);
		}
        }
	}

	// Converts the QNode object into an ArrayList
	private static ArrayList<String> getLadder(QNode qn) {
		ArrayList<String> al = new ArrayList<>();
		QNode currentNode = qn;
		while(currentNode.getParent() != null) {
			al.add(0, currentNode.getWord());
			currentNode = currentNode.getParent();
		}
		al.add(0, currentNode.getWord());
		return al;
	}

	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}

		return words;
	}
}
