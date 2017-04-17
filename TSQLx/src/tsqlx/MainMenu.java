import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import org.w3c.dom.Document;


public class MainMenu {
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_WHITE = "\u001B[37m";
        public static Document DOM;

	//public static void runPrgm(){
	public static void main(String[] args) throws IOException {
            
            
		
		// Welcome Screen
		welcomeScreen(72);//pass in the indentation value
		//mainmenu
		mainMenuScreen();
                
                goodByeScreen(72);
		
	}
	
	//mainmenu
	public static void mainMenuScreen() throws IOException {
        Scanner scan = new Scanner(System.in); 
        boolean exit= false;
		int userIntput;
		String userStr;
                ArrayList<Lexer.Token> tokens;
                
		
		   //Main Menu	
		      while(!exit){
		        
		        System.out.println("Select one of the following choices: ");
                        
		 		System.out.println("1 DDL Command");
		 		System.out.println("2 DML Command");
		 		System.out.println("3 Quit");
                               
                                    System.out.println("cmd:");
                                    userIntput = scan.nextInt();
                                
                                
		 		
            	String inputs= "(.*)";
		         switch(userIntput){
                             
		            case 1: //DDL cmd
                                
                                    Scanner scanDDL = new Scanner(System.in);
                                    System.out.print("Enter DDL command: ");
		            	userStr = scanDDL.findInLine(inputs);
                                tokens = Lexer.lex(userStr);
		            	DDL_Parser ddl = new DDL_Parser();
                                ddl.parse(tokens);
                                
                                
                                
		            	 
		            	
		            	//System.out.println(userStr);
		            	
		            	//System.out.println(ANSI_RED_BACKGROUND+"run/call DDL cmd parser/processor"+ANSI_RESET);
		                     break;
		            case 2: //DML cmd
                                
                                    Scanner scanDML = new Scanner(System.in); 
		            	System.out.print("Enter DML command: ");
		            	userStr = scanDML.findInLine(inputs);
                                
		            	tokens = Lexer.lex(userStr);
                                DML dl = new DML();
                                dl.DMLstart(tokens);

		            	//System.out.println(ANSI_RED_BACKGROUND+"run/call DML cmd parser/processor"+ANSI_RESET);
		                     break;
		            case 3: exit = true; //Quit     
		            		break;
		         }
		         if(!(userIntput==3)){
		            printAstriks(50);
		         }
		      }
		      scan.close();
		   }
	
	
	
	//WelcomeScreen
	public static void welcomeScreen(int indent){
		printArt(); //Comment this if prefer not to show art
		String memberName[] = {"by",
				"Marco Del Castillo", "Eric Gurvitz",
				"Kevin Poon","Kai Thawng","Nathan Wheeler"
		};
		
		System.out.println(String.format("%"+(indent-(20/1))+"s", "Welcome our TSQLx - Engine"));
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
		System.out.println("Thank you for using TSQLx - Engine");
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
