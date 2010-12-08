package de.dfki.lt.loot.gui.controllers;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import de.dfki.lt.loot.gui.MouseListener;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class ClickHighlightListener implements MouseListener {

  private GraphicalNode lastInverted = null;

  private boolean _onlyModelNodes;

  public ClickHighlightListener(boolean onlyModelNodes) {
    _onlyModelNodes = onlyModelNodes;
  }

  public ClickHighlightListener() {
    this(false);
  }

  private void redraw(MouseEvent e, GraphicalNode node) {
    JComponent view = (JComponent) e.getSource();
    Rectangle r = node.getAbsRect(); r.grow(3,3);
    view.repaint(r);
  }

  private void changeHighlight(MouseEvent e, boolean how) {
    lastInverted.setHighlight(how);
    redraw(e, lastInverted);
  }

  @Override
  public void mouseClicked(MouseEvent e, GraphicalNode node) {
    GraphicalNode withModel = node;
    while (_onlyModelNodes
        && withModel != null && withModel.getModel() == null) {
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

  /** Ignored */
  @Override
  public void mouseEnters(MouseEvent e, GraphicalNode node) { }

  /** Ignored */
  @Override
  public void mouseLeaves(MouseEvent e, GraphicalNode node) { }

}
