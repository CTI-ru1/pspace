package eu.uberdust.mobileclient.model;

import eu.uberdust.mobileclient.datasource.DataSource;
import android.app.Application;

public class GlobalData extends Application {
	
	private DataSource data;
	private TestbedTree currentTestbed;
	private TestbedTree currentVirtualTestbed;
	private RoomTree currentRoom;
	private NodeTree currentNode;
	private Capability currentCapability;
	
	
	public TestbedTree getCurrentVirtualTestbed() {
		return currentVirtualTestbed;
	}
	public void setCurrentVirtualTestbed(TestbedTree currentVirtualTestbed) {
		this.currentVirtualTestbed = currentVirtualTestbed;
	}
	
	public Capability getCurrentCapability() {
		return currentCapability;
	}
	public void setCurrentCapability(Capability currentCapability) {
		this.currentCapability = currentCapability;
	}
	public DataSource getData() {
		return data;
	}
	public void setData(DataSource data) {
		this.data = data;
	}
	public TestbedTree getCurrentTestbed() {
		return currentTestbed;
	}
	public void setCurrentTestbed(TestbedTree testbed) {
		currentTestbed = testbed;
	}
	
	public RoomTree getCurrentRoom() {
		return currentRoom;
	}
	
	public void setCurrentRoom(RoomTree room) {
		currentRoom = room;
	}
	
	public NodeTree getCurrentNode() {
		return currentNode;
	}
	
	public void setCurrentNode(NodeTree node) {
		currentNode = node;
	}
	
}
