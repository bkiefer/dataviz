/*
 * 
 */
package de.dfki.lt.loot.gui.jgraphviewer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;

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
public class JComponentNode<I, D> extends DefaultGraphCell implements Node<I, D> 
{
	
	/** The _view. */
	/*
	private NodeViewer _view;
	*/
	/** The _node. */
	private Node<I, D> _node;
	
	/** The _comp. */
	private JPanel _comp = null;

	/**
	 * Instantiates a new j component node.
	 * 
	 * @param node the node
	 * @param view the view
	 */
	public JComponentNode(Node<I, D> node, VertexView view, JPanel comp)
	{
		//_view = (NodeViewer) view;
		_node = node;
		_comp = comp;
		
		AttributeMap map = new AttributeMap();
		
		ComponentBoundsCreator.setComponentBounds(_comp);
		
		GraphConstants.setBounds(map, comp.getBounds());
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
	public JComponentNode(Node<I, D> node, JPanel comp)
	{
		this(node, new NodeViewer(node), comp);
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
	/**
	 * Gets the component ( the graphical visualization of the node )
	 * 
	 * @return JPanel the graphical visualization of the node
	 */
	public JPanel getComp(){
		return _comp;
	}
	
	/**
	 * Sets the component ( the graphical visualization of the node )
	 * 
	 * @param comp  the graphical visualization of the node
	 */
	public void setComp(JPanel comp){
		_comp = comp;
	}
}