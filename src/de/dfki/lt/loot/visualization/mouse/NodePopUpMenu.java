/*
 * 
 */
package de.dfki.lt.loot.visualization.mouse;

import javax.swing.JPopupMenu;

import de.dfki.lt.loot.visualization.InformationPanel;


// TODO: Auto-generated Javadoc
/**
 * The Class NodePopUpMenu.
 * 
 * @param <V> the value type
 * @param <E> the element type
 */
@SuppressWarnings("serial")
public class NodePopUpMenu<V, E> extends JPopupMenu {
	
	/**
	 * Instantiates a new node pop up menu.
	 * 
	 * @param informationPanel the information panel
	 */
	public NodePopUpMenu(InformationPanel informationPanel) {
		super("Node Menu");
		this.add(new DataMenuItem<V, E>(informationPanel));
		this.addSeparator();
	}
	
	/**
	 * Instantiates a new node pop up menu.
	 */
	public NodePopUpMenu() {
		super("Node Menu");
		this.add(new DataMenuItem<V, E>());
		this.addSeparator();
	}
}