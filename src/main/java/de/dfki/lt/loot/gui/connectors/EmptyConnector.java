package de.dfki.lt.loot.gui.connectors;

import java.awt.Graphics;
import java.awt.Rectangle;

import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class EmptyConnector extends Connector {

  public EmptyConnector(GraphicalNode from, GraphicalNode to) {
    super(from, to);
  }

  @Override
  public void paintAbsolute(Rectangle absoluteArea, Graphics g) {
  }

  @Override
  public void adjust(Graphics g) {
  }

}
