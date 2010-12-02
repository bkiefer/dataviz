package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public interface Decorator {

  /** To decorate a previously build node with additional elements */
  public abstract GraphicalNode decorate(GraphicalNode node, Object model,
    ViewContext context);

}
