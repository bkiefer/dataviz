package de.dfki.lt.loot.gui.layouts;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.connectors.EmptyConnector;
import de.dfki.lt.loot.gui.connectors.SquareBendConnector;
import de.dfki.lt.loot.gui.nodes.CircleNode;
import de.dfki.lt.loot.gui.nodes.GraphNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.TextNode;

public class ChartLayout implements Layout {

  private ViewContext _context;

  /** A DFS that adds node in inverse finishing order to the result list. Thus,
   *  the nodes in result are topologically sorted after this method returns
   */
  @SuppressWarnings("rawtypes")
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
    _context.setRepresentative(vertex, node);
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

  private class ChartEdge implements Comparable<ChartEdge> {
    int start;
    int end;
    int level;
    Object modelEdge;


    public ChartEdge(int s, int e, Object edge){
      start = s; end = e; modelEdge = edge;
      level = 0;
    }

    /** This computes the height of an edge based on its children only.
     *  That means that an edge with no children will get a level of zero,
     *  while an edge with children will get the maximum level of its children,
     *  plus one.
     */
    @SuppressWarnings("rawtypes")
    public int computeTreeLevel(IdentityHashMap<Object, ChartEdge> edgeLevel) {
      if (level != 0) {
        return level;
      }

      Iterable children = _context._adapt.getTreeDaughters(modelEdge);
      if (children != null) {
        for (Object child : children) {
          if (child != null) {
            int sublevel = edgeLevel.get(child).computeTreeLevel(edgeLevel);
            level = (start == end)
                ?  Math.min(level, sublevel) : Math.max(level, sublevel);
          }
        }
        if (start == end) {
          --level;
        } else {
          ++level;
        }
      }

      return (start == end) ? -level : level;
    }

    /** Compare edges on the basis of a) span and b) tree height (max path
     *  length to any of the leaves
     */
    public int compareTo(ChartEdge e2) {
      int diff = ((end - start) - (e2.end - e2.start));
      if (diff == 0) {
        diff = level - e2.level;
      }
      if (diff == 0) {
        diff = start - e2.start;
      }
      return diff;
    }
  }

  /** In general, this method should create all the necessary GraphicalNodes
   *  and Connectors, while the algorithm puts them into their final places,
   *  eventually with hints already given in this method.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public GraphicalNode computeView(Object model, ViewContext context) {
    _context = context;
    ModelAdapter adapt = _context._adapt;
    GraphNode graphNode = new GraphNode(model);

    // Sort vertices and edges according to topological sort:
    List<GraphicalNode> vertices = topSort(model, graphNode);

    // create nodes and connectors for all edges
    for (GraphicalNode sourceNode : vertices) {
      Iterable edges = adapt.outEdges(sourceNode.getModel());
      if (edges != null) {
        for (Object edge : edges) {
          String name = adapt.getAttribute(edge, "name");
          String active = adapt.getAttribute(edge, "edgeType");
          GraphicalNode edgeNode =
            new TextNode(((name != null) ? name : ""),
                Style.get(active != null ? active : "edge"));
          _context.setRepresentative(edge, edgeNode);
          graphNode.addNode(edgeNode);
          GraphicalNode targetNode =
            _context.getRepresentative(adapt.target(edge));
          assert(targetNode != null);
          boolean targetIsSource = sourceNode.equals(targetNode);
          graphNode.addConnector(
              targetIsSource
              ? new EmptyConnector(sourceNode, edgeNode)
                  : new SquareBendConnector(sourceNode, edgeNode, 'H'));
          graphNode.addConnector(
              targetIsSource
              ? new EmptyConnector(edgeNode, targetNode)
                  : new SquareBendConnector(edgeNode, targetNode, 'V'));
        }
      }
    }


    // the edges are sorted according to span first, then start vertex
    IdentityHashMap<Object, Integer> vertexPos =
      new IdentityHashMap<Object, Integer>();
    int vertexNo = 0;
    for (GraphicalNode node : vertices) {
      vertexPos.put(node.getModel(), vertexNo++);
    }
    // Map model edges onto ChartEdges to compute the layout
    IdentityHashMap<Object, ChartEdge> edgeMap =
      new IdentityHashMap<Object, ChartEdge>();
    for (GraphicalNode sourceNode : vertices) {
      int startPos = vertexPos.get(sourceNode.getModel());
      Iterable edges = adapt.outEdges(sourceNode.getModel());
      if (edges != null) {
        for (Object edge : edges) {
          edgeMap.put(edge,
              new ChartEdge(startPos, vertexPos.get(adapt.target(edge)), edge));
        }
      }
    }

    // compute the tree height for every edge, that is, the maximum length of
    // a path to some leaf (plus one) when looking at the chart edges as tree
    // nodes.
    for (ChartEdge cEdge : edgeMap.values()) {
      cEdge.computeTreeLevel(edgeMap);
    }

    // maxHeight[j] contains the maximum height of an edge in the gap between
    // nodes j and j + 1
    int[] maxHeight = new int[vertexNo];
    int[] minLoop = new int[vertexNo];
    for (int j = 0; j < vertexNo; ++j) {
      maxHeight[j] = 0;
      minLoop[j] = 0;
    }
    IdentityHashMap<GraphicalNode, Integer> edgeLevel =
      new IdentityHashMap<GraphicalNode, Integer>();
    PriorityQueue<ChartEdge> edgeQueue = new PriorityQueue(edgeMap.values());
    while (! edgeQueue.isEmpty()) {
      ChartEdge cEdge = edgeQueue.poll();
      int edgeHeight = 0;
      if (cEdge.start < cEdge.end) {
        // after this loop, edgeHeight will be the maximum height of an edge
        // between cEdge.start and cEdge.end
        for (int j = cEdge.start; j < cEdge.end; ++j) {
          edgeHeight = Math.max(edgeHeight, maxHeight[j]);
        }
        ++edgeHeight;
        // update maxHeight to include the currently treated edge
        for (int j = cEdge.start; j < cEdge.end; ++j) {
          maxHeight[j] = Math.max(edgeHeight, maxHeight[j]);
        }
      } else {
        edgeHeight = minLoop[cEdge.start] - 1;
        minLoop[cEdge.start] = edgeHeight;
      }
      // now we've computed the abstract (grid) height for an edge
      edgeLevel.put(_context.getRepresentative(cEdge.modelEdge), edgeHeight);
    }

    graphNode.setLayoutAlgorithm(new ChartLayoutAlgorithm(vertices, edgeLevel));
    return graphNode;
  }
}
