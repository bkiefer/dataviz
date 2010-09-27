package de.dfki.lt.loot.gui.layouts;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import de.dfki.lt.loot.gui.connectors.Connector;
import de.dfki.lt.loot.gui.nodes.GraphNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class mxCompactTreeLayout implements LayoutAlgorithm {

  /** Specifies the orientation of the layout. Default is false. */
  protected boolean horizontal;

  /** Holds the levelDistance. Default is 10. */
  protected int levelDistance = 10;

  /** Holds the nodeDistance. Default is 20. */
  protected int nodeDistance = 20;

  private GraphicalNode _root;

  /** @param root the root of the tree
   */
  public mxCompactTreeLayout(GraphicalNode root) {
    this(root, false);
  }

  /**
   * @param root the root of the tree
   * @param horizontal if true, layouts from right to left, otherwise from top
   *                   to bottom. Default is false;
   */
  public mxCompactTreeLayout(GraphicalNode root, boolean horizontal) {
    this._root = root;
    this.horizontal = horizontal;
  }

  /** If true, this algorithm layouts from right to left, otherwise from top
   *  to bottom.
   *  @return the horizontal */
  public boolean isHorizontal() {
    return horizontal;
  }

  /** If horizontal is true, this algorithm layouts from right to left,
   *  otherwise from top to bottom.
   */
  public void setHorizontal(boolean horizontal) {
    this.horizontal = horizontal;
  }

  /** @return the levelDistance */
  public int getLevelDistance() {
    return levelDistance;
  }

  /** @param levelDistance the levelDistance to set */
  public void setLevelDistance(int levelDistance) {
    this.levelDistance = levelDistance;
  }

  /** @return the nodeDistance */
  public int getNodeDistance() {
    return nodeDistance;
  }

  /** @param nodeDistance the nodeDistance to set */
  public void setNodeDistance(int nodeDistance) {
    this.nodeDistance = nodeDistance;
  }

  /** Implements {@link LayoutAlgorithm}.execute(). */
  public Rectangle execute(GraphNode someNode, Graphics g) {
    TreeNode node = createTreeNodes(_root);
    layout(node);

    // contains the area required for the whole tree after layout computation.
    Rectangle2D bounds = new Rectangle2D.Double();
    if (horizontal) {
      horizontalLayout(node, 0, 0, bounds);
    } else {
      verticalLayout(node, 0, 0, bounds);
    }

    moveNode(node, -bounds.getMinX(), -bounds.getMinY());

    bounds.setRect(0,0,Math.abs(bounds.getWidth()), Math.abs(bounds.getHeight()));
    return bounds.getBounds();
  }


  /** Moves the specified node and all of its children by the given amount. */
  protected void moveNode(TreeNode node, double dx, double dy) {
    node.x += dx;
    node.y += dy;

    node.width = Math.abs(node.width);
    node.height = Math.abs(node.height);
    node.cell.setOrigin((int) node.x, (int)node.y);

    TreeNode child = node.child;
    while (child != null) {
      moveNode(child, dx, dy);
      child = child.next;
    }
  }


  /** Does a depth first search starting at the specified cell. */
  protected TreeNode createTreeNodes(GraphicalNode root) {
    TreeNode node = new TreeNode(root);
    Rectangle r = root.getRect();
    if (horizontal) {
      node.width = r.height;
      node.height = r.width;
    }
    else {
      node.width = r.width;
      node.height = r.height;
    }
    TreeNode currChild = new TreeNode(null);
    node.child = currChild;
    for(Connector out : root.outConnectors()) {
      currChild.next = createTreeNodes(out.toNode());
      currChild = currChild.next;
    }
    node.child = node.child.next;
    return node;
  }


  /** Starts the actual compact tree layout algorithm at the given node. */
  protected void layout(TreeNode node) {
    if (node != null) {
      TreeNode child = node.child;

      while (child != null) {
        layout(child);
        child = child.next;
      }

      if (node.child != null) {
        attachParent(node, join(node));
      } else {
        layoutLeaf(node);
      }
    }
  }


  /** Apply the offsets recursively to the coordinates for horizontal layout */
  protected void horizontalLayout(TreeNode node, double x0, double y0,
      Rectangle2D bounds) {
    node.x += x0 + node.offsetX;
    node.y += y0 + node.offsetY;
    // successively computes the totally needed area
    apply(node, bounds);

    TreeNode child = node.child;
    if (child != null)
    {
      horizontalLayout(child, node.x, node.y, bounds);
      double siblingOffset = node.y + child.offsetY;
      TreeNode s = child.next;

      while (s != null)
      {
        horizontalLayout(s, node.x + child.offsetX, siblingOffset, bounds);
        siblingOffset += s.offsetY;
        s = s.next;
      }
    }
  }

  /** Apply the offsets recursively to the coordinates for vertical layout */
  protected void verticalLayout(TreeNode node, double x0, double y0,
      Rectangle2D bounds) {
    node.x += x0 + node.offsetY;
    node.y += y0 + node.offsetX;
    // successively computes the totally needed area
    apply(node, bounds);

    TreeNode child = node.child;
    if (child != null) {
      verticalLayout(child, node.x, node.y, bounds);
      double siblingOffset = node.x + child.offsetY;
      TreeNode s = child.next;

      while (s != null) {
        verticalLayout(s, siblingOffset, node.y + child.offsetX, bounds);
        siblingOffset += s.offsetY;
        s = s.next;
      }
    }
  }


  /**
   *
   */
  protected void attachParent(TreeNode node, double height) {
    double x = nodeDistance + levelDistance;
    double y2 = (height - node.width) / 2 - nodeDistance;
    double y1 = y2 + node.width + 2 * nodeDistance - height;

    node.child.offsetX = x + node.height;
    node.child.offsetY = y1;

    node.contour.upperHead = createLine(node.height, 0, createLine(x, y1,
        node.contour.upperHead));
    node.contour.lowerHead = createLine(node.height, 0, createLine(x, y2,
        node.contour.lowerHead));
  }

  /**
	 *
	 */
  protected void layoutLeaf(TreeNode node) {
    double dist = 2 * nodeDistance;

    node.contour.upperTail = createLine(node.height + dist, 0, null);
    node.contour.upperHead = node.contour.upperTail;
    node.contour.lowerTail = createLine(0, -node.width - dist, null);
    node.contour.lowerHead = createLine(node.height + dist, 0,
        node.contour.lowerTail);
  }

  /** Merge the contours of all childs of this node. */
  protected double join(TreeNode node) {
    double dist = 2 * nodeDistance;

    TreeNode child = node.child;
    node.contour = child.contour;
    double h = child.width + dist;
    double sum = h;
    child = child.next;

    while (child != null)
    {
      double d = merge(node.contour, child.contour);
      child.offsetY = d + h;
      child.offsetX = 0;
      h = child.width + dist;
      sum += d + h;
      child = child.next;
    }

    return sum;
  }

  /**
	 *
	 */
  protected double merge(Polygon p1, Polygon p2) {
    double x = 0;
    double y = 0;
    double total = 0;

    Polyline upper = p1.lowerHead;
    Polyline lower = p2.upperHead;

    while (lower != null && upper != null) {
      double d = offset(x, y, lower.dx, lower.dy, upper.dx, upper.dy);
      y += d;
      total += d;

      if (x + lower.dx <= upper.dx) {
        x += lower.dx;
        y += lower.dy;
        lower = lower.next;
      } else {
        x -= upper.dx;
        y -= upper.dy;
        upper = upper.next;
      }
    }

    if (lower != null) {
      Polyline b = bridge(p1.upperTail, 0, 0, lower, x, y);
      p1.upperTail = (b.next != null) ? p2.upperTail : b;
      p1.lowerTail = p2.lowerTail;
    } else {
      Polyline b = bridge(p2.lowerTail, x, y, upper, 0, 0);

      if (b.next == null) {
        p1.lowerTail = b;
      }
    }

    p1.lowerHead = p2.lowerHead;

    return total;
  }

  /**
	 *
	 */
  protected double offset(double p1, double p2, double a1, double a2,
      double b1, double b2) {
    double d = 0;

    if (b1 <= p1 || p1 + a1 <= 0) {
      return 0;
    }

    double t = b1 * a2 - a1 * b2;

    if (t > 0) {
      if (p1 < 0) {
        double s = p1 * a2;
        d = s / a1 - p2;
      } else if (p1 > 0) {
        double s = p1 * b2;
        d = s / b1 - p2;
      } else {
        d = -p2;
      }
    } else if (b1 < p1 + a1) {
      double s = (b1 - p1) * a2;
      d = b2 - (p2 + s / a1);
    } else if (b1 > p1 + a1) {
      double s = (a1 + p1) * b2;
      d = s / b1 - (p2 + a2);
    } else {
      d = b2 - (p2 + a2);
    }

    if (d > 0) {
      return d;
    } else {
      return 0;
    }
  }

  /**
	 *
	 */
  protected Polyline bridge(Polyline line1, double x1, double y1,
      Polyline line2, double x2, double y2) {
    double dx = x2 + line2.dx - x1;
    double dy = 0;
    double s = 0;

    if (line2.dx == 0) {
      dy = line2.dy;
    } else {
      s = dx * line2.dy;
      dy = s / line2.dx;
    }

    Polyline r = createLine(dx, dy, line2.next);
    line1.next = createLine(0, y2 + line2.dy - dy - y1, r);

    return r;
  }

  /**
   *
   */
  protected void apply(TreeNode node, Rectangle2D bounds) {
    Rectangle g = node.cell.getRect();

    double newX = Math.min(bounds.getX(), node.x);
    double newY = Math.min(bounds.getY(), node.y);
    double newXmax = Math.max(bounds.getX() + bounds.getWidth(),
        node.x + g.width);
    double newYmax = Math.max(bounds.getY() + bounds.getHeight(),
        node.y + g.height);
    bounds.setRect(newX, newY, newXmax - newX, newYmax - newY);
  }

  /**
   *
   */
  protected static class TreeNode
  {
    @Override
    public String toString() {
      return "["+x+","+y+"]("+width+","+height+")" ;
    }
    /**
     *
     */
    protected GraphicalNode cell;

    /**
     *
     */
    protected double x, y, width, height, offsetX, offsetY;

    /**
     *
     */
    protected TreeNode child, next; // parent, sibling

    /**
     *
     */
    protected Polygon contour = new Polygon();

    /**
     *
     */
    public TreeNode(GraphicalNode cell)
    {
      this.cell = cell;
    }

  }


  /**
	 *
	 */
  protected Polyline createLine(double dx, double dy, Polyline next) {
    return new Polyline(dx, dy, next);
  }

  /**
	 *
	 */
  protected static class Polygon {

    /**
		 *
		 */
    protected Polyline lowerHead, lowerTail, upperHead, upperTail;

  }

  /**
	 *
	 */
  protected static class Polyline {

    /**
		 *
		 */
    protected double dx, dy;

    /**
		 *
		 */
    protected Polyline next;

    /**
		 *
		 */
    protected Polyline(double dx, double dy, Polyline next) {
      this.dx = dx;
      this.dy = dy;
      this.next = next;
    }

  }

}
