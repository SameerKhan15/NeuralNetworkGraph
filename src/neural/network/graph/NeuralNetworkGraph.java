package neural.network.graph;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.JFrame;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;

public class NeuralNetworkGraph {
	protected static void positionAtVertexAt(JGraphModelAdapter m_jgAdapter, Object vertex, int x, int y, boolean bridgeNeuron)
	{
		DefaultGraphCell cell = m_jgAdapter.getVertexCell(vertex);
        Map attr = cell.getAttributes();
        Rectangle2D r2d = GraphConstants.getBounds(attr);
        
        double width = r2d.getWidth();
        double height = r2d.getHeight();
        if(bridgeNeuron)
        {
        	width = width*2;
        	height = height*2;
        	GraphConstants.setBackground(attr, Color.black);
        }else
        {
        	GraphConstants.setBackground(attr, Color.gray);
        }
        r2d.setRect(x, y, width, height);
        GraphConstants.setBounds(attr, r2d);
        
        Map cellAttr = new HashMap();
        cellAttr.put( cell, attr );
        m_jgAdapter.edit(cellAttr, null, null, null);
	}
	
	public static void plot(Map<Integer, LinkedHashSet<String>> neuronsByLevelMap,
			Map<String, Neuron> neuronsListMap, Map<Integer, String> biasNodesLevel) throws Exception
	{
		if(neuronsByLevelMap.containsKey(0))
		{
			throw new Exception("Invalid key of 0 found in neuronsByLevelMap. The key has to start with 1");
		}
		
		int layer = 1;
		/*Remove Bias Nodes from neuronsByLevelMap */
		Iterator<Entry<Integer, String>> biasNodesLevelIter = biasNodesLevel.entrySet().iterator();
		while(biasNodesLevelIter.hasNext())
		{
			neuronsByLevelMap.get(layer).remove(biasNodesLevelIter.next().getValue());
			layer++;
		}
		
		JFrame frame = new JFrame("Neural Network Plot");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		ListenableDirectedWeightedGraph<String,MyWeightedEdge> g = new 
				 ListenableDirectedWeightedGraph<String,MyWeightedEdge>(MyWeightedEdge.class);
		 
		 @SuppressWarnings({ "rawtypes", "unchecked" })
		JGraphModelAdapter m_jgAdapter = new JGraphModelAdapter(g);  
		 JGraph jgraph = new JGraph( m_jgAdapter );
	     jgraph.setEdgeLabelsMovable(true);
	     jgraph.setName("Neural Network Plot");
	    
	     String[][] neurons = new String[neuronsByLevelMap.size()][];
	     
	     for(int a = 1 ; a <= neuronsByLevelMap.size() ; a++)
	     {
	    	 LinkedHashSet<String> neuronsList = neuronsByLevelMap.get(a);
	    	 Iterator<String> neuronsListIter = neuronsList.iterator();
	    	 int neuronsListIterCounter = 0;
	    	 neurons[a - 1] = new String[neuronsList.size()];
	    	 while(neuronsListIter.hasNext())
	    	 {
	    		 neurons[a - 1][neuronsListIterCounter] =  neuronsListIter.next();
	    		 neuronsListIterCounter++;
	    	 }
	     }
	     
	     String[][] biasedNeurons = new String[biasNodesLevel.size()][1];
	     
	     for(int a = 1 ; a <= biasNodesLevel.size() ; a++)
	     {
	    	 biasedNeurons[a - 1][0] = biasNodesLevel.get(a);
	     }
	     
	     HashMap<String,String> bridgeNeuronsMap = new HashMap<>();
	     int MAX_HEIGHT = 12;
	     
	     for(int a = 0 ; a < neurons.length ; a++)
			{
				//Loop over all the layers - start
				 int xCounter = 0;
				
				 int[][] xStartValArray = {{100},{500},{900},{1200}};
				 int[] xStartValArrayPointer = {0};
				 
				 Integer[] x,y;		 
				 
				 if(a < (neurons.length - 1))
				 {
					 x = new Integer[neurons[a].length + biasedNeurons[a].length];
					 y = new Integer[neurons[a].length + biasedNeurons[a].length];
				 }else
				 {
					 x = new Integer[neurons[a].length];
					 y = new Integer[neurons[a].length];
				 }
				 
				 for(int b = 0 ; b < x.length ; b++)
				 {
					if(xStartValArrayPointer[0] == xStartValArray[a].length)
					{
						xStartValArrayPointer[0] = 0;
					}
					
					x[b] = xStartValArray[a][xStartValArrayPointer[0]];
					xStartValArrayPointer[0]++;
				 }
				 
				 int yStartVal = 40;
				 int yIncrement = 60;
				 int yCounter = 0;
				 
				 
				 y[0] = yStartVal;
				 
				 for(int c = 1 ; c < y.length ; c++)
				 {
					 y[c] = y[c - 1] + yIncrement;
				 }
				 
				 int bridgeNeuronsRangeStart = 0;
				 int overFlowCount = 0; //(inputNeurons[a].length + biasedInputNeurons[a].length) - MAX_HEIGHT;
				 if(a == (neurons.length - 1))
				 {
					 overFlowCount = (neurons[a].length) - MAX_HEIGHT;
				 }else
				 {
					 overFlowCount = (neurons[a].length + biasedNeurons[a].length) - MAX_HEIGHT;
				 }
				 
				 if(overFlowCount > 0)		   
				 {
					 bridgeNeuronsRangeStart = neurons[a].length - overFlowCount;
					 
					 for(int d = 0 ; d < bridgeNeuronsRangeStart ; d++)
					 {
						 g.addVertex(neurons[a][d]);
						 NeuralNetworkGraph.positionAtVertexAt(m_jgAdapter, neurons[a][d], x[xCounter++], y[yCounter++], false);
					 }
					 g.addVertex(String.valueOf(neurons[a][bridgeNeuronsRangeStart])+" - "+neurons[a][neurons[a].length - 1]);
					 NeuralNetworkGraph.positionAtVertexAt(m_jgAdapter, String.valueOf(neurons[a][bridgeNeuronsRangeStart])+" - "+neurons[a][neurons[a].length - 1], 
							 x[xCounter++], y[yCounter++], true); 
					 
					 for(int d = bridgeNeuronsRangeStart ; d < neurons[a].length ; d++)
					 {
						 bridgeNeuronsMap.put(neurons[a][d], String.valueOf(neurons[a][bridgeNeuronsRangeStart])+" - "+neurons[a][neurons[a].length - 1]); 
						// System.out.println("");
					 }		 
				 }else
				 {
					 for(int d = 0 ; d < neurons[a].length ; d++)
					 {
						 g.addVertex(neurons[a][d]);																	 
						 NeuralNetworkGraph.positionAtVertexAt(m_jgAdapter, neurons[a][d], x[xCounter++], y[yCounter++], false);
					 }
				 }
				 
				 if(a < (neurons.length - 1))
				 {
					 for(int e = 0 ; e < biasedNeurons[a].length ; e++)
					 {
						 g.addVertex(biasedNeurons[a][e]);
						 NeuralNetworkGraph.positionAtVertexAt(m_jgAdapter, String.valueOf(biasedNeurons[a][e]), 
								 x[xCounter++] + 200, y[yCounter++], false);
					 }
				 }		 
			}
	     
	     for(int a = 0 ; a < neurons.length ; a++)
			{
				for(int d = 0 ; d < neurons[a].length ; d++)			 
				{
					boolean brk = false;
					
					String sourceNode = neurons[a][d];
					 if(bridgeNeuronsMap.containsKey(sourceNode))
					 {
						 sourceNode = bridgeNeuronsMap.get(sourceNode);
						 brk = true;
					 }
					 
					if(a < (neurons.length - 1))
					 {
						 for(int e = 0 ; e < neurons[a+1].length; e++)
						 {
							// System.out.println(sourceNode+","+neurons[a+1][e]);
							 if(bridgeNeuronsMap.containsKey(neurons[a+1][e]) && bridgeNeuronsMap.containsKey(sourceNode.split("-")[0].trim()))
							 {								 									 
								 g.setEdgeWeight(g.addEdge(bridgeNeuronsMap.get(sourceNode.split("-")[0].trim()), bridgeNeuronsMap.get(neurons[a+1][e])), 
											 0);															 
								 break; 								 
							 }else if(bridgeNeuronsMap.containsKey(neurons[a+1][e]))
							 {
								 g.setEdgeWeight(g.addEdge(sourceNode, bridgeNeuronsMap.get(neurons[a+1][e])), 
										 0);															 
								 break;
							 }else if(bridgeNeuronsMap.containsKey(sourceNode.split("-")[0].trim()))
							 {
								 g.setEdgeWeight(g.addEdge(bridgeNeuronsMap.get(sourceNode.split("-")[0].trim()), neurons[a+1][e]), 
										 0);															 
								 break;
							 }
							 else 
							 { 
								 g.setEdgeWeight(g.addEdge(sourceNode, neurons[a+1][e]), 
										 neuronsListMap.get(sourceNode).getConnectionWeight(neurons[a+1][e]));								 
							 }						 						 												 
						 }					 				
					 }
					
					if(brk)
					{
						break;
					}
				 }
				
				if(a < (neurons.length - 1))
				 {
					for(int e = 0 ; e < neurons[a+1].length; e++)
					 {
						if(bridgeNeuronsMap.containsKey(neurons[a+1][e]))
						 {
							g.setEdgeWeight(g.addEdge(biasedNeurons[a][0], bridgeNeuronsMap.get(neurons[a+1][e])), 
									0);
							break;
						 }else
						 {
							 g.setEdgeWeight(g.addEdge(biasedNeurons[a][0], neurons[a+1][e]), 
									 neuronsListMap.get(biasedNeurons[a][0]).getConnectionWeight(neurons[a+1][e]));
						 }				
					 }	
				 }			 
			}	
			
			 frame.add(jgraph);
		     frame.pack();		    
		     frame.setLocationByPlatform(true);		    
		     frame.setVisible(true);		    
		     frame.setSize(2048, 2048);
	}
	
