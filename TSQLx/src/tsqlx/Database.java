// Author(s): Nathan Wheeler
import java.util.*;
import java.io.*;
//These are the JAXP APIs used: 
import javax.xml.parsers.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.*;
import javax.xml.XMLConstants;
import javax.xml.xpath.*;
//These classes are for the exceptions that can be thrown when the XML document is parsed:
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException; 
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.*;
import org.xml.sax.InputSource;
//These classes read the sample XML file and manage output:
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//Finally, import the W3C definitions for a DOM, DOM exceptions, entities and nodes:
import org.w3c.dom.*;


class Database {

private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
private static final String outputEncoding = "UTF-8";
private static HashSet<String> databaseFileList = new HashSet<String>();
private static File databaseFileListPerm = new File("databaseFileList.conf");


Database(){
if(databaseFileListPerm.exists() && !databaseFileListPerm.isDirectory()) { 
    databaseFileList= readDatabaseFileListPerm();
}
}//end database constructor

public static String printSpaces(int numOfSpaces){

StringBuilder stringBuilder = new StringBuilder();
for(int i = 0; i<numOfSpaces; i++){
stringBuilder.append(" ");
}
String spaces = stringBuilder.toString();
return spaces;
}
public static void selectT(Document database, String tableName){
   XPath xpath = XPathFactory.newInstance().newXPath();
   try{NodeList resultSet = (NodeList)xpath.compile("//" +tableName + "//row").evaluate(database, XPathConstants.NODESET);
      
      final int columnWidth = 25;
       
           
               Node currentElement = resultSet.item(0).getFirstChild();
               while(currentElement!=null){
               String elName = currentElement.getNodeName();
               int elSize =  elName.length();
               int offset = columnWidth - elSize;
               System.out.print(currentElement.getNodeName()+ printSpaces(offset));
               currentElement=currentElement.getNextSibling();
                    } 
                    System.out.println("");
                    System.out.println("");
                    
                                 for(int i=0; i<resultSet.getLength(); i++){
                                 Node currentElement2 = resultSet.item(i).getFirstChild();
                                   while(currentElement2!=null){
                                    //String elName2 = currentElement2.getNodeName();
                                    String elName2 = String.format("%-25s", currentElement2.getTextContent());
                                    // int elSize2 =  elName2.length();
//                                     int offset2 = columnWidth - elSize2;
                                    System.out.print(elName2);

                                    currentElement2=currentElement2.getNextSibling();
                                    } 
                                   System.out.println("");
                                   }
          }
      catch(XPathExpressionException xpee){
   System.out.println(xpee.getMessage());
   
   }
   }
   
