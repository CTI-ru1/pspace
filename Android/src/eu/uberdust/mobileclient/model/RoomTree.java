package eu.uberdust.mobileclient.model;

import java.util.ArrayList;
import java.util.List;

public class RoomTree extends Room{

	private List<NodeTree> nodes = new ArrayList<NodeTree>();
	
	public void appendNode(NodeTree n)
	{
		nodes.add(n);
	}
		
	public int getNodenum(){
		return nodes.size();
	}
	
	public NodeTree getNode(int i) {
		return nodes.get(i);
	}
	
}
