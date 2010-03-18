/*
 * 
 */
package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Point;

import de.dfki.lt.loot.gui.Style;

// TODO: Auto-generated Javadoc
/**
 *
 * This is the <code>BasicNode</code> class.
 *
 */
public abstract class BasicNode extends GraphicalNode {

  /**
   * Instantiates a new basic node.
   */
  BasicNode() {
    super();
  }

  /**
   * Instantiates a new basic node.
   * 
   * @param aStyle the a style
   */
  BasicNode(Style aStyle) {
    super(aStyle);
  }

  /**
   * Only relevant for complex nodes that have sub-nodes: add the given
   * node to the sub-nodes of this node.
   * 
   * @param subNode the sub node
   */
  @Override
  public void addNode(GraphicalNode subNode) {}

  /**
   * Only relevant for complex nodes that have sub-nodes: add the given
   * node to the sub-nodes of this node.
   * 
   * @param subNode the sub node
   */
  @Override
  public void removeNode(GraphicalNode subNode) {}

  /**
   * Only relevant for complex nodes that have sub-nodes: add the given
   * node to the sub-nodes of this node.
   * 
   * @param old the old
   * @param newNode the new node
   */
  @Override
  public void exchangeNode(GraphicalNode old, GraphicalNode newNode) {}

  /**
   * This is what all basic nodes return correctly. Complex nodes have to
   * implement this method. (another reason for having a BasicNode class)
   * 
   * @param p the p
   * @return the child containing point
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
