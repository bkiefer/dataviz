package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Rectangle;

/** An empty node, very convenient for TabularNode cells that should remain
 *  empty.
 */
public class EmptyNode extends BasicNode {

  /** A do-nothing */
  @Override
  public void paintAbsolute(Rectangle absoluteArea, Graphics g) {
  }

}
