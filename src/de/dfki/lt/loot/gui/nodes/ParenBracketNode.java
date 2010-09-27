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
    g.drawArc(r.x, r.y, r.width, r.height, from.get(ori), to.get(ori));
  } // end method

} // end class