package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.connectors.StraightConnector;
import de.dfki.lt.loot.gui.nodes.GraphNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class SimpleTreeLayout extends FacetLayoutBase {

  @Override
  public int facet() {
    return ModelAdapter.TREE;
  }

  @Override
  public GraphicalNode
  transform(Object model, ViewContext context, int facetMask) {
    GraphNode result = new GraphNode(model);
    GraphicalNode root = transformTreeInner(model, context, result, facetMask);
    mxCompactTreeLayout la = new mxCompactTreeLayout(root);
    result.setLayoutAlgorithm(la);
    //    new SimpleTreeLayoutAlgorithm(root, 5, 20));
    return result;
  }

  @SuppressWarnings("unchecked")
  private GraphicalNode
  transformTreeInner(Object model, ViewContext context,
                     GraphNode graphNode, int facetMask) {
    GraphicalNode parentNode =
      _meta.transform(context._adapt.getNodeContent(model), context,
          facetMask & ~ ModelAdapter.TREE);
    graphNode.addNode(parentNode);

    Iterable dtrs = context._adapt.getTreeDaughters(model);
    // first layout the daughters to get the references in the parent node,
    // if desired
    if (dtrs != null) {
      for (Object dtr : dtrs) {
        GraphicalNode nextDaughter =
          ((context._adapt.facets(dtr) & ModelAdapter.TREE) != 0)
          ? transformTreeInner(dtr, context, graphNode, facetMask)
          : _meta.transform(dtr, context, ModelAdapter.ALL);
          graphNode.addNode(nextDaughter);
          graphNode.addConnector(new StraightConnector(parentNode,
                                                       nextDaughter));
      }
    }
    return parentNode;
  }
}
