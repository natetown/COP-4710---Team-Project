/* Implements the LL(1) grammar used to parse DML statements.
   Takes tokens as a string array. Checks for syntax.
 */
package tsqlx;

/**
 *
 * @author kbp7
 */
import java.util.Queue;

public class DML {
    static int currentToken;
    static String[] tokens;
    static Queue commands;
    
    public static void DMLstart(String[] theTokens) {
        tokens = theTokens;
        //for now, only accepting 1 query at a time
        command();
        //commandList();
        System.out.println("ACCEPTED");
        //do something with it
    }
    public static void commandList()  {
        //if we allow multiple SQL queries at once, use this
        if(tokens[currentToken]!= null) {
            command();
            commandList();
        }
    }
    public static void command()    {
        
        currentToken = 0;
        if(tokens[currentToken].contains("INSERT -->"))   {
            insert();
        }
        else if(tokens[currentToken].contains("DELETE -->"))   {
            delete();
        }
        else if(tokens[currentToken].contains("SELECT -->"))   {
            select();
        }
        else if(tokens[currentToken].contains("CONVERT-->"))   {
            convert();
        }
        else    {
            rejected("ERROR: command not recognized");
        }
    }
    //--------------------------------------------------------------------------
    public static void insert() {
        if(tokens[currentToken].contains("INSERT -->"))   {
            currentToken++;
            if(tokens[currentToken].contains("INTO -->"))   {
                currentToken++;
                if(tokens[currentToken].contains("ID -->"))   {
                    currentToken++;
                    if(tokens[currentToken].contains("LPAREN -->")) {
                        fieldClause();
                        valuesClause();
                    }
                    else valuesClause();
                }
                else rejected("ERROR: Missing table name");
            }
            else rejected("ERROR: Missing keyword INTO");
        }
        else rejected("ERROR: Command not recognized");
    }
    //--------------------------------------------------------------------------
    public static void fieldClause()    {
        if(tokens[currentToken].contains("LPAREN -->")) {
            currentToken++;
            field();
                if(tokens[currentToken].contains("RPAREN -->")) {
                    currentToken++;
                }
                else rejected("ERROR: Missing closing parentheses");
        } // or empty
    }
    //--------------------------------------------------------------------------
    public static void valuesClause()   {
        if(tokens[currentToken].contains("VALUES -->")) {
            currentToken++;
            if(tokens[currentToken].contains("LPAREN -->")) {
                currentToken++;
                literal();
                if(tokens[currentToken].contains("RPAREN -->")) {
                    currentToken++;
                    if(tokens[currentToken].contains("SEMICOLON -->")) {
                        currentToken++;
                    }
                    else rejected("ERROR: Missing semicolon");
                }
                else rejected("ERROR: Missing closing parentheses");
            }
            else rejected("ERROR: Missing opening parentheses");
        }
        else rejected("ERROR: Missing VALUES clause");
    }
    //--------------------------------------------------------------------------
    public static void field() {
        if(tokens[currentToken].contains("ID -->")) {
            currentToken++;
            fieldList();
        }
        else rejected("ERROR: Missing field");
    }
    //--------------------------------------------------------------------------
    public static void fieldList() {
        if(tokens[currentToken].contains(","))  {
            currentToken++;
            if(tokens[currentToken].contains("ID -->")) {
                currentToken++;
                fieldList();
            }
            else rejected("Error: Invalid field list syntax");
        } //or empty
    }
    //--------------------------------------------------------------------------
    public static void literal() {
        String token = tokens[currentToken];
        if(token.contains("NUMBER -->") || token.contains("STRING -->") || 
           token.contains("DATE -->"))  {
            aLiteral();
            literalList();
        }
        else rejected("Syntax error on Literal List");
    }
    //--------------------------------------------------------------------------
    public static void aLiteral() {
        if(tokens[currentToken].contains("NUMBER -->")) {
            // do something with the number
            currentToken++;
        }
        else if(tokens[currentToken].contains("STRING -->")) {
            // do something with the number
            currentToken++;
        }
        else if(tokens[currentToken].contains("DATE -->")) {
            // do something with the number
            currentToken++;
        }
        else rejected("Invalid literal");
    }
    //--------------------------------------------------------------------------
    public static void literalList() {
        if(tokens[currentToken].contains(",")) {
            currentToken++;
            aLiteral();
            literalList();
        }// or empty
    }
    //--------------------------------------------------------------------------
    public static void whereClause() {
        if(tokens[currentToken].contains("LBRACK -->")) {
            currentToken++;
            if(tokens[currentToken].contains("WHERE -->"))  {
                currentToken++;
                condition();
                if(tokens[currentToken].contains("RBRACK -->")) {
                    currentToken++;
                }
                else rejected("ERROR: Missing closing bracket");
            }
            else rejected("ERROR: Missing keyword WHERE");
        } //or empty
    }
    //--------------------------------------------------------------------------
    public static void delete() {
        if(tokens[currentToken].contains("DELETE -->")) {
            currentToken++;
            if(tokens[currentToken].contains("FROM -->")) {
                currentToken++;
                //tablename
                if(tokens[currentToken].contains("ID -->")) {
                    currentToken++;
                    whereClause();
                    if(tokens[currentToken].contains("SEMICOLON -->"))  {
                        currentToken++;
                    }
                    else rejected("ERROR: missing semicolon");
                }
                else rejected("ERROR: missing table name");
            }
            else rejected("ERROR: missing FROM keyword");
        }
        else rejected("ERROR: invalid command");
    }
    //--------------------------------------------------------------------------
    public static void condition() {
        if(tokens[currentToken].contains("ID -->")) {
            field();
            relop();
            FL();
            AndOr();
        }
        else rejected("ERROR: Missing condition");
    }
    //--------------------------------------------------------------------------
    public static void FL() {
        if(tokens[currentToken].contains("ID -->")) {
            field();
        }
        else aLiteral();
    }
    //--------------------------------------------------------------------------
    public static void AndOr()  {
        if(tokens[currentToken].contains("AND"))    {
            currentToken++;
            condition();
        }
        if(tokens[currentToken].contains("OR"))    {
            currentToken++;
            condition();
        }
        //or empty
    }
    public static void relop() {
        if(isRelop(tokens[currentToken]))   {
            currentToken++;
        }
        else rejected("ERROR: Missing relop");
    }
    //--------------------------------------------------------------------------
    public static boolean isRelop(String x) {
        //returns true if token is relational operator
        switch (x) {
            case "<=":
                return true;
            case "<":
                return true;
            case ">":
                return true;
            case ">=":
                return true;
            default:
                return x.equals("=") || x.equals("!=");
        }
    }
    //--------------------------------------------------------------------------
    public static void select() {
        //type of select statement
        if(tokens[currentToken].contains("tSELECT -->")) {
            temporal();
        }
        else if(tokens[currentToken].contains("SELECT -->")) {
            normal();
        }
        else rejected("ERROR: Invalid command");
    }
    //--------------------------------------------------------------------------
    public static void normal() {
        if(tokens[currentToken].contains("SELECT -->")) {
            currentToken++;
            columns();
            if(tokens[currentToken].contains("FROM -->")) {
                currentToken++;
                if(tokens[currentToken].contains("ID -->")) {
                    currentToken++;
                }
                else rejected("ERROR: Missing table name");
            }
            else rejected("ERROR: Missing keyword FROM");
        }
        else rejected("ERROR: Invalid command");
    }
    //--------------------------------------------------------------------------
    public static void columns() {
        if(tokens[currentToken].contains("ASTK -->")) {
            currentToken++;
        }
        else if(tokens[currentToken].contains("ID -->")) {
            field();
        }
        else rejected("ERROR: Missing column selection");
    }
    //--------------------------------------------------------------------------
    public static void temporal() {
        if(tokens[currentToken].contains("tSELECT -->")) {
            currentToken++;
            columns();
            if(tokens[currentToken].contains("FROM -->")) {
                currentToken++;
                if(tokens[currentToken].contains("ID -->")) {
                    currentToken++;
                    whereClause();
                    if(tokens[currentToken].contains("SEMICOLON -->")) {
                        currentToken++;
                    }
                    else rejected("ERROR: Missing semicolon");
                }
                else rejected("ERROR: Missing table name");
            }
            else rejected("ERROR: Missing keyword FROM");
        }
        else rejected("ERROR: Invalid command");
    }
    //--------------------------------------------------------------------------
    public static void string() {
        
    }
    //--------------------------------------------------------------------------
    public static void convert() {
        if(tokens[currentToken].contains("CONVERT -->")) {
            currentToken++;
            if(tokens[currentToken].contains("XML -->")) {
                currentToken++;
                xmlFileName();
                xsd();
                if(tokens[currentToken].contains("AS -->")) {
                    currentToken++;
                    outputFile();
                }
                else rejected("ERROR: Missing keyword AS");
            }
            else rejected("ERROR: Missing XML");
        }
        else rejected("ERROR: Invalid command");
    }
    //--------------------------------------------------------------------------
    public static void xsd() {
        if(tokens[currentToken].contains("COMMA -->")) {
            currentToken++;
            if(tokens[currentToken].contains("XSD -->")) {
                currentToken++;
                xsdFileName();
            }
            else rejected("ERROR: Missing XSD");
        }// or empty
    }
    //--------------------------------------------------------------------------
    public static void xmlFileName() {
        if(tokens[currentToken].contains("ID -->")) {
            currentToken++;
            if(tokens[currentToken].contains("DOT -->")) {
                currentToken++;
                if(tokens[currentToken].contains("XML -->")) {
                    currentToken++;
                }
                else rejected("ERROR: Invalid XML file type");
            }
            else rejected("ERROR: Invalid XML file");
        }
        else rejected("ERROR: Invalid XML file");
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public static void xsdFileName() {
        if(tokens[currentToken].contains("ID -->")) {
            currentToken++;
            if(tokens[currentToken].contains("DOT -->")) {
                currentToken++;
                if(tokens[currentToken].contains("XSD -->")) {
                    currentToken++;
                }
                else rejected("ERROR: Invalid XSD file type");
            }
            else rejected("ERROR: Invalid XSD file");
        }
        else rejected("ERROR: Invalid XSD file");
    }
    public static void outputFile() {
        if(tokens[currentToken].contains("ID -->")) {
            currentToken++;
            if(tokens[currentToken].contains("DOT -->")) {
                currentToken++;
                if(tokens[currentToken].contains("TXT -->")) {
                    currentToken++;
                }
                else rejected("ERROR: Invalid output file type");
            }
            else rejected("ERROR: Invalid output file");
        }
        else rejected("ERROR: Invalid output file");
    }
    //-------------------------------------------------------------------------
    public static void rejected(String error)   {
        System.out.println("ERROR: " + error);
    }
}
