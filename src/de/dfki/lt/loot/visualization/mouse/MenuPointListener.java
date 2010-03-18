/*
 * 
 */
package de.dfki.lt.loot.visualization.mouse;

import java.awt.geom.Point2D;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving menuPoint events.
 * The class that is interested in processing a menuPoint
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addMenuPointListener<code> method. When
 * the menuPoint event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see MenuPointEvent
 */
public interface MenuPointListener {
 
 /**
  * Sets the point.
  * 
  * @param point the new point
  */
 void   setPoint(Point2D point);
}
