/*
 * 
 */
package de.dfki.lt.loot.gui.nodes;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.dfki.lt.loot.gui.connectors.Connector;
import de.dfki.lt.loot.gui.nodes.GraphNode.LayoutAlgorithm;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleTreeLayoutAlgorithm.
 */
public class SimpleTreeLayoutAlgorithm extends LayoutAlgorithm {

  /**
   * The Class RectTreeNode.
   */
  private class RectTreeNode {
    
    /** The rect. */
    Rectangle rect;
    
    /** The node. */
    GraphicalNode node;
    
    /** The subs. */
    List<RectTreeNode> subs = new LinkedList<RectTreeNode>();
  }
  
  /** The _root. */
  private GraphicalNode _root;
  
  /** The _vgap. */
  private int _hgap, _vgap;
  
  /**
   * Instantiates a new simple tree layout algorithm.
   * 
   * @param node the node
   * @param root the root
   * @param hgap the hgap
   * @param vgap the vgap
   */
  public SimpleTreeLayoutAlgorithm(GraphNode node, GraphicalNode root,
      int hgap, int vgap) {
    node.super();
    _root = root;
    _hgap = hgap;
    _vgap = vgap;
  }
  
  
  /**
   * Compute area.
   * 
   * @param root the root
   * @return the rect tree node
   */
  private RectTreeNode computeArea(GraphicalNode root) {
    RectTreeNode result = new RectTreeNode();
    result.node = root;
    
    int width = 0, height = 0;
    int subypos = root.getRect().height + _vgap; 
    
    Iterator<Connector> outs = root.outConnectors();
    while(outs.hasNext()) {
      RectTreeNode r = computeArea(outs.next().toNode());
      height = Math.max(height, r.rect.height);
      // adapt the x and y positions of the embedded rects
      r.rect.x += width;
      r.rect.y = subypos;
      width += r.rect.width + _hgap;
      result.subs.add(r);
    }
    width -= _hgap;
    
    // TODO: if the width of the root is larger than width, the space between
    // the rects should be equally distributed. I'll omit that for the moment.
    width = Math.max(width, root.getRect().width);
    
    height += root.getRect().height;
    if (! result.subs.isEmpty())
      height += _vgap;
    
    // adapt the root height and position
    root.setOrigin((width - root.getRect().width) / 2, 0);
    
    result.rect =
      new Rectangle(0, 0, width, height + root.getRect().height);
    
    return result;
  }
  
  
  /**
   * Adjust origins.
   * 
   * @param boxes the boxes
   * @param xoff the xoff
   * @param yoff the yoff
   */
  private void adjustOrigins(RectTreeNode boxes, int xoff, int yoff) {
    GraphicalNode here = boxes.node;
    here.setOrigin(here.getRect().x + xoff, here.getRect().y + yoff);
    for(RectTreeNode sub : boxes.subs) {
      adjustOrigins(sub, sub.rect.x + xoff, sub.rect.y + yoff);
    }
  }
  
  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.nodes.GraphNode.LayoutAlgorithm#execute()
   */
  @Override
  public Rectangle execute() {
    RectTreeNode boxes = computeArea(_root);
    // now the origins of the immediately enclosed boxes have to be adjusted
    adjustOrigins(boxes, 0, 0);
    
    return boxes.rect;
  }

}
