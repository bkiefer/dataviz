package de.dfki.lt.loot.visualization.graph;

import java.awt.Component;

import javax.swing.JPanel;

/**
 * Viewer Interface, for the graph JPanels.
 * 
 * @author chris
 *
 */
public interface Viewer {
	
	/**
	 * To set the name of this visualization. Usefull for the Menu.
	 * 
	 * @param String name the new name for this visualization
	 */
	public void setName(String name);
	
	/**
	 * To get the name of this visualization. Usefull for the Menu
	 * 
	 * @return String the name
	 */
	public String getName();
	
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
