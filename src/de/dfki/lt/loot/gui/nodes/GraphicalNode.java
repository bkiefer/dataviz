package de.dfki.lt.loot.gui.nodes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.dfki.lt.loot.gui.Padding;
import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.connectors.Connector;

public abstract class GraphicalNode {

  protected Object model;
  protected Rectangle area;
  protected GraphicalNode parentNode;
  protected List<Connector> _in, _out;
  protected Style style;

  protected boolean active = false;

  /** Creates a new instance of <code>GraphicalNode</code> with default style
   */
  public GraphicalNode() {
    this(Style.get(""));
  }

  /** Creates a new instance of <code>GraphicalNode</code> with style aStyle
   */
  public GraphicalNode(Style aStyle) {
    this.area = new Rectangle(0, 0, 0, 0);
    this.style = aStyle;
  }

  // BEGIN Getters and Setters
  /** @return The model belonging to this node */
  public Object getModel() { return this.model; }

  /** @return The model belonging to this node */
  public void setModel(Object aModel) { this.model = aModel; }

  /** @return The Rectangle surrounding this node */
  public Rectangle getRect() { return this.area; }

  /** @return the parentNode */
  public GraphicalNode getParentNode() { return this.parentNode; }

  /** @param parentnode the parentNode to set */
  protected void setParentNode(GraphicalNode parentnode) {
    this.parentNode = parentnode;
  }

  /** Only relevant for complex nodes that have sub-nodes: add the given
   *  node to the sub-nodes of this node
   */
  public abstract void addNode(GraphicalNode subNode);

  public abstract void removeNode(GraphicalNode subNode);

  public abstract void exchangeNode(GraphicalNode old, GraphicalNode newNode);

  /** @return the style */
  public Style getStyle() { return this.style; }

  /** @param aStyle the style to set */
  public void setStyle(Style aStyle) { this.style = aStyle; }
  // END Getters and Setters


  private static int showActiveRect = 0;

  /** code to test the paint and computeLayout method
   * @param absArea the total area of the node
   * @param g
   *
   * TODO we need a proper handling of inversion: XOR does not work for rendered
   * fonts. It must be stored somewhere that some nodes are to be drawn inverted,
   * this can not be stored in the style, because the style has to be used
   * to determine how the inversion has to be drawn, and seems not to be the
   * right place to have such dynamic information.
   */
  protected void showEnclosingRect(Rectangle absArea, Graphics g) {
    if (active && showActiveRect > 0) {
      Color current = g.getColor();
      if (showActiveRect == 1) {
        g.setColor(Color.red);
        g.drawRect(absArea.x, absArea.y, absArea.width, absArea.height);
        g.setColor(current);
      } else {
        g.setXORMode(Color.white);
        g.fillRect(absArea.x, absArea.y, absArea.width, absArea.height);
      }
    }
  }

  /** paints the node with regard to the given offset
   * @param parentArea the parent's absolute area
   * @param g Graphics object
   */
  public final void paint(Rectangle parentArea, Graphics g) {
    Rectangle absoluteArea = new Rectangle(this.area);
    // translate (relative) area by origin to get absolute coordinates
    absoluteArea.translate(parentArea.x, parentArea.y);
    // determine if the current node must be repainted
    if (! absoluteArea.intersects(g.getClipBounds())) return;
    showEnclosingRect(absoluteArea, g);
    if (this.style != null) this.style.setStyle(g);

    // now handle padding
    Padding padding = this.style.getPadding();
    if (padding.border > 0) {
      Graphics2D g2d = (Graphics2D) g;
      BasicStroke s = new BasicStroke(padding.border);
      g2d.setStroke(s);
      int margin = padding.margin;
      g2d.drawRect(absoluteArea.x + margin,
          absoluteArea.y + margin,
          absoluteArea.width - margin - margin -1,
          absoluteArea.height - margin - margin -1);
      g2d.setStroke(this.style.getStroke());
    }

    int offset = padding.getOffset();
    if (offset > 0) {
      offset = - offset;
      absoluteArea.grow(offset, offset);
    }

    paintAbsolute(absoluteArea, g);
  }

  public abstract void paintAbsolute(Rectangle absoluteArea, Graphics g);

  /** This function computes the size of the node, and sets area such that the
   * area rectangle is 0, 0, width, height. The computed size might be the
   * minimal required size, which could be changed by the growTo method.
   *
   * @param g The graphics context needed to determine the sizes of basic
   *          elements
   */
  public abstract void adjustSize(Graphics g);

  /** grow the current node to the given size, if wanted. This is mainly meant
   * for stretchable elements, such as brackets. By default, this function
   * does nothing
   * @param width, height a hint how to set the size for stretchable elements,
   *          such as brackets
   */
  public void growTo( int width, int height) { return; }

  /** Move area to the specified point */
  public void setOrigin(Point origin) {
    this.area.setLocation(origin);
  }

  /** Move area to the specified point */
  public void setOrigin(int x, int y) {
    this.area.setLocation(x, y);
  }

  /** return the innermost node still containing @param p */
  protected abstract GraphicalNode getChildContainingPoint(Point p);

  public GraphicalNode getDeepestIncludingPoint(Point p) {
    Rectangle currentRect = getAbsRect();
    GraphicalNode current = this, parent;
    while (! currentRect.contains(p)) {
      if ((parent = current.parentNode) != null) {
        currentRect.x -= area.x;
        currentRect.y -= area.y;
        currentRect.width = parent.area.width;
        currentRect.height = parent.area.height;
        current = parent;
      } else {
        return null;
      }
    }
    p.x -= currentRect.x;
    p.y -= currentRect.y;    return current.getChildContainingPoint(p);
  }

  public void mouseLeaves() {
    active = false;
    return;
  }

  public void mouseEnters() {
    active = true;
    return;
  }

  public Rectangle getAbsRect() {
    Rectangle absoluteArea = new Rectangle(area);
    GraphicalNode current = this, parent;
    while ((parent = current.parentNode) != null) {
      absoluteArea.translate(parent.area.x, parent.area.y);
      current = parent;
    }
    return absoluteArea;
  }

  public void addOutConnector(Connector conn) {
    if (_out == null) {
      _out = new LinkedList<Connector>();
    }
    _out.add(conn);
  }

  public Iterator<Connector> outConnectors() {
    return _out != null ? _out.iterator()
                        : new LinkedList<Connector>().iterator();
  }

  public void addInConnector(Connector conn) {
    if (_in == null) {
      _in = new LinkedList<Connector>();
    }
    _in.add(conn);
  }

  public Iterator<Connector> inConnectors() {
    return _in != null ? _in.iterator()
                       : new LinkedList<Connector>().iterator();
  }

  public Point2D pointOnRim(double dx, double dy) {
    double centerX = area.getCenterX(), centerY = area.getCenterY();
    double x = (dx < 0) ? area.x : area.x + area.width;

    double y = centerY + dy * (x - centerX) / dx;
    if (y >= area.y && y < (area.y + area.height))
      return new Point2D.Double(x,y);

    y = (dy > 0) ? area.y + area.height : area.y;
    return new Point2D.Double(centerX + dx* (y - centerY) / dy, y);
  }

  public Point2D intersect (Line2D line) {
    if (area.contains(line.getP1())) {
      return intersect(line.getP1(), line.getP2());
    }
    return intersect(line.getP2(), line.getP1());
  }

  public Point2D intersect(Point2D in, Point2D out) {
    return pointOnRim(out.getX() - in.getX(), out.getY() - in.getY());
  }

}
