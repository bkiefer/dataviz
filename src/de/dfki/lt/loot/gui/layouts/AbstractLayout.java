package de.dfki.lt.loot.gui.layouts;

import java.util.ArrayList;
import java.util.List;

import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.CompositeNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.TextNode;

/** This implements a quite generic `meta layout', which delegates the true
 *  layout to the facet layouts and cares about displaying coreferences
 *  (nodes with an indegree > 1) by displaying little boxes.
 *
 *  Alternative, a subclass can overwrite the transformBasic method and
 *  do its own basic transformation / dispatching.
 */
public abstract class AbstractLayout implements Layout, FacetLayout {

  private List<FacetLayout> _facetLayouts = new ArrayList<FacetLayout>(10);

  private List<Decorator> _decorators = new ArrayList<Decorator>(10);

  private int _facets;

  public void addLayout(FacetLayout layout) {
    _facetLayouts.add(layout);
    _facets |= layout.facet();
    layout.register(this);
  }

  public void addDecorator(Decorator deco) {
    _decorators.add(deco);
  }

  public int facet() {
    return _facets;
  }

  /** A do-noting. This is a meta layout that does not use others. */
  public void register(FacetLayout metaLayout) {
  }

  /** The main method for Layout. Compute a view out of a model, with the
   *  help of a ViewContext and its embedded ModelAdapter
   *
   * @param model
   *          an object to transform into a graphical representation
   * @return a GraphicalNode representation of the model You will need to call
   *         adjustSize(Graphics g) on the return value.
   */
  @Override
  public GraphicalNode computeView(Object model, ViewContext context) {
    GraphicalNode root = this.transform(model, context, ModelAdapter.ALL);
    // maybe the root node got a coref, but then, this can only occur because
    // of a cycle, so we are safe because that was not added through a change.
    return root;
  }

  /** Dispatch between the different types of models. The ModelAdapter has to
   *  provide the distinction between them, and together with the registered
   *  FacetLayouts, the method looks for a matching between model facets and
   *  the layout to handle them.
   */
  protected GraphicalNode transformBasic(Object model, ViewContext context,
    int facetMask) {
    int facets = context._adapt.facets(model) & facetMask;
    GraphicalNode result = null;
    for (FacetLayout layout : _facetLayouts) {
      if ((facets & layout.facet()) != 0) {
        result = layout.transform(model, context, facetMask);
      }
      if (result != null)
        break;
    }

    if (result == null)
      throw new RuntimeException("facets can not be handled " + facets);
    return result;
  }



  /** Recursively transform the model into a view, adding coreferences where
   *  necessary. The building blocks for each level are created using the
   *  facets and adapter, which decide what should be represented in what way.
   *
   *  Take this as a blueprint for layouts that display coreferences, it deals
   *  with cycles and other problems in the right way. If possible, avoid copy
   *  and paste, rather use a subclass.
   */
  public GraphicalNode transform(Object model, ViewContext context,
    int facetMask) {
    GraphicalNode node;
    // check if model has already been visited
    if (context.hasRepresentative(model)) {
      // check if this node has been visited more than once: is the
      // equivalence class no. for this model nonnegative
      int corefNo = context.getEquivalenceClassNo(model);
      if (corefNo < 0) {
        // this is the second visit of model, assign a new equivalence class no.
        corefNo = context.newEquivalenceClassNo(model);
        // if we have hit a cycle, we can check the class no for the member on
        // return of the recursive call that build its view

        GraphicalNode representative = context.getRepresentative(model);
        // node == null means: the model was visited, but no view has been
        // produced up to this point: we are still in the recursion and
        // have hit a cycle!
        // So, if node != null, we have to decorate the old representative
        // with a coref
        if (representative != null) {
          // get the old parent of node
          GraphicalNode parent = representative.getParentNode();
          assert(parent != null); // root only gets coref through cycle!
          // now we embed node into another composite node that also contains
          // a coreference
          CompositeNode newRepresentative = new CompositeNode('n');
          // change parent -- daughter link
          parent.exchangeNode(representative, newRepresentative);

          // exchange the representative
          context.changeRepresentative(newRepresentative, model);

          newRepresentative.addNode(
              new TextNode(Integer.toString(corefNo), Style.get("coref")));
          newRepresentative.addNode(representative);
        }
      }
      // now: draw only coref, corefNo is the equivalence class no and node
      // the representative
      node = new TextNode(Integer.toString(corefNo), Style.get("coref"));
      for (Decorator deco : _decorators) {
        node = deco.decorate(node, model, context);
      }
      context.addToEquivalenceClass(corefNo, node);
    }
    else {
      // no coref: construct only subnode
      // set visited
      context.setRepresentative(model, null);
      node = transformBasic(model, context, facetMask);
      for (Decorator deco : _decorators) {
        node = deco.decorate(node, model, context);
      }
      int corefNo = context.getEquivalenceClassNo(model);
      if (corefNo >= 0) {
        // there was a cycle found inside the recursive call, so
        // add the appropriate coref to the new node
        CompositeNode newRepresentative = new CompositeNode('n');
        newRepresentative.addNode(
            new TextNode(Integer.toString(corefNo), Style.get("coref")));
        newRepresentative.addNode(node);
        node = newRepresentative;
      }
      context.setRepresentative(model, node);
    }
    return node;
  }

  /* An abstract description of a proper transform method
  private void abstractAlgo(Object model) {
    if (visited(model)) {  // has model been visited
      // Yes, at least the second visit
      int corefNo = getClassNo(model); // get me its equivalence class no
      if (coref < 0) {
        // this marks model as having been visited at least twice, if we have
        // hit a cycle, we can check the class no for the member on return of
        // the recursive call that build its view
        corefNo = getNewClassNo(model);
        // this is the second visit: the (eventual) representative has not
        // been decorated yet.
        GraphicalNode representative = getRepresentative();
        if (representative != null) {
          // we are not in a cycle, model already has a proper representative
          // decorate the current representative and make the new decoration
          // node the new representative
          representative = decorate(representative, corefNo);
          exchangeRepresentative(representative, model);
        }
      }
      // here, corefNo has a positive value
      GrapicalNode newView = makeCorefOnlyView(corefNo);
      addToEquivClass(corefNo, newView);
      return newView;
    } else {
      setVisited(model);
      GraphicalNode node = recursivelyBuildView(model);
      int corefNo = getClassNo(model);
      if (corefNo >= 0) {
        // there was a cycle inside the recursive call
        node = decorateWithCoref(node);
      }
      setRepresentative(model, node);
      return node;
    }
  }
  */
}
