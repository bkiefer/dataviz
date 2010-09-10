package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import de.dfki.lt.loot.gui.Style;

public class CircleNode extends BasicNode {

  /** The default constructor: Creates a new instance of <code>TextNode</code>
   * @param content
   *          The TextNode's content
   */
  public CircleNode(Style aStyle) {
    super(aStyle);
  }

  @Override
  public void paintAbsolute(Rectangle area, Graphics g) {
    g.drawArc(area.x, area.y, area.width - 1 , area.height - 1, 0, 360);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.unisb.loot.gui.BasicNode#adjustSize(java.awt.Graphics)
   */
  @Override
  public void adjustSize(Graphics g) {
    assert (this.style.getFont() != null);
    Rectangle charBounds =
        this.style.getFont().getStringBounds("M",
            ((Graphics2D) g).getFontRenderContext()).getBounds();

    // add offset to Node's size
    this.area.width = charBounds.height;
    this.area.height = charBounds.height;
    // treat padding parameters like in all other basic nodes
    super.adjustSize(g);
  }


}
