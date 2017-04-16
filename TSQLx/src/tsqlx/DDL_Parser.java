// DDL Parser
package tsqlx;
import java.util.*;
import java.io.*;
/**
This program parses a DDL statement
*/
class DDL_Parser{

 //  static String[] lines = new String[50];
   static String[] tokens = new String[100]; // holds the types for each token for parsing
   static String[] values = new String[100]; // holds the values for each token 
   static int P = 0; // position in the parsing process
   static int count = 0; // number of attributes
   static boolean error = false; // semantic error false if no error
   static ddl_query q; // the query to be filled
   /** 
      arList_To_Array fill the tokens and value arrays from the array list provided
   */
   public static void arList_To_Array(ArrayList<Lexer.Token> mytokens, String[] arrayTknType, String[] arrayLexeme){ 
   int i; 
   for(i=0;i < mytokens.size(); i++){
      arrayTknType[i] = mytokens.get(i).type.toString(); 
      arrayLexeme[i] = mytokens.get(i).data; }
   } // end arrayList to Array tranform
   /**
      parse void method that tests the inputed line is syntaxicly correct
      it also assigns query values and the query type
      includes error messages
   */
   public static void parse(ArrayList<Lexer.Token> lexer) throws IOException{ 
        // System.out.println("Start Parse");
         arList_To_Array(lexer, tokens, values);
         printTokens(); // test that the two methods are correct by printing their contents
         P = 0; // reset P
         if(stmt(tokens[P]) == true && error == false){ // statement is correct
            System.out.println("Accept"); // print accept message
           // ((createQuery)q).display();
            if(tokens[0] == "CREATE"){
               ((createQuery)q).display();
               if(((createQuery)q).getType().equals("TABLE")){
                  ((createQuery)q).displayFields();
               }
            } // end if create query
            else if(tokens[0].equals("DROP")){
               ((dropQuery)q).display();
               ((dropQuery)q).displayFields();
            }
            else if(tokens[0].equals("SAVE")){
               ((saveQuery)q).display();
               ((saveQuery)q).displayFields();
            }
            else if(tokens[0].equals("LOAD")){
               ((loadQuery)q).display();
               ((loadQuery)q).displayFields();
            }
         } // end if stmt is accepted
         else{
            System.out.println("Reject");
         
         }
   } // end parse
   
   public static void printTokens(){ // print the values of the tokens and values array for error handling
      System.out.println("Tokens and Values");
      for(int i = 0; i < tokens.length; i++)
      {
         if(tokens[i] != null){
            System.out.println(i + " Token "+ tokens[i] + " Value "+ values[i]);
         }   
      }
   } // end printTokens
   
   /** stmt  
      Parse via Recursive Descent using the tokens array 
      if stmt returns true the program is correct
      if stmt returns false the program is incorrect
   */
   
