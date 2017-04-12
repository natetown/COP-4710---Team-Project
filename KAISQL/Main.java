import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    
    public static void main(String[] args) throws IOException {
        String userInput = " Create Database;";
        BufferedWriter bw = null;
        FileWriter fw = null;
        final String FILENAME = "C:\\test\\lexerOuput.txt";
        
        //e.g. ArrayList<Token> token = lex(inputString);
       
        	fw = new FileWriter(FILENAME);
        	bw = new BufferedWriter(fw);
        	
        	
        ArrayList<Lexer.Token> tokens = Lexer.lex(userInput);
 
        for (Lexer.Token token : tokens){
           // System.out.println(token);
            System.out.println(token.type +" --> "+token.data);
            bw.write(token.type + "  --> "+ token.data +"\n");
        }
        System.out.println("Done");
        
        if (bw != null)
			bw.close();

		if (fw != null)
			fw.close();
        
    }
    
    
    
}
