package de.dfki.lt.loot.gui;

import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import de.dfki.lt.loot.gui.adapters.CollectionsAdapter;
import de.dfki.lt.loot.gui.adapters.DOMAdapter;
import de.dfki.lt.loot.gui.adapters.EmptyModelAdapter;
import de.dfki.lt.loot.gui.adapters.MapAdapterIterator;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.adapters.ModelAdapterFactory;
import de.dfki.lt.loot.gui.connectors.SquareBendConnector;
import de.dfki.lt.loot.gui.connectors.StraightConnector;
import de.dfki.lt.loot.gui.controllers.ClickHighlightListener;
import de.dfki.lt.loot.gui.controllers.HoverHighlightListener;
import de.dfki.lt.loot.gui.layouts.AlignLayout;
import de.dfki.lt.loot.gui.layouts.CompactLayout;
import de.dfki.lt.loot.gui.layouts.Layout;
import de.dfki.lt.loot.gui.layouts.LayoutAlgorithm;
import de.dfki.lt.loot.gui.layouts.mxCompactTreeLayout;
import de.dfki.lt.loot.gui.nodes.AligningNode;
import de.dfki.lt.loot.gui.nodes.AngleBracketNode;
import de.dfki.lt.loot.gui.nodes.BraceBracketNode;
import de.dfki.lt.loot.gui.nodes.BracketNode.Orientation;
import de.dfki.lt.loot.gui.nodes.GraphNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;
import de.dfki.lt.loot.gui.nodes.ParenBracketNode;
import de.dfki.lt.loot.gui.nodes.SquareBracketNode;
import de.dfki.lt.loot.gui.nodes.TabularNode;
import de.dfki.lt.loot.gui.nodes.TextNode;

