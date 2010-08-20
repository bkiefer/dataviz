package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.Pair;
import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.adapters.MapAdapterIterator;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.CompositeNode;
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

    GraphicalNode result = null;
    GraphicalNode attributeNode = null;

    // create text / attribute node if necessary
    String type = context._adapt.getAttribute(model, "type");
    String fw = context._adapt.getAttribute(model, "forwardFails");
    String bw = context._adapt.getAttribute(model, "backwardFails");
    if (type != null || fw != null || bw != null) {
      CompositeNode wrapNode = new CompositeNode('w');

      if (type != null) {
        TextNode typeTextNode = new TextNode(type);
        typeTextNode.setStyle(Style.get("type"));

        // add type typeTextNode in first row
        wrapNode.addNode(typeTextNode);
      }
      // add eventual failures
      if (fw != null) {
        wrapNode.addNode(new TextNode(fw, Style.get("fwfailure")));
      }
      if (bw != null) {
        wrapNode.addNode(new TextNode(bw, Style.get("bwfailure")));
      }
      attributeNode = wrapNode;
    }

    // create parent node
    MapAdapterIterator fvpList = context._adapt.mapIterator(model);
    if (fvpList != null) {
      TabularNode node = new TabularNode (false, "c", 3);
      node.startNext("e");
      // add brackets to our node
      node.addNode(new SquareBracketNode(Orientation.west,
          Style.get("bracket")));

      node.startNext("c");

      // add TabularNode which holds the SubNodes in a tabular form
      TabularNode fvlistNode = new TabularNode(true, "WW");
      // create child nodes
      // add feature value pairs
      while (fvpList.hasNext()) {
        Pair<String, Object> fvp = fvpList.next();
        fvlistNode.startNext();
        TextNode featureNode = new TextNode(fvp.getFirst());
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
      node.addNode(new SquareBracketNode(Orientation.east,
          Style.get("bracket")));
      result = node;
    } else {
      result = (attributeNode == null) ? new TextNode("") : attributeNode;
    }

    return result;
  }

}
