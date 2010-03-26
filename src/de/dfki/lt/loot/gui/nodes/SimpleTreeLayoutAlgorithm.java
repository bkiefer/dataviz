package de.dfki.lt.loot.gui.nodes;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.dfki.lt.loot.gui.connectors.Connector;
import de.dfki.lt.loot.gui.nodes.GraphNode.LayoutAlgorithm;

public class SimpleTreeLayoutAlgorithm extends LayoutAlgorithm {

  private class RectTreeNode {
    Rectangle rect;
    GraphicalNode node;
    List<RectTreeNode> subs = new LinkedList<RectTreeNode>();
  }
  
  private GraphicalNode _root;
  
  private int _hgap, _vgap;
  
  public SimpleTreeLayoutAlgorithm(GraphNode node, GraphicalNode root,
      int hgap, int vgap) {
    node.super();
    _root = root;
    _hgap = hgap;
    _vgap = vgap;
  }
  
  
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
  
  
  private void adjustOrigins(RectTreeNode boxes, int xoff, int yoff) {
    GraphicalNode here = boxes.node;
    here.setOrigin(here.getRect().x + xoff, here.getRect().y + yoff);
    for(RectTreeNode sub : boxes.subs) {
      adjustOrigins(sub, sub.rect.x + xoff, sub.rect.y + yoff);
    }
  }
  
  @Override
  public Rectangle execute() {
    RectTreeNode boxes = computeArea(_root);
    // now the origins of the immediately enclosed boxes have to be adjusted
    adjustOrigins(boxes, 0, 0);
    
    return boxes.rect;
  }

}