   public static boolean stmt(String s) throws IOException{ // if stmt if syntaxicly correct, return true
      if(stmtA(s)){
      return true;
      }
      else{
         return false;
      }
   } // end stmt
   public static boolean stmtA(String s) throws IOException{ // determine stmt type
    //  System.out.println("stmtA "+ s);
      if(s.equals("CREATE")){ // create stmt
         q = new createQuery(); // make create query
         P++;
         if(cstmt(tokens[P]))
         {
            return true;
         }
         else{
            return false;
         }
      } // end if create
      else if(s.equals("DROP")){ // drop stmt
         q = new dropQuery(); // make drop query
         P++;
         if(dstmt(tokens[P]))
         {
            return true;
         }
         else{
            return false;
         }
      } // end if drop
      else if(s.equals("SAVE")){ // save stmt
         q = new saveQuery(); // make save query
         P++;
         if(sstmt(tokens[P])){
            return true;
         }
         else{
            return false;
         }

      } // end save
      else if(s.equals("LOAD")){ // load stmt
         q = new loadQuery(); // make load query
         P++;
         if(lstmt(tokens[P])){
            return true;
         }
         else{
            return false;
         }

      } // end load
      else{
            return false;
         }

   } // end stmtA
   public static boolean cstmt(String s) throws IOException{ // determine if cstmt is true
     // System.out.println("cstmt "+ s);
      ((createQuery)q).setType(s); // table or database
      if(s.equals("DATABASE")){ // create database command
         P++;
         if(tokens[P].equals("ID")){
            ((createQuery)q).setName(values[P]); // assign name of table or database
            P++;
            if(tokens[P] == null){ // missing semicolon error
               System.out.println("Error missing semicolon");
               return false;
            }
            if(tokens[P].equals("SEMICOLON")){ // completed database creation
               return true;
            }
            else{ // does not end with semicolon
               System.out.println("Error does not end with semicolon");
               return false;
            }
         }
         else{
            return false;
         }
      } // end if DATABASE
      
      else if(s.equals("TABLE")){ // create table command
     //    ((createQuery)q).setType(s);
       //  System.out.println(((createQuery)q).getType());
         P++;
         if(tokens[P].equals("ID")){
            ((createQuery)q).setName(values[P]);
             System.out.println(((createQuery)q).getName());
            P++;
            if(tdec(tokens[P])){ // if tdec returns true good table declaration
               System.out.println("is it good? "+ tokens[P]);
               if(tokens[P] == null){
                  System.out.println("Error no semicolon");
                  return false;
               }
               else if(tokens[P].equals("SEMICOLON")){
                  return true;
               }
               else{
                  return false;
               }
      
            }
            // otherwise the stmt is false and is rejected
            else{
               return false;
            }
         }
         else{
            return false;
         }

      }
      else{
         return false;
      }
   } // end cstmt
   
   public static boolean dstmt(String s) throws IOException{
      ((dropQuery)q).setType(s);
      field f = new field();
      if(s.equals("DATABASE")){ // create database command
         P++;
         if(tokens[P].equals("ID")){
            System.out.println(values[P]);
            ((dropQuery)q).setName(values[P]);
           // field f = new field();
            f.setName(values[P]);
            System.out.println(((dropQuery)q).getName());
            P++;
            if(tokens[P] == null){
               System.out.println("Error missing semicolon");
                  return false;
            }
            if(tokens[P].equals("SEMICOLON")){ // completed database creation
               ((dropQuery)q).addFields(f);
               return true;
            }
            else{
               return false;
            }
         }
         else{
            return false;
         }
      } // end if DATABASE
      else if(s.equals("TABLE")){
         P++;
         if(tokens[P].equals("ID")){
           ((dropQuery)q).setName(values[P]);
            f.setName(values[P]);
            P++;
            if(tokens[P] == null){
               System.out.println("Error missing semicolon");
               return false;
            }

            if(tokens[P].equals("SEMICOLON")){
               ((dropQuery)q).addFields(f);
               return true;
            }
            else{
               return false;
            }
         }
         else{
            return false;
         }
      } // end if table
      else{ // reject
         return false;
      }
   } // end dstmt
   
   public static boolean sstmt(String s) throws IOException{ // save stmt
      field f = new field();
      ((saveQuery)q).setType(s);
      if(s.equals("DATABASE")){
         P++;
         if(tokens[P].equals("ID")){
            ((saveQuery)q).setName(values[P]);
            f.setName(values[P]);
            P++;
            if(tokens[P] == null){
               System.out.println("Error missing semicolon");
               return false;
            }
            if(tokens[P].equals("SEMICOLON")){
               ((saveQuery)q).addFields(f);
               return true;
            }
            else{
               return false;
            }
         }
         else{
               return false;
            }
      }
      else{
               return false;
            }
   } // end sstmt
   
   public static boolean lstmt(String s) throws IOException{ // load stmt
      field f = new field();
      ((loadQuery)q).setType(s);
      if(s.equals("DATABASE")){
         P++;
         if(tokens[P].equals("ID")){
            ((loadQuery)q).setName(values[P]);
            f.setName(values[P]);
            P++;
            if(tokens[P] == null){
               System.out.println("Error missing semicolon");
               return false;
            }
            if(tokens[P].equals("SEMICOLON")){
               ((loadQuery)q).addFields(f);
               return true;
            }
            else{
               return false;
            }
         }
         else{
               return false;
            }
      }
      else{
               return false;
            }
   } // end lstmt

   
   public static boolean tdec(String s) throws IOException{ // check that tdec is correct
      System.out.println("tdec "+ s);
      if(s.equals("LPAREN")){
         P++;
         if(fieldlist(tokens[P])){ // list of all fields
               if(tokens[P].equals("RPAREN")){ // must have a closing paren
               //   System.out.println(tokens[P] + " bye");
                  P++;
                  return true;
               }
               else{
                  return false;
               }

         }
         else{
            return false;
         }
      }
      else{
            return false;
         }

   } // end tdec
   