class TestNodesLayout implements Layout {
  public GraphicalNode computeView(Object model, ViewContext ctxt) {
    return level5();
  }
  GraphicalNode level0() {
    TabularNode node =
      new TabularNode (true, "WnN");
    node.startNext();
    node.addNode(new TextNode("NorthWest"));
    node.addNode(new TextNode("North"));
    node.addNode(new TextNode("NorthEast"));
    node.startNext("wce");
    node.addNode(new TextNode("West West West", Style.get("type")));
    Style.add("VeryBig", new Font("DejaVu Sans", Font.ITALIC , 30),
        Color.cyan, Color.green, null, null);
    node.addNode(new TextNode("  Centered  ", Style.get("VeryBig")));
    node.addNode(new TextNode("East East East"));
    node.startNext("SsE");
    node.addNode(new TextNode("SouthWest"));
    node.addNode(new TextNode("South", Style.get("feature")));
    node.addNode(new TextNode("SouthEast"));
    return node;
  }
  GraphicalNode level1() {
    TabularNode node =
      new TabularNode (true, "ccc");
    node.startNext();
    node.addNode(null);
    node.addNode(new ParenBracketNode(Orientation.north,
                                               Style.get("bracket")));
    node.startNext();
    node.addNode(new ParenBracketNode(Orientation.west,
                                               Style.get("bracket")));
    node.addNode(level0());
    node.addNode(new ParenBracketNode(Orientation.east,
                                               Style.get("bracket")));
    node.startNext();
    node.addNode(null);
    node.addNode(new ParenBracketNode(Orientation.south,
                                               Style.get("bracket")));
    node.startNext();
    return node;
  }
  GraphicalNode level2() {
    TabularNode node =
      new TabularNode (true, "ccc");
    node.startNext();
    node.addNode(null);
    node.addNode(new BraceBracketNode(Orientation.north,
                                               Style.get("bracket")));
    node.startNext();
    node.addNode(new BraceBracketNode(Orientation.west,
                                               Style.get("bracket")));
    node.addNode(level1());
    node.addNode(new BraceBracketNode(Orientation.east,
                                               Style.get("bracket")));
    node.startNext();
    node.addNode(null);
    node.addNode(new BraceBracketNode(Orientation.south,
                                               Style.get("bracket")));
    node.startNext();
    return node;
  }
  GraphicalNode level3() {
    TabularNode node =
      new TabularNode (true, "ccc");
    node.startNext();
    node.addNode(null);
    node.addNode(new AngleBracketNode(Orientation.north,
                                               Style.get("bracket")));
    node.startNext();
    node.addNode(new AngleBracketNode(Orientation.west,
                                               Style.get("bracket")));
    node.addNode(level2());
    node.addNode(new AngleBracketNode(Orientation.east,
                                               Style.get("bracket")));
    node.startNext();
    node.addNode(null);
    node.addNode(new AngleBracketNode(Orientation.south,
                                               Style.get("bracket")));
    node.startNext();
    return node;
  }
  GraphicalNode level4() {
    TabularNode node =
      new TabularNode (true, "ccc");
    node.startNext();
    node.addNode(null);
    node.addNode(new SquareBracketNode(Orientation.north));
    node.startNext();
    node.addNode(new SquareBracketNode(Orientation.west));
    node.addNode(level3());
    node.addNode(new SquareBracketNode(Orientation.east));
    node.startNext();
    node.addNode(null);
    node.addNode(new SquareBracketNode(Orientation.south));
    node.startNext();
    return node;
  }
  GraphicalNode level5() {
    Style.add("test", null, null, null, new Padding(0,0,0), null);
    AligningNode hcompo =
      new AligningNode('h', Style.get("test"));
    hcompo.addNode(level4());
    hcompo.addNode(new TextNode("horizontal", Style.get("type")));
    hcompo.addNode(new SquareBracketNode(Orientation.east));
    AligningNode vcompo = new AligningNode('v', Style.get("test"));
    vcompo.addNode(hcompo);
    vcompo.addNode(new TextNode("vertical", Style.get("type")));
    vcompo.addNode(new SquareBracketNode(Orientation.south));
    hcompo = new AligningNode('s', Style.get("test"));
    hcompo.addNode(vcompo);
    hcompo.addNode(new SquareBracketNode(Orientation.west));
    hcompo.addNode(new TextNode("south", Style.get("type")));
    vcompo = new AligningNode('e', Style.get("test"));
    vcompo.addNode(hcompo);
    vcompo.addNode(new TextNode("east", Style.get("type")));
    vcompo.addNode(new SquareBracketNode(Orientation.south));
    hcompo = new AligningNode('n', Style.get("test"));
    hcompo.addNode(vcompo);
    hcompo.addNode(new TextNode("north", Style.get("type")));
    vcompo = new AligningNode('w', Style.get("test"));
    vcompo.addNode(hcompo);
    vcompo.addNode(new TextNode("west", Style.get("type")));
    return vcompo;
  }
}

class TestIntersectLayout implements Layout {
  public class NoLayoutAlgorithm implements LayoutAlgorithm {

    @Override
    public Rectangle execute(GraphNode root, Graphics g) {
      int width = 0 , height = 0;
      for (GraphicalNode node : root.getNodes()) {
        node.setOrigin(node.getRect().x - node.getRect().width / 2,
            node.getRect().y);
        width = Math.max(width, node.getRect().x + node.getRect().width);
        height = Math.max(height, node.getRect().y + node.getRect().height);
      }

      return new Rectangle(0,0,width,height);
    }
  }

  public GraphicalNode computeView(Object model, ViewContext ctxt) {
    int radius = 400;
    GraphNode node = new GraphNode(null);
    node.setLayoutAlgorithm(new NoLayoutAlgorithm());
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
    int center = radius/2 + 40;
    c.setOrigin(center, center);
    node.addNode(c);
    for(double deg = 0; deg < 360; deg += 10) {
      GraphicalNode nextNode =
        new TextNode("MMM", Style.get("coref"));
      double rad = Math.toRadians(deg);
      nextNode.setOrigin((int)(center + (radius/2 * Math.sin(rad))),
          (int)(center + radius/2 * Math.cos(rad)));
      node.addNode(nextNode);

      node.addConnector(new StraightConnector(c, nextNode));
    }
  return node;
  }
}

