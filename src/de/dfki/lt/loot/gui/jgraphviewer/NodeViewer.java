/*
 * 
 */
package de.dfki.lt.loot.gui.jgraphviewer;


import java.awt.Component;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;

// TODO: Auto-generated Javadoc
/**
 * The Class NodeView.
 */
public class NodeViewer extends VertexView {
	
	/** The renderer. */
	private static CompViewRenderer renderer = new CompViewRenderer();
	
	/**
	 * The Class CompViewRenderer.
	 */
	private static class CompViewRenderer extends VertexRenderer
	{	
		
		/**
		 * Instantiates a new comp view renderer.
		 */
		public CompViewRenderer()
		{
			super();
		}	

		/* (non-Javadoc)
		 * @see org.jgraph.graph.VertexRenderer#getRendererComponent(org.jgraph.JGraph, org.jgraph.graph.CellView, boolean, boolean, boolean)
		 */
		public Component getRendererComponent(JGraph jGraph, CellView cellView, boolean selected, boolean focused,
				boolean preview)
		{
			
			//DefaultGraphCell cell = (DefaultGraphCell) cellView.getCell();
			JComponentNode jcell = (JComponentNode)cellView.getCell();
			
			return jcell.getNodeView();
		}
		
	}

	/**
	 * Instantiates a new text node view.
	 * 
	 * @param cell the cell
	 */
	public NodeViewer(Object cell)
	{
		super(cell);
	}

	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.gui.jgraphviewer.NodeView#getRenderer()
	 */
	@Override
	public CellViewRenderer getRenderer()
	{
		return renderer;
	}
}
