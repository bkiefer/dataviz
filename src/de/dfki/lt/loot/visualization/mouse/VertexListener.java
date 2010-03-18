/*
 * 
 */
package de.dfki.lt.loot.visualization.mouse;

import edu.uci.ics.jung.visualization.VisualizationViewer;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving vertex events.
 * The class that is interested in processing a vertex
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addVertexListener<code> method. When
 * the vertex event occurs, that object's appropriate
 * method is invoked.
 * 
 * @param <V> the value type
 * @param <E> the element type
 * @see VertexEvent
 */
public interface VertexListener<V, E> {
    
    /**
     * Sets the vertex and view.
     * 
     * @param v the v
     * @param visView the vis view
     */
    void setVertexAndView(V v, VisualizationViewer<V, E> visView);    
}

