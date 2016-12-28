package neural.network.graph;

public class Connection {
	private double weight;
	private String srcNodeId, targetNodeId;
	
	public void setConnectionWeight(double weight){ this.weight = weight; }
	public double getConnectionWeight(){ return weight; }
	
	public void setSrcNodeId(String srcNodeId){ this.srcNodeId = srcNodeId; }
	public String getSrcNodeId(){ return this.srcNodeId; }
	
	public void setTargetNodeId(String targetNodeId){ this.targetNodeId = targetNodeId; }
	public String getTargetNodeId(){ return this.targetNodeId; }
}
