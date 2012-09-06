package de.dfki.lt.loot.gui.controllers;

import de.dfki.lt.loot.gui.MouseAdapter;
import de.dfki.lt.loot.gui.MouseEvent;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class HoverHighlightListener extends MouseAdapter {
  GraphicalNode lastInverted = null;

  @Override
  public synchronized void mouseLeaves(MouseEvent e, GraphicalNode node) {
    GraphicalNode withModel = node;
    while (withModel != null && withModel.getModel() == null) {
      withModel = withModel.getParentNode();
    }
    if (lastInverted == withModel) {
      e.panel.changeHighlight(lastInverted, false);
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
        e.panel.changeHighlight(lastInverted, false);
      }
      lastInverted = withModel;
      if (lastInverted != null) {
        e.panel.changeHighlight(lastInverted, true);
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
