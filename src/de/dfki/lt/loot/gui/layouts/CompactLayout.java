package de.dfki.lt.loot.gui.layouts;

public class CompactLayout extends AbstractLayout {

  public CompactLayout() {
    addLayout(new CompactMapLayout());
    addLayout(new CompactConsLayout());
    addLayout(new SimpleTreeLayout());
    addLayout(new BasicAtomLayout());
  }


  /*
  @Override
  protected GraphicalNode
  transform(Object model, ViewContext context) {
    GraphicalNode node = null;
    // draw coref and maybe return
    int corefNo = context.equivalenceClassNo(model);
    if (corefNo > 0) {
      // first coref, then conjunction
      CompositeNode tnode = new CompositeNode('n');
      tnode.addNode(new TextNode(Integer.toString(corefNo),
                                 Style.get("coref")));
      tnode.addNode(someFSNode(model, context));
      node = tnode;

    } else {
      if (corefNo < 0) {
        // draw only coref
        node = new TextNode(Integer.toString(- corefNo), Style.get("coref"));
      } else { // no coref: construct only conjunction
        node = someFSNode(model, context);
      }
    }
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

    context.setModel(node, model);
    return node;
  }

  @Override
  protected GraphicalNode
  transformMap(Object model, ViewContext context) {
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
        fvpNode.addNode(transform(fvp.getSecond(), context));
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


  @Override
  protected GraphicalNode
  transformCons(Object model, ViewContext context) {
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
        GraphicalNode firstNode = transform(first, context);
        // firstNode.getStyle().getPadding().margin += 2;
        node.addNode(firstNode);
      }
      if (rest != null) {
        if ((context._adapt.facets(rest) & ModelAdapter.CONS) != 0
            && (context._adapt.facets(rest) & ModelAdapter.NULL) != 0) {
          node.addNode(new TextNode(" \u2295 ", Style.get("symbol")));
          GraphicalNode thisNode = transform(rest, context);
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


  @Override
  protected GraphicalNode
  transformTree(Object model, ViewContext context) {
    GraphNode result = new GraphNode(model);
    GraphicalNode root = transformTreeInner(model, context, result);
    result.setLayoutAlgorithm(
        new SimpleTreeLayoutAlgorithm(result, root, 5, 20));
    return result;
  }

  @SuppressWarnings("unchecked")
  private GraphicalNode
  transformTreeInner(Object model, ViewContext context, GraphNode graphNode) {
    GraphicalNode parentNode =
      someFSNode(context._adapt.getRootNode(model), context,
          ModelAdapter.ALL & ~ ModelAdapter.TREE);
    graphNode.addNode(parentNode);

    Iterator dtrs = context._adapt.getTreeDaughters(model);
    // first layout the daughters to get the references in the parent node,
    // if desired
    while (dtrs != null && dtrs.hasNext()) {
      Object dtr = dtrs.next();
      GraphicalNode nextDaughter =
        ((context._adapt.facets(dtr) & ModelAdapter.TREE) != 0)
        ? transformTreeInner(dtr, context, graphNode)
        : someFSNode(dtr, context);
        graphNode.addNode(nextDaughter);
      graphNode.addConnector(new StraightConnector(parentNode, nextDaughter));
    }
    return parentNode;
  }
   */
}
