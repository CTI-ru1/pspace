package eu.uberdust.mobileclient.datasource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.util.Log;



import eu.uberdust.mobileclient.model.*;

/**
 * A utility class. Contains methods for fetching data from the server.
 * @author Prevezanos Ioannis
 * @version 1.2
 */
public class RestDataSource {
	/**
	 * Utility class. Cannot be instantiated
	 */
	private RestDataSource(){}
	
	/**
	 * Fetch a list of all available non-virtual nodes, from the status json
	 * @param url The url of a testbed
	 * @return A list of NodeTree objects
	 * @throws ClientProtocolException
	 * @throws JSONException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static List<NodeTree> fetchNodesList(String url) throws ClientProtocolException, JSONException, URISyntaxException, IOException
	{
		// Example url : http://carrot.cti.gr:8080/uberdust/rest/testbed/2/
		int i;
		List<NodeTree> nodes = new ArrayList<NodeTree>();
		JSONObject obj = getJSONObjectfromURL(url + "status/json");
		
		@SuppressWarnings("rawtypes")
		Iterator objectIterator = obj.keys();
		
		while(objectIterator.hasNext())
		{
			NodeTree node = new NodeTree();
			node.setName(objectIterator.next().toString());
			node.setUrl(url + "node/" + node.getName() + "/");
			node.setRoom("Undefined");
			node.setDescription("Undefined");
			node.setX("Undefined");
			node.setY("Undefined");
			node.setZ("Undefined");
			node.setPhi("Undefined");
			node.setTheta("Undefined");
			
			node.setReport("Undefined");
			node.setWidth("Undefined");
			node.setHeight("Undefined");
			
			if(!node.getName().contains("virtual")) {
				JSONArray arr = obj.getJSONArray(node.getName());
				for(i=0;i<arr.length();i++)
				{
					JSONObject cap = arr.getJSONObject(i);
					String capAttr = cap.getString("capability");
					if(		capAttr.equalsIgnoreCase("room") || capAttr.contains("capability:room"))
									node.setRoom(cap.getString("stringReading"));
					else if(capAttr.equalsIgnoreCase("description") || capAttr.contains("capability:description"))
									node.setDescription(cap.getString("stringReading"));
					else if(capAttr.equalsIgnoreCase("x") || capAttr.contains("capability:x"))
									node.setX(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("y") || capAttr.contains("capability:y"))
									node.setY(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("z") || capAttr.contains("capability:z"))
									node.setZ(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("phi") || capAttr.contains("capability:phi"))
									node.setPhi(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("theta") || capAttr.contains("capability:theta"))
									node.setTheta(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("report") || capAttr.contains("capability:report"))
									node.setReport(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("width") || capAttr.contains("capability:width"))
									node.setWidth(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("height") || capAttr.contains("capability:height"))
									node.setHeight(cap.getString("reading"));
					else
					{
						// Unknown capability. Add it to node
						Capability newCap = new Capability();
						newCap.setAttribute(capAttr);
						newCap.setUrl(node.getUrl() + "capability/" + capAttr + "/");
						node.appendCapability(newCap);
					}
					
				}
				nodes.add(node);
			}
		}
		return nodes;
	}
	
	/**
	 * Fetch a list of all available virtual nodes, from the status json
	 * @param url The url of a testbed
	 * @return A list of NodeTree objects
	 * @throws ClientProtocolException
	 * @throws JSONException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static List<NodeTree> fetchVirtualNodesList(String url) throws ClientProtocolException, JSONException, URISyntaxException, IOException
	{
		// Example url : http://carrot.cti.gr:8080/uberdust/rest/testbed/2/
		int i;
		List<NodeTree> nodes = new ArrayList<NodeTree>();
		JSONObject obj = getJSONObjectfromURL(url + "status/json");
		
		@SuppressWarnings("rawtypes")
		Iterator objectIterator = obj.keys();
		
		while(objectIterator.hasNext())
		{
			NodeTree node = new NodeTree();
			node.setName(objectIterator.next().toString());
			node.setUrl(url + "node/" + node.getName() + "/");
			node.setRoom("Undefined");
			node.setDescription("Undefined");
			node.setX("Undefined");
			node.setY("Undefined");
			node.setZ("Undefined");
			node.setPhi("Undefined");
			node.setTheta("Undefined");
			node.setReport("Undefined");
			node.setWidth("Undefined");
			node.setHeight("Undefined");
			if(node.getName().contains("virtual")) {
				Log.d("NODE",node.getName());
				JSONArray arr = obj.getJSONArray(node.getName());
				for(i=0;i<arr.length();i++)
				{
					JSONObject cap = arr.getJSONObject(i);
					String capAttr = cap.getString("capability");
					if(		capAttr.equalsIgnoreCase("room") || capAttr.contains("capability:room"))
						node.setRoom(cap.getString("stringReading"));
					else if(capAttr.equalsIgnoreCase("description") || capAttr.contains("capability:description"))
						node.setDescription(cap.getString("stringReading"));
					else if(capAttr.equalsIgnoreCase("x") || capAttr.contains("capability:x"))
						node.setX(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("y") || capAttr.contains("capability:y"))
						node.setY(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("z") || capAttr.contains("capability:z"))
						node.setZ(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("phi") || capAttr.contains("capability:phi"))
						node.setPhi(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("theta") || capAttr.contains("capability:theta"))
						node.setTheta(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("report") || capAttr.contains("capability:report"))
						node.setReport(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("width") || capAttr.contains("capability:width"))
						node.setWidth(cap.getString("reading"));
					else if(capAttr.equalsIgnoreCase("height") || capAttr.contains("capability:height"))
						node.setHeight(cap.getString("reading"));
					else
					{
						// Unknown capability. Add it to node
						Capability newCap = new Capability();
						newCap.setAttribute(capAttr);
						newCap.setUrl(node.getUrl() + "capability/" + capAttr + "/");
						node.appendCapability(newCap);
					}
					
				}

				nodes.add(node);
			}
		}
		return nodes;
	}
	
	
	/**
	 * Fetch list of available testbeds
	 * @param url Request's url
	 * @return A list of Testbed objects, with a valid name, id and url.
	 */
	public static List<Testbed> fetchTestbeds(String url) throws ClientProtocolException, JSONException, URISyntaxException, IOException
	{
		// Example url: http://carrot.cti.gr:8080/uberdust/
		int i;
		List<Testbed> testbeds = new ArrayList<Testbed>();
		JSONArray arr = getJSONArrayfromURL(url + "rest/testbed/json");
		for(i=0;i<arr.length();i++)
		{
			JSONObject o = arr.getJSONObject(i);
			Testbed t = new Testbed();
			t.setName(o.getString("testbedName"));
			t.setId(o.getString("testbedId"));
			t.setUrl(url + "rest/testbed/" + t.getId() + "/");
			testbeds.add(t);
		}
		return testbeds;
	}
	
