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
 *  layout to the facet layouts.
 */
public abstract class AbstractLayout implements Layout {

  private List<FacetLayout> _facetLayouts = new ArrayList<FacetLayout>(10);

  public void addLayout(FacetLayout layout) {
    _facetLayouts.add(layout);
    layout.register(this);
  }

  /**
   * @param model   an object to transform into a graphical representation
   * @return a GraphicalNode representation of the model
   *         You will need to call adjustSize(Graphics g) on the return value.
   */
  public GraphicalNode computeLayout(Object model, ModelAdapter adapt) {
    ViewContext context = new ViewContext(model, adapt);

    return this.transform(model, context, ModelAdapter.ALL);
  }


  //dispatch between the different types of fs nodes. The model has to
  // provide the distinction between the nodes
  protected GraphicalNode
  someFSNode(Object model, ViewContext context, int facetMask) {
    int facets = context._adapt.facets(model) & facetMask;
    GraphicalNode result = null;
    for (FacetLayout layout : _facetLayouts) {
      if ((facets & layout.facet()) != 0) {
        result = layout.transform(model, context, facetMask);
      }
      if (result != null) break;
    }

    if (result == null)
      throw new RuntimeException("facets can not be handled " + facets);
    return result;
  }

  /** dispatch between the different types of fs nodes. The model has to
   * provide the distinction between the nodes by fulfilling facets
   */
  @Override
  public GraphicalNode
  transform(Object model, ViewContext context, int facetMask) {
    GraphicalNode node = context.getRepresentative(model);
    if (node != null) {
      // check if this node has been visited more than once
      int corefNo = context.equivalenceClassNo(model);
      if (corefNo == 0) {
        // this is the second visit of model, assign a new equivalence class no.
        // add also a coreference sign between the parent and this node
        corefNo = context.setRepresentative(model, node);

        // get the old parent of node
        GraphicalNode parent = node.getParentNode();

        // now we embed node into another composite node that also contains
        // a coreference
        CompositeNode tnode = new CompositeNode('n');
        if (parent != null) {
          // this also releases the parent link of node and replaces node by
          // tnode
          parent.exchangeNode(node, tnode);
        }
        tnode.addNode(new TextNode(Integer.toString(corefNo),
            Style.get("coref")));
        tnode.addNode(node);
      }
      // now: draw only coref, corefNo is the equivalence class no and node
      // the representative
      node = new TextNode(Integer.toString(corefNo), Style.get("coref"));
    } else {
      // no coref: construct only subnode
      node = someFSNode(model, context, facetMask);
    }

    String fw = context._adapt.getAttribute(model, "fwfailure");
    String bw = context._adapt.getAttribute(model, "bwfailure");
    if ((fw != null) || (bw != null)) {
      CompositeNode failnode = new CompositeNode('w');
      if (fw != null) {
        failnode.addNode(new TextNode(fw.toString(), Style.get("fwfailure")));
      }
      if (bw != null) {
        failnode.addNode(new TextNode(bw.toString(), Style.get("bwfailure")));
      }
      failnode.addNode(node);
      node = failnode;
    }

    context.setModel(node, model);
    return node;
  }

}
