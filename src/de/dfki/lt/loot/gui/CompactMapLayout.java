package de.dfki.lt.loot.gui;

import de.dfki.lt.loot.Pair;
import de.dfki.lt.loot.gui.adapters.MapAdapterIterator;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.CompositeNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.SquareBracketNode;
import de.dfki.lt.loot.gui.nodes.TextNode;
import de.dfki.lt.loot.gui.nodes.BracketNode.Orientation;

public class CompactMapLayout extends FacetLayout {

  @Override
  public int facet() {
    return ModelAdapter.CONS;
  }

  @Override
  public GraphicalNode
  transform(Object model, ViewContext context, int facetMask) {
    MapAdapterIterator fvpList =
      context._adapt.mapIterator(model);
    if (fvpList != null) {
      // This CompositeNode will hold the type and feature-value pairs
      CompositeNode fvlistNode = new CompositeNode('w');

      String type = context._adapt.getAttribute(model, "type");
      if (type != null) {
        // add type typeTextNode in first row
        fvlistNode.addNode(new TextNode(type, Style.get("type")));
      }

      // create child nodes: add feature value pairs
      while (fvpList.hasNext()) {
        Pair<String, Object> fvp = fvpList.next();
        CompositeNode fvpNode = new CompositeNode('n');
        fvpNode.addNode(new TextNode(fvp.getFirst(), Style.get("feature")));
        fvpNode.addNode(_metaLayout.transform(fvp.getSecond(), context,
                                              facetMask));
        fvlistNode.addNode(fvpNode);
      }

      CompositeNode node = new CompositeNode('h');
      // add brackets and feature-value list to the result node
      node.addNode(new SquareBracketNode(Orientation.west,
          Style.get("bracket")));
      node.addNode(fvlistNode);
      node.addNode(new SquareBracketNode(Orientation.east,
          Style.get("bracket")));
      return node;
  }
    // only type
    return new TextNode(" " + context._adapt.getAttribute(model, "type"),
        Style.get("type"));
  }
}