class TestBendConnectors implements Layout {

  class NoLayoutAlgorithm implements LayoutAlgorithm {

    @Override
    public Rectangle execute(GraphNode result, Graphics g) {
      return new Rectangle(0,0,340,340);
    }
  }

  @Override
  public GraphicalNode computeView(Object model, ViewContext ctxt) {
    GraphNode result = new GraphNode(model);
    result.setLayoutAlgorithm(new NoLayoutAlgorithm());
    int[] xCoord = {40, 140, 240};
    int[] yCoord = {40, 140, 240};
    GraphicalNode[][] grid = new GraphicalNode[3][];
    int i = 0;
    for (int x : xCoord) {
      grid[i] = new GraphicalNode[3];
      int j = 0;
      for (int y : yCoord) {
        if (i != 1 && j != 1) {
          GraphicalNode c = new TextNode("" + i + "x" + j);
          grid[i][j] = c;
          c.setOrigin(x, y);
          result.addNode(c);
        }
        ++j;
      }
      ++i;
    }
    grid[1][1] = new TextNode("CENTER");
    grid[1][1].setOrigin(xCoord[1], yCoord[1]);
    result.addNode(grid[1][1]);

    for (i = 0; i < 3; ++i) {
      for (int j = 0; j < 3; ++j) {
        if (i != 1 && j != 1) {
          result.addConnector(new SquareBendConnector(grid[1][1], grid[i][j], 'H'));
          result.addConnector(new SquareBendConnector(grid[i][j], grid[1][1], 'H'));
        }
      }
    }
    return result;
  }
}

class TestTreeLayout implements Layout {

  TextNode[] nodes = new TextNode[11];

  @Override
  public GraphicalNode computeView(Object model, ViewContext ctxt) {
    GraphNode treeNode = new GraphNode(null);
    TextNode[] nodes = new TextNode[11];
    for (int i = 0; i < 11; ++i) {
      nodes[i] = new TextNode(Integer.toString(i));
      treeNode.addNode(nodes[i]);
    }
    int[][] edges = { {0,1},{0,2},{1,3},{3,7,},{3,8},{2,4},{2,5},{2,6},
        {6,9}, {6,10} };
    for (int[] edge : edges) {
      treeNode.addConnector(
          new StraightConnector(nodes[edge[0]], nodes[edge[1]]));
    }
    mxCompactTreeLayout la = new mxCompactTreeLayout(nodes[0], false, 20, 5);
    treeNode.setLayoutAlgorithm(la);
    return treeNode;
  }
}

class TestBracketsLayout implements Layout {

