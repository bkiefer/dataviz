package de.dfki.lt.loot.gui;

import java.util.Iterator;

import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.connectors.StraightConnector;
import de.dfki.lt.loot.gui.nodes.GraphNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.SimpleTreeLayoutAlgorithm;

public class SimpleTreeLayout extends FacetLayout {

  @Override
  public int facet() {
    return ModelAdapter.TREE;
  }

  @Override
  public GraphicalNode
  transform(Object model, ViewContext context, int facetMask) {
    GraphNode result = new GraphNode(model);
    GraphicalNode root = transformTreeInner(model, context, result, facetMask);
    result.setLayoutAlgorithm(
        new SimpleTreeLayoutAlgorithm(result, root, 5, 20));
    return result;
  }

  @SuppressWarnings("unchecked")
  private GraphicalNode
  transformTreeInner(Object model, ViewContext context,
                     GraphNode graphNode, int facetMask) {
    GraphicalNode parentNode =
      _metaLayout.transform(context._adapt.getRootNode(model), context,
          facetMask & ~ ModelAdapter.TREE);
    graphNode.addNode(parentNode);

    Iterator dtrs = context._adapt.getTreeDaughters(model);
    // first layout the daughters to get the references in the parent node,
    // if desired
    while (dtrs != null && dtrs.hasNext()) {
      Object dtr = dtrs.next();
      GraphicalNode nextDaughter =
        ((context._adapt.facets(dtr) & ModelAdapter.TREE) != 0)
        ? transformTreeInner(dtr, context, graphNode, facetMask)
        : _metaLayout.transform(dtr, context, ModelAdapter.ALL);
        graphNode.addNode(nextDaughter);
      graphNode.addConnector(new StraightConnector(parentNode, nextDaughter));
    }
    return parentNode;
  }
}
