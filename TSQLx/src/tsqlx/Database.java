import java.util.*;
import java.io.*;
//These are the JAXP APIs used:
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError; 
import javax.xml.parsers.ParserConfigurationException;
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
//Finally, import the W3C definitions for a DOM, DOM exceptions, entities and nodes:
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

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

public HashSet<String> readDatabaseFileListPerm(){
HashSet<String> dbFList = new HashSet<String>();
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

public Document convert(String xmlFileName, String xsdFileName, String fileName) {
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
            System.out.println(table);
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
return database;
}

//start main
public static void main(String[] args) {
Database db = new Database();

//Document DOM = db.convert("teamInsert.xml", "", "teamInsert.txt");

 Document database = createDatabase("somethingelse");
 commit(database);
 database = createTable("team", database);
  commit(database);
   database = createTable("another", database);
  commit(database);
     database = createTable("hey", database);
  commit(database);
//saveDatabase(database);
 //db.saveDatabase(database);
 //database = createDatabase("testing");
 //db.saveDatabase(database);
// //db.saveDatabase(database);
// //db.saveDatabase(database);
//Document database = createDatabase("testing");
//Document database = loadDatabase("somethingelse");
//dropDatabase("somethingelse");
//System.out.println(database.getFirstChild().getNodeName());
//db.dropDatabase("testing");
} //end main

} //end Database