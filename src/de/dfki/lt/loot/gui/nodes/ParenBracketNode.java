package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.EnumMap;

import de.dfki.lt.loot.gui.Style;

/** Class for parenthesis (round) brackets: '(' ')'
 */
public class ParenBracketNode extends BracketNode {

  private static EnumMap<Orientation, Integer> from, to;
  {
    from = new EnumMap<Orientation, Integer>(Orientation.class);
    to = new EnumMap<Orientation, Integer>(Orientation.class);
    from.put(Orientation.east, 90); to.put(Orientation.east, -180);
    from.put(Orientation.west, 90); to.put(Orientation.west, +180);
    from.put(Orientation.north, 0); to.put(Orientation.north, +180);
    from.put(Orientation.south, 0); to.put(Orientation.south, -180);
  }

  public ParenBracketNode(Orientation anOrientation) {
    super(anOrientation, Style.get("bracket"));
  }

  public ParenBracketNode(Orientation anOrientation, Style aStyle) {
    super(anOrientation, aStyle);
  }

  /** paint the bracket. r is in absolute coordinates, Padding already removed
   */
  @Override
  public void paintAbsolute(Rectangle r, Graphics g){
    // draw the bracket according to the OrientationType provided
    Orientation ori = this.getOrientation();
    if (false && r.height <= this.fontHeight + 2) {
      if (ori == Orientation.west) {
        g.drawString("(", r.x, r.y + r.height);
        return;
      }
      if (ori == Orientation.east) {
        g.drawString(")", r.x, r.y + r.height);
        return;
      }
    }
    g.drawArc(r.x, r.y, r.width, r.height, from.get(ori), to.get(ori));
  } // end method

  @Override
  public void growTo(int width, int height) {
    super.growTo(width, height);
    if (this.getOrientation() == Orientation.west ||
        this.getOrientation() == Orientation.east) {
      this.area.height = Math.min(this.area.height, 300);
    } else {
      this.area.width = Math.min(this.area.width, 300);
    }
  }
} // end class