package de.dfki.lt.loot.gui;

import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.nodes.AngleBracketNode;
import de.dfki.lt.loot.gui.nodes.CompositeNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.TextNode;
import de.dfki.lt.loot.gui.nodes.BracketNode.Orientation;

public class CompactConsLayout extends FacetLayout {

  @Override
  public int facet() {
    // TODO Auto-generated method stub
    return ModelAdapter.CONS;
  }

  @Override
  public GraphicalNode
  transform(Object model, ViewContext context, int facetMask) {
    CompositeNode node = new CompositeNode('h');

    node.addNode(new AngleBracketNode(Orientation.west, Style.get("bracket")));
    Object first, rest;
    boolean firstElement = true;
    while (model != null) {
      first = context._adapt.getFirst(model); rest = context._adapt.getRest(model);
      if (first != null) {
        if (firstElement) {
          node.addNode(new TextNode(" ", Style.get("symbol")));
          firstElement = false;
        } else {
          node.addNode(new TextNode(" , ", Style.get("symbol")));
        }
        GraphicalNode firstNode = _metaLayout.transform(first, context, facetMask);
        // firstNode.getStyle().getPadding().margin += 2;
        node.addNode(firstNode);
      }
      if (rest != null) {
        if ((context._adapt.facets(rest) & ModelAdapter.CONS) != 0
            && (context._adapt.facets(rest) & ModelAdapter.NULL) != 0) {
          node.addNode(new TextNode(" \u2295 ", Style.get("symbol")));
          GraphicalNode thisNode = _metaLayout.transform(rest, context, facetMask);
          // firstNode.getStyle().getPadding().margin += 2;
          node.addNode(thisNode);
          rest = null;
        } else {
          if ((context._adapt.facets(rest) & ModelAdapter.NULL) != 0) {
            rest = null;
          }
        }
      }
      model = rest;
    }

    node.addNode(new TextNode(" ", Style.get("bracket")));
    node.addNode(new AngleBracketNode(Orientation.east,
        Style.get("bracket")));

    return node;
  }

}
