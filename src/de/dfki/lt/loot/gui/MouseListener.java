package de.dfki.lt.loot.gui;

import java.awt.event.MouseEvent;

import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public interface MouseListener {
  public abstract void mouseEnters(MouseEvent e, GraphicalNode node);

  public abstract void mouseLeaves(MouseEvent e, GraphicalNode node);

  public abstract void mouseClicked(MouseEvent e, GraphicalNode node);
}
