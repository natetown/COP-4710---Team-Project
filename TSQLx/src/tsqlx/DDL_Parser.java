// DDL Parser
import java.util.*;
import java.io.*;
/**
This program parses a DDL statement
*/
class DDL_Parser{

   static String[] lines = new String[50];
   static String[] tokens = new String[100];
   static int P = 0;
   static int count = 0;
   public static void main(String args[]) throws IOException{ // only for testing
      if(args.length > 0){
         File file = new File(args[0]);
         parse(file);
      }
   } // end main
   
   public static void parse(File f) throws IOException{ 
         System.out.println("Start Parse");
         File file = f;
         Scanner reader = new Scanner(file);
         String s = null;
       //  String[] lines = new String[50]; // hold lines
         int j = 0;
         while(reader.hasNextLine() == true){ // read the entire file
            if(reader.hasNext() == true){
               s = reader.nextLine();
              // System.out.println(s);
               lines[j] = s;
               j++;
               
            } // end if
         } // end while
         for(int i = 0; i < j; i++){
          //  System.out.print(lines[i] + " ");
            if(lines[i].contains("-->")){
           //    System.out.print("hello\n");
               makeToken(lines[i]);
            }
         }
    //     printTokens();
         P = 0;
         if(stmt(tokens[P]) == true){
         
            System.out.println("Accept");
         }
         else{
            System.out.println("Reject");
         
         }
   } // end parse
   public static void makeToken(String s) throws IOException{
      int i;
      for(i = 0; i < s.length(); i++)
      {
         if(s.charAt(i) == '-')
         {
            break;
         }
         
      }
      String r = s.substring(0, i - 1); // i - 1 to remove space between closing character and '-'
      tokens[P] = r;
      P++;
   } // end makeToken
   
