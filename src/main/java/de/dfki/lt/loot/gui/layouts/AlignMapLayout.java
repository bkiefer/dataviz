package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.Pair;
import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.adapters.MapAdapterIterator;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.AligningNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.SquareBracketNode;
import de.dfki.lt.loot.gui.nodes.TabularNode;
import de.dfki.lt.loot.gui.nodes.TextNode;
import de.dfki.lt.loot.gui.nodes.BracketNode.Orientation;

public class AlignMapLayout extends FacetLayoutBase {

  @Override
  public int facet() {
    return ModelAdapter.MAP;
  }

  @Override
  public GraphicalNode
  transform(Object model, ViewContext context, int facetMask) {
    ModelAdapter adapt = context._adapt;

    GraphicalNode result = null;
    AligningNode attributeNode = null;

    // create text / attribute node if necessary
    String type = adapt.getAttribute(model, "type");
    if (type != null) {
      attributeNode = new AligningNode('w');
      attributeNode.addNode(new TextNode(type, Style.get("type")));
    }

    // create parent node
    MapAdapterIterator fvpList = adapt.mapIterator(model);
    if (fvpList != null) {
      TabularNode node = new TabularNode (false, "c", 3);
      node.startNext("e");
      // add brackets to our node
      node.addNode(new SquareBracketNode(Orientation.west));

      node.startNext("c");

      // add TabularNode which holds the SubNodes in a tabular form
      TabularNode fvlistNode = new TabularNode(true, "WW");
      // create child nodes
      // add feature value pairs
      while (fvpList.hasNext()) {
        Pair<Object, Object> fvp = fvpList.next();
        fvlistNode.startNext();
        GraphicalNode featureNode = // new TextNode(fvp.getFirst());
            _meta.transform(fvp.getFirst(), context, facetMask);
        featureNode.setStyle(Style.get("feature"));
        fvlistNode.addNode(featureNode);
        fvlistNode.addNode(
            _meta.transform(fvp.getSecond(), context, ModelAdapter.ALL));
      }
      result = fvlistNode;
      if (attributeNode != null) {
        attributeNode.addNode(fvlistNode);
        result = attributeNode;
      }
      node.addNode(result);

      node.startNext("w");
      node.addNode(new SquareBracketNode(Orientation.east));
      result = node;
    } else {
      result = (attributeNode == null) ? new TextNode("") : attributeNode;
    }

    return result;
  }

}
