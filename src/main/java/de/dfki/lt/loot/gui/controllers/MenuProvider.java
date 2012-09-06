package de.dfki.lt.loot.gui.controllers;

import javax.swing.JPopupMenu;

import de.dfki.lt.loot.gui.MouseEvent;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public interface MenuProvider {
  
  /** Return a menu to work on the contents of the selected node */
  public abstract JPopupMenu getMenu(MouseEvent e, GraphicalNode n);

}
