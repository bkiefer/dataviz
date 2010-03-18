/*
 * 
 */
package de.dfki.lt.loot.visualization;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.dfki.lt.loot.gui.jgraphviewer.GraphViewer;
import de.dfki.lt.loot.visualization.edges.DataGraphEdge;
import de.dfki.lt.loot.visualization.exceptions.HandlerException;
import de.dfki.lt.loot.visualization.graph.IndividualViewer;
import de.dfki.lt.loot.visualization.graph.SimpleGraphViewer;
import de.dfki.lt.loot.visualization.nodes.GraphNode;


// TODO: Auto-generated Javadoc
/**
 * The Class VisualizationPanel.
 */
@SuppressWarnings("serial")
public class VisualizationPanel extends JPanel {
	
	/** The _visualization container. */
	private JPanel _visualizationContainer;
	//private JScrollPane _scrollPanel;
	//private InformationPanel<GraphNode, DataGraphEdge> _informationsPanel;
	/** The _informations. */
	private InformationPanel _informations;
	
	
	/**
	 * Instantiates a new visualization panel.
	 * 
	 * @param root the root
	 * @param nodes the nodes
	 * @param edges the edges
	 * @param type the type
	 * @throws HandlerException the handler exception
	 */
	public VisualizationPanel(GraphNode root, HashMap<String, GraphNode> nodes, Vector<DataGraphEdge> edges, Type type) throws HandlerException
	{
		_informations = new InformationPanel();
		
		_visualizationContainer = viewHandler(root, nodes, edges, type);
		this.setSize(new Dimension(800, 600));
		this.setLayout(new BorderLayout());
		this.add(_informations, BorderLayout.WEST);
		this.add(_visualizationContainer, BorderLayout.CENTER);
	}
	
	/**
	 * Instantiates a new visualization panel.
	 * 
	 * @param root the root
	 * @param nodes the nodes
	 * @param type the type
	 * @throws HandlerException the handler exception
	 */
	public VisualizationPanel(GraphNode root, HashMap<String, GraphNode> nodes, Type type) throws HandlerException
	{
		this(root, nodes, null, type);
	}
	
	/**
	 * Instantiates a new visualization panel.
	 * 
	 * @param nodes the nodes
	 * @param type the type
	 * @throws HandlerException the handler exception
	 */
	public VisualizationPanel(HashMap<String, GraphNode> nodes, Type type) throws HandlerException
	{
		this(null, nodes, null, type);
	}
	
	
	/**
	 * View handler.
	 * 
	 * @param root the root
	 * @param nodes the nodes
	 * @param edges the edges
	 * @param type the type
	 * @return the j panel
	 * @throws HandlerException the handler exception
	 */
	private JPanel viewHandler(final GraphNode root, final HashMap<String, GraphNode> nodes, final Vector<DataGraphEdge> edges, final Type type) throws HandlerException
	{
		switch(type){
			case DAGGRAPH:
				if(root != null && nodes != null && edges != null)
					return (new SimpleGraphViewer(root, nodes, edges, _informations)).getView();
				break;
			case INDIVIDUAL:
				if(root == null && nodes != null && edges == null)
					return (new IndividualViewer(nodes, _informations)).getView();
				break;
			case JGRAPH:
				if(root != null && nodes != null && edges != null)
					return(new GraphViewer<Object, Object>(root, nodes, edges, _informations)).getView();
				break;
			default:
				throw new HandlerException("There is no representation for these parameters");
				
				
		}
		return null;
	}

}
