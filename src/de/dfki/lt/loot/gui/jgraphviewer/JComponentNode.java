/*
 * 
 */
package de.dfki.lt.loot.gui.jgraphviewer;

import java.awt.Color;
import java.awt.Rectangle;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;

import de.dfki.lt.loot.visualization.nodes.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class JComponentNode.
 * 
 * @param <I> the generic type
 * @param <D> the generic type
 */
public class JComponentNode<I, D> extends DefaultGraphCell implements Node<I, D> 
{
	
	/** The _view. */
	private NodeView _view;
	
	/** The _node. */
	private Node<I, D> _node;

	/**
	 * Instantiates a new j component node.
	 * 
	 * @param node the node
	 * @param view the view
	 */
	public JComponentNode(Node<I, D> node, VertexView view )
	{
		_view = (NodeView) view;
		_node = node;
		
		AttributeMap map = new AttributeMap();
		Rectangle rec = null;
		rec = new Rectangle(20, 20, 300, 300);
		
		GraphConstants.setBounds(map, rec);
		GraphConstants.setGradientColor(map, Color.white.brighter());
		GraphConstants.setBorderColor(map, Color.blue);
		GraphConstants.setBackground(map, Color.RED);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setEditable(map, false);
		this.setAttributes(map);
	}
	
	/**
	 * Instantiates a new j component node.
	 * 
	 * @param node the node
	 */
	public JComponentNode(Node<I, D> node)
	{
		this(node, new TextNodeView(node));
	}
	
	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.visualization.nodes.Node#setData(java.lang.Object)
	 */
	public void setData(D data) {
		
		_node.setData(data);
		
	}

	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.visualization.nodes.Node#getData()
	 */
	@Override
	public D getData() {
		return _node.getData();
	}

	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.visualization.nodes.Node#getId()
	 */
	@Override
	public I getId() {
		return _node.getId();
	}
	
	/**
	 * Gets the node.
	 * 
	 * @return the node
	 */
	public Node<I, D> getNode() {
		return _node;
	}
	
	/**
	 * Sets the node.
	 * 
	 * @param node the node
	 */
	public void setNode(Node<I, D> node) {
		_node = node;
		_view.setCell(node);
	}
	
	/**
	 * Gets the view.
	 * 
	 * @return the view
	 */
	public NodeView getView() {
		return _view;
	}
}