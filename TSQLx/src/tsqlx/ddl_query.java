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
      System.out.println(name + " "+ type);
   } // end display
} // end createQuery

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
