/* Implements the LL(1) grammar used to parse DML statements.
   Takes tokens as a string array. Checks for syntax.
   
   Refer to DMLgrammar file for full LL(1) grammar

   Also creates Query objects that describe the query. 
 */
package tsqlx;

/**
 Author: Kevin Poon
 */
import java.io.*;
import java.util.Queue;
import java.util.*;

public class DML {
    static int currentToken;        //index of current token
    static Lexer.Token[] tokens;    //holds tokens from the Lexical Analyzer
    static Query q;                 //generic Query object
    static Comparison c;            //Comparison object for WHERE clause
    static boolean isValid;         //tells if query is valid or not
    static int term;                //indicates which term is being read
    public static int readingWhere; //indicates that the parser has reached the WHERE clause
   
    public static void DMLstart(ArrayList<Lexer.Token> lexer) throws IOException {
        System.out.println("Begin parsing...");
        Lexer.Token line;
        tokens = new Lexer.Token[500];
        //iterates through the token list
        for (int i=0; i<lexer.size(); i++) {
            line = lexer.get(i);
            tokens[i] = line;
        }
       
        //for now, only accepting 1 query at a time
        readingWhere = 0;
        command();
        //commandList();
        isValid = true;
        
        /* ----------------------- TESTS ----------------------------
        // TEST SELECT QUERIES
        System.out.println("Testing SELECT query object:");
        System.out.println("Query type: " + q.queryType);
        System.out.println("Table name: " + q.tablename);
        ((SelectQuery)q).displayColumns();
        ((SelectQuery)q).displayCondtions();
        ((SelectQuery)q).displayLogicals();
        */
        
        
        /*
        // TEST INSERT QUERIES
        System.out.println("Testing INSERT query object:");
        System.out.println("Query type: " + q.queryType);
        System.out.println("Table name: " + q.tablename);
        System.out.println("Field: "+((InsertQuery)q).fields.get(0));
        System.out.println("Field: "+((InsertQuery)q).fields.get(1));
        System.out.println("Value: "+((InsertQuery)q).values.get(0));
        System.out.println("Value: "+((InsertQuery)q).values.get(1));
        */
       
        /*
        // TEST DELETE QUERIES
        System.out.println("Testing DELETE query object:");
        System.out.println("Query type: " + q.queryType);
        System.out.println("Table name: " + q.tablename);
        ((DeleteQuery)q).displayCondtions();
        ((DeleteQuery)q).displayLogicals();
        */
        
        /*
        // TEST CONVERT QUERIES
        System.out.println("Testing CONVERT query object:");
        System.out.println("Query type: " + q.queryType);
        System.out.println("XML file: " + ((ConvertQuery)q).xmlFileName);
        System.out.println("XSD file: " + ((ConvertQuery)q).xsdFileName);
        System.out.println("Output file: " + ((ConvertQuery)q).outFileName);
        */
        
        System.out.println("ACCEPTED");
        //do something with query
        
        
        
    }
    public static void commandList()  {
        //if we allow multiple SQL queries at once, use this method
        if(tokens[currentToken]!= null) {
            command();
            commandList();
        }
    }
    public static void command()    {
        //calls corresponding query type
        System.out.println("Invoked Command");
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
        System.out.println("field1");
        if((readingWhere < 1) && tokens[currentToken].type.toString().contains("ID")) {
            //add field to query
            System.out.println("check type");
            if(q.queryType.equals("INSERT")) {
                System.out.println("it's an INSERT");
                ((InsertQuery)q).assignFields(tokens[currentToken].data);
            }
            else if(q.queryType.equals("SELECT") || q.queryType.equals("TSELECT")) {
                System.out.println("it's a select");
                ((SelectQuery)q).assignColumns(tokens[currentToken].data);
            }
            
            currentToken++;
            fieldList();
        }
        else if((readingWhere>0) && tokens[currentToken].type.toString().contains("ID")) {
            if(q.queryType.equals("SELECT") || q.queryType.equals("TSELECT")) {
                System.out.println("Adding WHERE clause field: " + tokens[currentToken].data);
                //((SelectQuery)q).assignColumns(tokens[currentToken].data);
                if(term == 1)    {
                    c.assignT1(tokens[currentToken].data);
                }
                else    {
                    c.assignT2(tokens[currentToken].data);
                }
                currentToken++;
            }
            else if(q.queryType.equals("DELETE")) {
                System.out.println("it's a delete");
                System.out.println("Adding WHERE clause field: " + tokens[currentToken].data);
                ((DeleteQuery)q).assignWhere(tokens[currentToken].data);
                if(term == 1)    {
                    c.assignT1(tokens[currentToken].data);
                }
                else    {
                    c.assignT2(tokens[currentToken].data);
                }
                currentToken++;
            }
        }
        else rejected("ERROR: Missing field" + tokens[currentToken].type.toString());
    }
    //--------------------------------------------------------------------------
    public static void fieldList() {
        if(tokens[currentToken].type.toString().contains("COMMA"))  {
            currentToken++;
            if(tokens[currentToken].type.toString().contains("ID")) {
                if(q.queryType.contains("INSERT")) {
                    ((InsertQuery)q).assignFields(tokens[currentToken].data);
                    currentToken++;
                    fieldList();
                }
                if(q.queryType.contains("SELECT")||q.queryType.contains("TSELECT")) {
                    ((SelectQuery)q).assignColumns(tokens[currentToken].data);
                    currentToken++;
                    fieldList();
                }
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
        if(q.queryType.contains("SELECT")) {
            if(tokens[currentToken].type.toString().contains("NUMBER")) {
                // do something with the number
                ((SelectQuery)q).assignWhere(tokens[currentToken].data);
                c.assignT2(tokens[currentToken].data);
                currentToken++;
            }
            else if(tokens[currentToken].type.toString().contains("STRLITERAL")) {
                // do something with the number
                ((SelectQuery)q).assignWhere(tokens[currentToken].data);
                c.assignT2(tokens[currentToken].data);
                currentToken++;
            }
            else if(tokens[currentToken].type.toString().contains("DATE")) {
                // do something with the number
                ((SelectQuery)q).assignWhere(tokens[currentToken].data);
                c.assignT2(tokens[currentToken].data);
                currentToken++;
            }
            else rejected("Invalid literal");
        }
        else if(q.queryType.contains("INSERT")) {
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
        else if(q.queryType.contains("DELETE")) {
            if(tokens[currentToken].type.toString().contains("NUMBER")) {
                // do something with the number
                c.assignT2(tokens[currentToken].data);
                currentToken++;
            }
            else if(tokens[currentToken].type.toString().contains("STRLITERAL")) {
                c.assignT2(tokens[currentToken].data);
                currentToken++;
            }
            else if(tokens[currentToken].type.toString().contains("DATE")) {
                c.assignT2(tokens[currentToken].data);
                currentToken++;
            }
            else rejected("Invalid literal");
        }
        
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
            //c = new Comparison();
            readingWhere++;
            currentToken++;
            condition();
        }
        //or empty
    }
    //--------------------------------------------------------------------------
    public static void delete() {
        if(tokens[currentToken].type.toString().contains("DELETE")) {
            System.out.println("Creating DELETE query");
            q = new DeleteQuery();
            currentToken++;
            if(tokens[currentToken].type.toString().contains("FROM")) {
                currentToken++;
                //tablename
                if(tokens[currentToken].type.toString().contains("ID")) {
                    q.assignTN(tokens[currentToken].data);
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
            c = new Comparison();
            term = 1; //reading 1st term in a comparison
            field();
            relop();
            term = 2; //reading 2nd term
            FL();
            term = 1; //reset to 1st term to prepare for loop around
            if(q.queryType.equals("SELECT")||q.queryType.equals("TSELECT")) {
                ((SelectQuery)q).assignCondition(c);
            }
            else if(q.queryType.equals("DELETE")) {
                ((DeleteQuery)q).assignCondition(c);
            }
            AndOr();
        }
        else rejected("ERROR: Expected condition in WHERE clause");
    }
    //--------------------------------------------------------------------------
    public static void FL() {
        //Field or Literal
        if(tokens[currentToken].type.toString().contains("ID")) {
            field();
        }
        else aLiteral();
    }
    //--------------------------------------------------------------------------
    public static void AndOr()  {
        //accepts both SELECT and TSELECT
        if(q.queryType.contains("SELECT"))  {
            if(tokens[currentToken].data.contains("AND"))    {
                System.out.println("Invoked AndOr");
                ((SelectQuery)q).assignLogicals("AND");
                currentToken++;
                condition();
            }
            if(tokens[currentToken].data.contains("OR"))    {
                ((SelectQuery)q).assignLogicals("OR");
                currentToken++;
                condition();
            }
        }
        if(q.queryType.contains("DELETE"))  {
            if(tokens[currentToken].data.contains("AND"))    {
                ((DeleteQuery)q).assignLogicals("AND");
                currentToken++;
                condition();
            }
            if(tokens[currentToken].data.contains("OR"))    {
                ((DeleteQuery)q).assignLogicals("OR");
                currentToken++;
                condition();
            }
        }
        //or empty
    }
    public static void relop() {
        System.out.println("INVOKED RELOP: " + tokens[currentToken].data);
        //this if statement will work for both SELECT and TSELECT
        if(q.queryType.contains("SELECT")) {
            if(tokens[currentToken].type.toString().contains("RELOP"))   {
                ((SelectQuery)q).assignRelops(tokens[currentToken].data);
                c.assignRelop(tokens[currentToken].data);
                currentToken++;
            }
            else rejected("ERROR: Missing relop");
        }
        else if(q.queryType.equals("DELETE")) {
            if(tokens[currentToken].type.toString().contains("RELOP"))   {
                ((DeleteQuery)q).assignRelops(tokens[currentToken].data);
                c.assignRelop(tokens[currentToken].data);
                currentToken++;
            }
            else rejected("ERROR: Missing relop");
        }
        else rejected("ERROR: WHERE clause not allowed");
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
        if(tokens[currentToken].type.toString().equals("TSELECT")) {
            temporal();
        }
        else if(tokens[currentToken].type.toString().equals("SELECT")) {
            normal();
        }
        else rejected("ERROR: Invalid command");
    }
    //--------------------------------------------------------------------------
    public static void normal() {
        if(tokens[currentToken].type.toString().contains("SELECT")) {
            System.out.println("Creating SELECT query");
            q = new SelectQuery();
            System.out.println(q.queryType);
            currentToken++;
            columns();
            if(tokens[currentToken].type.toString().contains("FROM")) {
                currentToken++;
                if(tokens[currentToken].type.toString().contains("ID")) {
                    //check the table
                    q.assignTN(tokens[currentToken].data);
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
        //columns to be selected
        if(tokens[currentToken].type.toString().contains("ASTK")) {
            ((SelectQuery)q).assignColumns(tokens[currentToken].data);
            currentToken++;
        }
        else if(tokens[currentToken].type.toString().contains("LPAREN")) {
            currentToken++;
            field();
            if(tokens[currentToken].type.toString().contains("RPAREN")) {
                currentToken++;
            }
            else rejected("ERROR: Missing closing parentheses");
        }
        else rejected("ERROR: Missing column selection");
    }
    //--------------------------------------------------------------------------
    public static void temporal() {
        //nearly the same format as normal select
        if(tokens[currentToken].type.toString().contains("TSELECT")) {
            q = new TSelectQuery();
            currentToken++;
            columns();
            if(tokens[currentToken].type.toString().contains("FROM")) {
                currentToken++;
                if(tokens[currentToken].type.toString().contains("ID")) {
                    ((TSelectQuery)q).assignTN(tokens[currentToken].data);
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
        //placeholder
    }
    //--------------------------------------------------------------------------
    public static void convert() {
        if(tokens[currentToken].type.toString().contains("CONVERT")) {
            //create CONVERT query object
            q = new ConvertQuery();
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
                    //assign XML filename to query
                    ((ConvertQuery)q).assignXML(tokens[currentToken-2].data);
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
                    //assign XSD filename to query
                    ((ConvertQuery)q).assignXSD(tokens[currentToken-2].data);
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
                    //assign XML filename to query
                    ((ConvertQuery)q).assignOutFile(tokens[currentToken-2].data);
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
