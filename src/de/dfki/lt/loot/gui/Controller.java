package de.dfki.lt.loot.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class Controller implements MouseMotionListener, MouseListener {

  /** The list of listeners that are notified of events when entering or leaving
   *  a node
   */
  List<MouseListener> _nodeListeners;

  DrawingPanel view ;

  GraphicalNode underMouse;

  Controller(DrawingPanel dp) {
    view = dp;
    dp.addMouseMotionListener(this);
    underMouse = null;
    _nodeListeners = new ArrayList<MouseListener>(3);
  }

  public void addListener(MouseListener l) {
    _nodeListeners.add(l);
  }

  public void removeListener(MouseListener l) {
    _nodeListeners.add(l);
  }

  /** We don't currently support dragging events */
  public void mouseDragged(MouseEvent e) {
    return;
  }

  private void fireMouseEnters(MouseEvent e, GraphicalNode node) {
    for (MouseListener l : _nodeListeners) {
      l.mouseEnters(e, node);
    }
  }

  private void fireMouseLeaves(MouseEvent e, GraphicalNode node) {
    for (MouseListener l : _nodeListeners) {
      l.mouseLeaves(e, node);
    }
  }

  private void fireMouseClicked(MouseEvent e, GraphicalNode node) {
    for (MouseListener l : _nodeListeners) {
      l.mouseClicked(e, node);
    }
  }

  /** A preliminary implementation that has most of the needed functionality
   *  and good performance could easily be extended in a useful way.
   */
  public void mouseMoved(MouseEvent e) {
    Component originator = e.getComponent();
    assert(originator instanceof DrawingPanel);
    DrawingPanel view = (DrawingPanel) e.getComponent();
    assert(originator == view);
    Point p = e.getPoint();

    if (view.getVisibleRect().contains(p) && view.getRoot() != null) {
      GraphicalNode nowPointsTo =
        ((underMouse == null) ? view.getRoot() : underMouse)
        .getDeepestIncludingPoint(p);

      if (nowPointsTo != underMouse) {
        if (underMouse != null) {
          fireMouseLeaves(e, underMouse);
          // TODO the GraphicalNode must be able to render itself invalid.
          //Rectangle r = underMouse.getAbsRect(); r.grow(1,1);
          //view.repaint(r);
          //view.repaint(view.getVisibleRect());
        }
        if (nowPointsTo != null) {
          fireMouseEnters(e, nowPointsTo);
          // TODO the GraphicalNode must be able to render itself invalid.
          //Rectangle r = nowPointsTo.getAbsRect(); r.grow(1,1);
          //view.repaint(r);
          //view.repaint(view.getVisibleRect());
        }
        underMouse = nowPointsTo;
      }
    }
  }

  @Override
  public void mouseClicked(MouseEvent e, GraphicalNode node) {
    Component originator = e.getComponent();
    assert(originator instanceof DrawingPanel);
    DrawingPanel view = (DrawingPanel) e.getComponent();
    assert(originator == view);
    Point p = e.getPoint();

    if (view.getVisibleRect().contains(p) && view.getRoot() != null) {
      GraphicalNode nowPointsTo =
        ((underMouse == null) ? view.getRoot() : underMouse)
        .getDeepestIncludingPoint(p);
      if (nowPointsTo != null) {
        fireMouseClicked(e, nowPointsTo);
      }
    }
  }

  /** not an interesting event */
  @Override
  public void mouseEnters(MouseEvent e, GraphicalNode node) {
  }

  /** not an interesting event */
  @Override
  public void mouseLeaves(MouseEvent e, GraphicalNode node) {
  }
}
