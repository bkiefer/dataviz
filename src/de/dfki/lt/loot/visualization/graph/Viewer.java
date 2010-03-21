package de.dfki.lt.loot.visualization.graph;

import javax.swing.JPanel;

/**
 * Viewer Interface, for the graph JPanels.
 * 
 * @author chris
 *
 */
public interface Viewer {
	
	/**
	 * To obtain the JPanel with the graph visualisation
	 * 
	 * @return JPanel the graph visualisation
	 */
	public JPanel getView();
	
	/**
	 * To set the highlight text in the graphical nodes
	 * 
	 * @param toLight the text that must be searcht
	 * @return int the number of match or 0 if none
	 */
	public int setHightLight(String[] toLight);
	
}
