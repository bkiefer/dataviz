package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import de.dfki.lt.loot.gui.connectors.Connector;
import de.dfki.lt.loot.gui.layouts.LayoutAlgorithm;
import de.dfki.lt.loot.gui.quadtree.Quadtree;

public class GraphNode extends CompositeNode {

  // This node's list of subNodes
  private List<GraphicalNode> _subNodes;

  private List<Connector> _connectors;

  private LayoutAlgorithm _algorithm;

  private Quadtree<GraphicalNode> _quadtree;

  public GraphNode(Object aModel) {
    this.model = aModel;
  }

  @Override
  public void adjustSize(Graphics g) {
    // Adjust the size of all subNodes so we know their final dimensions
    if (_subNodes != null) {
      for (GraphicalNode subnode : this._subNodes) {
        subnode.adjustSize(g);
      }
    }

    // All sub nodes now have to have determined their size, that means that
    // having GraphicalNodes that respond to the growTo method as direct
    // sub nodes of a GraphNode does not really make sense, since there is
    // no meaningful size to call it with.

    // call the associated layout algorithm for the nodes
    this.area = _algorithm.execute(this, g);

    if (this._connectors != null) {
      // Adjust the connectors
      for(Connector conn : this._connectors) {
        conn.adjust(g);
      }
    }

    // build an efficient search data structure to find content nodes quickly
    if (_subNodes != null) {
      _quadtree = new Quadtree<GraphicalNode>(this.area);
      for (GraphicalNode subnode : this._subNodes) {
        _quadtree.insert(subnode.area, subnode);
      }
    }
    // System.out.println(_quadtree.getStats());
  }

  @Override
  protected GraphicalNode getChildContainingPoint(Point p) {
    if (_quadtree == null)
      return null;

    List<GraphicalNode> hits = _quadtree.query(p);
    for (GraphicalNode hit : hits) {
      GraphicalNode inner = hit.getChildContainingPoint(p);
      if (inner != null)
        return inner;
    }
    return null;
  }

  @Override
  protected Iterable<GraphicalNode> subNodes() {
    return (_subNodes == null) ? new LinkedList<GraphicalNode>() : _subNodes;
  }

  @Override
  protected void paintAbsolute(Rectangle absoluteArea, Graphics g,
    boolean inverted) {
    if (this._connectors != null)
      for (Connector conn : this._connectors) {
        conn.paintAbsolute(absoluteArea, g);
      }
    super.paintAbsolute(absoluteArea, g, inverted);
  }

  /** Adds another <code>GraphicalNode</code> to the list of subNodes.
   */
  @Override
  public void addNode(GraphicalNode subNode) {
    // initialize the subNode list if needed
    if (null == this._subNodes)
      this._subNodes = new LinkedList<GraphicalNode>();

    this._subNodes.add(subNode);
    // set the new subnode's parentNode
    subNode.setParentNode(this);
  }

  /** Exchange the given oldNode by newNode
   *  TODO Is it okay if this is a do-nothing?
   */
  @Override
  public void exchangeNode(GraphicalNode old, GraphicalNode newNode) {
  }

  /** Adds another <code>AbstractConnector</code> to the list of connectors.
   */
  public void addConnector(Connector conn) {
    // initialize the connectors list if needed
    if (null == this._connectors)
      this._connectors= new LinkedList<Connector>();

    _connectors.add(conn);
  }

  public void setLayoutAlgorithm(LayoutAlgorithm algo) {
    _algorithm = algo;
  }

  public List<GraphicalNode> getNodes() { return _subNodes; }

  public List<Connector> getConnectors() { return _connectors; }

}
