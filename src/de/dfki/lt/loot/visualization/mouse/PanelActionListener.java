/*
 * 
 */
package de.dfki.lt.loot.visualization.mouse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.uci.ics.jung.visualization.VisualizationViewer;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving panelAction events.
 * The class that is interested in processing a panelAction
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addPanelActionListener<code> method. When
 * the panelAction event occurs, that object's appropriate
 * method is invoked.
 * 
 * @param <V> the value type
 * @param <E> the element type
 * @see PanelActionEvent
 */
public class PanelActionListener<V, E> implements ActionListener {
	
	/** The vertex. */
	private V vertex;
    
    /** The vis comp. */
    private VisualizationViewer visComp;
	
	/** The _information panel. */
	private JPanel _informationPanel;
	
	/**
	 * Instantiates a new panel action listener.
	 * 
	 * @param information the information
	 */
	public PanelActionListener( JPanel information){
		_informationPanel = information;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//visComp.getPickedVertexState().pick(vertex, false);
        _informationPanel.add(new JLabel("Test2"));
        
        //visComp.repaint();
        System.out.println("Action");

	}

}