	/**
	 * Fetch list of all available non-virtual nodes
	 * @deprecated Please, use the method fetchNodesList to fetch the information from the status page
	 * @param url Request's url
	 * @return A list of Node objects, with a valid name and url
	 */
	public static List<Node> fetchNodes(String url) throws JSONException, ClientProtocolException, URISyntaxException, IOException
	{
		// Example url : http://carrot.cti.gr:8080/uberdust/rest/testbed/2/
		int i;
		List<Node> nodes = new ArrayList<Node>();
		JSONObject obj = getJSONObjectfromURL(url + "node/json");
		JSONArray recNodes = obj.getJSONArray("nodes");
		for(i=0;i<recNodes.length();i++)
		{
			if(! recNodes.getString(i).contains(":virtual:") )
			{
				Node n = new Node();
				n.setName(recNodes.getString(i));
				n.setUrl(url + "node/" + n.getName() + "/");
				nodes.add(n);
			}
		}
		return nodes;
	}
	/**
	 * Fetch list of all available virtual nodes
	 * @deprecated Please, use the method fetchVirtualNodesList to fetch the information from the status page
	 * @param url Request's url
	 * @return A list of Node objects, with a valid name and url
	 */
	public static List<Node> fetchVirtualNodes(String url) throws JSONException, ClientProtocolException, URISyntaxException, IOException
	{
		// Example url : http://carrot.cti.gr:8080/uberdust/rest/testbed/2/
		int i;
		List<Node> nodes = new ArrayList<Node>();
		JSONObject obj = getJSONObjectfromURL(url + "node/json");
		JSONArray recNodes = obj.getJSONArray("nodes");
		for(i=0;i<recNodes.length();i++)
		{
			if(recNodes.getString(i).contains(":virtual:") )
			{
				Node n = new Node();
				n.setName(recNodes.getString(i));
				n.setUrl(url + "node/" + n.getName() + "/");
				nodes.add(n);
			}
		}
		return nodes;
	}
	/**
	 * Fetch list of node capabilities
	 * @deprecated Please, use the methods fetchNodesList or fetchVirtualNodesList, instead
	 * @param url Request's url
	 * @return A list of capability objects, with valid attribute and url
	 */
	public static List<Capability> fetchCapabilities(String url) throws ClientProtocolException, URISyntaxException, IOException, JSONException
	{
		// Example url: http://carrot.cti.gr:8080/uberdust/rest/testbed/2/node/urn:pspace:test1/
		int i;
		List<Capability> capabilities = new ArrayList<Capability>();
		JSONObject obj = getJSONObjectfromURL(url + "capabilities/json");
		JSONArray recCapabilities = obj.getJSONArray("capabilities");
		for(i=0;i<recCapabilities.length();i++)
		{
			Capability c = new Capability();
			c.setAttribute(recCapabilities.getString(i));
			c.setUrl(url + "capability/" + c.getAttribute() + "/");
			capabilities.add(c);
			
		}
		return capabilities;
	}
	
