package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

/** This produces graphical representations based on a facet of the model
 *  A facet represents the data structure view on an object, such as list,
 *  map, set, atom, tree, graph
 */
public interface FacetLayout {

  abstract public int facet();

  abstract public GraphicalNode
  transform(Object model, ViewContext context, int facetMask);

  abstract public void register(FacetLayout metaLayout);

}