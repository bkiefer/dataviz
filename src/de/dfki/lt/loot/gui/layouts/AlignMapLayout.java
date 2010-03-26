package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.Pair;
import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.adapters.MapAdapterIterator;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.SquareBracketNode;
import de.dfki.lt.loot.gui.nodes.TabularNode;
import de.dfki.lt.loot.gui.nodes.TextNode;
import de.dfki.lt.loot.gui.nodes.BracketNode.Orientation;

public class AlignMapLayout extends FacetLayout {

  @Override
  public int facet() {
    return ModelAdapter.ALL;
  }

  @Override
  public GraphicalNode
  transform(Object model, ViewContext context, int facetMask) {
    // create parent node
    TabularNode node = new TabularNode (false, "c", 3);
    MapAdapterIterator fvpList = context._adapt.mapIterator(model);
    if (fvpList != null) {
      node.startNext("e");
      // add brackets to our node
      node.addNode(new SquareBracketNode(Orientation.west,
          Style.get("bracket")));

      node.startNext("c");
      // add CompositeNode which holds the SubNodes
      TabularNode fvlistNode = new TabularNode(true, "WW");

      // create text node and set its style
      String type = context._adapt.getAttribute(model, "type");
      if (type != null) {
        TextNode typeTextNode = new TextNode(type);
        typeTextNode.setStyle(Style.get("type"));

        // add type typeTextNode in first row
        fvlistNode.startNext();
        fvlistNode.addNode(typeTextNode);
      }

      String fw = context._adapt.getAttribute(model, "forwardFails");
      // add eventual failures
      if (fw != null) {
        fvlistNode.startNext();
        fvlistNode.addNode(new TextNode(fw, Style.get("fwfailure")));
      }
      String bw = context._adapt.getAttribute(model, "backwardFails");
      if (bw != null) {
        fvlistNode.startNext();
        fvlistNode.addNode(new TextNode(bw, Style.get("bwfailure")));
      }

      // create child nodes
      // add feature value pairs
      while (fvpList.hasNext()) {
        Pair<String, Object> fvp = fvpList.next();
        fvlistNode.startNext();
        TextNode featureNode = new TextNode(fvp.getFirst());
        featureNode.setStyle(Style.get("feature"));
        fvlistNode.addNode(featureNode);
        fvlistNode.addNode(
            _metaLayout.transform(fvp.getSecond(), context, ModelAdapter.ALL));
      }
      node.addNode(fvlistNode);

      node.startNext("w");
      node.addNode(new SquareBracketNode(Orientation.east,
          Style.get("bracket")));
    } else {
      node.startNext("c");
      node.addNode(new TextNode(context._adapt.getAttribute(model,"type"),
          Style.get("type")));
    }
    return node;
  }

}
