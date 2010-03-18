/*
 * 
 */
package de.dfki.lt.loot.gui;

import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.TextNode;

// TODO: Auto-generated Javadoc
/**
 * The Class BasicAtomLayout.
 */
public class BasicAtomLayout extends FacetLayout {

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.FacetLayout#facet()
   */
  @Override
  int facet() {
    return ModelAdapter.ATOM;
  }

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.FacetLayout#transform(java.lang.Object, de.dfki.lt.loot.gui.ViewContext, int)
   */
  @Override
  GraphicalNode transform(Object model, ViewContext context, int facetMask) {
    return new TextNode(
        (model == null ? "<NULL>" : model.toString()), Style.get("default"));
  }

}
