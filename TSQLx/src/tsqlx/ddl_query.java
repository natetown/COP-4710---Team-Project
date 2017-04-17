package tsqlx;
import java.util.*;
public abstract class ddl_query{
   String querytype;
   boolean valid; // is the query valid
   public void ddl_query(String type){
      querytype = type;
      valid = false; // false by default
   }
   public void validate(){
      valid = true; // valid query
   }
   public boolean getValid(){
      return valid;
   }
   public void confirm(){ // confirm status of valid
      System.out.println("The query is good is "+valid);
   }
} // end ddl_query
/**
   createQuery for create table and create database commands
   holds a name and its type. Type can be database or table
*/
class createQuery extends ddl_query{
   String type;
   String name;
   
   ArrayList<field> fields;
   public createQuery(){
      fields = new ArrayList<field>();
   }
   public void addFields(field f){
      fields.add(f);
   }
   public String getName(){
      return name;
   }
   public String getType(){
      return type;
   }
   public void setName(String n){
      name = n;
   } // end setName

   public void setType(String t){
      type = t;
   } // end setName
   public void displayFields(){
      for(int i = 0; i < fields.size(); i++){
         field f = fields.get(i);
         f.display();
      }
   } // end displayFields
   public void display(){
      System.out.println("Create\n"+name + " "+ type);
   } // end display
} // end createQuery

/** 
   dropQuery for drop table and drop database commands
   holds a name and its type. Type can be database or table
*/
class dropQuery extends ddl_query{
   String type;
   String name;
   ArrayList<field> fields;
   public dropQuery(){
      fields = new ArrayList<field>();
   }
   public void addFields(field f){
      fields.add(f);
   }

   public String getName(){
     return name;
   }
   public String getType(){
      return type;
   }
   public void setName(String n){
      name = n;
   } // end setName

   public void setType(String t){
      type = t;
   } // end setName
   public void displayFields(){
      for(int i = 0; i < fields.size(); i++){
         field f = fields.get(i);
         f.display();
      }
   } // end displayFields

   public void display(){
      System.out.println("Drop\n"+name + " "+ type);
   } // end display
   
} // end dropQuery

/** 
   saveQuery holds save database command
*/

class saveQuery extends ddl_query{
   String type;
   String name;
   ArrayList<field> fields;
   public saveQuery(){
      fields = new ArrayList<field>();
   }
   public void addFields(field f){
      fields.add(f);
   }

   public String getName(){
     return name;
   }
   public String getType(){
      return type;
   }
   public void setName(String n){
      name = n;
   } // end setName

   public void setType(String t){
      type = t;
   } // end setName
   public void displayFields(){
      for(int i = 0; i < fields.size(); i++){
         field f = fields.get(i);
         f.display();
      }
   } // end displayFields

   public void display(){
      System.out.println("Save\n"+name + " "+ type);
   } // end display
   
} // end saveQuery
/** 
   loadQuery holds load database command
*/
class loadQuery extends ddl_query{
   String type;
   String name;
   ArrayList<field> fields;
   public loadQuery(){
      fields = new ArrayList<field>();
   }
   public void addFields(field f){
      fields.add(f);
   }

   public String getName(){
     return name;
   }
   public String getType(){
      return type;
   }
   public void setName(String n){
      name = n;
   } // end setName

   public void setType(String t){
      type = t;
   } // end setName
   public void displayFields(){
      for(int i = 0; i < fields.size(); i++){
         field f = fields.get(i);
         f.display();
      }
   } // end displayFields

   public void display(){
      System.out.println("Load\n"+ name + " "+ type);
   } // end display
   
} // end loadQuery

/** 
   field class holds data about attributes (columns)
   name is used for all query types, while type size and n are only for create table
   type holds the attribute type (integer, number, character, date)
   the size holds the maximum size of the attribute 255 by default
   n is whether not null is included with the attribute
*/
class field{ // needs 
   String name;
   String type;
   int size; // default size 255
   boolean n; // not null
   
   public void setName(String n){
      name = n;
   } // end setName
   public void setType(String t){
      type = t;
   }
   public void setSize(int x){
      size = x;
   }
   public void setN(boolean b){
      n = b;
   }
   public void display(){
      System.out.println(name + " "+ type + " "+size+" "+n);
   }
} // end field
