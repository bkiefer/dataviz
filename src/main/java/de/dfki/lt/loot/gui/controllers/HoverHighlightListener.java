package de.dfki.lt.loot.gui.controllers;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import de.dfki.lt.loot.gui.MouseListener;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class HoverHighlightListener implements MouseListener {
  GraphicalNode lastInverted = null;

  private void redraw(MouseEvent e, GraphicalNode node) {
    JComponent view = (JComponent) e.getSource();
    Rectangle r = node.getAbsRect(); r.grow(1,1);
    view.repaint(r);
  }

  private void changeHighlight(MouseEvent e, boolean how) {
    lastInverted.setHighlight(how);
    redraw(e, lastInverted);
  }

  @Override
  public synchronized void mouseLeaves(MouseEvent e, GraphicalNode node) {
    GraphicalNode withModel = node;
    while (withModel != null && withModel.getModel() == null) {
      withModel = withModel.getParentNode();
    }
    if (lastInverted == withModel) {
      changeHighlight(e, false);
      lastInverted = null;
    }
  }


  @Override
  public synchronized void mouseEnters(MouseEvent e, GraphicalNode node) {
    GraphicalNode withModel = node;
    while (withModel != null && withModel.getModel() == null) {
      withModel = withModel.getParentNode();
    }
    if (lastInverted != withModel) {
      if (lastInverted != null) {
        changeHighlight(e, false);
      }
      lastInverted = withModel;
      if (lastInverted != null) {
        changeHighlight(e, true);
      }
    }
  }

  @Override
  public void mouseClicked(MouseEvent e, GraphicalNode node) {
    // TODO Auto-generated method stub
    /*
    node.setHighlight(false);
    DrawingPanel view = (DrawingPanel) e.getSource();
    Rectangle r = node.getAbsRect(); r.grow(1,1);
    view.repaint(r);
    */
  }
}
