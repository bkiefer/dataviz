package de.dfki.lt.loot.visualization.exceptions;

public class NodeViewException extends Exception {
	
	/**
	 * Instantiates a new Node View node exception.
	 */
	public NodeViewException ()
	{
		super();
	}
	
	/**
	 * Instantiates a new Node View exception.
	 * 
	 * @param error the error
	 */
	public NodeViewException ( String error)
	{
		super(error);
	}

}
