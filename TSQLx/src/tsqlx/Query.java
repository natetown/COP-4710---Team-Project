/* Author(s): Kevin Poon
*/
package tsqlx;

/*
These classes represent the types of DML queries that the parser reads. 
The parser will use these methods to create query objects that will be passed
to another method that will execute the queries based on which fields are
present in the object. 

 */
import java.util.*;

public class Query {
    String queryType;
    String tablename;
    
    public void Query(String type) {
        queryType = type;
    }
    public void assignTN(String tbl)    {
        //specifies which table to interact with
        tablename = tbl;
    }
    public static void main(String[] args)   {
        //testing
        InsertQuery q = new InsertQuery();
        q.assignTN("mytable");
        q.assignFields("field1");
        q.assignValues("abc");
        q.testQ(q);
    }
    public void testQ(Query q)  {
        System.out.println(q.queryType);
    }
    
}//end Query

class InsertQuery extends Query  {
    //list of fields and values to manipulate
    ArrayList<String> fields = new ArrayList<String>();
    ArrayList<String> values = new ArrayList<String>();
    
    public void InsertQuery()   {
        queryType = "INSERT";
    }
    public void assignFields(String field) {
        //optional, specifies which fields to insert into
        fields.add(field);
    }
    public void assignValues(String val)   {
        values.add(val);
    }
}//end InsertQuery

class SelectQuery extends Query {
    //list of columns to be selected
    List<String> columns;
    boolean selectAll;
    
    public void SelectQuery()   {
        queryType = "SELECT";
    }
    public void assignColumns(String col)   {
        //looks for simple select
        if(col.contains("ASTK") || col.contains("*")) {
            selectAll = true;
        }
        else columns.add(col);
    }
}//end SelectQuery

class TSelectQuery extends SelectQuery  {
    
    public void TSelectQuery()  {
        queryType = "TSELECT";
    }
}//end TSelectQuery

class ConvertQuery extends Query    {
    String xmlFileName;
    String xsdFileName;
    String outFileName;
    
    public void ConvertQuery()  {
        queryType = "CONVERT";
    }
    public void assignXML(String filename) {
        //xml file to be converted to insert commands
        xmlFileName = filename;
    }
    public void assignXSD(String filename)  {
        //optional XSD file
        xsdFileName = filename;
    }
    public void assignOutFile(String filename)  {
        //assigns name of converted file
        outFileName = filename;
    }
}//end ConvertQuery