	public static void main(String args[])
	{
		Random rand = new Random();
		Map<String,Neuron> neuronsMap = new HashMap<>();
		LinkedHashSet<String> layer1NeuronsList = new LinkedHashSet<String>();
		layer1NeuronsList.add("i1");
		layer1NeuronsList.add("i2");
		layer1NeuronsList.add("i3");
		layer1NeuronsList.add("i4");
		layer1NeuronsList.add("i5");
		layer1NeuronsList.add("i6");
		Iterator<String> layer1NeuronsListIter = layer1NeuronsList.iterator();
		
		LinkedHashSet<String> layer2NeuronsList = new LinkedHashSet<String>();
		layer2NeuronsList.add("h1");
		layer2NeuronsList.add("h2");
		layer2NeuronsList.add("h3");
		layer2NeuronsList.add("h4");
		layer2NeuronsList.add("h5");
		layer2NeuronsList.add("h6");
		layer2NeuronsList.add("h7");
		layer2NeuronsList.add("h8");
		layer2NeuronsList.add("h9");
		layer2NeuronsList.add("h10");
		layer2NeuronsList.add("h11");
		layer2NeuronsList.add("h12");
		layer2NeuronsList.add("h13");
		layer2NeuronsList.add("h14");
		layer2NeuronsList.add("h15");
		Iterator<String> layer2NeuronsListIter = layer2NeuronsList.iterator();
		
		while(layer1NeuronsListIter.hasNext())
		{
			String neuronName = layer1NeuronsListIter.next();
			Neuron neuron = new Neuron(neuronName);
			neuronsMap.put(neuronName, neuron);
			
			layer2NeuronsListIter = layer2NeuronsList.iterator();
			while(layer2NeuronsListIter.hasNext())
			{
				neuron.addOutgoingConnection(layer2NeuronsListIter.next(), rand.nextInt(50) + 1);
			}
		}		
		
		layer2NeuronsListIter = layer2NeuronsList.iterator();
		LinkedHashSet<String> layer3NeuronsList = new LinkedHashSet<String>();
		layer3NeuronsList.add("h16");
		layer3NeuronsList.add("h17");
		layer3NeuronsList.add("h18");
		layer3NeuronsList.add("h19");
		layer3NeuronsList.add("h20");
		layer3NeuronsList.add("h21");
		layer3NeuronsList.add("h22");
		layer3NeuronsList.add("h23");
		layer3NeuronsList.add("h24");
		layer3NeuronsList.add("h25");
		layer3NeuronsList.add("h26");
		layer3NeuronsList.add("h27");
		layer3NeuronsList.add("h28");
		layer3NeuronsList.add("h29");
		layer3NeuronsList.add("h30");
		Iterator<String> layer3NeuronsListIter = layer3NeuronsList.iterator();
		
		while(layer2NeuronsListIter.hasNext())
		{
			String neuronName = layer2NeuronsListIter.next();
			Neuron neuron = new Neuron(neuronName);
			neuronsMap.put(neuronName, neuron);
			
			layer3NeuronsListIter = layer3NeuronsList.iterator();
			while(layer3NeuronsListIter.hasNext())
			{
				neuron.addOutgoingConnection(layer3NeuronsListIter.next(), rand.nextInt(50) + 1);
			}
		}
		
		layer3NeuronsListIter = layer3NeuronsList.iterator();
		
		LinkedHashSet<String> layer4NeuronsList = new LinkedHashSet<String>();
		layer4NeuronsList.add("o1");
		layer4NeuronsList.add("o2");
		Iterator<String> layer4NeuronsListIter = layer4NeuronsList.iterator();
		
		while(layer3NeuronsListIter.hasNext())
		{
			String neuronName = layer3NeuronsListIter.next();
			Neuron neuron = new Neuron(neuronName);
			neuronsMap.put(neuronName, neuron);
			
			layer4NeuronsListIter = layer4NeuronsList.iterator();
			while(layer4NeuronsListIter.hasNext())
			{
				neuron.addOutgoingConnection(layer4NeuronsListIter.next(), rand.nextInt(50) + 1);
			}
		}
	
		Map<Integer, LinkedHashSet<String>> neuronsByLevelMap = new HashMap<>();
		neuronsByLevelMap.put(1, layer1NeuronsList);
		neuronsByLevelMap.put(2, layer2NeuronsList);
		neuronsByLevelMap.put(3, layer3NeuronsList);
		neuronsByLevelMap.put(4, layer4NeuronsList);
		
		Map<Integer, String> biasedNeuronsMap = new HashMap<>();
		biasedNeuronsMap.put(1, "b1");
		Neuron neuron = new Neuron(biasedNeuronsMap.get(1));
		neuronsMap.put(biasedNeuronsMap.get(1), neuron);
		layer2NeuronsListIter = layer2NeuronsList.iterator();
		
		while(layer2NeuronsListIter.hasNext())
		{
			neuron.addOutgoingConnection(layer2NeuronsListIter.next(), rand.nextInt(50) + 1);
		}
		
		biasedNeuronsMap.put(2, "b2");
		neuronsMap.put(biasedNeuronsMap.get(2), new Neuron(biasedNeuronsMap.get(2)));
		layer3NeuronsListIter = layer3NeuronsList.iterator();
		
		while(layer3NeuronsListIter.hasNext())
		{
			neuronsMap.get(biasedNeuronsMap.get(2)).addOutgoingConnection(layer3NeuronsListIter.next(), rand.nextInt(50) + 1);
		}
		
		biasedNeuronsMap.put(3, "b3");
		neuronsMap.put(biasedNeuronsMap.get(3), new Neuron(biasedNeuronsMap.get(3)));
		layer4NeuronsListIter = layer4NeuronsList.iterator();
		
		while(layer4NeuronsListIter.hasNext())
		{
			neuronsMap.get(biasedNeuronsMap.get(3)).addOutgoingConnection(layer4NeuronsListIter.next(), rand.nextInt(50) + 1);
		}
		
		try{
			NeuralNetworkGraph.plot(neuronsByLevelMap, neuronsMap, biasedNeuronsMap);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
