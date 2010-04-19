/*
 * 
 */
package de.dfki.lt.loot.visualization.nodes;

import javax.swing.JPanel;

// TODO: Auto-generated Javadoc
/**
 * The Interface Node.
 * 
 * @param <I> the generic type
 * @param <D> the generic type
 */
public interface Node {
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Object getId();
	
	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	public Object getData();
	
	/**
	 * Gets the name
	 * 
	 * @return the name
	 */
	public Object getName();
	
	/**
	 * Sets the data.
	 * 
	 * @param data the new data
	 */
	public void setData(Object data);
	
	/**
	 * Sets the name
	 * 
	 * @param name the new name
	 */
	public void setName(Object name);
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	public String toString();
	
	/**
	 * Gets the view for this node
	 * 
	 * @return the view (JPanel)
	 */
	public JPanel getNodeView();
	
	/**
	 * Sets the view for this node
	 * 
	 * @param view, the view (Jpanel)
	 */
	public void setNodeView(JPanel view);
}
