/*
 * 
 */
package de.dfki.lt.loot.visualization;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.ScrollPaneLayout;

import de.dfki.lt.loot.gui.jgraphviewer.GraphViewer;
import de.dfki.lt.loot.visualization.VisualizationFrame.Item;
import de.dfki.lt.loot.visualization.edges.DataGraphEdge;
import de.dfki.lt.loot.visualization.exceptions.HandlerException;
import de.dfki.lt.loot.visualization.graph.IndividualViewer;
import de.dfki.lt.loot.visualization.graph.SimpleGraphViewer;
import de.dfki.lt.loot.visualization.graph.Viewer;
import de.dfki.lt.loot.visualization.nodes.GraphNode;

// TODO: Auto-generated Javadoc
/**
 * The Class VisualizationPanel. This Class is used to handel the different Types of GraphVisualisation.<br/>
 * This Panel contains the menus, the left information Panel and at the right the visualization Panel.<br/>
 * This <i>should be</i> the Class used by the adapter classes. But can be bypassed by using directly a Viewer class.
 * @autor chris
 */
public class VisualizationPanel extends JFrame {
	
	
	/** The main JPanel */
	private JPanel _panel = new JPanel(new BorderLayout());
	
	
	/** The _combo. */
	private JComboBox _combo = new JComboBox();
	
	/** The visualization container.  Implemented Viewer Class for the graph visualization.<br/>
	 *  Will be print at the right side of the VisualizationPanel.
	 */
	private  Viewer _visualizationContainer;
	
	/** The _all viewer. */
	private Vector<Viewer> _allViewer;

	/** The informations panel. Contains informations about the graph visualization ( only if the viewer use it )*/
	private InformationPanel _informations, _visuPanel;
	
	
	/**
	 * Instantiates a new visualization panel.
	 * 
	 * @param root the root
	 * @param nodes the nodes
	 * @param edges the edges
	 * @param type the type of Graph
	 * @throws HandlerException the handler exception
	 */
	public VisualizationPanel(VisualizationAdapter adapter)
	{
		super();
		_allViewer = new Vector<Viewer>();
		_informations = new InformationPanel(new Dimension(200, 600), "Informations");
		_visuPanel = new InformationPanel("LogicalForm");
		
		for(int i = 0; i < adapter.getTypes().length; i++)
		{
			try {
				_allViewer.add(viewHandler(adapter, adapter.getTypes()[i]));
			} catch (HandlerException e) {
				e.printStackTrace();
			}
		}
		
		for(Viewer elements : _allViewer)
		{
			String[] h = {"Owner"};
			elements.setHightLight(h);
			_combo.addItem(elements);
		}
		
		
		_combo.addItemListener(new ItemState(adapter));
		_visualizationContainer = _allViewer.firstElement();
		
		_visuPanel.changeInfo(_visualizationContainer.getView());
		//_port.setView(_visualizationContainer.getView());
		
		_panel.setSize(new Dimension(800, 600));
		_panel.add(_informations, BorderLayout.WEST);
		_panel.add(_visuPanel, BorderLayout.CENTER);
		_combo.setPreferredSize(new Dimension(100, 50));
		_panel.add(_combo, BorderLayout.NORTH);
		
		this.setTitle("DebbugViewer");
    	
    	
    	//Nous allons maintenant dire ˆ notre objet de se positionner au centre
    	this.setLocationRelativeTo(null);
    	
    	//this.setSize(500, 500);
    	this.setSize(800, 600);
    	
    	this.setContentPane(_panel);
    	
    	 	
    	//Terminer le processus lorsqu'on clique sur "Fermer"
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	// Non modifiable
    	this.setResizable(true);

    	this.setVisible(true);
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
	private Viewer viewHandler(VisualizationAdapter adapter, Type type) throws HandlerException
	{
		switch(type){
			case DAGGRAPH:
				if(adapter.getRoot() != null && adapter.getNodes() != null && adapter.getEdges() != null)
					return (new SimpleGraphViewer(adapter, _informations));
				break;
			case INDIVIDUAL:
				if(adapter.getNodes() != null)
					return (new IndividualViewer(adapter, _informations));
				break;
			case JGRAPH:
				if(adapter.getRoot() != null && adapter.getNodes() != null && adapter.getEdges() != null)
					return(new GraphViewer(adapter, _informations));
				break;
			default:
				throw new HandlerException("There is no representation for these parameters");
				
		}
		return null;
	}
	
	/**
	 * To set the Highlighter.
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
		/**
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(800, 600));
		panel.setLayout(new BorderLayout());
		panel.add(_informations, BorderLayout.WEST);
		panel.add(_visualizationContainer.getView(), BorderLayout.CENTER);
		_combo.setPreferredSize(new Dimension(100, 50));
		panel.add(_combo, BorderLayout.NORTH);
		*/
		//_port.setView(_panel);
		return _panel;
		
	}
	
	public JComboBox combo()
	{
		return _combo;
	}
	
    /**
     * The Class ItemState.
     */
    class ItemState implements ItemListener{
    	VisualizationAdapter _adapter;
    		public ItemState (VisualizationAdapter adapter){
    			_adapter = adapter;
    		}
    		
            /**
             * Item state changed.
             * 
             * @param e the ItemEvent
             */
            public void itemStateChanged(ItemEvent e) {
            	if(e.getStateChange() == ItemEvent.SELECTED)
            	{
            		System.out.println("TETETETETETETETETETETETETETE");
            		_visualizationContainer = (Viewer)e.getItem();
                    //_informations.changeInfo(_visualizationContainer.getView());
                   
                   _visuPanel.changeInfo(_visualizationContainer.getView());
                   
                    
                    /*
                   JFrame frame = new JFrame();
                   
                   frame.setTitle("DebbugViewer2");
               	
               	
               	//Nous allons maintenant dire ˆ notre objet de se positionner au centre
                   frame.setLocationRelativeTo(null);
               	//this.setSize(500, 500);
                   frame.setSize(800, 600);
               	
               
               	
               	 	
               	//Terminer le processus lorsqu'on clique sur "Fermer"
                   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

               	// Non modifiable
                   frame.setResizable(true);

               		frame.setVisible(true);
                   
               		frame.setContentPane(getContentPane());
               		//setContentPane(_visuPanel);
                   //repaint();
                   getContentPane();
                   getContentPane().add(new JTextArea("tetetttebebbe e ebeb"), BorderLayout.CENTER);
                   repaint();
                   pack();
               		frame.pack();*/
                    
                    repaint();
                    
            	}
            }               
    }

}
