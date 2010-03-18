/*
 * 
 */
package de.dfki.lt.loot.visualization.edges;

import de.dfki.lt.loot.visualization.exceptions.GraphEdgeException;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleGraphEdge.
 */
public class SimpleGraphEdge extends GraphEdge {

	/** The _next free id. */
	private static int _nextFreeId = 0;
	
	/**
	 * Instantiates a new simple graph edge.
	 * 
	 * @param in the in
	 * @param out the out
	 * @throws GraphEdgeException the graph edge exception
	 */
	public SimpleGraphEdge( String in, String out) throws GraphEdgeException
	{
		super(getNextFreeId(), in, out);
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
