package de.dfki.lt.loot.gui.layouts;

import java.awt.Graphics;
import java.awt.Rectangle;

import de.dfki.lt.loot.gui.nodes.GraphNode;

public interface LayoutAlgorithm {

  /** Arrange the nodes in a GraphNode in an appropriate Fashion. The size
   *  of the nodes has been adapted before this method is called, thus, only
   *  the positions relative to the GraphNode have to be adjusted.
   * @param g TODO
   */
  public abstract Rectangle execute(GraphNode node, Graphics g);

}
