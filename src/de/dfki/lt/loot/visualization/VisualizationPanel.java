package de.dfki.lt.loot.visualization;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;

import de.dfki.lt.loot.gui.jgraphviewer.GraphViewer;
import de.dfki.lt.loot.visualization.edges.DataGraphEdge;
import de.dfki.lt.loot.visualization.exceptions.HandlerException;
import de.dfki.lt.loot.visualization.graph.IndividualViewer;
import de.dfki.lt.loot.visualization.graph.SimpleGraphViewer;
import de.dfki.lt.loot.visualization.graph.Viewer;
import de.dfki.lt.loot.visualization.nodes.GraphNode;


/**
 * The Class VisualizationPanel. This Class is used to handel the different Types of GraphVisualisation.<br/>
 * This Panel contains the menus, the left information Panel and at the right the visualization Panel.<br/>
 * This <i>should be</i> the Class used by the adapter classes. But can be bypassed by using directly a Viewer class.
 * @autor chris
 */
public class VisualizationPanel {
	
	/** The visualization container.  Implemented Viewer Class for the graph visualization.<br/>
	 *  Will be print at the right side of the VisualizationPanel.
	 */
	private Viewer _visualizationContainer;

	/** The informations panel. Contains informations about the graph visualization ( only if the viewer use it )*/
	private InformationPanel _informations;
	
	
	/**
	 * Instantiates a new visualization panel.
	 * 
	 * @param root the root
	 * @param nodes the nodes
	 * @param edges the edges
	 * @param type the type of Graph
	 * @throws HandlerException the handler exception
	 */
	public VisualizationPanel(GraphNode root, HashMap<String, GraphNode> nodes, Vector<DataGraphEdge> edges, Type type) throws HandlerException
	{
		_informations = new InformationPanel();
		
		_visualizationContainer = viewHandler(root, nodes, edges, type);
		
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
	 * View handler. Witch Viewer is appropirate ? Are they enough informations passed in the constructor ?
	 * 
	 * @param root the root
	 * @param nodes the nodes
	 * @param edges the edges
	 * @param type the type
	 * @return the j panel for the Graph visualization
	 * @throws HandlerException the handler exception
	 */
	private Viewer viewHandler(final GraphNode root, final HashMap<String, GraphNode> nodes, final Vector<DataGraphEdge> edges, final Type type) throws HandlerException
	{
		switch(type){
			case DAGGRAPH:
				if(root != null && nodes != null && edges != null)
					return (new SimpleGraphViewer(root, nodes, edges, _informations));
				break;
			case INDIVIDUAL:
				if(root == null && nodes != null && edges == null)
					return (new IndividualViewer(nodes, _informations));
				break;
			case JGRAPH:
				if(root != null && nodes != null && edges != null)
					return(new GraphViewer<Object, Object>(root, nodes, edges, _informations));
				break;
			default:
				throw new HandlerException("There is no representation for these parameters");
				
				
		}
		return null;
	}
	
	/**
	 * To set the Highlighter
	 * 
	 * @param toLight the words, sentences to highlight
	 */
	public void highLight(String[] toLight)
	{
		_visualizationContainer.setHightLight(toLight);
	}
	
	/**
	 * To get the Visualization Panel view. Information + Graph + menus
	 * @return JPanel, the View
	 */
	public JPanel getView()
	{
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(800, 600));
		panel.setLayout(new BorderLayout());
		panel.add(_informations, BorderLayout.WEST);
		panel.add(_visualizationContainer.getView(), BorderLayout.CENTER);
		
		return panel;
		
	}

}
