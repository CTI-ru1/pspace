package eu.uberdust.mobileclient.datasource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.os.Environment;
import android.util.Xml;

import eu.uberdust.mobileclient.model.Capability;
import eu.uberdust.mobileclient.model.NodeTree;
import eu.uberdust.mobileclient.model.RoomTree;
import eu.uberdust.mobileclient.model.Testbed;
import eu.uberdust.mobileclient.model.TestbedTree;


/**
 * This class represents a data cache for a testbed tree.
 * @author Prevezanos Ioannis
 * @version 1.0
 */
public class DataCache {
	private String filename;
	private enum XppState {INTESTBED, INROOM, INNODE, INCAPABILITY, OUT};
	
	/**
	 * DataCache constructor. 
	 * @param testbed An object representing a testbed.
	 * @param prefix A prefix for the file
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public DataCache(Testbed testbed, String prefix) throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		byte[] filenamebytes = (prefix + testbed.getUrl()).getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] filehashbytes = md.digest(filenamebytes);
		StringBuffer filehash = new StringBuffer();
		for(int i=0;i<filehashbytes.length;i++)
		{
			String hex = Integer.toHexString(0xFF & filehashbytes[i]);
			if(hex.length() == 1)
				filehash.append("0");
			filehash.append(hex);
		}
		this.filename = filehash.toString();
		checkAndMkDirs();
	}
	/**
	 * Check if the testbed exist in cache
	 * @return true of false
	 */
	public boolean cacheExists()
	{
		File testfile = new File(Environment.getExternalStorageDirectory() + "/uberdust-mobile/cache/" + this.filename);
		return testfile.exists();
	}
	/**
	 * Put testbed in cache
	 * @param tree A testbedTree object.
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void testbedMarshal(TestbedTree tree) throws IllegalArgumentException, IllegalStateException, IOException
	{
		int i,j,k;
		File newxmlfile = new File(Environment.getExternalStorageDirectory() + "/uberdust-mobile/cache/" + this.filename);
        newxmlfile.createNewFile();
        FileOutputStream fileos = null;        
	    fileos = new FileOutputStream(newxmlfile);
	    XmlSerializer serializer = Xml.newSerializer();
	    serializer.setOutput(fileos, "UTF-8");
	    serializer.startDocument(null, Boolean.valueOf(true));   
	  
	    serializer.startTag(null, "TestbedTree");
	    	serializer.startTag(null, "id");
	    	if( tree.getId() == null )
	    		serializer.text("null");
	    	else
	    		serializer.text(tree.getId());
	    	serializer.endTag(null, "id");
	    	
	    	serializer.startTag(null, "name");
	    	if( tree.getName() == null )
	    		serializer.text("null");
	    	else
	    		serializer.text(tree.getName());
	    	serializer.endTag(null, "name");
	    	
	    	serializer.startTag(null, "url");
	    	if( tree.getUrl() == null )
	    		serializer.text("null");
	    	else
	    		serializer.text(tree.getUrl());
	    	serializer.endTag(null, "url");
	    	
	    	serializer.startTag(null, "roomnum");
	    	serializer.text(Integer.toString(tree.getRoomnum()));
	    	serializer.endTag(null, "roomnum");
	    	
	    	serializer.startTag(null, "roomsList");
	    	// List of rooms
	    	for(i=0;i<tree.getRoomnum();i++){
	    		serializer.startTag(null, "RoomTree");
	    		
	    			serializer.startTag(null, "name");
	    			if( tree.getRoom(i).getName() == null )
	    	    		serializer.text("null");
	    	    	else
	    	    		serializer.text(tree.getRoom(i).getName());
	    			serializer.endTag(null, "name");
	    			
	    			serializer.startTag(null, "nodenum");
	    			serializer.text(Integer.toString(tree.getRoom(i).getNodenum()));
	    			serializer.endTag(null, "nodenum");
	    			
	    			serializer.startTag(null, "nodesList");
	    			// List of nodes
	    			for(j=0;j<tree.getRoom(i).getNodenum();j++)
	    			{
	    				serializer.startTag(null, "NodeTree");
	    				
	    					serializer.startTag(null, "name");
	    					if( tree.getRoom(i).getNode(j).getName() == null )
	    	    	    		serializer.text("null");
	    	    	    	else
	    	    	    		serializer.text(tree.getRoom(i).getNode(j).getName());
	    					serializer.endTag(null, "name");
	    		    	
	    					serializer.startTag(null, "description");
	    					if( tree.getRoom(i).getNode(j).getDescription() == null )
	    	    	    		serializer.text("null");
	    	    	    	else
	    	    	    		serializer.text(tree.getRoom(i).getNode(j).getDescription());
	    					serializer.endTag(null, "description");
	    		    	
	    					serializer.startTag(null, "x");
	    					if( tree.getRoom(i).getNode(j).getX() == null )
	    	    	    		serializer.text("null");
	    	    	    	else
	    	    	    		serializer.text(tree.getRoom(i).getNode(j).getX());
	    					serializer.endTag(null, "x");
	    		    	
	    					serializer.startTag(null, "y");
	    					if( tree.getRoom(i).getNode(j).getY() == null )
	    	    	    		serializer.text("null");
	    	    	    	else
	    	    	    		serializer.text(tree.getRoom(i).getNode(j).getY());
	    					serializer.endTag(null, "y");
	    		    	
	    					serializer.startTag(null, "z");
	    					if( tree.getRoom(i).getNode(j).getZ() == null )
	    	    	    		serializer.text("null");
	    	    	    	else
	    	    	    		serializer.text(tree.getRoom(i).getNode(j).getZ());
	    					serializer.endTag(null, "z");
	    		    	
	    					serializer.startTag(null, "phi");
	    					if( tree.getRoom(i).getNode(j).getPhi() == null )
	    	    	    		serializer.text("null");
	    	    	    	else
	    	    	    		serializer.text(tree.getRoom(i).getNode(j).getPhi());
	    					serializer.endTag(null, "phi");
	    		    	
	    					serializer.startTag(null, "theta");
	    					if( tree.getRoom(i).getNode(j).getTheta() == null )
	    	    	    		serializer.text("null");
	    	    	    	else
	    	    	    		serializer.text(tree.getRoom(i).getNode(j).getTheta());
	    					serializer.endTag(null, "theta");
	    		    	 					
	    					serializer.startTag(null, "report");
	    					if( tree.getRoom(i).getNode(j).getReport() == null )
	    	    	    		serializer.text("null");
	    	    	    	else
	    	    	    		serializer.text(tree.getRoom(i).getNode(j).getReport());
	    					serializer.endTag(null, "report");
	    					
	    					serializer.startTag(null, "width");
	    					if( tree.getRoom(i).getNode(j).getWidth() == null )
	    	    	    		serializer.text("null");
	    	    	    	else
	    	    	    		serializer.text(tree.getRoom(i).getNode(j).getWidth());
	    					serializer.endTag(null, "width");
	    					
	    					serializer.startTag(null, "height");
	    					if( tree.getRoom(i).getNode(j).getHeight() == null )
	    	    	    		serializer.text("null");
	    	    	    	else
	    	    	    		serializer.text(tree.getRoom(i).getNode(j).getHeight());
	    					serializer.endTag(null, "height");

	    					serializer.startTag(null, "room");
	    					if( tree.getRoom(i).getNode(j).getRoom() == null )
	    	    	    		serializer.text("null");
	    	    	    	else
	    	    	    		serializer.text(tree.getRoom(i).getNode(j).getRoom());
	    					serializer.endTag(null, "room");
	    		    	
	    					serializer.startTag(null, "url");
	    					if( tree.getRoom(i).getNode(j).getUrl() == null )
	    	    	    		serializer.text("null");
	    	    	    	else
	    	    	    		serializer.text(tree.getRoom(i).getNode(j).getUrl());
	    					serializer.endTag(null, "url");
	    		    	
	    					serializer.startTag(null, "capabilitiesnum");
	    					serializer.text(Integer.toString(tree.getRoom(i).getNode(j).getCapabilitiesNum()));
	    					serializer.endTag(null, "capabilitiesnum");
	    		    	
	    					serializer.startTag(null, "capabilityList");
	    					// List of capabilities
	    					for(k=0;k<tree.getRoom(i).getNode(j).getCapabilitiesNum();k++)
	    					{
	    						serializer.startTag(null, "Capability");
	    						
	    							serializer.startTag(null, "attribute");
	    							if( tree.getRoom(i).getNode(j).getCapability(k).getAttribute() == null )
	    			    	    		serializer.text("null");
	    			    	    	else
	    			    	    		serializer.text(tree.getRoom(i).getNode(j).getCapability(k).getAttribute());
	    							serializer.endTag(null, "attribute");
	    							
	    							serializer.startTag(null, "url");
	    							if( tree.getRoom(i).getNode(j).getCapability(k).getUrl() == null )
	    			    	    		serializer.text("null");
	    			    	    	else
	    			    	    		serializer.text(tree.getRoom(i).getNode(j).getCapability(k).getUrl());
	    							serializer.endTag(null, "url");
	    							
	    						serializer.endTag(null, "Capability");
	    					}
	    					serializer.endTag(null, "capabilityList");
	    					
	    				serializer.endTag(null, "NodeTree");
	    			}
	    			serializer.endTag(null, "nodesList");
	    		serializer.endTag(null, "RoomTree");	    		
	    	}
	    	serializer.endTag(null, "roomsList");
	    	
	    	serializer.endDocument();
	    	serializer.flush();
	    	fileos.close();
		
	}
	/**
	 * Get testbed from cache
	 * @return A testbedTree object.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public TestbedTree testbedUnmarshal() throws XmlPullParserException, IOException
	{
		  File testbedfile = new File(Environment.getExternalStorageDirectory() + "/uberdust-mobile/cache/" + this.filename);
		  FileInputStream is = new FileInputStream(testbedfile);
		  
		  
		  XmlPullParserFactory xppfactory = XmlPullParserFactory.newInstance();
		  xppfactory.setNamespaceAware(true);
		  XmlPullParser xpp = xppfactory.newPullParser();
		  xpp.setInput(new InputStreamReader(is));
		 
		  XppState state = XppState.OUT;
		  
		  TestbedTree tree = new TestbedTree();
		  
		  RoomTree temproom = null;
		  NodeTree tempnode = null;
		  Capability tempcap = null;
		  
		  int event = xpp.getEventType();
		  while( event != XmlPullParser.END_DOCUMENT)
		  {
			  if( event == XmlPullParser.START_TAG && xpp.getName().equals("TestbedTree"))
				  state = XppState.INTESTBED;
			  else if( event == XmlPullParser.END_TAG && xpp.getName().equals("TestbedTree"))
				  state = XppState.OUT;
			  else if( event == XmlPullParser.START_TAG && xpp.getName().equals("RoomTree"))
			  {
				  state = XppState.INROOM;
				  temproom = new RoomTree();
			  }
			  else if( event == XmlPullParser.END_TAG && xpp.getName().equals("RoomTree"))
			  {
				  state = XppState.INTESTBED;
				  tree.appendRoom(temproom);
			  }
			  
			  else if( event == XmlPullParser.START_TAG && xpp.getName().equals("NodeTree"))
			  {
				  state = XppState.INNODE;
				  tempnode = new NodeTree();
			  }
			  else if( event == XmlPullParser.END_TAG && xpp.getName().equals("NodeTree"))
			  {
				  state = XppState.INROOM;
				  temproom.appendNode(tempnode);
			  }
			  else if( event == XmlPullParser.START_TAG && xpp.getName().equals("Capability"))
			  {
				  state = XppState.INCAPABILITY;
				  tempcap = new Capability();
			  }
			  else if( event == XmlPullParser.END_TAG && xpp.getName().equals("Capability"))
			  {
				  state = XppState.INNODE;
				  tempnode.appendCapability(tempcap);
			  }
			  
			  if( state == XppState.INTESTBED)
			  {
				  if ( event == XmlPullParser.START_TAG && xpp.getName().equals("id")){
					  xpp.next();
					  tree.setId(xpp.getText());
				  }
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("name")){
					  xpp.next();
					  tree.setName(xpp.getText());
				  }
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("url")){
					  xpp.next();
					  tree.setUrl(xpp.getText());
				  } 
			  }
			  else if ( state == XppState.INROOM)
			  {
				  if ( event == XmlPullParser.START_TAG && xpp.getName().equals("name")){
					  xpp.next();
					  temproom.setName(xpp.getText());
				  }
			  }
			  else if ( state == XppState.INNODE)
			  {
				  if ( event == XmlPullParser.START_TAG && xpp.getName().equals("name")){
					  xpp.next();
					  tempnode.setName(xpp.getText());
				  }
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("description")){
					  xpp.next();
					  tempnode.setDescription(xpp.getText());
				  }
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("phi")){
					  xpp.next();
					  tempnode.setPhi(xpp.getText());
				  } 
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("theta")){
					  xpp.next();
					  tempnode.setTheta(xpp.getText());
				  } 
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("x")){
					  xpp.next();
					  tempnode.setX(xpp.getText());
				  } 
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("y")){
					  xpp.next();
					  tempnode.setY(xpp.getText());
				  } 
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("z")){
					  xpp.next();
					  tempnode.setZ(xpp.getText());
				  }
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("report")){
					  xpp.next();
					  tempnode.setReport(xpp.getText());
				  } 
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("width")){
					  xpp.next();
					  tempnode.setWidth(xpp.getText());
				  } 
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("height")){
					  xpp.next();
					  tempnode.setHeight(xpp.getText());
				  }
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("room")){
					  xpp.next();
					  tempnode.setRoom(xpp.getText());
				  } 
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("url")){
					  xpp.next();
					  tempnode.setUrl(xpp.getText());
				  }   
			  }
			  else if ( state == XppState.INCAPABILITY)
			  {
				  if ( event == XmlPullParser.START_TAG && xpp.getName().equals("attribute")){
					  xpp.next();
					  tempcap.setAttribute(xpp.getText());
				  }
				  else if ( event == XmlPullParser.START_TAG && xpp.getName().equals("url")){
					  xpp.next();
					  tempcap.setUrl(xpp.getText());
				  } 
			  }
			  xpp.next();
			  event = xpp.getEventType();
		  } // End while END_DOCUMENT
		  
		  return tree;
	}
	/**
	 * Check if cache directory exist. If not create it.
	 */
	private void checkAndMkDirs()
	{
		File dir = new File(Environment.getExternalStorageDirectory() + "/uberdust-mobile/cache/");
		if(!dir.exists())
			dir.mkdirs();
	}
	
	
}
