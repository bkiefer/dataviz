package de.dfki.lt.loot.gui.controllers;

import de.dfki.lt.loot.gui.MouseAdapter;
import de.dfki.lt.loot.gui.MouseEvent;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class ClickHighlightListener extends MouseAdapter {

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
}
