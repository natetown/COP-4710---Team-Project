package tsqlx;
import java.util.*;
public abstract class ddl_query{
   String querytype;
   public void ddl_query(String type){
      querytype = type;
   }
} // end ddl_query

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
  /* public field getField(int x){
      return fields.get(x);
   }*/
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
    //  String n = toString(name);
     // String t = toString(type);
      System.out.println("Create\n"+name + " "+ type);
   } // end display
} // end createQuery

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
