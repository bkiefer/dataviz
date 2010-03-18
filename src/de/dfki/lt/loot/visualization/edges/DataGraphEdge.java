/*
 * 
 */
package de.dfki.lt.loot.visualization.edges;

import de.dfki.lt.loot.visualization.exceptions.GraphEdgeException;

// TODO: Auto-generated Javadoc
/**
 * The Class DataGraphEdge.
 */
public class DataGraphEdge extends SimpleGraphEdge{
	
	/** The _data. */
	private Object _data;
	
	/**
	 * Instantiates a new data graph edge.
	 * 
	 * @param in the in
	 * @param out the out
	 * @param data the data
	 * @throws GraphEdgeException the graph edge exception
	 */
	public DataGraphEdge (String in, String out, Object data) throws GraphEdgeException
	{
		super( in, out);
		_data = data;	
	}
	
	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	public Object getData()
	{
		return _data;
	}
	
	/**
	 * Sets the data.
	 * 
	 * @param data the new data
	 */
	public void setData(Object data)
	{
		_data = data;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object dge)
	{
		if(dge == this)
			return true;
		else if(!(dge instanceof DataGraphEdge))
			return false;
		DataGraphEdge castDGE = (DataGraphEdge)dge;
		if((this.getIn().equals(castDGE.getIn())) 
				&& (this.getOut().equals(castDGE.getOut())) 
				&& (this.getData().equals(castDGE.getData())) )
		{
			return true;
		}
		else
			return false;
	}
	
	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.visualization.edges.GraphEdge#toString()
	 */
	public String toString()
	{
		return _data.toString();
	}

}
