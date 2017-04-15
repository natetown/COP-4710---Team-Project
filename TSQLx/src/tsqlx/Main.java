package tsqlx;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    
    public static void main(String[] args) throws IOException {
        String userInput = "INSERT INTO REEE; VALUES(123, 456);";
        BufferedWriter bw = null;
        FileWriter fw = null;
        final String FILENAME = "lexerOuput.txt";
        
        //e.g. ArrayList<Token> token = lex(inputString);
       
        	fw = new FileWriter(FILENAME);
        	bw = new BufferedWriter(fw);
        	
        	
        ArrayList<Lexer.Token> tokens = Lexer.lex(userInput);
 
        for (Lexer.Token token : tokens){
           // System.out.println(token);
            System.out.println(token.type +" --> "+token.data);
            bw.write(token.type + " --> "+ token.data +"\n");
        }
        System.out.println("Done");
        DML dtest = new DML();
        dtest.DMLstart(tokens);
        if (bw != null)
			bw.close();

		if (fw != null)
			fw.close();
        
    }
    
    
    
}
