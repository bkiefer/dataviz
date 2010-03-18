/*
 * 
 */
package de.dfki.lt.loot.visualization.edges;

import de.dfki.lt.loot.visualization.exceptions.GraphEdgeException;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphEdge.
 */
public class GraphEdge {
	
	/** The Constant NULL_ID. */
	private static final String NULL_ID = "The identification can not be null"; 
	
	/** The Constant NULL_IN. */
	private static final String NULL_IN = "The incoming node can not be null"; 
	
	/** The Constant NULL_OUT. */
	private static final String NULL_OUT = "The outgoing node can not be null"; 
	
	/** The _id. */
	private Object _id;
	
	/** The _out. */
	private String _in, _out;
	
	/**
	 * Instantiates a new graph edge.
	 * 
	 * @param id the id
	 * @param in the in
	 * @param out the out
	 * @throws GraphEdgeException the graph edge exception
	 */
	public GraphEdge( Object id, String in, String out) throws GraphEdgeException
	{
		if (id == null)
			throw new GraphEdgeException(NULL_ID);
		if (in == null)
			throw new GraphEdgeException(NULL_IN);
		if (out == null)
			throw new GraphEdgeException(NULL_OUT);
		_in = in;
		_out = out;
		_id = id;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Object getId() {return _id;}

	/**
	 * Gets the in.
	 * 
	 * @return the in
	 */
	public String getIn() {return _in;}

	/**
	 * Gets the out.
	 * 
	 * @return the out
	 */
	public String getOut() {return _out;}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return ("{Id:" + _id + "}");
	}
}
