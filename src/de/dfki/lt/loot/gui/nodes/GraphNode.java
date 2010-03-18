/*
 * 
 */
package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import de.dfki.lt.loot.gui.connectors.Connector;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphNode.
 */
public class GraphNode extends GraphicalNode {

  /**
   * The Class LayoutAlgorithm.
   */
  public abstract class LayoutAlgorithm {
    
    /**
     * Execute.
     * 
     * @return the rectangle
     */
    public abstract Rectangle execute();
  }

  // This node's list of subNodes
  /** The _sub nodes. */
  private List<GraphicalNode> _subNodes;

  /** The _connectors. */
  private List<Connector> _connectors;

  /** The _algorithm. */
  private LayoutAlgorithm _algorithm;

  /**
   * Instantiates a new graph node.
   * 
   * @param aModel the a model
   */
  public GraphNode(Object aModel) {
    this.model = aModel;
  }

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.nodes.GraphicalNode#adjustSize(java.awt.Graphics)
   */
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
    this.area = _algorithm.execute();

    if (this._connectors != null) {
      // Adjust the connectors
      for(Connector conn : this._connectors) {
        conn.adjust(g);
      }
    }
  }

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.nodes.GraphicalNode#getChildContainingPoint(java.awt.Point)
   */
  @Override
  protected GraphicalNode getChildContainingPoint(Point p) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.nodes.GraphicalNode#paintAbsolute(java.awt.Rectangle, java.awt.Graphics)
   */
  @Override
  public void paintAbsolute(Rectangle absoluteArea, Graphics g) {
    if (this._connectors != null)
      for (Connector conn : this._connectors) {
        conn.paintAbsolute(absoluteArea, g);
      }
    // call the paint method of every sub node
    if (this._subNodes != null)
      for (GraphicalNode subNode : this._subNodes) {
        subNode.paint(absoluteArea, g);
      }
  }

  /**
   * Adds another <code>GraphicalNode</code> to the list of subNodes.
   * 
   * @param subNode the sub node
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

  /**
   * Remove the given subNode from my sub-nodes list.
   * 
   * @param subNode the sub node
   */
  @Override
  public void removeNode(GraphicalNode subNode) {
    this._subNodes.remove(subNode);
    subNode.setParentNode(null);
  }

  /**
   * Exchange the given oldNode by newNode.
   * 
   * @param old the old
   * @param newNode the new node
   */
  @Override
  public void exchangeNode(GraphicalNode old, GraphicalNode newNode) {
    // TODO Auto-generated method stub
  }

  /**
   * Adds another <code>AbstractConnector</code> to the list of connectors.
   * 
   * @param conn the conn
   */
  public void addConnector(Connector conn) {
    // initialize the connectors list if needed
    if (null == this._connectors)
      this._connectors= new LinkedList<Connector>();

    _connectors.add(conn);
  }

  /**
   * Sets the layout algorithm.
   * 
   * @param algo the new layout algorithm
   */
  public void setLayoutAlgorithm(LayoutAlgorithm algo) {
    _algorithm = algo;
  }

  /**
   * Gets the nodes.
   * 
   * @return the nodes
   */
  public List<GraphicalNode> getNodes() { return _subNodes; }

  /**
   * Gets the connectors.
   * 
   * @return the connectors
   */
  public List<Connector> getConnectors() { return _connectors; }

}
