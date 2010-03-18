/*
 * 
 */
package de.dfki.lt.loot.visualization.nodes;

// TODO: Auto-generated Javadoc
/**
 * The Interface Node.
 * 
 * @param <I> the generic type
 * @param <D> the generic type
 */
public interface Node<I, D> {
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public I getId();
	
	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	public D getData();
	
	/**
	 * Sets the data.
	 * 
	 * @param data the new data
	 */
	public void setData(D data);
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	public String toString();
}
