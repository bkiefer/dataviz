/*
 * 
 */
package de.dfki.lt.loot.visualization.edges;

import org.apache.commons.collections15.Factory;

import de.dfki.lt.loot.visualization.exceptions.GraphEdgeException;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating DataGraphEdge objects.
 */
public class DataGraphEdgeFactory implements Factory<DataGraphEdge> {

	/** The _edge. */
	private DataGraphEdge _edge;
	
	/** The _instance. */
	private static DataGraphEdgeFactory _instance = new DataGraphEdgeFactory(); 
	
	/* (non-Javadoc)
	 * @see org.apache.commons.collections15.Factory#create()
	 */
	@Override
	public DataGraphEdge create() {
		try {
			_edge = new DataGraphEdge("in", "out", null);
			return _edge;
		} catch (GraphEdgeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the single instance of DataGraphEdgeFactory.
	 * 
	 * @return single instance of DataGraphEdgeFactory
	 */
	public static DataGraphEdgeFactory getInstance()
	{
		return _instance;
	}

}
