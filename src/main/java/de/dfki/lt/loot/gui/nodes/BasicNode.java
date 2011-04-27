package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Point;

import de.dfki.lt.loot.gui.Style;

/**
 *
 * This is the <code>BasicNode</code> class.
 *
 */
public abstract class BasicNode extends GraphicalNode {

  BasicNode() {
    super();
  }

  BasicNode(Style aStyle) {
    super(aStyle);
  }

  /** This is what all basic nodes return correctly. Complex nodes have to
   *  implement this method. (another reason for having a BasicNode class)
   */
  @Override
  protected GraphicalNode getChildContainingPoint(Point p) {
    return this;
  }

  /* (non-Javadoc)
   * @see de.unisb.loot.gui.GraphicalNode#adjustSize(java.awt.Graphics)
   */
  @Override
  public void adjustSize(Graphics g) {
    /**
     * @TODO maybe call calcWidth/Height() if one dimension has been altered
     *       from the outside (via getRect())
     */
    // treat padding parameters uniformly for all BasicNodes
    int offset = this.style.getPadding().getOffset();
    // add offset twice
    offset += offset;

    this.area.height += offset;
    this.area.width += offset;
  }
}