   public static boolean fieldlist(String s) throws IOException{
      System.out.println("fieldlist "+ s);
      if(s.equals("ID")){    
         if(field(tokens[P])){
            if(fieldlistA(tokens[P])){
               System.out.println("good fieldlist "+tokens[P]);
               System.out.println(tokens[P]);
               return true;
            }
            else{
               return false;
            }

         }
         else{
               return false;
            }

      }
      else{
            return false;
         }

   } // end fieldlist
   
   public static boolean fieldlistA(String s) throws IOException{
      System.out.println("fieldlistA "+ s);
      if(s.equals("RPAREN")){
         System.out.println("FieldlistA complete");
         return true;
      }
      
      else if(s.equals("COMMA")){
         P++;
         if(field(tokens[P])){
            if(fieldlistA(tokens[P])){
               return true;
            }
            else{
               System.out.println("fieldlistA fail "+ tokens[P]);
               return false;
            }
            
         }
         else{
            return false;
         }

      }
      else{
         return false;
      }
   } // end fieldlistA*/
   
   public static boolean field(String s) throws IOException{
      System.out.println("field "+s);
      field f = new field();
      if(s.equals("ID"))
      {
         f.setName(values[P]);
         P++;
         if(fieldtype(tokens[P], f)){
            if(nullA(tokens[P], f))
            {
               System.out.println("field is okay");
               f.display();
               ((createQuery)q).addFields(f);
              // System.out.println(((createQuery)q).get(0));
               count++;
              // System.out.println(count);
             //  P++;
               return true;
            }
            else{
               System.out.println("error in nullA from field");
               return false;
            }

         }
         else{
            return false;
         }

      }
      else{
         return false;
      }

   } // end field
   
   public static boolean fieldtype(String s, field f) throws IOException{
      System.out.println("fieldtype "+s);
      if(s.equals("STRING")){
         f.setType("character");
         P++;
         if(tokens[P].equals("LPAREN")){
            P++;
            if(tokens[P].equals("NUMBER")){
               if(isInteger(values[P])){
                  f.setSize(Integer.parseInt(values[P]));
                  P++;
                  if(tokens[P].equals("RPAREN")){
                     P++;
                     return true;
                  }
                  else{
                     return false;
               }
            }
               P++;
               if(tokens[P].equals("RPAREN")){
                  P++;
                  return true;
               }
               else{
                  return false;
            }
 
            }
            else{
               return false;
            }

         }
         else{
               return false;
            }

      } // end if STRING
      else if(s.equals("INTEGER")){ // INT for an integer, no decimal
         f.setType(s);
         P++;
         if(tokens[P].equals("LPAREN")){
            if(iLen(tokens[P], f)){
               System.out.println("iLen resolved "+tokens[P]);
               return true;
            }
            else{
               return false;
            }
         } // end if LPAREN
         else if(tokens[P].equals("COMMA")){ // no length
            f.setSize(255); // 255 for no set length
            return true;
         }
         else if(tokens[P].equals("RPAREN")){
            f.setSize(255); // 255 for no set length
            return true;
         }
          else if(tokens[P].equals("NOT NULL")){
            f.setSize(255); // 255 for no set length
            return true;
         }
         else{
               System.out.println("failed int type");
               return false;
            }

      } // end if INT
      else if(s.equals("DEC")){ // DEC for a decimal number
         f.setType(s);
         P++;
         if(tokens[P].equals("LPAREN")){
            if(dLen(tokens[P], f)){
               System.out.println("dLen resolved "+tokens[P]);
               return true;
            }
            else{
               return false;
            }
         } // end if LPAREN
         else if(tokens[P].equals("COMMA")){ // no length
            f.setSize(255); // 255 for no set length
            return true;
         }
         else if(tokens[P].equals("RPAREN")){
            f.setSize(255); // 255 for no set length
            return true;
         }
          else if(tokens[P].equals("NOT NULL")){
            f.setSize(255); // 255 for no set length
            return true;
         }
         else{
               System.out.println("failed dec type");
               return false;
            }

      } // end Dec
      else if(s.equals("DATETYPE")){ // date type
         f.setType(s);
         P++;
         if(date(tokens[P])){
            return true;
         }   
         else{
        //    System.out.println("Error date is wrong "+ tokens[P]);
            return false;
         }      
      } // end DATE


      else{ // reject 
            return false;
         }

   } // end fieldtype
   
