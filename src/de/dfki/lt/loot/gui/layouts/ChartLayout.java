package de.dfki.lt.loot.gui.layouts;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;

import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.connectors.SquareBendConnector;
import de.dfki.lt.loot.gui.nodes.CircleNode;
import de.dfki.lt.loot.gui.nodes.GraphNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.TextNode;

public class ChartLayout implements Layout {

  private ViewContext _context;

  @SuppressWarnings("unchecked")
  private void DFS_inv_finish_add(Object vertex,
      IdentityHashMap<Object, Boolean> visited, List<GraphicalNode> result,
      GraphNode modelNode) {
    ModelAdapter adapt = _context._adapt;
    Iterable edges = adapt.outEdges(vertex);
    if (edges != null) {
      for (Object edge : edges) {
        Object target = adapt.target(edge);
        if (!visited.containsKey(target)) {
          visited.put(target, true);
          DFS_inv_finish_add(target, visited, result, modelNode);
        }
      }
    }
    String name = adapt.getAttribute(vertex, "name");
    GraphicalNode node = ((name != null)
        ? new TextNode(name, Style.get("vertex"))
    : new CircleNode(Style.get("vertex")));
    _context.setModel(node, vertex);
    modelNode.addNode(node);
    result.add(0, node);
  }


  private List<GraphicalNode> topSort(Object model, GraphNode modelNode) {
      // L Empty list that will contain the sorted nodes
    // S Set of all nodes with no incoming edges
    List<GraphicalNode> result = new LinkedList<GraphicalNode>();
    IdentityHashMap<Object, Boolean> visited =
      new IdentityHashMap<Object, Boolean>();
    for (Object vertex : _context._adapt.getVertices(model)) {
      if (!visited.containsKey(vertex)) {
        visited.put(vertex, true);
        // start a DFS from here
        DFS_inv_finish_add(vertex, visited, result, modelNode);
      }
    }
    return result;
  }


  @SuppressWarnings("unchecked")
  private int computeMinLevel(Object edge,
      IdentityHashMap<GraphicalNode, Integer> edgeLevel) {
    GraphicalNode edgeNode = _context.getRepresentative(edge);
    assert(edgeNode != null);
    if (edgeLevel.containsKey(edgeNode)) {
      return edgeLevel.get(edgeNode);
    }
    int level = 0;
    Iterable children = _context._adapt.getTreeDaughters(edge);
    if (children != null) {
      for (Object child : children) {
        int sublevel = computeMinLevel(child, edgeLevel);
        level = Math.max(level, sublevel);
      }
      ++level;
    }
    edgeLevel.put(edgeNode, level);
    return level;
  }


  /** In general, this method should create all the necessary GraphicalNodes
   *  and Connectors, while the algorithm puts them into their final places,
   *  eventually with hints already given in this method.
   */
  @SuppressWarnings("unchecked")
  @Override
  public GraphicalNode computeLayout(Object model, ModelAdapter adapt) {
    _context = new ViewContext(model, adapt);
    GraphNode graphNode = new GraphNode(model);

    // Sort vertices and edges according to topological sort:
    List<GraphicalNode> vertices = topSort(model, graphNode);

    // create nodes and connectors for all edges
    for (GraphicalNode sourceNode : vertices) {
      Iterable edges = adapt.outEdges(sourceNode.getModel());
      if (edges != null) {
        for (Object edge : edges) {
          String name = adapt.getAttribute(edge, "name");
          GraphicalNode edgeNode =
            new TextNode((name != null) ? name : "", Style.get("edge"));
          _context.setModel(edgeNode, edge);
          graphNode.addNode(edgeNode);

          graphNode.addConnector(
              new SquareBendConnector(sourceNode, edgeNode, 'H'));
          GraphicalNode targetNode =
            _context.getRepresentative(adapt.target(edge));
          assert(targetNode != null);
          graphNode.addConnector(
              new SquareBendConnector(edgeNode, targetNode, 'V'));
        }
      }
    }

    // the edges are sorted according to the graph that is established by the
    // children relation
    IdentityHashMap<GraphicalNode, Integer> edgeLevel =
      new IdentityHashMap<GraphicalNode, Integer>();
    for (GraphicalNode sourceNode : vertices) {
      Iterable edges = adapt.outEdges(sourceNode.getModel());
      if (edges != null) {
        for (Object edge : edges) {
          computeMinLevel(edge, edgeLevel);
        }
      }
    }

    graphNode.setLayoutAlgorithm(new ChartLayoutAlgorithm(vertices, edgeLevel));
    return graphNode;
  }
}
