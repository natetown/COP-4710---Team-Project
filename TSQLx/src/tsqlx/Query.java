/* Author(s): Kevin Poon
*/


/*
These classes represent the types of DML queries that the parser reads. 
The parser will use these methods to create query objects that will be passed
to another method that will execute the queries based on which fields are
present in the object. 

 */
import java.util.*;

public abstract class Query {
    String queryType;
    String tablename;
    boolean valid;
    
    public void Query(String type) {
        queryType = type;
        valid = true;
    }
    public void assignTN(String tbl)    {
        //specifies which table to interact with
        tablename = tbl;
    }
    public void invalidate()  {
        valid = false;
    }
    /*
    public static void main(String[] args)   {
        //testing
        InsertQuery q = new InsertQuery();
        q.assignTN("mytable");
        q.assignFields("field1");
        q.assignValues("abc");
        q.testQ(q);
        test.testing();
    }
*/
    public void testQ(Query q)  {
        System.out.println(q.queryType);
    }
    
}//end Query

class InsertQuery extends Query  {
    //list of fields and values to manipulate
    ArrayList<String> fields = new ArrayList<String>();
    ArrayList<String> values = new ArrayList<String>();
    
    public InsertQuery()   {
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
class Comparison    {
    String term1;
    String relop;
    String term2;
    
    public void assignT1(String t1) {
        term1 = t1;
    }
    public void assignRelop(String r) {
        relop = r;
    }
    public void assignT2(String t2)  {
        term2 = t2;
    }
    
}
class SelectQuery extends Query {
    //list of columns to be selected
    ArrayList<String> columns;      //columns selected
    ArrayList<String> whereLeft;    //left side of a comparison
    ArrayList<String> relops;
    ArrayList<String> whereRight;   //right side of a comparison
    ArrayList<String> logicals;
    ArrayList<Comparison> conditions;
    boolean selectAll;
    
    public SelectQuery()   {
        queryType = "SELECT";
        columns = new ArrayList<String>();
        whereLeft = new ArrayList<String>();
        relops = new ArrayList<String>();
        logicals = new ArrayList<String>();
        conditions = new ArrayList<Comparison>();
    }
    public void assignColumns(String col)   {
        //looks for simple select
        if(col.contains("ASTK") || col.contains("*")) {
            selectAll = true;
            columns.add(col);
        }
        else columns.add(col);
    }
    public void assignCondition(Comparison comp)    {
        conditions.add(comp);
    }
    public void assignWhere(String col) {
        //adds columns to the WHERE clause
        whereLeft.add(col);
    }
    public void assignRelops(String rel)    {
        relops.add(rel);
    }
    public void assignLogicals(String logic)    {
        //adds AND or OR
        logicals.add(logic);
    }
    public void displayCondtions()  {
        System.out.println("Conditions");
        for(int i=0; i<conditions.size(); i++)  {
            System.out.printf(conditions.get(i).term1 + " ");
            System.out.printf(conditions.get(i).relop + " ");
            System.out.println(conditions.get(i).term2);
        }
        System.out.println();
    }
    public void displayLogicals()  {
        System.out.println("Logical Operators");
        for(int i=0; i<logicals.size(); i++)  {
            System.out.printf(logicals.get(i) + " ");
        }
        System.out.println();
    }
    public void displayColumns()  {
        System.out.println("Columns: ");
        for(int i=0; i<columns.size(); i++)  {
            System.out.printf(columns.get(i) + " ");
        }
        System.out.println();
    }
}//end SelectQuery

class TSelectQuery extends SelectQuery  {
    
    public TSelectQuery()  {
        queryType = "TSELECT";
    }
}//end TSelectQuery

class ConvertQuery extends Query    {
    String xmlFileName;
    String xsdFileName;
    String outFileName;
    
    public ConvertQuery()  {
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

class InputQuery extends Query  {
    String txtFileName;
    
    public InputQuery() {
        queryType = "INPUT"; 
    }
    public void assignInputFile(String filename)   {
        txtFileName = filename;
    }
}
class DeleteQuery extends Query {
    ArrayList<String> whereLeft; //
    ArrayList<String> logicals;
    ArrayList<String> relops;
    ArrayList<Comparison> conditions;
    boolean selectAll;
    
    public DeleteQuery()    {
        whereLeft = new ArrayList<String>();
        relops = new ArrayList<String>();
        logicals = new ArrayList<String>();
        conditions = new ArrayList<Comparison>();
        queryType = "DELETE";
    }
    public void assignWhere(String col) {
        //adds columns to the WHERE clause
        whereLeft.add(col);
    }
    public void assignRelops(String rel)    {
        //adds a relational operator
        relops.add(rel);
    }
    public void assignLogicals(String logic)    {
        //adds AND or OR
        logicals.add(logic);
    }
    public void assignCondition(Comparison c)   {
        //adds a condition to the WHERE clause
        conditions.add(c);
    }
    public void displayCondtions()  {
        System.out.println("Conditions: ");
        for(int i=0; i<conditions.size(); i++)  {
            System.out.printf(conditions.get(i).term1 + " ");
            System.out.printf(conditions.get(i).relop + " ");
            System.out.println(conditions.get(i).term2);
        }
        System.out.println();
    }
    public void displayLogicals()  {
        System.out.println("Logical Operators");
        for(int i=0; i<logicals.size(); i++)  {
            System.out.printf(logicals.get(i) + " ");
        }
        System.out.println();
    }
}