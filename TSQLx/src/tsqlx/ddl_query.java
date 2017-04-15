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
   
   public void setName(String n){
      name = n;
   } // end setName

   public void setType(String t){
      type = t;
   } // end setName
   
} // end createQuery
