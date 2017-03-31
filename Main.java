import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
	    String input = "CREATE create TABLE table * lkjjk2343 INTO into CONVERT convert , CREATE create";
	 
	    
	    //e.g. ArrayList<Token> token = lex(inputString);
	    
	    ArrayList<Lexer.Token> tokens = Lexer.lex(input);
	    for (Lexer.Token token : tokens){
	      System.out.println(token);
	     //System.out.println(token.type);
	     //System.out.println(token.data);

	    }
	    
	  }

}
