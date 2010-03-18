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
import de.dfki.lt.loot.visualization.nodes.GraphNode;

// TODO: Auto-generated Javadoc
/**
 * The Class IndividualViewer.
 */
public class IndividualViewer {
	
	/** The _individual view. */
	private JPanel _individualView;
	
	/**
	 * Instantiates a new individual viewer.
	 * 
	 * @param nodes the nodes
	 * @param informationPanel the information panel
	 */
	public IndividualViewer(HashMap<String, GraphNode> nodes, InformationPanel informationPanel)
	{
		_individualView = new JPanel(new BorderLayout());
		JTextArea system = new JTextArea(nodes.get("0").getData().toString());
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

}
