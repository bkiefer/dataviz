/*
 * 
 */
package de.dfki.lt.loot.gui;

import de.dfki.lt.loot.gui.nodes.GraphicalNode;

// TODO: Auto-generated Javadoc
/**
 * This produces graphical representations based on a facet of the model
 * A facet represents the data structure view on an object, such as list,
 * map, set, atom, tree, graph.
 */
public abstract class FacetLayout {

  /** The _meta layout. */
  protected Layout _metaLayout;

  /**
   * Facet.
   * 
   * @return the int
   */
  abstract int facet();

  /**
   * Transform.
   * 
   * @param model the model
   * @param context the context
   * @param facetMask the facet mask
   * @return the graphical node
   */
  abstract GraphicalNode
  transform(Object model, ViewContext context, int facetMask);

  /**
   * Register.
   * 
   * @param metaLayout the meta layout
   */
  void register(Layout metaLayout) {
    _metaLayout = metaLayout;
  }

}
