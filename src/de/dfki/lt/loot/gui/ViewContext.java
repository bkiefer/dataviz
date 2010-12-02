package de.dfki.lt.loot.gui;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;

import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class ViewContext {

  // a mapping of nodes with indegree > 0 to integers
  private IdentityHashMap<Object, Integer> _visited;

  // The equivalence classes of GUI nodes representing the same model node
  private List<List<GraphicalNode>> _equivs;

  // links from the Model to the GUI for all nodes not represented in equivs
  private IdentityHashMap<Object, GraphicalNode> _backPointer;

  // Mediator/Adapter between the actual data structure and the view
  public ModelAdapter _adapt;

  public ViewContext(Object model, ModelAdapter adapt) {
    _adapt = adapt;
    _adapt.setTopModel(model);
    _visited = new IdentityHashMap<Object, Integer>();
    _equivs = new ArrayList<List<GraphicalNode>>();
    _equivs.add(null);
    _backPointer = new IdentityHashMap<Object, GraphicalNode>();
  }

  /** Return an integer that represents the equivalence class number for this
   *  model. If this class has arity zero or one to the knowledge of this
   *  ViewContext, this method will return zero, and the appropriate equivalence
   *  class number otherwise.
   *  @param model a model node
   *  @return a positive integer if there is a set of views for this model that
   *          contains more that one element, -1 otherwise
   */
  public int getEquivalenceClassNo(Object model) {
    Integer corefNoInteger = _visited.get(model);
    return (corefNoInteger == null) ? -1 : corefNoInteger.intValue();
  }

  /** Return an integer for a new equivalence class of view nodes, representing
   *  model. It is illegal to call this method if model was already assigned
   *  a class number.
   */
  public int newEquivalenceClassNo(Object model) {
    assert(! _visited.containsKey(model));
    int result = _equivs.size();
    _visited.put(model, result);
    _equivs.add(new LinkedList<GraphicalNode>());
    return result;
  }

  /** Add this view to the specified equivalence class. This class must exist,
   *  that is, the class number must have been acquired by calling
   *  newEquivalenceClassNo()
   */
  public void addToEquivalenceClass(int classNo, GraphicalNode node) {
    assert(_equivs.size() > classNo);
    _equivs.get(classNo).add(node);
  }


  /** Exchange the representative view for some model.
   *  This should be done if the representative view has been changed or
   *  deleted. The link from the old view to the model is removed and
   *  established for the new view.
   */
  public void changeRepresentative(GraphicalNode newView, Object model) {
    GraphicalNode old = _backPointer.get(model);
    assert(old != null);
    old.setModel(null);
    newView.setModel(model);
    _backPointer.put(model, newView);
    Integer corefNo = _visited.get(old);
    if (corefNo != null) {
      _visited.remove(old);
      _visited.put(newView, corefNo);
    }
  }

  /** return the graphicalnode that primarily represents an object */
  public GraphicalNode getRepresentative(Object model) {
    return _backPointer.get(model);
  }

  /** return the graphicalnode that primarily represents an object */
  public boolean hasRepresentative(Object model) {
    return _backPointer.containsKey(model);
  }

  /** return the graphicalnode that primarily represents an object */
  public void setRepresentative(Object model, GraphicalNode view) {
    _backPointer.put(model, view);
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
