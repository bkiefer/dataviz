package de.dfki.lt.loot.gui.controllers;

import javax.swing.JPopupMenu;

import de.dfki.lt.loot.gui.MouseAdapter;
import de.dfki.lt.loot.gui.MouseEvent;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class MenuPopupListener extends MouseAdapter {
  
  private GraphicalNode _clickTarget = null;

  private MenuProvider _provider = null;
  
  private boolean _onlyModelNodes;
  
  public MenuPopupListener(boolean onlyModelNodes, MenuProvider provider) {
    _onlyModelNodes = onlyModelNodes;
    _provider = provider;
  }

  public MenuPopupListener(MenuProvider provider) {
    this(true, provider);
  }

  private void maybeShowPopup(MouseEvent e, GraphicalNode node) {
    GraphicalNode withModel = node;
    while (_onlyModelNodes
        && withModel != null && withModel.getModel() == null) {
      withModel = withModel.getParentNode();
    }
    _clickTarget = withModel;
    JPopupMenu menu = _provider.getMenu(e, _clickTarget);
    if (menu != null)
      menu.show(e.awtEvent.getComponent(),
          e.awtEvent.getX(), e.awtEvent.getY());
  }
  
  @Override
  public void mouseClicked(MouseEvent e, GraphicalNode node) {
    maybeShowPopup(e, node);
  }

  /** Ignored */
  @Override
  public void mouseReleased(MouseEvent e, GraphicalNode node) {
    maybeShowPopup(e, node);
  }

}