   public static void selectW(Document database, String tableName){
   XPath xpath = XPathFactory.newInstance().newXPath();
   try{NodeList resultSet = (NodeList)xpath.compile("//" +tableName + "//row").evaluate(database, XPathConstants.NODESET);
      
      final int columnWidth = 25;
       
           
               Node currentElement = resultSet.item(0).getFirstChild();
               while(currentElement!=null){
               String elName = currentElement.getNodeName();
               if(elName.equals("insertDateTime")){
               currentElement=currentElement.getNextSibling();
               }
               else{
               int elSize =  elName.length();
               int offset = columnWidth - elSize;
               System.out.print(currentElement.getNodeName()+ printSpaces(offset));
               currentElement=currentElement.getNextSibling();
               }
                    } 
                    System.out.println("");
                    System.out.println("");
                    
                                 for(int i=0; i<resultSet.getLength(); i++){
                                 Node currentElement2 = resultSet.item(i).getFirstChild();
                                   while(currentElement2!=null){
                                   
                                   if(currentElement2.getNodeName().equals("insertDateTime")){
                                   currentElement2=currentElement2.getNextSibling();
                                   }
                                   else{
                                    //String elName2 = currentElement2.getNodeName();
                                    String elName2 = String.format("%-25s", currentElement2.getTextContent());
                                    // int elSize2 =  elName2.length();
//                                     int offset2 = columnWidth - elSize2;
                                    System.out.print(elName2);

                                    currentElement2=currentElement2.getNextSibling();
                                    }//end else
                                    } 
                                   System.out.println("");
                                   }
          }
      catch(XPathExpressionException xpee){
   System.out.println(xpee.getMessage());
   
   }

   }//end class
/*
public static void displayResultSet(List<Element> resultSet){
//Assumes that all attributes are in the same order in the resultSet and that timestamp is last
//Prints the header row
Node headerRow = resultSet.get(0);
Node currentHeaderField = headerRow.getFirstChild();
while(currentHeaderField!=null){
if(currentHeaderField.getNodeName().equals("insertDateTime")){
System.out.print("Omitting date Header");
}
else{
System.out.print(currentHeaderField.getNodeName()+ "               ");
}
//currentHeaderField  = currentRow(i++);
}//end while
//Prints out each row
for(int i=0; i<resultSet.size(); i++){
Node currentRow = resultSet.get(i);
Node currentField = currentRow.getFirstChild();
   while(currentField!=null){
   if(currentHeaderField.getNodeName().equals("insertDateTime")){
   System.out.print("Omitting date");
   }
   else{
   System.out.println(currentField.getNodeName());
   }
   }//end inner while loop
}//end outer for loop
}//end displayResultSet
*/
/*
  public static List<Element> convertToStandardResultSet(List<Element> resultSet){
    //Get the first row and use that structure as the order.
    ArrayList<String> attributeNames = new ArrayList<String>();
    Element headerRow = (Element) resultSet.get(0);
    Element currentHeaderField = (Element) headerRow.getFirstChild();
    //Add first row's attribute names to the list.
    while(currentHeaderField!=null){
    if(currentHeaderField.getNodeName().equals("insertDateTime")){
    System.out.print("Omitting date Header");
    }
    else{
    attributeNames.add(currentHeaderField.getNodeName());
    }
    //currentHeaderField  = currentRow(i++);
    }//end while
    //Get all of the rows and add them to the NodeList  
    List<Element> nodeList = resultSet;  //create a nodeList to store elements temporarilty
      
    //loop through all rows and change structure to match first row.
    
    for(int i=0; i<resultSet.size(); i++){
       List<Element> orderedAttList = resultSet;
       orderedAttList.clear();
       Element currentRow = (Element) resultSet.get(i);
       Element currentField = (Element) currentRow.getFirstChild();
          while(currentField!=null){
          if(currentHeaderField.getNodeName().equals("insertDateTime")){
          System.out.print("Omitting date");
          }
          else{
          
          orderedAttList.add(currentfield);
         //add to result set
         //add result set to a Row element
          
          }
          currentField = (Element) currentField.getNextSibling();
          }//End While
  
       currentRow = (Element) resultSet.get(i);
  }
  return resultSet;
  }
*/

public static HashSet<String> readDatabaseFileListPerm(){
HashSet<String> dbFList = new HashSet<String>();
//read databases line by line from file
try (BufferedReader br = new BufferedReader(new FileReader(databaseFileListPerm))) {
    String line;
    while ((line = br.readLine()) != null) {
       dbFList.add(line); 
    } 
}     catch(FileNotFoundException fnfe){
      System.out.println(fnfe.getMessage());
    } catch(IOException ioe){
      System.out.println(ioe.getMessage());
    }
return dbFList;
}

public static void saveDataBaseFileListPerm(HashSet<String> fileList){
try{
PrintWriter printWriter = new PrintWriter(new FileWriter(databaseFileListPerm));
for (String s : fileList) {
    printWriter.println(s);
    } 
    printWriter.close();
} catch(IOException ioe){
    System.out.println(ioe.getMessage());
    }
}

public static Document convert(String xmlFileName, String xsdFileName, String fileName) {
   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
   dbf.setIgnoringComments(true);
   dbf.setIgnoringElementContentWhitespace(true);
         if (xsdFileName.endsWith(".xsd")) {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Source source = new StreamSource(new File(xsdFileName));
            try{
            dbf.setSchema(schemaFactory.newSchema(source));
            } catch(SAXException se){
            System.out.println(se.getMessage());
            }
             dbf.setNamespaceAware(true);
             dbf.setValidating(false);
         } 
            try{  
            DocumentBuilder db = dbf.newDocumentBuilder();
            OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, outputEncoding);
            db.setErrorHandler(new MyErrorHandler(new PrintWriter(errorWriter)));
            Document doc = db.parse(new InputSource(xmlFileName));
            clean(doc);
            //Convert Dom into Insert Queries 
            Node database = doc.getFirstChild();
            Node table = database.getFirstChild();
            String tableName=table.getNodeName();
            StringBuilder currentInsertQuery = new StringBuilder();
            //Create a new file and open it
            try{
            PrintWriter outputStream = new PrintWriter(fileName);
            Node currentRow=table.getFirstChild();
            while(currentRow!=null){
            //Build the query
            currentInsertQuery.append("INSERT INTO ");
            currentInsertQuery.append(tableName);
            //loop through attributes
               Node currentAttribute = currentRow.getFirstChild();
               currentInsertQuery.append("("); 
                  while(currentAttribute!=null){
               currentInsertQuery.append(currentAttribute.getNodeName()+",");   
               currentAttribute = currentAttribute.getNextSibling();   
               }
            currentInsertQuery.setLength(currentInsertQuery.length() - 1);
            //done with attributes
            currentInsertQuery.append(")"); 
            currentInsertQuery.append(" VALUES(");
            //loop through values
             Element thisAttribute = (Element) currentRow.getFirstChild();
            
                  while(thisAttribute!=null){
               
               if(thisAttribute.getAttribute("type").equals("string")){
               currentInsertQuery.append("'" + thisAttribute.getTextContent() + "',");
               }
               else{
               currentInsertQuery.append(thisAttribute.getTextContent()+",");   
               }
               thisAttribute = (Element) thisAttribute.getNextSibling();  
               }
            currentInsertQuery.setLength(currentInsertQuery.length() - 1);

            //done with values
            currentInsertQuery.append(");");
            	outputStream.println(currentInsertQuery);
            	currentInsertQuery.setLength(0);
            	currentRow = currentRow.getNextSibling();
            }
            outputStream.close();
            } catch(FileNotFoundException e){
            	System.out.println("The file wasn't found.");
            }
            //end conversion
            System.out.println("You file has been successfully converted.");
            return doc;
            } catch(Exception ex){
            System.out.println("Fatal Error: " + ex.getMessage());
            }
            return null;
            }  

