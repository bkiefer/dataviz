package de.dfki.lt.loot.gui.controllers;

import de.dfki.lt.loot.gui.MouseEvent;
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

  @Override
  public void mouseClicked(MouseEvent e, GraphicalNode node) {
    GraphicalNode withModel = node;
    while (_onlyModelNodes
        && withModel != null && withModel.getModel() == null) {
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

  /** Ignored */
  @Override
  public void mouseEnters(MouseEvent e, GraphicalNode node) { }

  /** Ignored */
  @Override
  public void mouseLeaves(MouseEvent e, GraphicalNode node) { }

}
