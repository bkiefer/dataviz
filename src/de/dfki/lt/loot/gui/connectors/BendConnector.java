/*
 * 
 */
package de.dfki.lt.loot.gui.connectors;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

// TODO: Auto-generated Javadoc
/**
 * The Class BendConnector.
 */
public class BendConnector extends Connector {
  
  /** The _y points. */
  int[] _xPoints, _yPoints;
  
  /** The _n points. */
  int _nPoints;
  
  /** The _from delta y. */
  double _fromDeltaX, _fromDeltaY;
  
  /** The _to delta y. */
  double _toDeltaX, _toDeltaY;

  /**
   * Sets the deltas.
   * 
   * @param fromAngle the from angle
   * @param toAngle the to angle
   */
  private void setDeltas(double fromAngle, double toAngle) {
    _fromDeltaX = Math.acos(fromAngle);
    _fromDeltaX = Math.asin(fromAngle);
    _toDeltaX = Math.acos(toAngle);
    _toDeltaX = Math.asin(toAngle);
  }
  
  /**
   * Instantiates a new bend connector.
   * 
   * @param from the from
   * @param to the to
   * @param fromAngle the from angle
   * @param toAngle the to angle
   */
  public BendConnector(GraphicalNode from, GraphicalNode to,
      double fromAngle, double toAngle) {
    super(from, to);
    setDeltas(fromAngle, toAngle);
  }
  
  /**
   * Instantiates a new bend connector.
   * 
   * @param from the from
   * @param to the to
   * @param fromAngle the from angle
   * @param toAngle the to angle
   * @param s the s
   */
  public BendConnector(GraphicalNode from, GraphicalNode to,
      double fromAngle, double toAngle, Style s) {
    super(from, to, s);
    setDeltas(fromAngle, toAngle);
  }
  
  /**
   * Gets the direction.
   * 
   * @param rect the rect
   * @param rimPoint the rim point
   * @return the direction
   */
  public int getDirection(Rectangle rect, Point2D rimPoint) {
    if ((rimPoint.getX() > rect.x)
        && (rimPoint.getX() < rect.x + rect.width)) {
      // it's either up (-1) or down (1)
      return ((rimPoint.getY() < rect.y + rect.height >>> 1) ? -1 : 1);
    }
    // either left (-2) or right (2)
    return ((rimPoint.getX() < rect.x + rect.width >>> 1) ? -2 : 2);
  }
  
  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.connectors.Connector#adjust(java.awt.Graphics)
   */
  @Override
  public void adjust(Graphics g) {
    Rectangle fromRect = _fromNode.getRect();
    Rectangle toRect = _toNode.getRect();
    
    if (_fromDeltaX == 0 && _fromDeltaY == 0) {
      _fromDeltaX = toRect.getCenterX() - fromRect.getCenterX();
      _fromDeltaY = toRect.getCenterY() - fromRect.getCenterY();
      _toDeltaX = - _fromDeltaX;
      _toDeltaY = - _fromDeltaY;
    }
    
    Point2D from, to;
    _xPoints = new int[5]; _yPoints = new int[5];
    from = _fromNode.pointOnRim(_fromDeltaX, _fromDeltaY);
    to = _toNode.pointOnRim(_toDeltaX, _toDeltaY);
    int dirFrom = getDirection(fromRect, from);
    int dirTo = getDirection(toRect, to);
    _xPoints[0] = (int) from.getX();
    _yPoints[0] = (int) to.getY();

  }

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.connectors.Connector#paintAbsolute(java.awt.Rectangle, java.awt.Graphics)
   */
  @Override
  public void paintAbsolute(Rectangle absRect, Graphics g) {
    g.drawPolyline(_xPoints, _yPoints, _nPoints);
  }

}
