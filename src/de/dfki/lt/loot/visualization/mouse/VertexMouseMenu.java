/*
 * 
 */
package de.dfki.lt.loot.visualization.mouse;

/**
 * @Author: Yevgeni Barzak
 * Adapted from http://www.grotto-networking.com/JUNG/JUNG2-Tutorial.pdf
 * Original author: Dr. Greg M. Bernstein
 */
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JPopupMenu;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;



// TODO: Auto-generated Javadoc
/**
 * The Class VertexMouseMenu.
 * 
 * @param <V> the value type
 * @param <E> the element type
 */
public class VertexMouseMenu<V, E> extends AbstractPopupGraphMousePlugin {

	/** The edge popup. */
	private JPopupMenu edgePopup;
	
	/** The vertex popup. */
	private JPopupMenu vertexPopup;

	/**
	 * Instantiates a new vertex mouse menu.
	 */
	public VertexMouseMenu() {
		this(MouseEvent.BUTTON3_MASK);
	}

	/**
	 * Creates a new instance of PopupVertexEdgeMenuMousePlugin.
	 * 
	 * @param modifiers mouse event modifiers see the jung visualization Event class.
	 */
	public VertexMouseMenu(int modifiers) {
		super(modifiers);
	}

	/**
	 * Implementation of the VertexMouseMenu method.
	 * 
	 * @param e the e
	 */
	@SuppressWarnings("unchecked")
	protected void handlePopup(MouseEvent e) {
		final VisualizationViewer<V,E> vv =
			(VisualizationViewer<V,E>)e.getSource();
		Point2D p = e.getPoint();

		GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
		if(pickSupport != null) {
			//Hier habe ich was geŠndert : GraphNode => V und pickSupport.getVertex nicht gecastet
			final V v =  pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
			if(v != null) {
				// System.out.println("Vertex " + v + " was right clicked");
				updateVertexMenu(v, vv, p);
				vertexPopup.show(vv, e.getX(), e.getY());
			}
		}
	}

	/**
	 * Update vertex menu.
	 * 
	 * @param v the v
	 * @param vv the vv
	 * @param point the point
	 */
	private void updateVertexMenu(V v, VisualizationViewer vv, Point2D point) {
		if (vertexPopup == null) return;
		Component[] menuComps = vertexPopup.getComponents();
		for (Component comp: menuComps) {
			if (comp instanceof VertexListener) {
				((VertexListener) comp).setVertexAndView(v, vv);
			}
			if (comp instanceof MenuPointListener) {
				((MenuPointListener) comp).setPoint(point);
			}
		}

	}

	/**
	 * Getter for the edge popup.
	 * 
	 * @return the edge popup
	 * @return
	 */
	public JPopupMenu getEdgePopup() {
		return edgePopup;
	}

	/**
	 * Setter for the Edge popup.
	 * 
	 * @param edgePopup the new edge popup
	 */
	public void setEdgePopup(JPopupMenu edgePopup) {
		this.edgePopup = edgePopup;
	}

	/**
	 * Getter for the vertex popup.
	 * 
	 * @return the vertex popup
	 * @return
	 */
	public JPopupMenu getVertexPopup() {
		return vertexPopup;
	}

	/**
	 * Setter for the vertex popup.
	 * 
	 * @param vertexPopup the new vertex popup
	 */
	public void setVertexPopup(JPopupMenu vertexPopup) {
		this.vertexPopup = vertexPopup;
	}

}
