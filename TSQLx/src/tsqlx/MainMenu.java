package tsqlx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MainMenu {
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_WHITE = "\u001B[37m";

	//public static void runPrgm(){
	public static void main(String[] args) {
		
		// Welcome Screen
		welcomeScreen(72);//pass in the indentation value
		//mainmenu
		mainMenuScreen();
		
	}
	
	//mainmenu
	public static void mainMenuScreen() {
        Scanner scan = new Scanner(System.in); 
        boolean exit= false;
		int userIntput;
		String userStr;
		
		   //Main Menu	
		      while(!exit){
		        
		        System.out.println("Select one of the following choices: ");
		 		System.out.println("1 Sys Lvl Command");
		 		System.out.println("2 DDL Command");
		 		System.out.println("3 DML Command");
		 		System.out.println("4 Quit");
		 		userIntput = scan.nextInt();
            	String inputs= "(.*)";
		         switch(userIntput){
		            case 1: //Run system lvl cmd
		            	Scanner scanSLC = new Scanner(System.in); 
		            	System.out.print("Enter Sys Lvl command: "); 
		            	inputs= "(.*)";
		            	userStr=scanSLC.findInLine(inputs);
		            	//System.out.println(userStr);
		            	runLex(userStr);
		            	//Call sys lvl cmd processor
		            	System.out.println(ANSI_RED_BACKGROUND+"run/call a sys lvl cmd parser/processor"+ANSI_RESET);		            	
		            
		                break;
		            
		            case 2: //DDL cmd
		            	Scanner scanDDL = new Scanner(System.in); 
		            	System.out.print("Enter DDL command: ");
		            	userStr = scanDDL.findInLine(inputs);
		            	//System.out.println(userStr);
		            	runLex(userStr);
		            	//Call DDL cmd processor
		            	System.out.println(ANSI_RED_BACKGROUND+"run/call DDL cmd parser/processor"+ANSI_RESET);
		                     break;
		            case 3: //DML cmd
		            	Scanner scanDML = new Scanner(System.in); 
		            	System.out.print("Enter DDL command: ");
		            	userStr = scanDML.findInLine(inputs);
		            	//System.out.println(userStr);
		            	runLex(userStr); //run lexer
		            	//Call DML cmd processor
		            	System.out.println(ANSI_RED_BACKGROUND+"run/call DML cmd parser/processor"+ANSI_RESET);
		                     break;
		            case 4: exit = true; //Quit     
		            		break;
		         }
		         if(!(userIntput==4)){
		            printAstriks(50);
		         }
		      }
		      scan.close();
		   }
	//run lex
	public static void runLex(String input){
		ArrayList<Lexer.Token> tokens = Lexer.lex(input);
		 BufferedWriter bw = null;
	        FileWriter fw = null;
	        final String FILENAME = "lexerOutput.txt"; //output file path
	        try {
				fw = new FileWriter(FILENAME);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	bw = new BufferedWriter(fw);
	    	
	    	for (Lexer.Token token : tokens){
	             System.out.println(token.type +" --> "+token.data);
	             try {
					bw.write(token.type + " --> "+ token.data +"\n");
                                        
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }        
	         if (bw != null)
				try {
					bw.close();

	 		if (fw != null)
	 			fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                 try{ DML.DMLstart(tokens); }
                 catch(IOException e) {}
		
	}
	
	//WelcomeScreen
	public static void welcomeScreen(int indent){
		printArt(); //Comment this if prefer not to show art
		String memberName[] = {"by",
				"Marco Del Castillo", "Eric Gurvitz",
				"Kevin Poon","Kai Thawng","Nathan Wheeler"
		};
		
		System.out.println(ANSI_RED_BACKGROUND + String.format("%"+(indent-(20/1))+"s", "Welcome to Best SQL-engine ever" +ANSI_RESET.replace(' ', '+')));
    	for( String name: memberName){
		System.out.println(String.format("%"+indent+"s",name ).replace(' ', '-'));
    	}
		printAstriks(indent);
	}
	
	public static void printArt() {
		//read Art
        try(BufferedReader br = new BufferedReader(new FileReader("art.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            System.out.print(everything);
        }catch (FileNotFoundException e){
        	System.out.println("Go Database");
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//print goodbye
	public static void goodByeScreen(int numofStar){
		printAstriks(numofStar);
		System.out.println("Thank you for using Best SQL-engine");
		System.out.println("Goodbye!");
	}
	//print Astricks 
	   public static void printAstriks(int numOfAstk){
	      for(int i =0; i< numOfAstk; i++){
	         System.out.print("*");
	      }
	      System.out.println("\n");
	   }

}