   public static void printTokens() throws IOException{
      System.out.println("Tokens");
      for(int i = 0; i < tokens.length; i++)
      {
         if(tokens[i] != null){
            System.out.println(tokens[i]);
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
   public static boolean stmtA(String s) throws IOException{ // determine if create or drop stmt
      System.out.println("stmtA "+ s);
      if(s.equals("CREATE")){
         P++;
         if(cstmt(tokens[P]))
         {
            return true;
         }
         else if(s.equals("DROP"))
         {
            P++;
            if(cstmt(tokens[P]))
            {
               return true;
            }
            else{
               return false;
            }


         }
         else {return false;}   
      }
      else{
            return false;
         }

   } // end stmtA
   public static boolean cstmt(String s) throws IOException{ // determine if cstmt is true
      System.out.println("cstmt "+ s);
      if(s.equals("DATABASE")){ // create database command
         P++;
         if(tokens[P].equals("ID")){
            P++;
            if(tokens[P].equals("SEMICOLON")){ // completed database creation
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
      
      else if(s.equals("TABLE")){ // create table command
         P++;
         if(tokens[P].equals("ID")){
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
   
   public static boolean tdec(String s) throws IOException{ // check that tdec is correct
      System.out.println("tdec "+ s);
      if(s.equals("LPAREN")){
         P++;
         if(fieldlist(tokens[P])){ // list of all fields
               if(tokens[P].equals("RPAREN")){ // must have a closing paren
                  System.out.println(tokens[P] + " bye");
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
      if(s.equals("ID"))
      {
         P++;
         if(fieldtype(tokens[P])){
            P++;
            if(nullA(tokens[P]))
            {
               System.out.println("field is okay");
               count++;
               System.out.println(count);
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
   
   public static boolean fieldtype(String s) throws IOException{
      System.out.println("fieldtype "+s);
      if(s.equals("STRING")){
         P++;
         if(tokens[P].equals("LPAREN")){
            P++;
            if(tokens[P].equals("NUMBER")){
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

      } // end if STRING
      else if(s.equals("INT")){ // INT for an integer, no decimal
         P++;
         if(tokens[P].equals("LPAREN")){
            if(iLen(tokens[P])){
               System.out.println("iLen resolved "+tokens[P]);
               return true;
            }
            else{
               return false;
            }
         } // end if LPAREN
         else if(tokens[P].equals("COMMA")/* || tokens[P].equals("RPAREN")*/){ // no length
            P--;
            return true;
         }
         else if(tokens[P].equals("RPAREN")){
            return true;
         }
          else if(tokens[P].equals("NOT NULL")){
            System.out.println("test for not null");
            if(nullA(tokens[P])){
               return true;
            }
            else{
               return false;
            }
         }
         else{
               System.out.println("failed int type");
               return false;
            }

      } // end if INT
      else if(s.equals("DEC")){ // DEC for a decimal number
         P++;
         if(tokens[P].equals("LPAREN")){
            if(dLen(tokens[P])){
               System.out.println("dLen resolved "+tokens[P]);
               return true;
            }
            else{
               return false;
            }
         } // end if LPAREN
         else if(tokens[P].equals("COMMA")/* || tokens[P].equals("RPAREN")*/){ // no length
            P--;
            return true;
         }
         else if(tokens[P].equals("RPAREN")){
            return true;
         }
          else if(tokens[P].equals("NOT NULL")){
            System.out.println("test for not null");
            if(nullA(tokens[P])){
               return true;
            }
            else{
               return false;
            }
         }
         else{
               System.out.println("failed dec type");
               return false;
            }

        /* P++;
         if(tokens[P].equals("LPAREN")){
            if(dLen(tokens[P])){
               return true;
            }
            else{
               return false;
            }
         } // end if LPAREN
         else if(tokens[P].equals("COMMA") || tokens[P].equals("RPAREN")){ // no length
            return true;
         }
         
                  else{
               return false;
            }*/

      }
      else if(s.equals("DATE")){
         P++;
         if(date(tokens[P])){
            return true;
         }   
         else{
            System.out.println("Error date is wrong "+ tokens[P]);
            return false;
         }      
      } // end DATE


      else{ // reject 
            return false;
         }

   } // end fieldtype
   
   public static boolean iLen(String s) throws IOException{ // optional extension for integer length
      System.out.println("iLen "+ s);
      if(s.equals("LPAREN")){
         P++;
            if(tokens[P].equals("NUMBER")){
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
               System.out.println("Fail iLen");
               return false;
            }
   } // end iLen
   
   public static boolean dLen(String s) throws IOException { // decimal length
      System.out.println("dLen "+ s);
      if(tokens[P].equals("LPAREN")){
         P++;
            if(tokens[P].equals("NUMBER")){
               P++;
               if(dLenA(tokens[P])){
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

   } // end dLen 
   
   public static boolean dLenA(String s) throws IOException{
      System.out.println("dLenA "+s);
      if(s.equals("RPAREN")){
         return true;
      }
      else if(s.equals("COMMA")){
         P++;
         if(tokens[P].equals("NUMBER")){
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
   } // end dLenA
   
   public static boolean date(String s){
      System.out.println("date" + s);
      if(s.equals("LPAREN")){
         P++;
         if(tokens[P].equals("NUMBER")){
            P++;
            if(tokens[P].equals("SLASH")){
               P++;
               if(tokens[P].equals("NUMBER")){
               P++;
                  if(tokens[P].equals("SLASH")){
                     P++;
                     if(tokens[P].equals("NUMBER")){
                     //   if(year(tokens[P]) == true)
                      //  {
                           P++;
                           if(tokens[P].equals("RPAREN")){
                              System.out.println(tokens[P] + " bye");
                              return true;
                              }
                        
                          /* else{
                              return false;
                           }
                        }*/
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

            }
            else{
               return false;
            }

         }
         else{
            return false;
         }

      } // end if
      else{
         return false;
      }
   } // end date
   
   public static boolean year(String s) throws IOException{ // check year length
   
      if(s.length() == 2 || s.length() == 4){
         return true;
      }
      else{
         return false;
      }
   } // end year
   
   public static boolean nullA(String s) throws IOException{
      System.out.println("nullA "+s);
      if(s.equals("NOT NULL") && tokens[P+1].equals("RPAREN")){
         P++;
         return true;
      }
      else if(s.equals("NOT NULL") && !tokens[P+1].equals("RPAREN")){
         return true;
      }
      else if(s.equals("COMMA") || s.equals("RPAREN")){
      //   P++;
         return true;
      }
      else{
            System.out.println("Error, nullA");
            return false;
         }

   } // end nullA
   
} // end DDL_Parser