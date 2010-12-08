package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.TextNode;

public class BasicAtomLayout implements FacetLayout {

  @Override
  public
  int facet() {
    return ModelAdapter.ATOM;
  }

  @Override
  public
  GraphicalNode transform(Object model, ViewContext context, int facetMask) {
    GraphicalNode result =
      new TextNode(model == null ? "<NULL>" : model.toString());
    if (model != null) result.setModel(model);
    return result;
  }

  /** This layout does not use any meta layout */
  @Override
  public void register(FacetLayout metaLayout) {}

}
