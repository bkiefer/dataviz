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

  /** We don't currently support dragging events */
  public void mouseDragged(MouseEvent e) {
    return;
  }

  /** A preliminary implementation that has most of the needed functionality
   *  and good performance could easily be extended in a useful way.
   */
  public void mouseMoved(MouseEvent e) {
    Point p = e.getPoint();

    if (view.getVisibleRect().contains(p) && view.getRoot() != null) {
      GraphicalNode nowPointsTo =
        ((underMouse == null) ? view.getRoot() : underMouse)
        .getDeepestIncludingPoint(p);

      if (nowPointsTo != underMouse) {
        if (underMouse != null) {
          underMouse.mouseLeaves();
          // TODO the GraphicalNode must be able to render itself invalid.
          Rectangle r = underMouse.getAbsRect(); r.grow(1,1);
          view.repaint(r);
          //view.repaint(view.getVisibleRect());
        }
        if (nowPointsTo != null) {
          nowPointsTo.mouseEnters();
          // TODO the GraphicalNode must be able to render itself invalid.
          Rectangle r = nowPointsTo.getAbsRect(); r.grow(1,1);
          view.repaint(r);
          //view.repaint(view.getVisibleRect());
        }
        underMouse = nowPointsTo;
      }
    }
  }
}
