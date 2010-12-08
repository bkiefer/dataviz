package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public interface Layout {

  /** This method transforms the model into a hierarchy of GraphicalNodes,
   *  possibly with the help of the ModelAdapter, which adapts to a set of
   *  common interfaces, called facets.
   *  TODO \todo rename this to computeView
   */
  GraphicalNode computeView(Object model, ViewContext context);

}
