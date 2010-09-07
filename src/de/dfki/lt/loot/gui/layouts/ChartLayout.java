package de.dfki.lt.loot.gui.layouts;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

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

  private class ChartEdge implements Comparable<ChartEdge> {
    int start;
    int end;
    int level;
    Object modelEdge;
   
    
    public ChartEdge(int s, int e, Object edge){
      start = s; end = e; modelEdge = edge;
      level = -1;
    }
    
    @SuppressWarnings("unchecked")
    private int computeMinLevel(IdentityHashMap<Object, ChartEdge> edgeLevel) {
      if (level >= 0) {
        return level;
      }
      
      Iterable children = _context._adapt.getTreeDaughters(modelEdge);
      if (children != null) {
        for (Object child : children) {
          if (child != null) {
            int sublevel = edgeLevel.get(child).computeMinLevel(edgeLevel);
            level = Math.max(level, sublevel);
          }
        }
        ++level;
      }
    
      return level;
    }
    
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

    
    // the edges are sorted according to span first, then start vertex
    IdentityHashMap<Object, Integer> vertexPos =
      new IdentityHashMap<Object, Integer>();
    int i = 0;
    for (GraphicalNode node : vertices) {
      vertexPos.put(node.getModel(), i++);
    }
    IdentityHashMap<Object, ChartEdge> edgeSet =
      new IdentityHashMap<Object, ChartEdge>();
    for (GraphicalNode sourceNode : vertices) {
      int startPos = vertexPos.get(sourceNode.getModel());
      Iterable edges = adapt.outEdges(sourceNode.getModel());
      if (edges != null) {
        for (Object edge : edges) {
          edgeSet.put(edge,
              new ChartEdge(startPos, vertexPos.get(adapt.target(edge)), edge));
        }
      }
    }
    
    for (ChartEdge cEdge : edgeSet.values()) {
      cEdge.computeMinLevel(edgeSet);
    }
    
    System.out.print(edgeSet.size());
    int[] minHeight = new int[i];
    for (int j = 0; j < i; ++j) {
      minHeight[j] = 0;
    }
    IdentityHashMap<GraphicalNode, Integer> edgeLevel =
      new IdentityHashMap<GraphicalNode, Integer>();
    PriorityQueue<ChartEdge> edgeQueue = new PriorityQueue(edgeSet.values());
    while (! edgeQueue.isEmpty()) {
      ChartEdge cEdge = edgeQueue.poll();
      int edgeHeight = 0;
      for (int j = cEdge.start; j < cEdge.end; ++j) {
        edgeHeight = Math.max(edgeHeight, minHeight[j]);
      }
      ++edgeHeight;
      edgeLevel.put(_context.getRepresentative(cEdge.modelEdge), edgeHeight);
      for (int j = cEdge.start; j < cEdge.end; ++j) {
        minHeight[j] = Math.max(edgeHeight, minHeight[j]);
      }
    }

    graphNode.setLayoutAlgorithm(new ChartLayoutAlgorithm(vertices, edgeLevel));
    return graphNode;
  }
}
