/*
 * 
 */
package de.dfki.lt.loot.gui;

import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

// TODO: Auto-generated Javadoc
/**
 * The Interface Layout.
 */
public interface Layout {

  /**
   * Compute layout.
   * 
   * @param model the model
   * @param adapt the adapt
   * @return the graphical node
   */
  GraphicalNode computeLayout(Object model, ModelAdapter adapt);

  /**
   * Transform.
   * 
   * @param model the model
   * @param context the context
   * @param facetMask the facet mask
   * @return the graphical node
   */
  GraphicalNode transform(Object model, ViewContext context, int facetMask);

}
