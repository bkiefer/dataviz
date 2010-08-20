package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public interface Layout {

  GraphicalNode computeLayout(Object model, ModelAdapter adapt);

  // GraphicalNode transform(Object model, ViewContext context, int facetMask);

}
