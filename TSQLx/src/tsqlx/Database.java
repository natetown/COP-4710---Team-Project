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

static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
static final String outputEncoding = "UTF-8";

public Document convert(String xmlFileName, String xsdFileName) {
   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
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
            PrintWriter outputStream = new PrintWriter(tableName+"Insert.txt");
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

static void printDatabase(Node DOM){
try {
    String databaseName = DOM.getFirstChild().getNodeName();
    PrintWriter outputStream = new PrintWriter(databaseName+".txt");
    // Use a Transformer for output
    TransformerFactory tFactory =
    TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer();
    DOMSource source = new DOMSource(DOM);
    StreamResult result = new StreamResult(outputStream);
    transformer.transform(source, result);
    } catch(TransformerException tce){
    System.out.println("Tramsformer is messed up");
    } catch(FileNotFoundException Fnfe){
    System.out.println(Fnfe.getMessage());
    }
}

//start main
public static void main(String[] args) {
Database db = new Database();

Node DOM = db.convert("teamInsert.xml", "");
//db.printDatabase(DOM);
} //end main

} //end Database