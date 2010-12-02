package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Rectangle;

import de.dfki.lt.loot.gui.Style;

/** Class for curly brackets: '{' '}'
 */
public class BraceBracketNode  extends BracketNode  {

  public BraceBracketNode(Orientation anOrientation) {
    super(anOrientation, Style.get("bracket"));
  }

  public BraceBracketNode(Orientation anOrientation, Style aStyle) {
    super(anOrientation, aStyle);
  }

  /** paint the bracket. r is in absolute coordinates, Padding already removed
   */
  @Override
  public void paintAbsolute(Rectangle r, Graphics g){
    // The corresponding corner point ('R'ight 'L'ower)
    int rlx = r.x + r.width;
    int rly = r.y + r.height;

    int h_radius = r.width/2;
    int h_mid_x = r.x + h_radius;
    int h_mid_y = r.y + r.height / 2;

    int v_radius = r.height/2;
    int v_mid_x = r.x + r.width / 2;
    int v_mid_y = r.y + v_radius;

    // draw the bracket according to the OrientationType provided
    switch (this.getOrientation()) {
    case west:
      if (false && r.height <= this.fontHeight + 2) {
        g.drawString("{", r.x, rly);
      } else {
        g.drawArc(h_mid_x, r.y, r.width, r.width, 90, 90);
        g.drawLine(h_mid_x, r.y + h_radius, h_mid_x, h_mid_y - h_radius);
        g.drawArc(r.x - h_radius -1, h_mid_y - r.width, r.width, r.width,0,-90);
        g.drawArc(r.x - h_radius -1, h_mid_y , r.width, r.width, 90, -90);
        g.drawLine(h_mid_x, h_mid_y + h_radius, h_mid_x, rly - h_radius);
        g.drawArc(h_mid_x, rly - r.width, r.width, r.width, 180, 90);
      }
      break;
    case east:
      if (false && r.height <= this.fontHeight + 2) {
        g.drawString("}", r.x, rly );
      } else {
        /*
        int bc = 16;
        int y = r.y - this.fontOffset;
        int x = r.x;
        g.drawString(BRACKET_CHARS.substring(bc,bc+1), x , y );
        int yLast = r.y + r.height - this.fontHeight - this.fontOffset;
        g.drawString(BRACKET_CHARS.substring(bc+2,bc+3), x , yLast);
        int yMiddle = (y + yLast) / 2;
        g.drawString(BRACKET_CHARS.substring(bc+1,bc+2), x , yMiddle);
        yLast -= this.fontHeight;
        while (y < yMiddle - this.fontHeight) {
          y += this.fontHeight;
          g.drawString(BRACKET_CHARS.substring(bc-1, bc), x, y);
        }
        y = yMiddle;
        while (y < yLast) {
          y += this.fontHeight;
          g.drawString(BRACKET_CHARS.substring(bc-1, bc), x, y);
        }
        */
        g.drawArc(r.x - h_radius -1, r.y, r.width, r.width, 0, 90);
        g.drawLine(h_mid_x, r.y + h_radius, h_mid_x, h_mid_y - h_radius);
        g.drawArc(h_mid_x, h_mid_y - r.width, r.width, r.width,180,90);
        g.drawArc(h_mid_x, h_mid_y , r.width, r.width, 180, -90);
        g.drawLine(h_mid_x, h_mid_y + h_radius, h_mid_x, rly - h_radius);
        g.drawArc(r.x - h_radius -1, rly - r.width, r.width, r.width, 270, 90);
      }
      break;
    case north:
      g.drawArc(r.x, v_mid_y, r.height, r.height, 90, 90);
      g.drawLine(r.x + v_radius, v_mid_y, v_mid_x - v_radius, v_mid_y);
      g.drawArc(v_mid_x - r.height, r.y -v_radius -1, r.height, r.height, 270, 90);
      g.drawArc(v_mid_x, r.y -v_radius -1, r.height, r.height, 180, 90);
      g.drawLine(v_mid_x + v_radius, v_mid_y, rlx - v_radius, v_mid_y);
      g.drawArc(rlx - r.height, rly - v_radius, r.height, r.height, 0, 90);
      break;
    case south:
      g.drawArc(r.x, v_mid_y - r.height, r.height, r.height, 180, 90);
      g.drawLine(r.x + v_radius, v_mid_y, v_mid_x - v_radius, v_mid_y);
      g.drawArc(v_mid_x - r.height, rly - v_radius, r.height, r.height, 0, 90);
      g.drawArc(v_mid_x, rly - v_radius, r.height, r.height, 90, 90);
      g.drawLine(v_mid_x + v_radius, v_mid_y, rlx - v_radius, v_mid_y);
      g.drawArc(rlx - r.height, r.y - v_radius -1, r.height, r.height, 270, 90);
      break;
    } //  end switch
  } // end method

} // end class