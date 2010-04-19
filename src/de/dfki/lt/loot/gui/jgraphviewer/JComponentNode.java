/*
 * 
 */
package de.dfki.lt.loot.gui.jgraphviewer;

import java.awt.Color;


import javax.swing.JPanel;

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
public class JComponentNode extends DefaultGraphCell implements Node
{
	
	/** The _view. */
	/*
	private NodeViewer _view;
	*/
	/** The _node. */
	private Node _node;

	/**
	 * Instantiates a new j component node.
	 * 
	 * @param node the node
	 * @param view the view
	 */
	public JComponentNode(Node node, VertexView view)
	{
		//_view = (NodeViewer) view;
		_node = node;
		
		AttributeMap map = new AttributeMap();
		
		ComponentBoundsCreator.setComponentBounds(node.getNodeView());
		
		GraphConstants.setBounds(map, node.getNodeView().getBounds());
		GraphConstants.setGradientColor(map, Color.white.brighter());
		GraphConstants.setBorderColor(map, Color.blue);
		GraphConstants.setBackground(map, Color.RED);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setEditable(map, false);
		this.setAttributes(map);
	}
	
	/**
	 * Instantiates a new j component node, with a TextNodeView.
	 * 
	 * @param node the node
	 */
	public JComponentNode(Node node)
	{
		this(node, new NodeViewer(node));
	}
	
	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.visualization.nodes.Node#setData(java.lang.Object)
	 */
	public void setData(Object data) {
		
		_node.setData(data);
		
	}

	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.visualization.nodes.Node#getData()
	 */
	@Override
	public Object getData() {
		return _node.getData();
	}

	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.visualization.nodes.Node#getId()
	 */
	@Override
	public Object getId() {
		return _node.getId();
	}
	
	/**
	 * Gets the node.
	 * 
	 * @return the node
	 */
	public Node getNode() {
		return _node;
	}
	
	/**
	 * Sets the node.
	 * 
	 * @param node the node
	 */
	public void setNode(Node node) {
		_node = node;
		//_view.setCell(node);
	}
	
	/**
	 * Gets the view.
	 * 
	 * @return the view
	 */
	/*
	public NodeViewer getView() {
		return _view;
	}
	*/

	@Override
	public Object getName() {
		
		return _node.getName();
	}

	@Override
	public void setName(Object name) {
		_node.setName(name);
		
	}

	@Override
	public JPanel getNodeView() {
		return _node.getNodeView();
	}

	@Override
	public void setNodeView(JPanel view) {
		_node.setNodeView(view);
		
	}
}