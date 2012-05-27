package eu.uberdust.mobileclient.model;

import java.util.ArrayList;
import java.util.List;

public class NodeTree extends Node {
	
	private List<Capability> capabilities = new ArrayList<Capability>();
	
	public void appendCapability(Capability c)
	{
		capabilities.add(c);
	}
	
	public int getCapabilitiesNum()
	{
		return capabilities.size();
	}
	public Capability getCapability(int i)
	{
		return capabilities.get(i);
	}

}
