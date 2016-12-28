package neural.network.graph;

import java.util.HashMap;
import java.util.Map;

public class Neuron {
	private Map<String,Double> outgoingConnections;
	private String id;
	
	public Neuron(String id)
	{
		this.id = id;
		outgoingConnections = new HashMap<>();
	}
	
	public String getId(){ return this.id; }
	
	public void addOutgoingConnection(String targetNode, double connectionWeight)
	{
		outgoingConnections.put(targetNode, connectionWeight);
	}
	
	public double getConnectionWeight(String nodeId)
	{
		return outgoingConnections.get(nodeId);
	}
}