//Error handler
   private static class MyErrorHandler implements ErrorHandler {
     
    private PrintWriter out;
           MyErrorHandler(PrintWriter out) {
           this.out = out;
    }

    private String getParseExceptionInfo(SAXParseException spe) {
        String systemId = spe.getSystemId();
        if (systemId == null) {
            systemId = "null";
        }

        String info = "URI=" + systemId + " Line=" + spe.getLineNumber() + ": " + spe.getMessage();
        return info;
    }

    public void warning(SAXParseException spe) throws SAXException {
        System.out.println("Warning: " + getParseExceptionInfo(spe));
    }
        
    public void error(SAXParseException spe) throws SAXException {
        System.out.println("Error: " + getParseExceptionInfo(spe));
    }

    public void fatalError(SAXParseException spe) throws SAXException {
         System.out.println("Fatal Error: " + getParseExceptionInfo(spe));
    }
}
//removes useless text nodes
public static void clean(Node node)
{
  NodeList childNodes = node.getChildNodes();

  for (int n = childNodes.getLength() - 1; n >= 0; n--)
  {
     Node child = childNodes.item(n);
     short nodeType = child.getNodeType();

     if (nodeType == Node.ELEMENT_NODE)
        clean(child);
     else if (nodeType == Node.TEXT_NODE)
     {
        String trimmedNodeVal = child.getNodeValue().trim();
        if (trimmedNodeVal.length() == 0)
           node.removeChild(child);
        else
           child.setNodeValue(trimmedNodeVal);
     }
     else if (nodeType == Node.COMMENT_NODE)
        node.removeChild(child);
  }
}
//custom get next sibling method
Node getNextElementSibling(Node node){
boolean element = false;
while (element==false && node.getNextSibling()!=null){
if(node.getNodeType() == Node.ELEMENT_NODE){
	element=true;
	return node;
}
if(node.getNextSibling()==null){
	element=true;
	return null;
}
node=node.getNextSibling();
}
return null;
}

