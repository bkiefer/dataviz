package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

/** This produces graphical representations based on a facet of the model
 *  A facet represents the data structure view on an object, such as list,
 *  map, set, atom, tree, graph
 */
public abstract class FacetLayout {

  protected Layout _metaLayout;

  abstract int facet();

  abstract GraphicalNode
  transform(Object model, ViewContext context, int facetMask);

  void register(Layout metaLayout) {
    _metaLayout = metaLayout;
  }

}
