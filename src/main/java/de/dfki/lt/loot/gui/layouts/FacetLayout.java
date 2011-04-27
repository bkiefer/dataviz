package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

/** This produces graphical representations based on a facet of the model
 *  A facet represents the data structure view on an object, such as list,
 *  map, set, atom, tree, graph
 */
public interface FacetLayout {

  /** A bitmask of the facets the implementing class provides */
  abstract public int facet();

  /** The actual transformation method. This method should call its meta class
   *  for dispatching according to facets.
   */
  abstract public GraphicalNode
  transform(Object model, ViewContext context, int facetMask);

  /** Register the implementing class at a meta class that dispatches according
   *  to the provided facets.
   */
  abstract public void register(FacetLayout metaLayout);

}