public static Document createDatabase(String databaseName){
   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
   dbf.setIgnoringComments(true);
   dbf.setIgnoringElementContentWhitespace(true);
   try{  
            DocumentBuilder db = dbf.newDocumentBuilder();
            OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, outputEncoding);
            db.setErrorHandler(new MyErrorHandler(new PrintWriter(errorWriter)));
            Document doc = db.newDocument();
            Element database = doc.createElement(databaseName);
            doc.appendChild(database);
            System.out.println("Your database, " + databaseName + ", has been created.");
            return doc;
            } catch(Exception ex){
            System.out.println(ex.getMessage());
            }
  return null;
}

public static void saveDatabase(Document DOM){
try {
    String databaseName = DOM.getFirstChild().getNodeName();
    String databaseFileName = databaseName+".xml";
    PrintWriter outputStream = new PrintWriter(databaseFileName);
    //Need to check if this database exists in a list of strings. If not,
    if(databaseFileList.contains(databaseFileName)){
    System.out.println("Your database, " + databaseName + ", has been saved.");
    }
    else{
    databaseFileList.add(databaseFileName);
    saveDataBaseFileListPerm(databaseFileList);
    System.out.println("Your database, " + databaseName + ", has been saved for the first time.");
    } 
    // Use a Transformer for output
    TransformerFactory tFactory =
    TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer();
    DOMSource source = new DOMSource(DOM);
    StreamResult result = new StreamResult(outputStream);
    transformer.transform(source, result);
    outputStream.close();
    } catch(TransformerException tce){
    System.out.println("Tramsformer is messed up");
    } catch(FileNotFoundException Fnfe){
    System.out.println(Fnfe.getMessage());
    }
}

public static Document loadDatabase(String databaseName){
   String databaseFileName=databaseName+".xml";
   if(databaseFileList.contains(databaseFileName)){
   //Open file 
   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
   try{  
               DocumentBuilder db = dbf.newDocumentBuilder();
               OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, outputEncoding);
               db.setErrorHandler(new MyErrorHandler(new PrintWriter(errorWriter)));
               Document doc = db.parse(new InputSource(databaseFileName));
               clean(doc);
               System.out.println("Your database, " + databaseName + " has been loaded successfully.");
               return doc;
   } catch(Exception se){
      System.out.println(se.getMessage());
   }
   }
   else{
   System.out.println("This database does not exist.");
   return null;
   }
   return null;
}

public static void dropDatabase(String databaseName){
   String databaseFileName=databaseName+".xml";
   if(databaseFileList.contains(databaseFileName)){
   //Delete file and delete name from hashset
   boolean success = (new File(databaseFileName)).delete();
   databaseFileList.remove(databaseFileName);
   saveDataBaseFileListPerm(databaseFileList);
   System.out.println("You database, " + databaseName +" has been dropped");
      }
   else{
   System.out.println("The specified table doesn't exist.");
   }
}

public static void commit(Document DOM){
try {
    String databaseName = DOM.getFirstChild().getNodeName();
    String databaseFileName = databaseName+".xml";
    PrintWriter outputStream = new PrintWriter(databaseFileName);
    // Use a Transformer for output
    TransformerFactory tFactory =
    TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer();
    DOMSource source = new DOMSource(DOM);
    StreamResult result = new StreamResult(outputStream);
    transformer.transform(source, result);
    outputStream.close();
    System.out.println("Your changes have been committed.");
    } catch(TransformerException tce){
    System.out.println("Tramsformer is messed up");
    } catch(FileNotFoundException Fnfe){
    System.out.println(Fnfe.getMessage());
    }
}

public static Document createTable(String tableName, Document database){
Element newTable = database.createElement(tableName);
database.getFirstChild().appendChild(newTable);
System.out.println("Your table, " + tableName + " has been successfully created. Type Commit to save your changes.");
return database;
}

