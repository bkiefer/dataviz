/*
 * 
 */
package de.dfki.lt.loot.visualization.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import de.dfki.lt.loot.visualization.edges.DataGraphEdge;
import de.dfki.lt.loot.visualization.exceptions.GraphEdgeException;
import de.dfki.lt.loot.visualization.exceptions.GraphNodeException;
import de.dfki.lt.loot.visualization.nodes.GraphNode;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleGraph.
 */
public class SimpleGraph {
	
	/** The _graph. */
	private DirectedSparseMultigraph<GraphNode, DataGraphEdge> _graph;
	
	/**
	 * Instantiates a new simple graph.
	 * 
	 * @param nodes the nodes
	 * @param edges the edges
	 */
	public SimpleGraph(HashMap<String, GraphNode> nodes, Vector<DataGraphEdge> edges )
	{
		_graph = new DirectedSparseMultigraph<GraphNode, DataGraphEdge>();
		initGraph(nodes, edges);
		
		try {
			GraphNode test = new GraphNode("Test", "kk");
		
			nodes.put("Test", test);
			_graph.addVertex(test);
		
			DataGraphEdge dge = new DataGraphEdge("Test", "size", "-");
			_graph.addEdge( dge, nodes.get(dge.getOut()), test);
			dge = new DataGraphEdge("Test", "color", "-");
			_graph.addEdge( dge, nodes.get(dge.getOut()), test);
		} catch (GraphNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GraphEdgeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Inits the graph.
	 * 
	 * @param nodes the nodes
	 * @param edges the edges
	 */
	private void initGraph(HashMap<String, GraphNode> nodes, Vector<DataGraphEdge> edges)
	{
		System.out.println("Nodes : " + nodes.values());
		Collection<GraphNode> nodeCol = nodes.values();
		Iterator<GraphNode> itN = nodeCol.iterator();
		while(itN.hasNext())
		{
			_graph.addVertex((GraphNode) itN.next());
		}
		
		Iterator<DataGraphEdge>itE = edges.iterator();
		while(itE.hasNext())
		{
			DataGraphEdge dge = (DataGraphEdge) itE.next();
			_graph.addEdge(dge, nodes.get(dge.getIn()), nodes.get(dge.getOut()));
		}
		
	}
	
	/**
	 * Gets the graph.
	 * 
	 * @return the graph
	 */
	public DirectedSparseMultigraph<GraphNode, DataGraphEdge> getGraph()
	{
		return _graph;
	}
	

}