  @Override
  public GraphicalNode computeView(Object model, ViewContext ctxt) {
    Style.add("pad", null, null, null, new Padding(2,0,2), null);
    TabularNode res = new TabularNode(true, "cc");
    res.startNext();
    GraphicalNode node = new TextNode("<Test>");
    for (int i = 0 ; i < 10; ++i) {
      TabularNode n = new TabularNode(true, "ccc");
      n.startNext();
      n.addNode(null);
      n.addNode(new AngleBracketNode(Orientation.north));
      n.addNode(null);
      n.startNext();
      n.addNode(new AngleBracketNode(Orientation.west));
      n.addNode(node);
      n.addNode(new AngleBracketNode(Orientation.east));
      n.startNext();
      n.addNode(null);
      n.addNode(new AngleBracketNode(Orientation.south));
      n.addNode(null);
      node = n;
    }
    res.addNode(node);
    node = new TextNode("{Test}");
    for (int i = 0 ; i < 10; ++i) {
      TabularNode n = new TabularNode(true, "ccc");
      n.startNext();
      n.addNode(null);
      n.addNode(new BraceBracketNode(Orientation.north));
      n.addNode(null);
      n.startNext();
      n.addNode(new BraceBracketNode(Orientation.west));
      n.addNode(node);
      n.addNode(new BraceBracketNode(Orientation.east));
      n.startNext();
      n.addNode(null);
      n.addNode(new BraceBracketNode(Orientation.south));
      n.addNode(null);
      node = n;
    }
    res.addNode(node);
    res.startNext();
    node = new TextNode("(Test)");
    for (int i = 0 ; i < 10; ++i) {
      TabularNode n = new TabularNode(true, "ccc");
      n.startNext();
      n.addNode(null);
      n.addNode(new ParenBracketNode(Orientation.north));
      n.addNode(null);
      n.startNext();
      n.addNode(new ParenBracketNode(Orientation.west));
      n.addNode(node);
      n.addNode(new ParenBracketNode(Orientation.east));
      n.startNext();
      n.addNode(null);
      n.addNode(new ParenBracketNode(Orientation.south));
      n.addNode(null);
      node = n;
    }
    res.addNode(node);
    node = new TextNode("[Test]");
    for (int i = 0 ; i < 10; ++i) {
      TabularNode n = new TabularNode(true, "ccc");
      n.startNext();
      n.addNode(null);
      n.addNode(new SquareBracketNode(Orientation.north));
      n.addNode(null);
      n.startNext();
      n.addNode(new SquareBracketNode(Orientation.west));
      n.addNode(node);
      n.addNode(new SquareBracketNode(Orientation.east));
      n.startNext();
      n.addNode(null);
      n.addNode(new SquareBracketNode(Orientation.south));
      n.addNode(null);
      node = n;
    }
    res.addNode(node);
    return res;
  }
}

class DagNode {
  public DagNode(String t) { type = t; }
  public String type;
  public HashMap<String, DagNode> edges = new HashMap<String, DagNode>();
}

class DagNodeAdapter extends CollectionsAdapter {
  @Override
  public int facets(Object model) {
    // TODO Auto-generated method stub
    if (model instanceof DagNode)
      return ModelAdapter.MAP;
    return ModelAdapter.ATOM;
  }

  @Override
  public String getAttribute(Object model, String name) {
    if ((model instanceof DagNode) && name.equals("type"))
      return ((DagNode)model).type;
    return null;
  }

  @Override
  public MapAdapterIterator mapIterator(Object model) {
    return new CollectionsMapAdapterIterator(((DagNode) model).edges);
  }
}

public class DisplayTest {

  protected static String example0 =
    "[ rule ARGS [ *cons* FIRST [rule ARGS [ *cons* FIRST [i] REST [ *cons* FIRST [ *cons* FIRST [ g ] REST [*null*] ] REST [*null*] ] ] ] REST [ *cons* FIRST [j] REST [*null*] ] ] ]";

  protected static String example =
    "[ rule ARGS [ *cons* FIRST [rule] REST [ *cons* FIRST [j] REST [*null*] ] ] ] ";

