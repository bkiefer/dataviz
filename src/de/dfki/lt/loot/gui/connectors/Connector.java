package de.dfki.lt.loot.gui.connectors;

import java.awt.Graphics;
import java.awt.Rectangle;

import de.dfki.lt.loot.gui.Style;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public abstract class Connector {
  
  protected GraphicalNode _fromNode, _toNode;
  
  protected Style _style;
  
  public Connector(GraphicalNode from, GraphicalNode to) {
    _fromNode = from;
    _toNode = to;
    _fromNode.addOutConnector(this);
    _toNode.addInConnector(this);
  }
  
  public Connector(GraphicalNode from, GraphicalNode to, Style style) {
    _fromNode = from;
    _toNode = to;
    _fromNode.addOutConnector(this);
    _toNode.addInConnector(this);
    _style = style;
  }
  
  /** paints the node with regard to the given offset
   * @param parentArea the parent's absolute area 
   * @param g Graphics object
   */
  public final void paint(Rectangle parentArea, Graphics g) {
    //Rectangle absoluteArea = new Rectangle(this.area);
    // translate (relative) area by origin to get absolute coordinates
    //absoluteArea.translate(parentArea.x, parentArea.y);
    // determine if the current node must be repainted
    //if (! absoluteArea.intersects(g.getClipBounds())) return;
    //showEnclosingRect(absoluteArea, g);
    if (this._style != null) this._style.setStyle(g);
    /*
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
    */
    paintAbsolute(parentArea, g);
  }

  /** really paint this connector, using absolute coordinates */
  public abstract void paintAbsolute(Rectangle absoluteArea, Graphics g);

  
  /** assume the _from and _to nodes have computed their appropriate size and
   *  position, now do necessary computations for yourself
   */
  public abstract void adjust(Graphics g);
  
  /** get the node from which this connector leaves */
  public GraphicalNode fromNode() {
    return _fromNode;
  }
  
  /** get the node from which this connector leaves */
  public GraphicalNode toNode() {
    return _toNode;
  }
  
  /** @return the style */
  public Style getStyle() { return this._style; }

  /** @param aStyle the style to set */
  public void setStyle(Style aStyle) { this._style = aStyle; }
  // END Getters and Setters


}
