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
 * The Class StraightConnector.
 */
public class StraightConnector extends Connector {

  /** The _to. */
  Point2D _from, _to;
  
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
   * Instantiates a new straight connector.
   * 
   * @param from the from
   * @param to the to
   * @param fromAngle the from angle
   * @param toAngle the to angle
   */
  public StraightConnector(GraphicalNode from, GraphicalNode to,
      double fromAngle, double toAngle) {
    super(from, to);
    setDeltas(fromAngle, toAngle);
  }
  
  /**
   * Instantiates a new straight connector.
   * 
   * @param from the from
   * @param to the to
   * @param fromAngle the from angle
   * @param toAngle the to angle
   * @param s the s
   */
  public StraightConnector(GraphicalNode from, GraphicalNode to,
      double fromAngle, double toAngle, Style s) {
    super(from, to, s);
    setDeltas(fromAngle, toAngle);
  }
  
  /**
   * Instantiates a new straight connector.
   * 
   * @param from the from
   * @param to the to
   */
  public StraightConnector(GraphicalNode from, GraphicalNode to) {
    super(from, to);
    _fromDeltaX = _fromDeltaY = 0;
  }
  
  /**
   * Instantiates a new straight connector.
   * 
   * @param from the from
   * @param to the to
   * @param s the s
   */
  public StraightConnector(GraphicalNode from, GraphicalNode to, Style s) {
    super(from, to, s);
    _fromDeltaX = _fromDeltaY = 0;
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
    
    _from = _fromNode.pointOnRim(_fromDeltaX, _fromDeltaY);
    _to = _toNode.pointOnRim(_toDeltaX, _toDeltaY);
  }

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.connectors.Connector#paintAbsolute(java.awt.Rectangle, java.awt.Graphics)
   */
  @Override
  public void paintAbsolute(Rectangle absRect, Graphics g) {
    g.drawLine((int) (absRect.x + _from.getX()),
        (int) (absRect.y + _from.getY()),
        (int) (absRect.x + _to.getX()),
        (int) (absRect.y + _to.getY()));
  }

}
