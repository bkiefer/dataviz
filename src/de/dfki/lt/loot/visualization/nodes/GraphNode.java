/*
 * 
 */
package de.dfki.lt.loot.visualization.nodes;

import javax.swing.JPanel;

import de.dfki.lt.loot.visualization.exceptions.GraphNodeException;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphNode.
 */
public class GraphNode implements Node{
	
	/** The Constant NULL_ID. */
	private static final String NULL_ID = "The identification can not be null"; 
	
	/** The _id. */
	private Object _id;
	
	/** The Name */
	private Object _name;
	
	/** The _data. */
	private Object _data;
	
	/** The _view */
	private JPanel _view;
	
	/**
	 * Instantiates a new graph node.
	 * 
	 * @param id the id
	 * @param data the data
	 * @throws GraphNodeException the graph node exception
	 */
	public GraphNode(Object id, Object name, Object data, JPanel view) throws GraphNodeException
	{
		if (id == null)
			throw  new GraphNodeException(NULL_ID);
		_id = id;
		_name = name;
		_data = data;
		_view = view;
	}
	
	/**
	 * Instantiates a new graph node.
	 * 
	 * @param id the id
	 * @param data the data
	 * @throws GraphNodeException the graph node exception
	 */
	public GraphNode(Object id, Object name, Object data) throws GraphNodeException
	{
		this(id, name, data, new ClassicNodeView(name, data));
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
	
	public Object getName()
	{
		return _name;
	}
	
	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.visualization.nodes.Node#setData(java.lang.Object)
	 */
	public void setData(Object data)
	{
		_data = data;
	}
	
	public void setName(Object name)
	{
		_name = name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return ( _name.toString());
	}

	@Override
	public JPanel getNodeView() {
		return _view;
	}

	@Override
	public void setNodeView(JPanel view) {
		_view = view;		
	}

}
