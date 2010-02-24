package de.dfki.lt.loot.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class ViewContext {
  // the next equivalence class no
  private int _nextCoref;

  // a mapping of nodes with indegree > 0 to integers
  private HashMap<Object, Integer> _visited;

  // The equivalence classes of GUI nodes representing the same model node
  private List<List<GraphicalNode>> _equivs;

  // links from the Model to the GUI for all nodes not represented in equivs
  private HashMap<Object, GraphicalNode> _backPointer;

  // Mediator/Adapter between the actual data structure and the view
  public ModelAdapter _adapt;

  public ViewContext(Object model, ModelAdapter adapt) {
    _adapt = adapt;
    _visited = new HashMap<Object, Integer>();
    _nextCoref = 0;
    _equivs = new ArrayList<List<GraphicalNode>>();
    _backPointer = new HashMap<Object, GraphicalNode>();
  }

  int equivalenceClassNo(Object model) {
    Integer corefNoInteger = _visited.get(model);
    if (corefNoInteger == null) return 0;
    return corefNoInteger.intValue();
  }

  int setRepresentative(Object model, GraphicalNode node) {
    _visited.put(model, ++_nextCoref);
    _equivs.add(new LinkedList<GraphicalNode>());
    return _nextCoref;
  }

  @SuppressWarnings("unchecked")
  public void setModel(GraphicalNode node, Object model) {
    node.setModel(model);
    Integer corefNoInteger = _visited.get(model);
    if (corefNoInteger == null) {
      // this is eventually a representative: first visit
      _backPointer.put(model, node);
    } else {
      int corefNo = corefNoInteger.intValue();
      List equiv = _equivs.get(corefNo);
      equiv.add(node);
    }
  }

  /** return the graphicalnode that primarily represents an object */
  public GraphicalNode getRepresentative(Object model) {
    return _backPointer.get(model);
  }

  /** The list of Graphicalnodes that represent a model, without the
   *  representative */
  public List<GraphicalNode> equivalenceClass(Object model) {
    Integer corefNoInteger = _visited.get(model);
    if (corefNoInteger == null) {
      return null;
    }
    return _equivs.get(corefNoInteger.intValue());
  }

}