   public static boolean iLen(String s, field f) throws IOException{ // optional extension for integer length
      System.out.println("iLen "+ s);
      if(s.equals("LPAREN")){
         P++;
            if(tokens[P].equals("NUMBER")){
               if(isInteger(values[P]))
               {
                  f.setSize(Integer.parseInt(values[P]));
                  P++;
                  if(tokens[P].equals("RPAREN")){
                     P++;
                     return true;
                  }
                  else{
                     System.out.println("Error missing )");
                     return false;
                  }
            }
            else{
               return false;
            }
          } // end if number
            else{
               System.out.println("Error number needed but not given");
               return false;
            }

         }
         else{
               System.out.println("Error length must begin with a (");
               return false;
            }
   } // end iLen
   
   public static boolean dLen(String s, field f) throws IOException { // decimal length
      System.out.println("dLen "+ s);
      if(tokens[P].equals("LPAREN")){
         P++;
            if(tokens[P].equals("NUMBER")){
               if(isInteger(values[P])){
                  f.setSize(Integer.parseInt(values[P]));
                  P++;
                  if(dLenA(tokens[P])){
                     if(tokens[P].equals("RPAREN")){
                        P++;
                        return true;
                     }
                     else{
                        System.out.println("Error missing )");
                        return false;
                     }
                  }
                  else{
                     System.out.println("Error decimal number syntax incorrect");
                     return false;
                  }
             }
             else{
               return false;
             }
           } // end if number
            else{
               System.out.println("Error number needed");
               return false;
            }

         }
         else{
               return false;
            }

   } // end dLen 
   
   public static boolean dLenA(String s) throws IOException{
      System.out.println("dLenA "+s);
      if(s.equals("RPAREN")){
         return true;
      }
      else if(s.equals("COMMA")){
         P++;
         if(tokens[P].equals("NUMBER")){
            if(isInteger(values[P])){
               P++;
               if(tokens[P].equals("RPAREN")){
                  return true;
               }
               else{
                  return false;
               }
         }
         else{
            return false;
         }
      }
      else{
         return false;
      }
    }
      else{
         return false;
      }
   } // end dLenA
   
   public static boolean date(String s){
      System.out.println("date " + s);
      if(s.equals("LPAREN")){
         P++;
         if(tokens[P].equals("DATE")){
            P++;
            if(tokens[P].equals("RPAREN")){
               P++;
               return true;
            }
            else{
               System.out.println("Error missing )");
               return false;
            }
         }
         else{
            System.out.println("Error date is wrong");
            return false;
         }
      } // end if
      else{
         System.out.println("Error missing (");
         return false;
      }
   } // end date
      
   public static boolean nullA(String s, field f) throws IOException{
      System.out.println("nullA "+s);
      if(s.equals("NOT NULL")){
         f.setN(true);
         P++;
         return true;
      }
      else if(s.equals("COMMA") || s.equals("RPAREN") || s.equals("SEMICOLON")){
         f.setN(false);
         return true;
      }
      else{
            System.out.println("Error with nullA");
            return false;
         }

   } // end nullA
   
   public static boolean isInteger(String s) throws IOException{
      System.out.println("check for int "+s);
       for(int i = 0; i < s.length(); i++){
         if(!Character.isDigit(s.charAt(i))){
            //return false;
            error = true;
            break;
         }
       }
       try 
       { 
           Integer.parseInt(s); 
       } 
       catch(NumberFormatException e) 
       { 
           return false; 
       } 
       catch(NullPointerException e) 
       {
           return false;
       }
       // only got here if we didn't return false
       return true;

   } // end isInteger
   
} // end DDL_Parser