package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public interface Layout {

  /** This method transforms the model into a hierarchy of GraphicalNodes,
   *  possibly with the help of the ModelAdapter, which adapts to a set of
   *  common interfaces, called facets.
   */
  GraphicalNode computeLayout(Object model, ModelAdapter adapt);

}
