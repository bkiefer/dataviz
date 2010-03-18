/*
 * 
 */
package de.dfki.lt.loot.visualization.nodes;

import de.dfki.lt.loot.visualization.exceptions.GraphNodeException;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphNode.
 */
public class GraphNode implements Node<Object, Object>{
	
	/** The Constant NULL_ID. */
	private static final String NULL_ID = "The identification can not be null"; 
	
	/** The _id. */
	private Object _id;
	
	/** The _data. */
	private Object _data;
	
	/**
	 * Instantiates a new graph node.
	 * 
	 * @param id the id
	 * @param data the data
	 * @throws GraphNodeException the graph node exception
	 */
	public GraphNode(Object id, Object data) throws GraphNodeException
	{
		if (id == null)
			throw  new GraphNodeException(NULL_ID);
		_id = id;
		_data = data;
	}
	
	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.visualization.nodes.Node#getId()
	 */
	public Object getId()
	{
		return _id;
	}
	
	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.visualization.nodes.Node#getData()
	 */
	public Object getData()
	{
		return _data;
	}
	
	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.visualization.nodes.Node#setData(java.lang.Object)
	 */
	public void setData(Object data)
	{
		_data = data;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return ( _id.toString());
	}
}
