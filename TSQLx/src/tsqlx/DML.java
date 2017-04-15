/* Implements the LL(1) grammar used to parse DML statements.
   Takes tokens as a string array. Checks for syntax.
 */
package tsqlx;

/**
 *
 * @author kbp7
 */
import java.io.*;
import java.util.Queue;
import java.util.*;

public class DML {
    static int currentToken;
    static Lexer.Token[] tokens;
    static Queue commands;
    static Query q;
    static boolean isValid;
    static boolean readingWhere; //indicates that the parser has reached the WHERE clause
    
    public static void DMLstart(ArrayList<Lexer.Token> lexer) throws IOException {
        System.out.println("Begin parsing...");
        Lexer.Token line;
        tokens = new Lexer.Token[500];
        for (int i=0; i<lexer.size(); i++) {
            line = lexer.get(i);
            tokens[i] = line;
        }
        
       
        //for now, only accepting 1 query at a time
        command();
        //commandList();
        isValid = true;
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
        //calls corresponding query type
        currentToken = 0;
        if(tokens[currentToken].type.toString().contains("INSERT"))   {
            insert();
        }
        else if(tokens[currentToken].type.toString().contains("DELETE"))   {
            delete();
        }
        else if(tokens[currentToken].type.toString().contains("SELECT"))   {
            select();
        }
        else if(tokens[currentToken].type.toString().contains("CONVERT"))   {
            convert();
        }
        else    {
            rejected("ERROR: command not recognized");
        }
    }
    //--------------------------------------------------------------------------
    public static void insert() {
        if(tokens[currentToken].type.toString().contains("INSERT"))   {
            //create InsertQuery object
            q = new InsertQuery();
            currentToken++;
            if(tokens[currentToken].type.toString().contains("INTO"))   {
                currentToken++;
                if(tokens[currentToken].type.toString().contains("ID"))   {
                    //assign table name to query
                    String table = tokens[currentToken].data;
                    q.assignTN(table);
                    
                    currentToken++;
                    if(tokens[currentToken].type.toString().contains("LPAREN")) {
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
        if(tokens[currentToken].type.toString().contains("LPAREN")) {
            currentToken++;
            field();
                if(tokens[currentToken].type.toString().contains("RPAREN")) {
                    currentToken++;
                }
                else rejected("ERROR: Missing closing parentheses");
        } // or empty
    }
    //--------------------------------------------------------------------------
    public static void valuesClause()   {
        if(tokens[currentToken].type.toString().contains("VALUES")) {
            currentToken++;
            if(tokens[currentToken].type.toString().contains("LPAREN")) {
                currentToken++;
                literal();
                if(tokens[currentToken].type.toString().contains("RPAREN")) {
                    currentToken++;
                    if(tokens[currentToken].type.toString().contains("SEMICOLON")) {
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
        if(!readingWhere && tokens[currentToken].type.toString().contains("ID")) {
            //add field to query
            if(q.queryType.equals("INSERT")) {
                ((InsertQuery)q).assignFields(tokens[currentToken].data);
            }
            if(q.queryType.equals("SELECT") || q.queryType.equals("TSELECT")) {
                ((SelectQuery)q).assignColumns(tokens[currentToken].data);
            }
            currentToken++;
            fieldList();
        }
        if(readingWhere && tokens[currentToken].type.toString().contains("ID")) {
            if(q.queryType.equals("SELECT") || q.queryType.equals("TSELECT")) {
                //((SelectQuery)q).assignColumns(tokens[currentToken].data);
            }
        }
        else rejected("ERROR: Missing field");
    }
    //--------------------------------------------------------------------------
    public static void fieldList() {
        if(tokens[currentToken].type.toString().contains(","))  {
            currentToken++;
            if(tokens[currentToken].type.toString().contains("ID")) {
                ((InsertQuery)q).assignFields(tokens[currentToken].data);
                currentToken++;
                fieldList();
            }
            else rejected("Error: Invalid field list syntax");
        } //or empty
    }
    //--------------------------------------------------------------------------
    public static void literal() {
        String token = tokens[currentToken].type.toString();
        if(token.contains("NUMBER") || token.contains("STRLITERAL") || 
           token.contains("DATE"))  {
            aLiteral();
            literalList();
        }
        else rejected("Syntax error on Literal List");
    }
    //--------------------------------------------------------------------------
    public static void aLiteral() {
        if(tokens[currentToken].type.toString().contains("NUMBER")) {
            // do something with the number
            ((InsertQuery)q).assignValues(tokens[currentToken].data);
            currentToken++;
        }
        else if(tokens[currentToken].type.toString().contains("STRLITERAL")) {
            // do something with the number
            ((InsertQuery)q).assignValues(tokens[currentToken].data);
            currentToken++;
        }
        else if(tokens[currentToken].type.toString().contains("DATE")) {
            // do something with the number
            ((InsertQuery)q).assignValues(tokens[currentToken].data);
            currentToken++;
        }
        else rejected("Invalid literal");
    }
    //--------------------------------------------------------------------------
    public static void literalList() {
        if(tokens[currentToken].type.toString().contains("COMMA")) {
            currentToken++;
            aLiteral();
            literalList();
        }// or empty
    }
    //--------------------------------------------------------------------------
    public static void whereClause() {
        if(tokens[currentToken].type.toString().contains("WHERE"))  {
            readingWhere = true;
            currentToken++;
            condition();
        }
        //or empty
    }
    //--------------------------------------------------------------------------
    public static void delete() {
        if(tokens[currentToken].type.toString().contains("DELETE")) {
            currentToken++;
            if(tokens[currentToken].type.toString().contains("FROM")) {
                currentToken++;
                //tablename
                if(tokens[currentToken].type.toString().contains("ID")) {
                    currentToken++;
                    whereClause();
                    if(tokens[currentToken].type.toString().contains("SEMICOLON"))  {
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
        if(tokens[currentToken].type.toString().contains("ID")) {
            field();
            relop();
            FL();
            AndOr();
        }
        else rejected("ERROR: Expected condition in WHERE clause");
    }
    //--------------------------------------------------------------------------
    public static void FL() {
        if(tokens[currentToken].type.toString().contains("ID")) {
            field();
        }
        else aLiteral();
    }
    //--------------------------------------------------------------------------
    public static void AndOr()  {
        if(tokens[currentToken].type.toString().contains("AND"))    {
            currentToken++;
            condition();
        }
        if(tokens[currentToken].type.toString().contains("OR"))    {
            currentToken++;
            condition();
        }
        //or empty
    }
    public static void relop() {
        if(isRelop(tokens[currentToken].data))   {
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
        if(tokens[currentToken].type.toString().contains("TSELECT")) {
            temporal();
        }
        else if(tokens[currentToken].type.toString().contains("SELECT")) {
            normal();
        }
        else rejected("ERROR: Invalid command");
    }
    //--------------------------------------------------------------------------
    public static void normal() {
        if(tokens[currentToken].type.toString().contains("SELECT")) {
            q = new SelectQuery();
            currentToken++;
            columns();
            if(tokens[currentToken].type.toString().contains("FROM")) {
                currentToken++;
                if(tokens[currentToken].type.toString().contains("ID")) {
                    //check the table
                    currentToken++;
                    //optional
                    whereClause();
                }
                else rejected("ERROR: Missing table name");
            }
            else rejected("ERROR: Missing keyword FROM");
        }
        else rejected("ERROR: Invalid command");
    }
    //--------------------------------------------------------------------------
    public static void columns() {
        if(tokens[currentToken].type.toString().contains("ASTK")) {
            ((SelectQuery)q).assignColumns(tokens[currentToken].data);
            currentToken++;
        }
        else if(tokens[currentToken].type.toString().contains("ID")) {
            field();
        }
        else rejected("ERROR: Missing column selection");
    }
    //--------------------------------------------------------------------------
    public static void temporal() {
        if(tokens[currentToken].type.toString().contains("TSELECT")) {
            currentToken++;
            columns();
            if(tokens[currentToken].type.toString().contains("FROM")) {
                currentToken++;
                if(tokens[currentToken].type.toString().contains("ID")) {
                    currentToken++;
                    whereClause();
                    if(tokens[currentToken].type.toString().contains("SEMICOLON")) {
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
        if(tokens[currentToken].type.toString().contains("CONVERT")) {
            currentToken++;
            if(tokens[currentToken].type.toString().contains("XML")) {
                currentToken++;
                xmlFileName();
                xsd();
                if(tokens[currentToken].type.toString().contains("AS")) {
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
        if(tokens[currentToken].type.toString().contains("COMMA")) {
            currentToken++;
            if(tokens[currentToken].type.toString().contains("XSD")) {
                currentToken++;
                xsdFileName();
            }
            else rejected("ERROR: Missing XSD");
        }// or empty
    }
    //--------------------------------------------------------------------------
    public static void xmlFileName() {
        if(tokens[currentToken].type.toString().contains("ID")) {
            currentToken++;
            if(tokens[currentToken].type.toString().contains("DOT")) {
                currentToken++;
                if(tokens[currentToken].type.toString().contains("XML")) {
                    currentToken++;
                }
                else rejected("ERROR: Invalid XML file type");
            }
            else rejected("ERROR: Invalid XML file");
        }
        else rejected("ERROR: Invalid XML file");
    }
    //--------------------------------------------------------------------------
    public static void xsdFileName() {
        if(tokens[currentToken].type.toString().contains("ID")) {
            currentToken++;
            if(tokens[currentToken].type.toString().contains("DOT")) {
                currentToken++;
                if(tokens[currentToken].type.toString().contains("XSD")) {
                    currentToken++;
                }
                else rejected("ERROR: Invalid XSD file type");
            }
            else rejected("ERROR: Invalid XSD file");
        }
        else rejected("ERROR: Invalid XSD file");
    }
    public static void outputFile() {
        if(tokens[currentToken].type.toString().contains("ID")) {
            currentToken++;
            if(tokens[currentToken].type.toString().contains("DOT")) {
                currentToken++;
                if(tokens[currentToken].type.toString().contains("TXT")) {
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
        System.out.println(error);
        isValid = false;
    }
}
