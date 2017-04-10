import java.util.*;
import java.io.*;
//These are the JAXP APIs used:
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
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

public static void main(String[] args) {
Database db = new Database();

Node DOM = db.convert("purchaseOrder.xml", "purchaseOrder.xsd");
System.out.print(DOM.getFirstChild());

} //end main

} //end Database