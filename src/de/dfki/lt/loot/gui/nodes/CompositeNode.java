/*
 * 
 */
package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import de.dfki.lt.loot.gui.Style;

// TODO: Auto-generated Javadoc
/**
 * This is the <code>CompositeNode</code> class. <code>CompositeNodes</code>
 * are <code>GraphicalNodes</code> with a number of subNodes (which are
 * <code>GraphicalNodes</code>, as well).
 *
 * @author Tassilo Barth
 * @author Pia Mennig
 * @author Antonia Scheidel
 */
public class CompositeNode extends GraphicalNode {

  // This node's list of subNodes
  /** The sub nodes. */
  private List<GraphicalNode> subNodes;

  // The way the subNodes are aligned. n, s, e, w like in TabularNode,
  // h and v are horizontally resp. vertically centered
  /** The align. */
  private char align;

  /**
   * Instantiates a new composite node.
   * 
   * @param alignment the alignment
   * @param aStyle the a style
   */
  public CompositeNode(char alignment, Style aStyle) {
    super(aStyle);
    align = alignment;
  }

  /**
   * Instantiates a new composite node.
   * 
   * @param alignment the alignment
   */
  public CompositeNode(char alignment) { align = alignment; }

  /**
   * Adds another <code>GraphicalNode</code> to this
   * <code>CompositeNode</code>'s list of subNodes.
   * 
   * @param subNode the sub node
   */
  @Override
  public void addNode(GraphicalNode subNode) {
    // initialize the subNode list if needed
    if (null == this.subNodes)
      this.subNodes = new LinkedList<GraphicalNode>();

    this.subNodes.add(subNode);
    // set the new subnode's parentNode
    subNode.setParentNode(this);
  }

  /**
   * Remove the given subNode from my sub-nodes list.
   * 
   * @param subNode the sub node
   */
  @Override
  public void removeNode(GraphicalNode subNode) {
    this.subNodes.remove(subNode);
    subNode.setParentNode(null);
  }

  /**
   * Replace the node old by newNode in my sub-nodes list.
   * 
   * @param old the old
   * @param newNode the new node
   */
  @Override
  public void exchangeNode(GraphicalNode old, GraphicalNode newNode) {
    ListIterator<GraphicalNode> li = subNodes.listIterator();
    while (li.hasNext()) {
      GraphicalNode node = li.next();
      if (node == old) {
        li.set(newNode);
        old.setParentNode(null);
        return;
      }
    }
    throw new IllegalArgumentException("subnode not contained in this node");
  }

  /*
   * (non-Javadoc)
   *
   * @see de.unisb.loot.gui.GraphicalNode#paint(java.awt.Point,
   *      java.awt.Graphics)
   */
  @Override
  public void paintAbsolute(Rectangle absoluteArea, Graphics g) {
    // call the paint method of every sub node
    for (GraphicalNode subNode : subNodes)
      subNode.paint(absoluteArea, g);
  }

  /**
   * Adjust horizontal.
   * 
   * @param g the g
   */
  private void adjustHorizontal(Graphics g) {
    // Used to determine final width and height of the CompositeNode.
    int maxHeight = 0, maxWidth = 0;
    // Adjust the size of all subNodes so we know their final dimensions
    // as well as maxWidth and maxHeight according to the CompositeNode's
    // alignment, which is a horizontal alignment here
    for (GraphicalNode subnode : this.subNodes) {
      subnode.adjustSize(g);
      maxHeight = Math.max(maxHeight, subnode.getRect().height);
      maxWidth += subnode.getRect().width;
    }
    maxWidth = 0;
    // allow nodes to grow eventually
    for (GraphicalNode subnode : this.subNodes) {
      subnode.growTo(subnode.getRect().width, maxHeight);
      maxHeight = Math.max(maxHeight, subnode.getRect().height);
      maxWidth += subnode.getRect().width;
    }
    // Get the offset as dictated by the padding.
    int offset = this.style.getPadding().getOffset();
    int x = offset; // now adjust origins
    for (GraphicalNode subnode : this.subNodes) {
      int yoff = 0;
      if (this.align != 'n') { // at the top, from left to right
        yoff = maxHeight - subnode.getRect().height;
        if (this.align == 'h') // horizontally centered
          yoff /= 2;
        // else 's': at the bottom
      }
      subnode.setOrigin(x,offset + yoff);
      x += subnode.getRect().width;
    }
    offset += offset;
    // set the CompositeNode's size accordingly
    this.area.setSize(maxWidth + offset, maxHeight + offset);
  }

  /**
   * Adjust vertical.
   * 
   * @param g the g
   */
  private void adjustVertical(Graphics g) {
    // Used to determine final width and height of the CompositeNode.
    int maxHeight = 0, maxWidth = 0;
    // Adjust the size of all subNodes so we know their final dimensions
    // as well as maxWidth and maxHeight according to the CompositeNode's
    // alignment, which is a vertical alignment here
    for (GraphicalNode subnode : this.subNodes) {
      subnode.adjustSize(g);
      maxWidth = Math.max(maxWidth, subnode.getRect().width);
      maxHeight += subnode.getRect().height;
    }
    maxHeight = 0;
    // allow nodes to grow eventually
    for (GraphicalNode subnode : this.subNodes) {
      subnode.growTo(maxWidth, subnode.getRect().height);
      maxWidth = Math.max(maxWidth, subnode.getRect().width);
      maxHeight += subnode.getRect().height;
    }
    // Get the offset as dictated by the padding.
    int offset = this.style.getPadding().getOffset();
    int y = offset; // now adjust origins
    for (GraphicalNode subnode : this.subNodes) {
      int xoff = 0;
      if (this.align != 'w') { // all at the left, from top to bottom
        xoff = maxWidth - subnode.getRect().width;
        if (this.align == 'v') // vertically centered, from top to bottom
          xoff /= 2;
        // else 'w': all at the right
      }
      subnode.setOrigin(offset + xoff, y);
      y += subnode.getRect().height;
    }
    offset += offset;
    // set the CompositeNode's size accordingly
    this.area.setSize(maxWidth + offset, maxHeight + offset);
  }


  /*
   * (non-Javadoc)
   *
   * @see de.unisb.loot.gui.GraphicalNode#adjustSize(java.awt.Graphics)
   */
  @Override
  public void adjustSize(Graphics g) {
    switch (this.align) {
    case 'v': // vertically centered, from top to bottom
    case 'w': // all at the right, from top to bottom
    case 'e': // all at the left, from top to bottom
      adjustVertical(g); break;
    case 'h': // horizontally centered, from left to right
    case 'n': // all at the top, from left to right
    case 's': // all at the left, from left to right
      adjustHorizontal(g); break;
    }
  } // end adjustSize

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.nodes.GraphicalNode#getChildContainingPoint(java.awt.Point)
   */
  @Override
  protected GraphicalNode getChildContainingPoint(Point p) {
    for (GraphicalNode child : subNodes) {
      if (child.area.contains(p)) {
        p.x -= child.area.x;
        p.y -= child.area.y;
        return child.getChildContainingPoint(p);
      }
    }
    return this;
  }
} // end class
