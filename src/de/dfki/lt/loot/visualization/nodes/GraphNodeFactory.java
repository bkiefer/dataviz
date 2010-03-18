/*
 * 
 */
package de.dfki.lt.loot.visualization.nodes;

import org.apache.commons.collections15.Factory;

import de.dfki.lt.loot.visualization.exceptions.GraphNodeException;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating GraphNode objects.
 */
public class GraphNodeFactory implements Factory<GraphNode> {

	/** The _node. */
	private GraphNode _node;
	
	/** The _instance. */
	private static GraphNodeFactory _instance = new GraphNodeFactory(); 
	
	/* (non-Javadoc)
	 * @see org.apache.commons.collections15.Factory#create()
	 */
	@Override
	public GraphNode create() {
		try {
			_node = new GraphNodeAutoId(null);
			return _node;
		} catch (GraphNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets the single instance of GraphNodeFactory.
	 * 
	 * @return single instance of GraphNodeFactory
	 */
	public static GraphNodeFactory getInstance()
	{
		return _instance;
	}

}
