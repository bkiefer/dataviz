package de.dfki.lt.loot.gui.layouts;

import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.nodes.CompositeNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.TextNode;

public class FailureDecorator implements Decorator {

  public GraphicalNode decorate(GraphicalNode node, Object model,
    ViewContext context) {

    String fw = context._adapt.getAttribute(model, "fwfailure");
    String bw = context._adapt.getAttribute(model, "bwfailure");
    if ((fw != null) || (bw != null)) {
      CompositeNode failnode = new CompositeNode('w');
      if (fw != null) {
        failnode.addNode(new TextNode(fw.toString(), Style.get("fwfailure")));
      }
      if (bw != null) {
        failnode.addNode(new TextNode(bw.toString(), Style.get("bwfailure")));
      }
      failnode.addNode(node);
      node = failnode;
    }
    return node;
  }

}
