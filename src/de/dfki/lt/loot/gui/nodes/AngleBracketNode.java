package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Rectangle;

import de.dfki.lt.loot.gui.Style;

/** Class for angle brackets: '<' '>'
 */
public class AngleBracketNode  extends BracketNode  {

  public AngleBracketNode(Orientation anOrientation) {
    super(anOrientation, Style.get("bracket"));
  }

  public AngleBracketNode(Orientation anOrientation, Style aStyle) {
    super(anOrientation, aStyle);
  }

  /** paint the bracket. r is in absolute coordinates, Padding already removed
   */
  @Override
  public void paintAbsolute(Rectangle r, Graphics g){
    // The rectangle's height and width with added origin offset, the lower
    // right corner
    int lrx = r.width + r.x;
    int lry = r.height + r.y;
    int mrx = r.x + r.width / 2;
    int mry = r.y + r.height / 2;
    if (r.height <= this.fontHeight + 2) {
      Orientation ori = this.getOrientation();
      if (ori == Orientation.west) {
        g.drawString("\u27E8", r.x, lry);
        return;
      }
      if (ori == Orientation.east) {
        g.drawString("\u27E9", r.x, lry);
        return;
      }
    }
    // draw the bracket according to the OrientationType provided
    switch (this.getOrientation()) {
    case west:
      g.drawLine(lrx, r.y, r.x, mry);
      g.drawLine(r.x, mry, lrx, lry);
      break;
    case east:
      g.drawLine(r.x, r.y, lrx, mry);
      g.drawLine(lrx, mry, r.x, lry);
      break;
    case north:
      g.drawLine(r.x, lry, mrx, r.y);
      g.drawLine(mrx, r.y, lrx, lry);
      break;
    case south:
      g.drawLine(r.x, r.y, mrx, lry);
      g.drawLine(mrx, lry, lrx, r.y);
      break;
    } // end switch
  }

  @Override
  public void growTo(int width, int height) {
    if (height > width) {
      this.area.height = Math.min(height, 300);
      this.area.width = Math.max(width, (this.area.height/13));
    } else {
      this.area.width = Math.min(width, 300);
      this.area.height= Math.max(height, (this.area.width/13));
    }
  }
}