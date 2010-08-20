package de.dfki.lt.loot.gui.layouts;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.IdentityHashMap;
import java.util.List;

import de.dfki.lt.loot.gui.connectors.Connector;
import de.dfki.lt.loot.gui.nodes.GraphNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class ChartLayoutAlgorithm implements LayoutAlgorithm {

  private List<GraphicalNode> _verticesTopSort;
  private IdentityHashMap<GraphicalNode, Integer> _edgeLevel;

  ChartLayoutAlgorithm(List<GraphicalNode> verticesTopSort,
      IdentityHashMap<GraphicalNode, Integer> edgeLevel) {
    _verticesTopSort = verticesTopSort;
    _edgeLevel = edgeLevel;
  }

  @Override
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
        if (_edgeLevel.get(edgeNode) == 1) {
          Rectangle r = edgeNode.getRect();
          if (r.width > maxWidth)
            maxWidth = r.width;
          if (r.height > maxEdgeHeight)
            maxEdgeHeight = r.height;
        }
      }
      x += maxWidth + height;
    }
    // now x gets the width of the resulting area.
    x += node.getStyle().getPadding().getOffset();
    height /= 2;
    y += height;
    // This becomes the gap between edges
    height /= 2;
    // base edge level
    y += height;
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

    return new Rectangle(0, 0, x, height);
  }
}

