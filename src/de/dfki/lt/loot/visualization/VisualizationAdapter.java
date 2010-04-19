
package de.dfki.lt.loot.visualization;

import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;

import de.dfki.lt.loot.HighLighter;
import de.dfki.lt.loot.visualization.edges.DataGraphEdge;
import de.dfki.lt.loot.visualization.nodes.GraphNode;
import de.dfki.lt.loot.visualization.nodes.Node;

/**
 * The Interface VisualizationAdapter.
 * @author chris
 */
public interface VisualizationAdapter {

	
	/**
	 * Getter for the Node.
	 * 
	 * @return the root node, null if none
	 */
	public String getRoot();
	
	/**
	 * Nodes getter
	 * 
	 * @return nodes, null if none
	 */
	public HashMap<String, Node> getNodes();
	
	/**
	 * Edges getter
	 * 
	 * @return edges, null if none
	 */
	public Vector<DataGraphEdge> getEdges();
	
	/**
	 * Types Getter: Witch kind of visualization ?
	 * 
	 * @return types : the Type array, null if none
	 */
	public Type[] getTypes();
	
	/**
	 * HighLighter Getter
	 * 
	 * @return HighLighter<?>
	 */
	public HighLighter getHighLighter();
	
	/**
	 * HighLighter Setter
	 * 
	 * @param high, HighLighter;
	 */
	public void setHighLighter(HighLighter high);
	
}
