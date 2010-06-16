package de.dfki.lt.loot.gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class Controller implements MouseMotionListener {

  DrawingPanel view ;

  GraphicalNode underMouse;

  Controller(DrawingPanel dp) {
    view = dp;
    dp.addMouseMotionListener(this);
    underMouse = null;
  }

  public void mouseDragged(MouseEvent e) {
    // TODO Auto-generated method stub
    return;
  }

  public void mouseMoved(MouseEvent e) {
    // TODO Auto-generated method stub
    Point p = e.getPoint();
    boolean invalidate = false;
    if (view.getVisibleRect().contains(p) && view.getRoot() != null) {
      GraphicalNode nowPointsTo =
        ((underMouse == null) ? view.getRoot() : underMouse)
        .getDeepestIncludingPoint(p);

      // Das redraw klappt so nur bedingt, weil eigentlich zuerst das alte\
      // zurueckgesetzt und dann das neue gezeichnet werden muesste.
      // das hat auch was damit zu tun, wie ich das in GraphicalNode
      // im Moment mit dem Zeichnen mache.
      if (nowPointsTo != underMouse) {
        if (underMouse != null) {
          invalidate = true;
          underMouse.mouseLeaves();
          Rectangle r = underMouse.getAbsRect(); r.grow(1,1);
          view.repaint(r);
        }
        if (nowPointsTo != null) {
          invalidate = true;
          nowPointsTo.mouseEnters();
          Rectangle r = nowPointsTo.getAbsRect(); r.grow(1,1);
          view.repaint(r);
        }
        underMouse = nowPointsTo;
      }
    }
    if (invalidate) view.invalidate();
  }
}
