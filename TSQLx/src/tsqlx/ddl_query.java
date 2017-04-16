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
   public void setName(String n){
      name = n;
   } // end setName

   public void setType(String t){
      type = t;
   } // end setName
      
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