public static Document dropTable(String tableName, Document database){
NodeList deletedTables = database.getElementsByTagName(tableName);
if(deletedTables.getLength() == 0){
System.out.println("This table doesn't exist.");
}
else{
for (int i = 0; i<deletedTables.getLength(); i++){
deletedTables.item(i).getParentNode().removeChild(deletedTables.item(i));
}
System.out.println("Your table, " + tableName + " has been dropped. Type Commit to save your changes.");
return database;
}
return null;
}

public static Document insert(String tableName, ArrayList<String> fields, ArrayList<String> values, Document database){
Element newRow = database.createElement("row");
for(int i=0; i < values.size(); i++ ){
Element newField = database.createElement(fields.get(i));
newField.setTextContent(values.get(i));
newRow.appendChild(newField);
}
//create a new date element and append it to the row
Element newField = database.createElement("insertDateTime");
DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
Date dateobj = new Date();
newField.setTextContent(df.format(dateobj));
newRow.appendChild(newField);

//append the new row to the table
NodeList insertTables = database.getElementsByTagName(tableName);
Element insertTable = (Element)insertTables.item(0);
insertTable.appendChild(newRow);
return database;
}
/*
 public static void input(String insertFileName){
 
 try (BufferedReader br = new BufferedReader(new FileReader(insertFileName))) {
     String line;
     DML dml = new DML();
     while ((line = br.readLine()) != null) {
        ArrayList<Lexer.Token> lexerTokens = Lexer.lex(line);
        dml.DMLstart(lexerTokens);
        //call function 
     } 
 }     catch(FileNotFoundException fnfe){
       System.out.println(fnfe.getMessage());
     } catch(IOException ioe){
       System.out.println(ioe.getMessage());
     }
 }
*/
public static void removeChilds(Node node) {
    while (node.hasChildNodes())
        node.removeChild(node.getFirstChild());
}

public static Document delete(String tableName, Document database){
NodeList table = database.getElementsByTagName(tableName);
Node matchingTable = table.item(0); 
if(table.getLength() == 0){
System.out.println("This table doesn't exist.");
}
else{
removeChilds(matchingTable);
System.out.println("Your rows have been deleted from " + tableName);
return database;
}
return null;
}
//start main
public static void main(String[] args) {
Database db = new Database();

//Document database; 
//database = createDatabase("database");
//saveDatabase(database);
//input("teamInsert.txt");

 // database = createTable("team", database);
 //database= loadDatabase("database");
 //commit(database);

 // commit(database);
//  Element row = (Element)database.getFirstChild().getFirstChild();
//  NodeList test = (NodeList)database.getFirstChild().getChildNodes();
Document database = db.loadDatabase("Jag");
//Document DOM = db.convert("teamInsert.xml", "", "teamInsert.txt");
//   Document database = createDatabase("test");
//   saveDatabase(database);
// //  commit(database);
//   database = createTable("team", database);
//    commit(database);
//  ArrayList<String> fields = new ArrayList<String>();
//  fields.add("yo");
//  fields.add("hey");
//  ArrayList<String> values = new ArrayList<String>();
//  values.add("va1");
//  values.add("va2");
//  database = db.insert("team", fields, values, database);
//  database = db.insert("team", fields, values, database);
//  database = db.insert("team", fields, values, database);
//  commit(database);
//  database = db.delete("ORDERS", database);
//   commit(database);
 selectW(database, "ORDERS");
 selectW(database, "CUST");
 selectW(database, "S");
 selectW(database, "PRDCT");
//    database = createTable("another", database);
//   commit(database);
//      database = createTable("hey", database);
//   commit(database);
//   database = dropTable("tem", database);
//     commit(database);
// saveDatabase(database);
// database = createTable("team", database);
// commit(database);
//     database = createTable("team", database);
//     input("teamInsert.txt");
//     commit(database);
 //db.saveDatabase(database);
 //database = createDatabase("testing");
 //db.saveDatabase(database);
// //db.saveDatabase(database);
// //db.saveDatabase(database);
//Document database = createDatabase("testing");
//Document database = loadDatabase("test");
//db.selectW(database, "name");
//dropDatabase("somethingelse");
//System.out.println(database.getFirstChild().getNodeName());
//db.dropTable("erf", database);
} //end main

} //end Database