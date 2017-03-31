/* Implements the LL(1) grammar used to parse DML statements.
 
 */
package tsqlx;

/**
 *
 * @author kbp7
 */
public class DML {
    static int currentToken;
    static String[] tokens;
    public static void command(String[] theTokens)    {
        tokens = theTokens;
        currentToken = 0;
        if(tokens[currentToken].contains("KEYWORD: INSERT"))   {
            insert();
        }
        else if(tokens[currentToken].contains("KEYWORD: DELETE"))   {
            delete();
        }
        else if(tokens[currentToken].contains("KEYWORD: SELECT"))   {
            select();
        }
        else if(tokens[currentToken].contains("KEYWORD: CONVERT"))   {
            convert();
        }
        else    {
            System.out.println("ERROR: command not recognized");
        }
    }
    //--------------------------------------------------------------------------
    public static void insert() {
        
    }
    //--------------------------------------------------------------------------
    public static void field() {
        
    }
    //--------------------------------------------------------------------------
    public static void fieldList() {
        
    }
    //--------------------------------------------------------------------------
    public static void literal() {
        
    }
    //--------------------------------------------------------------------------
    public static void aLiteral() {
        
    }
    //--------------------------------------------------------------------------
    public static void literalList() {
        
    }
    //--------------------------------------------------------------------------
    public static void whereClause() {
        
    }
    //--------------------------------------------------------------------------
    public static void delete() {
        
    }
    //--------------------------------------------------------------------------
    public static void condition() {
        
    }
    //--------------------------------------------------------------------------
    public static void FL() {
        
    }
    //--------------------------------------------------------------------------
    public static void relop() {
        
    }
    //--------------------------------------------------------------------------
    public static void select() {
        
    }
    //--------------------------------------------------------------------------
    public static void normal() {
        
    }
    //--------------------------------------------------------------------------
    public static void columns() {
        
    }
    //--------------------------------------------------------------------------
    public static void temporal() {
        
    }
    //--------------------------------------------------------------------------
    public static void string() {
        
    }
    //--------------------------------------------------------------------------
    public static void convert() {
        
    }
    //--------------------------------------------------------------------------
    public static void xsd() {
        
    }
    //--------------------------------------------------------------------------
    public static void xmlFileName() {
        
    }
    //--------------------------------------------------------------------------
    public static void outputFile() {
        
    }
}
