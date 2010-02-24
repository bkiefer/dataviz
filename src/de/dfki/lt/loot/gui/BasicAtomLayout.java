package de.dfki.lt.loot.gui;

import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.TextNode;

public class BasicAtomLayout extends FacetLayout {

  @Override
  int facet() {
    return ModelAdapter.ATOM;
  }

  @Override
  GraphicalNode transform(Object model, ViewContext context, int facetMask) {
    return new TextNode(
        (model == null ? "<NULL>" : model.toString()), Style.get("default"));
  }

}
