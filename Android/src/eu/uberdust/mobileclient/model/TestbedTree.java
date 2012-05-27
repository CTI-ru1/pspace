package eu.uberdust.mobileclient.model;

import java.util.ArrayList;
import java.util.List;

public class TestbedTree extends Testbed {
	private List<RoomTree> rooms = new ArrayList<RoomTree>();
	
	public void appendRoom(RoomTree r)
	{
		rooms.add(r);
	}
	
	public int getRoomnum(){
		return rooms.size();
	}
	
	public RoomTree getRoom(int i) {
		return rooms.get(i);
	}
}
