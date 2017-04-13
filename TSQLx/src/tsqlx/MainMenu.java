import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MainMenu {
	//There
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_WHITE = "\u001B[37m";

	// public static void runPrgm(){
	public static void main(String[] args) {
		// Welcome Screen
		welcomeScreen(72);// pass in the indentation value
		// mainmenu
		mainMenuScreen();

	}

	// mainmenu
	public static void mainMenuScreen() {
		Scanner scan = new Scanner(System.in);
		boolean exit = false;
		String userIntput = null;
		String userStr = null;
		String inputs = "(.*)";
		

		// Main Menu
		while (!exit) {

			System.out.println("Select one of the following choices: ");
			System.out.println("1 Sys Lvl Command");
			System.out.println("2 DDL Command");
			System.out.println("3 DML Command");
			System.out.println("4 Upload XML & XSD Files");
			System.out.println("5 Quit");
			File outfile = null;
			try {
				System.out.print("cmd: ");
				userIntput = scan.next();
				

			} catch (Exception e) {
				// TODO: handle exception
				scan = new Scanner(System.in);
				System.out.println(ANSI_RED_BACKGROUND + "Please enter a valid character" + ANSI_RESET);
			}
			
			switch (userIntput) {
			case "1": // Run system lvl cmd
				Scanner scanSLC = new Scanner(System.in);
				System.out.print("Enter Sys Lvl command: ");
				inputs = "(.*)";
				userStr = scanSLC.findInLine(inputs);
				// System.out.println(userStr);
				outfile = runLex(userStr);
				// Call sys lvl cmd processor
				System.out.println(ANSI_RED_BACKGROUND + "run/call a sys lvl cmd parser-->create database" + ANSI_RESET);
				break;

			case "2": // DDL cmd
				Scanner scanDDL = new Scanner(System.in);
				System.out.print("Enter DDL command: ");
				userStr = scanDDL.findInLine(inputs);
				// System.out.println(userStr);
				outfile = runLex(userStr);
				// Call DDL cmd processor
				System.out.println(ANSI_RED_BACKGROUND + "run/call DDL cmd parser/processor-->table" + ANSI_RESET);
				break;
			case "3": // --DML cmd
				Scanner scanDML = new Scanner(System.in);
				System.out.print("Enter DDL command: ");
				userStr = scanDML.findInLine(inputs);
				// System.out.println(userStr);
				// --run lexer
				outfile = runLex(userStr);
				// --Call DML cmd processor
				System.out.println(ANSI_RED_BACKGROUND + "run/call DML cmd parser/processor--->table---rows col" + ANSI_RESET);
				break;
			case "4":
				Scanner scanXML = new Scanner(System.in);
				System.out.print("Enter command: ");
				userStr = scanXML.findInLine(inputs);
				// --run lexer
				outfile= runLex(userStr);
				
				// --run xml,xsd processor
				break;
			case "5":
				exit = true; // Quit
				// Save table and database
				System.out.println("Saving datable and database");
				goodByeScreen(72);
				break;

			default:
				 System.out.println(ANSI_RED_BACKGROUND+"Please enter a valid character"+ANSI_RESET);
			}
			if (!(userIntput == "5")) {
				printAstriks(72);
			}
		}
		scan.close();
	}

	/*
	 *  run lex calls the Lexer.java to process 
	 */
	public static File runLex(String input) {
		ArrayList<Lexer.Token> tokens = Lexer.lex(input);
		
		File file = new File("FileWriter.txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            for(Lexer.Token token : tokens){
    		System.out.println(token.type + " --> " + token.data);// comment this line in final revision
            fr.write(token.type + " --> "+ token.data +"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
		}
		
		return file;
	}
	
	

	/*
	 *  WelcomeScreen, gets call by MainMenu.java 
	 */
	public static void welcomeScreen(int indent) {
		// Comment this if prefer not to show art
		printArt(); 
		//Stores members and print it 
		String memberName[] = { "by", "Marco Del Castillo", "Eric Gurvitz", "Kevin Poon", "Kai Thawng","Nathan Wheeler" };
		System.out.println(ANSI_RED_BACKGROUND + String.format("%" + (indent - (20 / 1)) + "s",
				"Welcome to Best SQL-engine ever" + ANSI_RESET.replace(' ', '+')));
		for (String name : memberName) {
			System.out.println(String.format("%" + indent + "s", name).replace(' ', '-'));
		}
		printAstriks(indent);
	}

	/*
	 * Prints art call by welcomeScreen
	 * */
	
	public static void printArt() {
		// read Art
		try (BufferedReader br = new BufferedReader(new FileReader("art.txt"))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			System.out.print(everything);
		} catch (FileNotFoundException e) {
			System.out.println("Go Database");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* prints goodbye
	 * 
	 */
	public static void goodByeScreen(int numofStar) {
		printAstriks(numofStar);
		System.out.println("Thank you for using Best SQL-engine ever");
		System.out.println("Goodbye!");
	}

	// print Astricks
	public static void printAstriks(int numOfAstk) {
		for (int i = 0; i < numOfAstk; i++) {
			System.out.print("*");
		}
		System.out.println("\n");
	}

}
