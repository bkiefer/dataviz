/*
 * 
 */
package de.dfki.lt.loot.visualization.nodes;

import de.dfki.lt.loot.visualization.exceptions.GraphNodeException;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphNodeAutoId.
 */
public class GraphNodeAutoId extends GraphNode{
	
	/** The _next free id. */
	private static int _nextFreeId = 0;

	/**
	 * Instantiates a new graph node auto id.
	 * 
	 * @param data the data
	 * @throws GraphNodeException the graph node exception
	 */
	public GraphNodeAutoId(Object name, Object data) throws GraphNodeException
	{
		super(getNextFreeId(), name, data);
	}
	
	/**
	 * Gets the next free id.
	 * 
	 * @return the next free id
	 */
	private static int getNextFreeId()
	{
		return _nextFreeId++;
	}
	
	/**
	 * Gets the last id.
	 * 
	 * @return the last id
	 */
	public static int getLastId()
	{
		return (_nextFreeId - 1);
	}

}
