/*
 * 
 */
package de.dfki.lt.loot.visualization.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import de.dfki.lt.loot.visualization.edges.DataGraphEdge;
import de.dfki.lt.loot.visualization.nodes.Node;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleGraph.
 */
public class SimpleGraph {
	
	/** The _graph. */
	private DirectedSparseMultigraph<Node, DataGraphEdge> _graph;
	
	/**
	 * Instantiates a new simple graph.
	 * 
	 * @param nodes the nodes
	 * @param edges the edges
	 */
	public SimpleGraph(HashMap<String, Node> nodes, Vector<DataGraphEdge> edges )
	{
		_graph = new DirectedSparseMultigraph<Node, DataGraphEdge>();
		initGraph(nodes, edges);
	}
	
	/**
	 * Inits the graph.
	 * 
	 * @param nodes the nodes
	 * @param edges the edges
	 */
	private void initGraph(HashMap<String, Node> nodes, Vector<DataGraphEdge> edges)
	{
		Collection<Node> nodeCol = nodes.values();
		Iterator<Node> itN = nodeCol.iterator();
		while(itN.hasNext())
		{
			_graph.addVertex((Node) itN.next());
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
	public DirectedSparseMultigraph<Node, DataGraphEdge> getGraph()
	{
		return _graph;
	}
	

}
