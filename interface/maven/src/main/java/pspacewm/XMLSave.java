/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pspacewm;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Konstantinos
 */
public class XMLSave {

    public Map<String,PspaceWM> xml_read(Map<String,PspaceWM> toread) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
        Document read=db.parse(new File("notifications.xml"));
        NodeList nl=read.getDocumentElement().getChildNodes();
        String node;
        for(int i=0;i<nl.getLength();i++)
        {

            node=nl.item(i).getAttributes().getNamedItem("name").getNodeValue();
            NodeList capabilities=nl.item(i).getChildNodes();
            if(toread.containsKey(node))
            {
                for(int j=0;j<capabilities.getLength();j++)
                {
                    String capability=capabilities.item(j).getAttributes().getNamedItem("name").getNodeValue();
                    if(toread.get(node).get_capability().containsKey(capability))
                         toread.get(node).get_capability().put(capability, true);
                }
            }

        }

        return toread;
    }
    public void xml_write(Map<String,PspaceWM> towrite) throws ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
   		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();


		Document doc = db.newDocument();
		Element root = doc.createElement("notifications");
		doc.appendChild(root);

                Element node = null;
                Element capability;
                Element enabled;
                int first_element;
		for(Map.Entry<String, PspaceWM> element : towrite.entrySet())
                {
                    first_element=0;
                    for(Map.Entry<String, Boolean> element2 : element.getValue().get_capability().entrySet())
                    {
                        if(element2.getValue())
                        {
                            if(first_element==0)
                            {
                                first_element=1;
                                node = doc.createElement("node");
                                root.appendChild(node);
                                Attr attr = doc.createAttribute("name");
                                attr.setValue(element.getKey());
                                node.setAttributeNode(attr);
                            }
                            capability = doc.createElement("capability");
                            node.appendChild(capability);
                            Attr attr2 = doc.createAttribute("name");
                            attr2.setValue(element2.getKey());
                            capability.setAttributeNode(attr2);

                            enabled=doc.createElement("enabled");
                            enabled.setTextContent("true");
                            capability.appendChild(enabled);
                        }

                    }


                }


		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("./notifications.xml"));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		System.out.println("File saved!");
    }

}
