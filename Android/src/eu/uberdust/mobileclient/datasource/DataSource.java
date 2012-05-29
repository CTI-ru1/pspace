package eu.uberdust.mobileclient.datasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

import eu.uberdust.mobileclient.model.*;

/**
 * This class represents a data source, used to fetch data from an uberdust server
 * @author Prevezanos Ioannis
 * @version 1.3
 */
public class DataSource {
	
	private Server dataserver;
	
	/**
	 * Class constructor. Creates a data source object, binding it to Server.
	 * @param dataserver An object, representing a server. A valid url is necessary
	 */
	public DataSource(Server dataserver)
	{
		this.dataserver = dataserver;
	}
	
	/**
	 * Method for obtaining all the available testbeds on server.
	 * @return A list of Testbed objects.
	 */
	public List<Testbed> getTestbeds()
	{
		List<Testbed> testbeds = null;
		try {
			testbeds = RestDataSource.fetchTestbeds(this.dataserver.getUrl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testbeds;
	}
	
	/** 
	 * Method for fetching a tree of Rooms, Nodes and Capabilities, from a testbed on server. Called by getRoomsAndNodes.
	 * @param testbed A valid testbed object. Passed by the caller.
	 * @return A TestbedTree object containing a list of RoomTree objects. Each RoomTree object contains a
	 * list of NodeTree objects, which in turn contains a list of Capability objects
	 */
	private TestbedTree fetchRoomsAndNodes(Testbed testbed)
	{
		int i;
		TestbedTree tree = new TestbedTree();
		tree.setId(testbed.getId());
		tree.setName(testbed.getName());
		tree.setUrl(testbed.getUrl());
		
		List<NodeTree> recNodes = null;
		try {
			recNodes = RestDataSource.fetchNodesList(testbed.getUrl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		HashMap<String,RoomTree> roomsMap = new HashMap<String,RoomTree>();

		// For every recognized node
		for(i=0;i<recNodes.size();i++)
		{
			if( roomsMap.get(recNodes.get(i).getRoom()) == null)
			{
				//If that room dosent exist, add a new room
				RoomTree newRoom = new RoomTree();
				newRoom.setName(recNodes.get(i).getRoom());
				newRoom.appendNode(recNodes.get(i));
				roomsMap.put(newRoom.getName(), newRoom);
			}
			else
			{
				//If room already exist, add node
				roomsMap.get(recNodes.get(i).getRoom()).appendNode(recNodes.get(i));
			}
		}
		List<String> recRooms = new ArrayList<String>(roomsMap.keySet());
		for(i=0;i<recRooms.size();i++)
		{
			//Add rooms to tree
			tree.appendRoom(roomsMap.get(recRooms.get(i)));
		}
		return tree;
	}
	
	
	/**
	 * Method for obtaining a tree of Rooms, Nodes and Capabilities, from a testbed.
	 * @param testbed A valid testbed object. Usually returned by getTestbeds().
	 * @return A TestbedTree object containing a list of RoomTree objects. Each RoomTree object contains a
	 * list of NodeTree objects, which in turn contains a list of Capability objects
	 */
	public TestbedTree getRoomsAndNodes(Testbed testbed)
	{
		DataCache dc = null;
		TestbedTree tree = null;
		try {
			dc = new DataCache(testbed,"physical");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(dc.cacheExists())
		{
			// Cached file exist. Load testbed
			try {
				tree = dc.testbedUnmarshal();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			tree = fetchRoomsAndNodes(testbed);
			try {
				dc.testbedMarshal(tree);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tree;
	}
	
	
	/**
	 * Method for obtaining a tree of Rooms, Nodes and Capabilities, from a testbed.
	 * @param testbed A valid testbed object. Usually returned by getTestbeds().
	 * @return A TestbedTree object containing a list of RoomTree objects. Each RoomTree object contains a
	 * list of NodeTree objects, which in turn contains a list of Capability objects
	 */
	public TestbedTree getVirtualRoomsAndNodes(Testbed testbed)
	{
		DataCache dc = null;
		TestbedTree tree = null;
		try {
			dc = new DataCache(testbed,"virtual");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(dc.cacheExists())
		{
			// Cached file exist. Load testbed
			try {
				tree = dc.testbedUnmarshal();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			tree = fetchVirtualRoomsAndNodes(testbed);
			try {
				dc.testbedMarshal(tree);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tree;
	}
	
	/**
	 * Method for fetching a tree of Rooms, Nodes and Capabilities, from a testbed. Called by getVirtualRoomsAndNodes.
	 * @param testbed A valid testbed object. Passed by the caller.
	 * @return A TestbedTree object containing a list of RoomTree objects. Each RoomTree object contains a
	 * list of NodeTree objects, which in turn contains a list of Capability objects
	 */
	private TestbedTree fetchVirtualRoomsAndNodes(Testbed testbed)
	{
		int i;
		TestbedTree tree = new TestbedTree();
		tree.setId(testbed.getId());
		tree.setName(testbed.getName());
		tree.setUrl(testbed.getUrl());
		
		List<NodeTree> recNodes = null;
		try {
			recNodes = RestDataSource.fetchVirtualNodesList(testbed.getUrl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<String,RoomTree> roomsMap = new HashMap<String,RoomTree>();

		// For every recognized node
		for(i=0;i<recNodes.size();i++)
		{
			if( roomsMap.get(recNodes.get(i).getRoom()) == null)
			{
				//If that room dosent exist add a new room
				RoomTree newRoom = new RoomTree();
				newRoom.setName(recNodes.get(i).getRoom());
				newRoom.appendNode(recNodes.get(i));
				roomsMap.put(newRoom.getName(), newRoom);
			}
			else
			{
				//If room already exist, add node
				roomsMap.get(recNodes.get(i).getRoom()).appendNode(recNodes.get(i));
			}
		}
		List<String> recRooms = new ArrayList<String>(roomsMap.keySet());
		for(i=0;i<recRooms.size();i++)
		{
			//Add rooms to tree
			tree.appendRoom(roomsMap.get(recRooms.get(i)));
		}
		return tree;		
	}
	
	/**
	 * Method for obtaining the latest reading of a capability
	 * @param capability An object representing the capability. Usually from a NodeTree
	 * @return A Reading object. Contains a timestamp and a value 
	 */
	public Reading getReading(Capability capability)
	{
		Reading reading = null;
		try {
			reading = RestDataSource.fetchLatestReading(capability.getUrl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reading;
	}
	/**
	 * Method for the readings history for a capability.
	 * @param cap An object representing the capability. Usually from a NodeTree
	 * @param limit The maximum number of readings to be returned
	 * @return A list of Reading objects. Each of them contains a timestamp and a value 
	 */
	public List<Reading> getReadingsHistory(Capability cap, int limit)
	{
		List<Reading> readings = null;
		try {
			readings = RestDataSource.fetchReadingsHistory(cap.getUrl(), Integer.toString(limit));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return readings; 
	}
	/**
	 * Send a command to capability
	 * @param cap The object representing the capability
	 * @param timestamp A timestamp for the command
	 * @param payload The command's payload
	 */
	public void sendCommand(Capability cap, String timestamp, String payload)
	{
		try {
			RestDataSource.sendCommand(cap.getUrl(), timestamp, payload);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}
