package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.connectors.StraightConnector;
import de.dfki.lt.loot.gui.nodes.GraphNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class SimpleTreeLayout extends FacetLayoutBase {

  /** Specifies the orientation of the layout. Default is false. */
  protected boolean horizontal = false;

  /** Holds the levelDistance. Default is 10. */
  protected int levelDistance = 10;

  /** Holds the nodeDistance. Default is 20. */
  protected int nodeDistance = 20;

  /** If true, this algorithm layouts from right to left, otherwise from top
   *  to bottom.
   *  @return the horizontal */
  public boolean isHorizontal() {
    return horizontal;
  }

  /** If horizontal is true, this algorithm layouts from right to left,
   *  otherwise from top to bottom.
   */
  public void setHorizontal(boolean horizontal) {
    this.horizontal = horizontal;
  }

  /** @return the levelDistance */
  public int getLevelDistance() {
    return levelDistance;
  }

  /** @param levelDistance the levelDistance to set */
  public void setLevelDistance(int levelDistance) {
    this.levelDistance = levelDistance;
  }

  /** @return the nodeDistance */
  public int getNodeDistance() {
    return nodeDistance;
  }

  /** @param nodeDistance the nodeDistance to set */
  public void setNodeDistance(int nodeDistance) {
    this.nodeDistance = nodeDistance;
  }

  public int facet() {
    return ModelAdapter.TREE;
  }

  public GraphicalNode
  transform(Object model, ViewContext context, int facetMask) {
    GraphNode result = new GraphNode(model);
    GraphicalNode root = transformTreeInner(model, context, result, facetMask);
    mxCompactTreeLayout la =
      new mxCompactTreeLayout(root, horizontal, levelDistance, nodeDistance);
    result.setLayoutAlgorithm(la);
    //    new SimpleTreeLayoutAlgorithm(root, 5, 20));
    return result;
  }

  @SuppressWarnings("rawtypes")
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
