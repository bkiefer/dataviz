/*
 * 
 */
package de.dfki.lt.loot.gui.jgraphviewer;


import java.awt.Component;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;

// TODO: Auto-generated Javadoc
/**
 * The Class NodeView.
 */
public abstract class NodeView extends VertexView {
	
	/**
	 * Instantiates a new node view.
	 * 
	 * @param cell the cell
	 */
	public NodeView(Object cell)
	{
		super(cell);
	}

	/* (non-Javadoc)
	 * @see org.jgraph.graph.VertexView#getRenderer()
	 */
	@Override
	public CellViewRenderer getRenderer()
	{
		return null;
	}
	
	/**
	 * Gets the component.
	 * 
	 * @return the component
	 */
	public Component getComponent()
	{
		return null;
	}
	
	

}
