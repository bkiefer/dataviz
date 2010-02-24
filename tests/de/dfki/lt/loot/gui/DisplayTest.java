package de.dfki.lt.loot.gui;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.HashMap;

import de.dfki.lt.loot.gui.adapters.CollectionsAdapter;
import de.dfki.lt.loot.gui.adapters.EmptyModelAdapter;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.connectors.StraightConnector;
import de.dfki.lt.loot.gui.nodes.GraphNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.TextNode;

class TestIntersectLayout implements Layout {
  public class NoLayoutAlgorithm extends GraphNode.LayoutAlgorithm {
    GraphNode _root;

    NoLayoutAlgorithm(GraphNode node) {
      node.super();
      _root = node;
    }

    @Override
    public Rectangle execute() {
      int width = 0 , height = 0;
      for (GraphicalNode node : _root.getNodes()) {
        node.setOrigin(node.getRect().x - node.getRect().width / 2,
            node.getRect().y);
        width = Math.max(width, node.getRect().x + node.getRect().width);
        height = Math.max(height, node.getRect().y + node.getRect().height);
      }

      return new Rectangle(0,0,width,height);
    }
  }

  public GraphicalNode computeLayout(Object model, ModelAdapter adapt) {
    int radius = 400;
    GraphNode node = new GraphNode(null);
    node.setLayoutAlgorithm(new NoLayoutAlgorithm(node));
    Style.add("dashed",
        new Font("DejaVu Sans", Font.BOLD, 14), null, null, null,
        new BasicStroke(
            1,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_ROUND,
            0,
            new float[] {9,1},
            0
        ));
    TextNode c = new TextNode("Center", Style.get("coref"));
    c.setOrigin(radius, radius);
    node.addNode(c);
    for(double deg = 0; deg < 360; deg += 10) {
      GraphicalNode nextNode =
        new TextNode("MMM", Style.get("coref"));
      double rad = Math.toRadians(deg);
      nextNode.setOrigin((int)(radius + (radius/2 * Math.sin(rad))),
          (int)(radius + radius/2 * Math.cos(rad)));
      node.addNode(nextNode);

      node.addConnector(new StraightConnector(c, nextNode));
    }
  return node;
  }

  @Override
  public GraphicalNode transform(Object model, ViewContext context,
      int facetMask) {
    // TODO Auto-generated method stub
    return null;
  }


}


public class DisplayTest {

  protected static String example0 =
    "[ rule ARGS [ *cons* FIRST [rule ARGS [ *cons* FIRST [i] REST [ *cons* FIRST [ *cons* FIRST [ g ] REST [*null*] ] REST [*null*] ] ] ] REST [ *cons* FIRST [j] REST [*null*] ] ] ]";

  protected static String example =
    "[ rule ARGS [ *cons* FIRST [rule] REST [ *cons* FIRST [j] REST [*null*] ] ] ] ";

  public static void main(String[] args) throws Exception {
    for (int which = 0; which < 5; ++which)
    { //int which = 2;
      switch(which) {
      case 0: {
        DrawingPanel contentArea =
          new DrawingPanel(
              new TestIntersectLayout(),
              new EmptyModelAdapter());
        MainFrame mf = new MainFrame("NodeTest", contentArea);
        mf.setModel("Test");
        break;
      }
      case 2: {
        DrawingPanel dp =
          new DrawingPanel(new CompactLayout(), new CollectionsAdapter());
        MainFrame mf = new MainFrame("Test", dp);
        HashMap<String, Object> tfs1 = new HashMap<String, Object>() ;
        HashMap<String, Object> tfs2 = new HashMap<String, Object>() ;
        HashMap<String, Object> tfs3 = new HashMap<String, Object>() ;
        tfs1.put("feat1", tfs2);
        tfs1.put("feat2", tfs3);
        tfs2.put("feat3", tfs3);
        tfs3.put("feat4", "Val1");
        mf.setModel(tfs1);
        break;
      }
      /*
      case 1: {
        FSGrammar gram =
          new FSGrammar("/home/kiefer/grammars/minimal/uniftest.grm");
        TFS exampleFS =
          TFS.buildFS(new JxchgTokenizer(new StringReader(example0)));
        TFS.displayAndWait(exampleFS);
        break;
      }
      case 3: {
        MainFrame mf = new MainFrame("Test");
        TypedFeatStruct tfs1 = new TypedFeatStruct("type1");
        TypedFeatStruct tfs2 = new TypedFeatStruct("type2");
        TypedFeatStruct tfs3 = new TypedFeatStruct("type3");

        tfs1.addFeatValPair(new FeatValPair("feat1", tfs2));
        tfs1.addFeatValPair(new FeatValPair("feat2", tfs3));
        tfs2.addFeatValPair(new FeatValPair("feat3", tfs3));
        tfs3.addFeatValPair(new FeatValPair("feat4", "Val1"));

        mf.setModel(tfs1);
      }
      case 4: {
        FSGrammar gram = new FSGrammar("/home/kiefer/doc/phdthesis/erg/english.grm");
        MainFrame mf = new MainFrame("test");
        for (int type : gram.getRuleTypes()) {
          TFS fs = gram.getPi(type);
          mf.setModel(fs.dag());
          Thread.sleep(500);
        }
        break;
      }
      */
      }
    }
  }
}