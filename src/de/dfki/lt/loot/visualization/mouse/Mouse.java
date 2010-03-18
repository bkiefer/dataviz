/*
 * 
 */
package de.dfki.lt.loot.visualization.mouse;

import de.dfki.lt.loot.visualization.InformationPanel;
import de.dfki.lt.loot.visualization.edges.DataGraphEdge;
import de.dfki.lt.loot.visualization.nodes.GraphNode;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;

// TODO: Auto-generated Javadoc
/**
 * The Class Mouse.
 * 
 * @param <V> the value type
 * @param <E> the element type
 */
public class Mouse<V, E> extends DefaultModalGraphMouse<V, E> {
	
	
	/** The plugin. */
	private VertexMouseMenu<V, E> plugin;
	
	/** The data item. */
	private NodePopUpMenu<V, E> dataItem;
	
	/**
	 * Instantiates a new mouse.
	 */
	public Mouse()
	{
		plugin = new VertexMouseMenu<V, E>();
		dataItem = new NodePopUpMenu<V, E>();
		
		plugin.setVertexPopup(dataItem);
		//gm.remove(gm.getEditingPlugin());
		//gm.remove(gm.getPopupEditingPlugin());
		this.add(new PickingGraphMousePlugin<GraphNode, DataGraphEdge>());
		this.add(plugin);
	}
	
	/**
	 * Instantiates a new mouse.
	 * 
	 * @param informationPanel the information panel
	 */
	public Mouse(InformationPanel informationPanel)
	{
		plugin = new VertexMouseMenu<V, E>();
		dataItem = new NodePopUpMenu<V, E>(informationPanel);
		
		plugin.setVertexPopup(dataItem);
		//gm.remove(gm.getEditingPlugin());
		//gm.remove(gm.getPopupEditingPlugin());
		this.add(new PickingGraphMousePlugin<GraphNode, DataGraphEdge>());
		this.add(plugin);
	}

}
