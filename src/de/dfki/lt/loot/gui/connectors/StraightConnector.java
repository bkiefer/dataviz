package de.dfki.lt.loot.gui.connectors;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class StraightConnector extends Connector {

  Point2D _from, _to;
  double _fromDeltaX, _fromDeltaY;
  double _toDeltaX, _toDeltaY;

  private void setDeltas(double fromAngle, double toAngle) {
    _fromDeltaX = Math.acos(fromAngle);
    _fromDeltaX = Math.asin(fromAngle);
    _toDeltaX = Math.acos(toAngle);
    _toDeltaX = Math.asin(toAngle);
  }
  
  public StraightConnector(GraphicalNode from, GraphicalNode to,
      double fromAngle, double toAngle) {
    super(from, to);
    setDeltas(fromAngle, toAngle);
  }
  
  public StraightConnector(GraphicalNode from, GraphicalNode to,
      double fromAngle, double toAngle, Style s) {
    super(from, to, s);
    setDeltas(fromAngle, toAngle);
  }
  
  public StraightConnector(GraphicalNode from, GraphicalNode to) {
    super(from, to);
    _fromDeltaX = _fromDeltaY = 0;
  }
  
  public StraightConnector(GraphicalNode from, GraphicalNode to, Style s) {
    super(from, to, s);
    _fromDeltaX = _fromDeltaY = 0;
  }
  

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

  @Override
  public void paintAbsolute(Rectangle absRect, Graphics g) {
    g.drawLine((int) (absRect.x + _from.getX()),
        (int) (absRect.y + _from.getY()),
        (int) (absRect.x + _to.getX()),
        (int) (absRect.y + _to.getY()));
  }

}
