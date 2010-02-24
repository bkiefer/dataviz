/**
 * Determines how a model is rendered by constructing the appropriate structure
 * from subclasses of GraphicalNode
 */
package de.dfki.lt.loot.gui;

import de.dfki.lt.loot.Pair;
import de.dfki.lt.loot.gui.adapters.MapAdapterIterator;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.SquareBracketNode;
import de.dfki.lt.loot.gui.nodes.TabularNode;
import de.dfki.lt.loot.gui.nodes.TextNode;
import de.dfki.lt.loot.gui.nodes.BracketNode.Orientation;

/**
 * @author Michael Haas
 * @author Hanne Kosinowski
 * @author Marc Schulder
 * @author Bernd Kiefer
 * @version $Id: Layout.java 125 2009-01-08 09:20:40Z marc $
 */

public class AlignLayout extends AbstractLayout {

  /**
   * @param model
   *          a typed feature structure
   * @return a GraphicalNode representation of the TypedFeatStruct
   */
  @Override
  protected GraphicalNode
  transform(Object model, ViewContext context) {
    GraphicalNode node = null;
    // draw coref and maybe return
    int corefNo = context.visited(model);
    if (corefNo > 0) {
      // embed node in coref
      TabularNode tnode = new TabularNode(false, "N", 4);
      tnode.startNext();
      tnode.addNode(new TextNode(Integer.toString(corefNo), Style.get("coref")));
      transformMap(tnode, model, context);
      node = tnode;
    } else {
      if (corefNo < 0) {
        // draw only coref
        node = new TextNode(Integer.toString(- corefNo), Style.get("coref"));
      } else {
        // create parent node
        TabularNode tnode = new TabularNode (false, "c", 3);
        transformMap(tnode, model, context);
        node = tnode;
      }
    }
    context.setModel(node, model);
    return node;
  }

  private void
  transformMap(TabularNode node, Object model, ViewContext context) {
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
        fvlistNode.addNode(transform(fvp.getSecond(), context));
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
  }
}
