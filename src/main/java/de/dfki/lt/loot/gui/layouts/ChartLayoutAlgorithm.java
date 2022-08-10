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

  private double _relativeGap = .0;// 1.0/3.0 ;

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
    int paddingOffset = node.getStyle().getPadding().getOffset();

    // Now compute max height and width for the vertices and edge nodes
    int maxVerticesHeight = _verticesTopSort.get(0).getRect().height;
    int maxVerticesWidth = _verticesTopSort.get(0).getRect().width;
    for (GraphicalNode vertexNode : _verticesTopSort) {
      for (Connector conn : vertexNode.outConnectors()) {
        GraphicalNode edgeNode = conn.toNode();
        assert(_edgeLevel.containsKey(edgeNode));
        Rectangle r = edgeNode.getRect();
        if (r.width > maxVerticesWidth)
          maxVerticesWidth = r.width;
        if (r.height > maxVerticesHeight)
          maxVerticesHeight = r.height;
      }
    }
    maxVerticesHeight = (int) ((1 + _relativeGap) * maxVerticesHeight);

    // now compute offset from left border
    int leftOffset = (maxVerticesWidth + paddingOffset) / 2;
    int topOffset = (maxVerticesHeight + paddingOffset) / 2;

    // set the proper x coordinates for the chart vertices
    int x = leftOffset;
    for (GraphicalNode vertexNode : _verticesTopSort) {
      vertexNode.setOrigin(x, topOffset);
      x += maxVerticesWidth + paddingOffset;
    }

    // compute max extensions of edges: up, down and to the right,
    int height = 0, depth = 0, width = 0;
    for (GraphicalNode edgeNode : _edgeLevel.keySet()) {
      Rectangle sourceRect = edgeNode.inConnectors().iterator().next()
          .fromNode().getRect();
      int sourceX = sourceRect.x + (sourceRect.width / 2);

      Rectangle targetRect = edgeNode.outConnectors().iterator().next()
          .toNode().getRect();
      int targetX = targetRect.x + (targetRect.width / 2);
      if (width < targetX + leftOffset) {
        width = targetX + leftOffset;
      }

      Rectangle edgeRect = edgeNode.getRect();

      int edgeX = (sourceX + targetX - edgeRect.width) / 2;
      int level = _edgeLevel.get(edgeNode);
      int edgeY = topOffset + level * maxVerticesHeight;

      if (level > 0) {
        if (edgeY > height)
          height = edgeY;
      } else {
        if (edgeY < depth)
          depth = edgeY;
      }
      // edgeY will have to be corrected after we now height and depth
      // because the zero line will have to be moved
      edgeNode.setOrigin(edgeX, edgeY);
    }

    // height and depth have to extended to make the edge labels fit
    height += topOffset;
    depth -= topOffset + topOffset;

    // how we have to adjust the y coordinates of the vertex origins
    if (_bottomToTop) {
      int h = height - depth - topOffset;
      // turn the visualization upside down
      for (GraphicalNode vertexNode : _verticesTopSort) {
        Rectangle area = vertexNode.getRect();
        vertexNode.setOrigin(area.x, h - (area.y - depth));
      }
      for (GraphicalNode edgeNode : _edgeLevel.keySet()) {
        Rectangle area = edgeNode.getRect();
        edgeNode.setOrigin(area.x, h - (area.y - depth));
      }
    } else {
      for (GraphicalNode vertexNode : _verticesTopSort) {
        Rectangle area = vertexNode.getRect();
        vertexNode.setOrigin(area.x, area.y - depth - topOffset);
      }
      for (GraphicalNode edgeNode : _edgeLevel.keySet()) {
        Rectangle area = edgeNode.getRect();
        edgeNode.setOrigin(area.x, area.y - depth - topOffset);
      }
    }

    return new Rectangle(0, 0, width, height - depth);
  }
}

