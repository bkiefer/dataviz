package de.dfki.lt.loot.gui.connectors;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class SquareBendConnector extends Connector {

  private int _bendSize = 0;

  Point _from, _to;

  char _out;

  /** A connector that uses a square angle (with an optional small diagonal
   *  bend) to connect two nodes. The from node will be left on the bottom
   *  or top, while the to node will be entered on left or right side.
   * @param out The orientation of the outgoing part of the connector (H,V)
   */
  public SquareBendConnector(GraphicalNode from, GraphicalNode to, char out) {
    super(from, to);
    _out = out;
  }

  /** A connector that uses a square angle (with an optional small diagonal
   *  bend) to connect two nodes. The from node will be left on the bottom
   *  or top, while the to node will be entered on left or right side.
   * @param in
   * @param out
   */
  public SquareBendConnector(GraphicalNode from, GraphicalNode to, char out,
      Style s) {
    super(from, to, s);
    _out = out;
  }

  @Override
  public void adjust(Graphics g) {
    if (_bendSize == 0) {
      assert (_style.getFont() != null);
      Rectangle charBounds =
        _style.getFont().getStringBounds("x",
            ((Graphics2D) g).getFontRenderContext()).getBounds();
      _bendSize = charBounds.height;
    }
    Rectangle fromRect = _fromNode.getRect();
    Rectangle toRect = _toNode.getRect();

    switch (_out) {
    case 'H' :
    case 'h' : {
      int fromX = fromRect.x + (fromRect.width / 2);
      int toY = toRect.y + (toRect.height / 2);
      _from = new Point(fromX,
          (toY < fromRect.y + (fromRect.height / 2))
          ? fromRect.y : fromRect.y + fromRect.height);
      _to = new Point((fromX < (toRect.x + (toRect.width / 2))
          ? toRect.x : toRect.x + toRect.width), toY);
      break;
    }
    case 'V' :
    case 'v' : {
      int fromY = fromRect.y + (fromRect.height / 2);
      int toX = toRect.x + (toRect.width / 2);
      _from = new Point((toX < (fromRect.x + (fromRect.width / 2))
          ? fromRect.x : fromRect.x + fromRect.width), fromY);
      _to = new Point(toX, (fromY < toRect.y + (toRect.height / 2))
          ? toRect.y : toRect.y + toRect.height);
      break;
    }
    }
  }

  @Override
  public void paintAbsolute(Rectangle absRect, Graphics g) {
    switch (_out) {
    case 'H' :
    case 'h' : {
      int[] xPoints = {
          absRect.x + _from.x,
          absRect.x + _from.x,
          (int) (absRect.x + _from.x + Math.signum(_to.x - _from.x) * _bendSize),
          absRect.x + _to.x
      };
      int[] yPoints = {
          absRect.y + _from.y,
          (int) (absRect.y + _to.y + Math.signum(_from.y - _to.y) * _bendSize),
          absRect.y + _to.y,
          absRect.y + _to.y
      };
      g.drawPolyline(xPoints, yPoints, xPoints.length);
      break;
    }
    case 'V' :
    case 'v' : {
      int[] xPoints = {
          absRect.x + _from.x,
          (int) (absRect.x + _to.x + Math.signum(_from.x - _to.x) * _bendSize),
          absRect.x + _to.x,
          absRect.x + _to.x
      };
      int[] yPoints = {
          absRect.y + _from.y,
          absRect.y + _from.y,
          (int) (absRect.y + _from.y + Math.signum(_to.y - _from.y) * _bendSize),
          absRect.y + _to.y
      };
      g.drawPolyline(xPoints, yPoints, xPoints.length);
      break;
    }
    }
  }

}