  public static void main(String[] args) throws Exception {
    CollectionsAdapter.init();
    DOMAdapter.init();
    for (int which = 0; which < 9; ++which) {
      //if (which != 6) continue;
      switch(which) {
      case 0: {
        DrawingPanel contentArea =
          new DrawingPanel(
              new TestIntersectLayout(),
              new EmptyModelAdapter());
        MainFrame mf = new MainFrame("IntersectTest", contentArea);
        ((DrawingPanel)mf.getContentArea()).setModel("Test");
        break;
      }
      case 2: {
        HashMap<String, Object> tfs1 = new HashMap<String, Object>() ;
        HashMap<String, Object> tfs2 = new HashMap<String, Object>() ;
        HashMap<String, Object> tfs3 = new HashMap<String, Object>() ;
        List<Object> li1 = new ArrayList<Object>();
        List<Object> li2 = new ArrayList<Object>();
        li1.add("first");
        li1.add("second");
        li2.add("third");
        li2.add("fourth");
        li1.add(li2);
        tfs1.put("feat1", tfs2);
        tfs1.put("feat2", tfs3);
        tfs2.put("feat3", tfs3);
        tfs3.put("feat4", "Val1");
        tfs3.put("feat5", li1);
        // tfs3.put("feat6", li2.iterator());
        ModelAdapter ma = ModelAdapterFactory.getAdapter(tfs1);
        DrawingPanel dp = new DrawingPanel(new CompactLayout(), ma);
        dp.addListener(new ClickHighlightListener());
        MainFrame mf = new MainFrame("CollectionsTest", dp);
        ((DrawingPanel)mf.getContentArea()).setModel(tfs1);
        break;
      }
      case 3: {
        DrawingPanel contentArea =
          new DrawingPanel(new TestNodesLayout(), new EmptyModelAdapter());
        MainFrame mf = new MainFrame("NodeTest", contentArea);
        ((DrawingPanel)mf.getContentArea()).setModel("Test");
        break;
      }
      case 4: {
        DrawingPanel contentArea =
          new DrawingPanel(
              new TestBendConnectors(),
              new EmptyModelAdapter());
        MainFrame mf = new MainFrame("SquareBendTest", contentArea);
        ((DrawingPanel)mf.getContentArea()).setModel("SquareBendTest");
        break;
      }
      case 5: {
        DrawingPanel contentArea =
          new DrawingPanel(
              new TestTreeLayout(),
              new EmptyModelAdapter());
        MainFrame mf = new MainFrame("TreeLayoutTest", contentArea);
        ((DrawingPanel)mf.getContentArea()).setModel("TreeLayoutTest");
        break;
      }
      case 6: {
        DrawingPanel contentArea =
          new DrawingPanel(
              new TestBracketsLayout(),
              new EmptyModelAdapter());
        contentArea.addListener(new ClickHighlightListener());
        MainFrame mf = new MainFrame("BracketTest", contentArea);
        ((DrawingPanel)mf.getContentArea()).setModel("BracketTest");
        break;
      }
      case 7: {
        DagNode root = new DagNode("1");
        DagNode sub = new DagNode("3");
        DagNode sub2 = new DagNode("77"); sub2.edges.put("feat77", root);
        sub.edges.put("feat2", root);
        sub.edges.put("feat4", sub2);
        root.edges.put("feat0", new DagNode("2"));
        root.edges.put("feat1", sub);
        root.edges.put("very_long_feature3", new DagNode("4"));
        DrawingPanel contentArea =
          new DrawingPanel(new CompactLayout(), new DagNodeAdapter());

        contentArea.addListener(new HoverHighlightListener());

        MainFrame mf = new MainFrame("CycleTest", contentArea);
        ((DrawingPanel)mf.getContentArea()).setModel(root);
        break;
      }
      case 8: {
        DagNode root = new DagNode("1");
        DagNode sub = new DagNode("3");
        DagNode sub2 = new DagNode("77"); sub2.edges.put("feat77", root);
        sub.edges.put("feat2", root);
        sub.edges.put("feat4", sub2);
        root.edges.put("feat0", new DagNode("2"));
        root.edges.put("feat1", sub);
        root.edges.put("very_long_feature3", new DagNode("4"));
        DrawingPanel contentArea =
          new DrawingPanel(new AlignLayout(), new DagNodeAdapter());

        contentArea.addListener(new HoverHighlightListener());

        MainFrame mf = new MainFrame("CycleTest", contentArea);
        ((DrawingPanel)mf.getContentArea()).setModel(root);
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
        tfs1.addFeatValPair(new FeatValPair("feat2", tTestNodesLayoutfs3));
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
  
  @Test public void testDisplay() {}
}