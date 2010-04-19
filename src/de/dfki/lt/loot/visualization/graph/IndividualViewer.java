/*
 * 
 */
package de.dfki.lt.loot.visualization.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import de.dfki.lt.loot.visualization.InformationPanel;
import de.dfki.lt.loot.visualization.VisualizationAdapter;
import de.dfki.lt.loot.visualization.nodes.GraphNode;
import de.dfki.lt.loot.visualization.nodes.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class IndividualViewer.
 */
public class IndividualViewer implements Viewer{
	
	/** The name of this visualization. */
	private String _name; 
	
	/** The _individual view. */
	private JPanel _individualView;
	
	/** The Adapter */
	private VisualizationAdapter _adapter;
	
	/**
	 * Instantiates a new individual viewer.
	 * 
	 * @param nodes the nodes
	 * @param informationPanel the information panel
	 */
	public IndividualViewer(VisualizationAdapter adapter, InformationPanel informationPanel)
	{
		_adapter = adapter;
		
		_individualView = new JPanel(new BorderLayout());
		JTextArea system = new JTextArea(((GraphNode) _adapter.getNodes().values().toArray()[0]).getData().toString());
		system.setBackground(new Color(237, 237, 237));
		JScrollPane scroll = new JScrollPane(system);
		scroll.setBackground(new Color(237, 237, 237));
		scroll.setBorder(new TitledBorder("System : "));
		_individualView.add(scroll, BorderLayout.CENTER);
		system.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
		});
	}
	
	/**
	 * Gets the view.
	 * 
	 * @return the view
	 */
	public JPanel getView(){
		return _individualView;
	}

	@Override
	public int setHightLight(String[] toLight) {
		// TODO Not implemented now
		return 0;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public void setName(String name) {
		_name = name;
		
	}
	
	/**
	 * To String.
	 * 
	 * @return The name of this visualization. Usefull for the Menu creation.
	 */
	public String toString()
	{
		return _name;
	}

	@Override
	public VisualizationAdapter getAdapter() {
		
		return _adapter;
	}

}
