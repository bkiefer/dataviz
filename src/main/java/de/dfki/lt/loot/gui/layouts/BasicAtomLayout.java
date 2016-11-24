package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.TextNode;

public class BasicAtomLayout implements FacetLayout {

  public
  int facet() {
    return ModelAdapter.ATOM | ModelAdapter.SYMBOL;
  }

  public
  GraphicalNode transform(Object model, ViewContext context, int facetMask) {
    if (model == null) return new TextNode("<NULL>");
    GraphicalNode result =
      new TextNode(((context._adapt.facets(model) & ModelAdapter.SYMBOL) != 0)
          ? context._adapt.getString(model) : model.toString());
    result.setModel(model);
    return result;
  }

  /** This layout does not use any meta layout */
  public void register(FacetLayout metaLayout) {}

}
