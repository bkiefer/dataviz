package de.dfki.lt.loot.gui.layouts;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.IdentityHashMap;
import java.util.List;

import de.dfki.lt.loot.gui.connectors.Connector;
import de.dfki.lt.loot.gui.nodes.GraphNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class ChartLayoutAlgorithm implements LayoutAlgorithm {

  private boolean _bottomToTop = true;

  private double _relativeGap = 0;// 1.0/3.0 ;

  private List<GraphicalNode> _verticesTopSort;
  private IdentityHashMap<GraphicalNode, Integer> _edgeLevel;

  ChartLayoutAlgorithm(List<GraphicalNode> verticesTopSort,
      IdentityHashMap<GraphicalNode, Integer> edgeLevel) {
    _verticesTopSort = verticesTopSort;
    _edgeLevel = edgeLevel;
  }

  /** This computes real coordinates from the abstract, grid-based coordinates
   *  given by the vertex number and edgeLevel.
   */
  public Rectangle execute(GraphNode node, Graphics g) {

    int y = node.getStyle().getPadding().getOffset();
    int x = node.getStyle().getPadding().getOffset();

    int height = _verticesTopSort.get(0).getRect().height;
    height += height;
    // Now arrange the vertices left to right and compute levels for the
    // edges

    int maxEdgeHeight = 0;
    for (GraphicalNode vertexNode : _verticesTopSort) {
      vertexNode.setOrigin(x, y);
      int maxWidth = vertexNode.getRect().width;

      for (Connector conn : vertexNode.outConnectors()) {
        GraphicalNode edgeNode = conn.toNode();
        assert(_edgeLevel.containsKey(edgeNode));
        Rectangle r = edgeNode.getRect();
        if (r.width > maxWidth)
          maxWidth = r.width;
        if (r.height > maxEdgeHeight)
          maxEdgeHeight = r.height;
      }
      x += maxWidth + height;
    }
    // now x gets the width of the resulting area.
    x += node.getStyle().getPadding().getOffset();
    height /= 2;
    y += height;
    // This becomes the gap between edges
    height *= _relativeGap;
    // base edge level
    // y += height;
    // distance per level
    maxEdgeHeight += height;

    for (GraphicalNode edgeNode : _edgeLevel.keySet()) {
      Rectangle sourceRect = edgeNode.inConnectors().iterator().next()
          .fromNode().getRect();
      int sourceX = sourceRect.x + (sourceRect.width / 2);

      Rectangle targetRect = edgeNode.outConnectors().iterator().next()
          .toNode().getRect();
      int targetX = targetRect.x + (targetRect.width / 2);

      Rectangle edgeRect = edgeNode.getRect();

      int edgeX = (sourceX + targetX - edgeRect.width) / 2;
      int edgeY = y + _edgeLevel.get(edgeNode) * maxEdgeHeight;

      if (edgeY > height)
        height = edgeY;

      edgeNode.setOrigin(edgeX, edgeY);
    }

    height += maxEdgeHeight + node.getStyle().getPadding().getOffset();

    if (_bottomToTop) {
      int h = height - maxEdgeHeight;
      // turn the visualization upside down
      for (GraphicalNode vertexNode : _verticesTopSort) {
        Rectangle area = vertexNode.getRect();
        vertexNode.setOrigin(area.x, h - area.y);
      }
      for (GraphicalNode edgeNode : _edgeLevel.keySet()) {
        Rectangle area = edgeNode.getRect();
        edgeNode.setOrigin(area.x, h - area.y);
      }
    }

    return new Rectangle(0, 0, x, height);
  }
}

