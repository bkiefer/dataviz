/*
 * 
 */
package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Rectangle;

import de.dfki.lt.loot.gui.Style;

// TODO: Auto-generated Javadoc
/**
 * Class for square brackets: '[' ']'.
 */
public class SquareBracketNode extends BracketNode {

  /** The Constant useCharacters. */
  private static final boolean useCharacters = false;

  /**
   * Instantiates a new square bracket node.
   * 
   * @param anOrientation the an orientation
   * @param aStyle the a style
   */
  public SquareBracketNode(Orientation anOrientation, Style aStyle) {
    super(anOrientation, aStyle);
  }

  /**
   * paint the bracket. r is in absolute coordinates, Padding already removed
   * 
   * @param r the r
   * @param g the g
   */
  @Override
  public void paintAbsolute(Rectangle r, Graphics g){
    int lry = r.height + r.y;
    int lrx = r.width + r.x;
    // draw the bracket according to the OrientationType provided
    switch (this.getOrientation()) {
    case east:
    case west:
      if (! useCharacters) {
        g.drawLine(r.x, r.y, lrx, r.y);
        g.drawLine(r.x, lry, lrx, lry);
        if (this.getOrientation() == Orientation.west) lrx = r.x;
        g.drawLine(lrx, r.y, lrx, lry);
      } else {
        // This would be the preferred way of painting Brackets and Braces,
        // but the fonts are rendered strangely, and support seems incomplete?
        int bc = (this.getOrientation() == Orientation.west) ? 0 : 3;
        if (r.height <= this.fontHeight ) {
          g.drawString(bc == 0 ? "[" : "]", r.x, r.y - this.fontOffset);
        } else {
          int y = r.y - this.fontOffset;
          int x = r.x;
          g.drawString(BracketChars.substring(bc,bc+1), x , y );
          int yLast = r.y + r.height - this.fontHeight - this.fontOffset;
          g.drawString(BracketChars.substring(bc+2,bc+3), x , yLast);
          yLast -= this.fontHeight;
          while (y < yLast) {
            y += this.fontHeight;
            g.drawString(BracketChars.substring(bc+1,bc+2), x, y);
          }
        }
      }
      break;
    case north:
    case south:
      g.drawLine(r.x, lry, r.x, r.y);
      g.drawLine(lrx, r.y, lrx, lry);
      if (this.getOrientation() == Orientation.north) lry = r.y;
      g.drawLine(r.x, lry, lrx, lry);
      break;
    } // end switch
  } // end method

} // end class