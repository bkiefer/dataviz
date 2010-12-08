package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import de.dfki.lt.loot.gui.Style;

/**
 *
 * This is the abstract <code>BracketNode</code> class. It contains some
 * implementation details for <code>BracketNodes</code>. To obtain concrete
 * implementations of the four kinds of <code>BracketNodes</code>, use the
 * <code>BracketNodeFactory</code>.
 *
 * @author Tassilo Barth
 * @author Pia Mennig
 * @author Antonia Scheidel
 */
public abstract class BracketNode extends BasicNode {
  protected static final String BRACKET_CHARS =
    "\u239B\u239C\u239D\u239E\u239F\u23A0\u23A1\u23A2\u23A3\u23A4\u23A5\u23A6" +
    "\u23A7\u23A8\u23A9\u23AA\u23AB\u23AC\u23AD";

  protected int fontHeight = -1, fontWidth, fontOffset;

  // these are the fields, see enumerations for values.
  protected Orientation orientation;

  // In which direction does the bracket point? < points west, } points east
  public enum Orientation {
    north, south, east, west
  }

  /**
   * Default constructor: Create a new <code>BracketNode</code> and set its
   * orientation and style
   *
   * @param anOrientation
   * @param aStyle
   */
  protected BracketNode(Orientation anOrientation, Style aStyle) {
    super(aStyle);
    this.orientation = anOrientation;
  }

  // BEGIN Getters and Setters
  /** @return the bracket's orientation */
  public Orientation getOrientation() {
    return this.orientation;
  }

  /** set the bracket's orientation */
  public void setOrientation(Orientation anOrientation) {
    this.orientation = anOrientation;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.unisb.loot.gui.GraphicalNode#adjustSize(java.awt.Graphics)
   */
  @Override
  public void adjustSize(Graphics g) {

    if (this.fontHeight == -1) {
      Rectangle charBounds =
        this.style.getFont().getStringBounds(BRACKET_CHARS.substring(1,2),
            ((Graphics2D) g).getFontRenderContext()).getBounds();
      this.fontHeight = charBounds.height;
      this.fontWidth = charBounds.width;
      this.fontOffset = charBounds.y;
    }

    if (this.orientation == Orientation.west ||
        this.orientation == Orientation.east) {
      this.area.height = 0;
      this.area.width = fontWidth;
    } else {
      this.area.width = 0;
      this.area.height = fontWidth;
    }
    // adjust padding parameters
    super.adjustSize(g);
  } // end adjustSize

  @Override
  public void growTo(int width, int height) {
    int offset = style.getPadding().getOffset();
    offset += offset;
    width -= offset;
    height -= offset;
    if (this.getOrientation() == Orientation.west ||
        this.getOrientation() == Orientation.east) {
      this.area.height = height;
      this.area.width = (int) Math.max(width/2,
          Math.min(width, Math.round(height / 4.0)));
    } else {
      this.area.width = width;
      this.area.height= (int) Math.max(height/2,
          Math.min(height, Math.round(width / 4.0)));
    }
  }

  public void growToOrig(int width, int height) {
    if (this.orientation == Orientation.west ||
        this.orientation == Orientation.east) {
      this.area.width = Math.min(width, height);
      this.area.height = height;
    } else {
      this.area.width = width;
      this.area.height = Math.min(width, height);
    }
  }

} // end BracketNode