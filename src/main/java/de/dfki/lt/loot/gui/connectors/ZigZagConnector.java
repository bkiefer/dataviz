package de.dfki.lt.loot.gui.connectors;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class ZigZagConnector extends Connector {

  private int _bendSize = 0;

  Point _from, _mid, _to;

  char _out;

  /** A connector that uses a square angle (with an optional small diagonal
   *  bend) to connect two nodes, such that nodes are connected top to bottom (V)
   *  or left to right (H), so this connector has two bends
   * @param out The orientation of the connector (H,V)
   */
  public ZigZagConnector(GraphicalNode from, GraphicalNode to, char out) {
    super(from, to);
    _out = out;
  }

  /** A connector that uses a square angle (with an optional small diagonal
   *  bend) to connect two nodes. The from node will be left on the bottom
   *  or top, while the to node will be entered on left or right side.
   * @param in
   * @param out
   */
  public ZigZagConnector(GraphicalNode from, GraphicalNode to, char out,
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
      int leftToRight = (fromRect.x < toRect.x) ? 1 : -1;
      //int topToBottom = (fromRect.y < toRect.y) ? 1 : -1;
      int fromX = fromRect.x + ((fromRect.x < toRect.x) ? fromRect.width : 0) ;
      int fromY = fromRect.y + (fromRect.height / 2);
      int toX = toRect.x + ((fromRect.x < toRect.x) ? 0 : toRect.width) ;
      int toY = toRect.y + (toRect.height / 2);
      int midX = toX + ((fromX - toX) / 2);
      int midY = toY + ((fromY - toY) / 2);
      _from = new Point(fromX, fromY);
      _mid = new Point(midX, midY);
      _to = new Point(toX, toY);
      break;
    }
    case 'V' :
    case 'v' : {
      int topToBottom = (fromRect.y < toRect.y) ? 1 : -1;
      int fromX = fromRect.x + (fromRect.width / 2);
      int fromY = fromRect.y + topToBottom * (fromRect.height / 2);
      int toX = toRect.x + (toRect.width / 2);
      int toY = toRect.y - topToBottom * (toRect.height / 2);
      int midX = toX + ((fromX - toX) / 2);
      int midY = toY + ((fromY - toY) / 2);
      _from = new Point(fromX, fromY);
      _mid = new Point(midX, midY);
      _to = new Point(toX, toY);
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
          absRect.x + _mid.x,
          absRect.x + _mid.x,
          absRect.x + _to.x
      };
      int[] yPoints = {
          absRect.y + _from.y,
          absRect.y + _from.y,
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
          absRect.x + _from.x,
          absRect.x + _to.x,
          absRect.x + _to.x
      };
      int[] yPoints = {
          absRect.y + _from.y,
          absRect.y + _mid.y,
          absRect.y + _mid.y,
          absRect.y + _to.y
      };
      g.drawPolyline(xPoints, yPoints, xPoints.length);
      break;
    }
    }
  }

  public void paintAbsolute0(Rectangle absRect, Graphics g) {
    int xsgn = (_to.x < _from.x ? -1 : 1);
    int ysgn = (_to.y < _from.y ? -1 : 1);
    switch (_out) {
    case 'H' :
    case 'h' : {
      int[] xPoints = {
          absRect.x + _from.x,
          absRect.x + _from.x,
          (absRect.x + _from.x + xsgn * _bendSize),
          absRect.x + _to.x
      };
      int[] yPoints = {
          absRect.y + _from.y,
          (absRect.y + _to.y - ysgn * _bendSize),
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
          (absRect.x + _to.x - xsgn * _bendSize),
          absRect.x + _to.x,
          absRect.x + _to.x
      };
      int[] yPoints = {
          absRect.y + _from.y,
          absRect.y + _from.y,
          (absRect.y + _from.y + ysgn * _bendSize),
          absRect.y + _to.y
      };
      g.drawPolyline(xPoints, yPoints, xPoints.length);
      break;
    }
    }
  }

}
