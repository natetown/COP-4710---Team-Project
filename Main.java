import java.util.ArrayList;

public class Main {
    
    public static void main(String[] args) {
        String userInput = "(987 09-12-99  09-12-1999 CREATE; create TABLE ( field1 , field2, field3 ) table *FROM lkjjk2343 INTO into CONVERT convert , CREATE create";
        
        
        //e.g. ArrayList<Token> token = lex(inputString);
        
        ArrayList<Lexer.Token> tokens = Lexer.lex(userInput);
        for (Lexer.Token token : tokens){
           // System.out.println(token);
            System.out.println(token.type +" --> "+token.data);
    
        }
        
    }
    
    
    
}
