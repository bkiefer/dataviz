package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;

import de.dfki.lt.loot.gui.Style;

public abstract class CompositeNode extends GraphicalNode {

  public CompositeNode() {
  }

  /** Creates a new instance of <code>ComplexNode</code> with style aStyle
   */
  public CompositeNode(Style aStyle) {
    super(aStyle);
  }

  /** Return an iterable container over the directly embedded nodes. May
   *  return null to indicate empty container. Also the iterator may return
   *  null elements for empty cells.
   */
  protected abstract Iterable<GraphicalNode> subNodes();

  /** Only relevant for complex nodes that have sub-nodes: add the given
   *  node to the sub-nodes of this node
   */
  public abstract void addNode(GraphicalNode subNode);

  /** Only relevant for complex nodes that have sub-nodes: replace the given
   *  old node by the new node, putting it in the position were the old node
   *  was. Must establish correct parent link of new node to <code>this</code>.
   *
   *  That removes the parent link of the old node to its ancestor, thus, if
   *  the node is still used, the parent link to the correct node has to be
   *  established afterwards.
   */
  public abstract void exchangeNode(GraphicalNode old, GraphicalNode newNode);


  /** Remove the given subNode from my sub-nodes list */
  public void removeNode(GraphicalNode subNode) {
    if (subNodes() == null) return;

    Iterator<GraphicalNode> it = subNodes().iterator();
    GraphicalNode next = null;
    while(it.hasNext() && (next = it.next()) != subNode) { }
    if (next == subNode) {
      it.remove();
    }
    subNode.setParentNode(null);
  }

  @Override
  protected void paintAbsolute(Rectangle absoluteArea, Graphics g,
    boolean inverted) {
    if (subNodes() != null) {
      // call the paint method of every valid sub node
      for (GraphicalNode subNode : subNodes()) {
        if (subNode != null) {
          subNode.paint(absoluteArea, g, inverted);
        }
      }
    }
  }

  /** This implementation is feasible for hierarchically organized complex
   *  nodes, not for arbitrary arrangements of large size, because it will be
   *  very inefficient in these cases.
   */
  @Override
  protected GraphicalNode getChildContainingPoint(Point p) {
    if (subNodes() != null) {
      for (GraphicalNode child : subNodes()) {
        if (child != null && child.area.contains(p)) {
          p.x -= child.area.x;
          p.y -= child.area.y;
          return child.getChildContainingPoint(p);
        }
      }
    }
    return this;
  }

}
