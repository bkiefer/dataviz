
package de.dfki.lt.loot.gui.quadtree;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Represents a node of a {@link Quadtree}.  Nodes contain
 * items which have a spatial extent corresponding to the node's position
 * in the quadtree.
 */
@SuppressWarnings("unchecked")
public class Node<Content> {
  /**
   * subquads are numbered as follows:
   * <pre>
   *  2 | 3
   *  --+--
   *  0 | 1
   * </pre>
   */
  protected Node<Content>[] subnode = new Node[4];

  protected List<Content> items;
  protected List<Rectangle> envelopes;

  // the singleton root quad is centered at the origin.

  private Rectangle env;

  class Stats {
    int subnodesPerNode = 0;
    int emptySubnodes = 0;
    int itemsPerNode = 0;
    int nodeCount = 0;

    ArrayList<Integer> itDistrib = new ArrayList<Integer>();

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      Collections.sort(itDistrib);
      sb.append(
      " nc:" + nodeCount +
      " esn: " + emptySubnodes +
      " it: " + itemsPerNode +
      " sn/nd: " + (float) subnodesPerNode / nodeCount +
      " it/nd: " + (float) itemsPerNode / nodeCount +
      " it/ned: " + (float) itemsPerNode / (nodeCount - emptySubnodes) +
      " it/median: " + itDistrib.get(itDistrib.size() / 2) +
      "  |");
      for (int i : itDistrib) {
        sb.append(Integer.toString(i)); sb.append('|');
      }
      return sb.toString();
    }
  }

  void getStats(Stats stats) {
    ++stats.nodeCount;
    if (items != null) {
      stats.itemsPerNode += items.size();
      stats.itDistrib.add(items.size());
    }
    else {
      ++stats.emptySubnodes;
    }
    for (Node<Content> sub : subnode) {
      if (sub != null) {
        ++stats.subnodesPerNode;
        sub.getStats(stats);
      }
    }
  }	//


  /**
   * Returns the index of the subquad that wholly contains the given envelope.
   * If none does, returns -1.
   */
  private int getSubnodeIndex(Rectangle input) {
    int subnodeIndex = -1;
    if (input.x >= env.x + (env.width >> 1)) {
      if (input.y >= env.y + (env.height >> 1)) subnodeIndex = 3;
      if (input.y + input.height <= env.y + (env.height >> 1)) subnodeIndex = 1;
    }
    if (input.x + input.width <= env.x + (env.width >> 1)) {
      if (input.y >= env.y + (env.height >> 1)) subnodeIndex = 2;
      if (input.y + input.height <= env.y + (env.height >> 1)) subnodeIndex = 0;
    }
    return subnodeIndex;
  }

  /** Create the root node */
  Node(Rectangle env) {
    this.env = env;
  }

  private void add(Content item, Rectangle env) {
    if (items == null) {
      items = new ArrayList<Content>();
      envelopes = new ArrayList<Rectangle>();
    }
    items.add(item);
    envelopes.add(env);
  }

  void addAllItemsFromOverlapping(Rectangle searchEnv,
      List<Content> resultItems) {
    if (! env.intersects(searchEnv))
      return;

    if (items != null) {
      if (items != null) {
        for (int i = 0; i < envelopes.size(); ++i) {
          if (envelopes.get(i).intersects(searchEnv)) {
            resultItems.add(items.get(i));
          }
        }
      }
    }

    for (int i = 0; i < 4; i++) {
      if (subnode[i] != null) {
        subnode[i].addAllItemsFromOverlapping(searchEnv, resultItems);
      }
    }
  }

  void addAllItemsFromOverlapping(Point searchEnv, List<Content> resultItems) {
    if (! env.contains(searchEnv))
      return;

    if (items != null) {
      for (int i = 0; i < envelopes.size(); ++i) {
        if (envelopes.get(i).contains(searchEnv)) {
          resultItems.add(items.get(i));
        }
      }
    }

    for (int i = 0; i < 4; ++i) {
      if (subnode[i] != null) {
        subnode[i].addAllItemsFromOverlapping(searchEnv, resultItems);
      }
    }
  }

  /**
   * Returns the smallest <i>existing</i>
   * node containing the envelope.
   */
  public Node<Content> find(Rectangle searchEnv)
  {
    int subnodeIndex = getSubnodeIndex(searchEnv);
    if (subnodeIndex == -1)
      return this;
    if (subnode[subnodeIndex] != null) {
      // query lies in subquad, so search it
      Node<Content> node = subnode[subnodeIndex];
      return node.find(searchEnv);
    }
    // no existing subquad, so return this one anyway
    return this;
  }

  /**  create a new subquad in the appropriate quadrant */
  private Node<Content> createSubnode(int index) {
    int minx = 0;
    int maxx = 0;
    int miny = 0;
    int maxy = 0;

    switch (index) {
    case 0:
      minx = env.x;
      maxx = env.x + (env.width >> 1);
      miny = env.y;
      maxy = env.y + (env.height >> 1);
      break;
    case 1:
      minx = env.x + (env.width >> 1);
      maxx = env.x + env.width;
      miny = env.y;
      maxy = env.y + (env.height >> 1);
      break;
    case 2:
      minx = env.x;
      maxx = env.x + (env.width >> 1);
      miny = env.y + (env.height >> 1);
      maxy = env.y + env.height;
      break;
    case 3:
      minx = env.x + (env.width >> 1);
      maxx = env.x + env.width;
      miny = env.y + (env.height >> 1);
      maxy = env.y + env.height;
      break;
    }
    Rectangle sqEnv = new Rectangle(minx, miny, maxx - minx, maxy - miny);
    Node<Content> node = new Node<Content>(sqEnv);
    return node;
  }

  /**
   * Insert an item into the quadtree this is the root of.
   */
  public void insert(Rectangle itemEnv, Content item)
  {
    int index = getSubnodeIndex(itemEnv);
    // if index is -1, itemEnv must cross the X or Y axis.
    if (index == -1) {
      add(item, itemEnv);
      return;
    }

    Node<Content> node = subnode[index];
    if (node == null) {
      node = subnode[index] = createSubnode(index);
    }
    node.insert(itemEnv, item);
  }

  @Override
  public String toString() {
    return (int) env.getX() + " " + (int) env.getY()
    + " " + (int) env.getWidth() + " " + (int) env.getHeight();
  }
}
