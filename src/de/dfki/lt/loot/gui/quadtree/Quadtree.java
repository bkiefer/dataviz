
package de.dfki.lt.loot.gui.quadtree;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Quadtree<Content>
{
  private Node<Content> root;

  /**
   * Constructs a Quadtree with zero items.
   */
  public Quadtree(Rectangle maxRect) {
    root = new Node<Content>(maxRect);
  }

  public void insert(Rectangle itemEnv, Content item) {
    root.insert(itemEnv, item);
  }

  public List<Content> query(Rectangle searchEnv) {
    /**
     * the items that are matched are the items in quads which
     * overlap the search envelope
     */
    List<Content> foundItems = new ArrayList<Content>();
    root.addAllItemsFromOverlapping(searchEnv, foundItems);
    return foundItems;
  }

  public List<Content> query(Point searchEnv) {
    /**
     * the items that are matched are the items in quads which
     * overlap the search envelope
     */
    List<Content> foundItems = new ArrayList<Content>();
    root.addAllItemsFromOverlapping(searchEnv, foundItems);
    return foundItems;
  }

  public Node<Content>.Stats getStats() {
    Node<Content>.Stats result = root.new Stats();
    root.getStats(result);
    return result;
  }
}