	/**
	 * Fetch latest reading
	 * @param url Request's url
	 * @return A reading object, with valid timestamp and value.
	 */
	public static Reading fetchLatestReading(String url) throws ClientProtocolException, JSONException, URISyntaxException, IOException
	{
		// Example url: http://carrot.cti.gr:8080/uberdust/rest/testbed/2/node/urn:pspace:test1/capability/temperature/
		Reading r = new Reading();
		JSONObject obj = getJSONObjectfromURL(url + "latestreading/json");
		JSONObject lr = obj.getJSONArray("readings").getJSONObject(0);
		
		Timestamp tt = new java.sql.Timestamp(lr.getLong("timestamp"));
		r.setTimestamp(tt.toString());
		
		String sr = lr.getString("stringReading");
		if(sr == null || sr.equals("") || sr.equals("null"))
			r.setValue(Double.toString(lr.getDouble("reading")));
		else
			r.setValue(sr);
		return r;
	}
	/**
	 * Fetch readings history
	 * @param url Request's url
	 * @param limit The maximum number of readings
	 * @return A list of reading objects with valid timestamp and value
	 */
	public static List<Reading> fetchReadingsHistory(String url, String limit)throws ClientProtocolException, JSONException, URISyntaxException, IOException
	{
		// Example url: http://carrot.cti.gr:8080/uberdust/rest/testbed/2/node/urn:pspace:test1/capability/temperature/
		int i;
		List<Reading> readings = new ArrayList<Reading>();
		JSONObject obj = getJSONObjectfromURL(url + "json/limit/" + limit);
		JSONArray arr = obj.getJSONArray("readings");
		for(i=0;i<arr.length();i++)
		{
			JSONObject reading = arr.getJSONObject(i);
			Reading r = new Reading();
			Timestamp tt = new java.sql.Timestamp(reading.getLong("timestamp"));
			r.setTimestamp(tt.toString());
			String sr = reading.getString("stringReading");
			if(sr == null || sr.equals("") || sr.equals("null"))
				r.setValue(Double.toString(reading.getDouble("reading")));
			else
				r.setValue(sr);
			readings.add(r);			
		}
		return readings;
	}
	/**
	 * Sends a command to a capability
	 * @param url Capability url
	 * @param timestamp A timestamp for the command
	 * @param payload The command's payload
	 */
	public static void sendCommand(String url, String timestamp, String payload) throws ClientProtocolException, IOException, URISyntaxException
	{
		HttpClient httpclient = new DefaultHttpClient();
		URI uri = new URI(url + "insert/timestamp/" + timestamp + "/reading/" + payload + "/");
		HttpGet httpget = new HttpGet(uri);
		httpclient.execute(httpget);
	}
	
	/**
	 * Returns a JSONObject making a GET request on url
	 * @param url The target url
	 * @return A JSONObject
	 */
	private static JSONObject getJSONObjectfromURL(String url) throws JSONException, ClientProtocolException, URISyntaxException, IOException{
		JSONObject jObject;
		//Parse the response string to a JSONObject
		jObject = new JSONObject(getStringResponseFromURL(url));
		return jObject;
	}
	
	/**
	 * Returns a JSONArray making a GET request on url
	 * @param url The target url
	 * @return A JSONArray
	 */
	private static JSONArray getJSONArrayfromURL(String url) throws ClientProtocolException, JSONException, URISyntaxException, IOException
	{
		JSONArray jArray;
		jArray = new JSONArray(getStringResponseFromURL(url));
		return jArray;
	}
	/**
	 * Returns the response string, making a GET request on url
	 * @param url The target url
	 * @return The response string
	 */
	private static String getStringResponseFromURL(String url) throws URISyntaxException, ClientProtocolException, IOException
	{
		InputStream is = null;
		String result = new String();

		//HTTP Get request
		HttpClient httpclient = new DefaultHttpClient();
		URI uri = new URI(url);
		HttpGet httpget = new HttpGet(uri);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		is = entity.getContent();

		//Convert to string
		BufferedReader reader = new BufferedReader(new InputStreamReader(is),8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		result=sb.toString();
		return result;
	}
}
