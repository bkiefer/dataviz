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
  protected static String BracketChars="⎡⎢⎣⎤⎥⎦⎧⎨⎩⎪⎫⎬⎭⎮⎰⎱⎝⎜⎞⎟⎠";
  
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

  /** Calculate height from width. */
  private void calcHeight() {
    this.area.height = //Math.max(7, this.area.width / 15);
      this.fontHeight;
  }

  /** Calculate width from height. */
  private void calcWidth() {
    this.area.width = //Math.max(7, this.area.height / 15);
      this.fontWidth;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.unisb.loot.gui.GraphicalNode#adjustSize(java.awt.Graphics)
   */
  public void adjustSize(Graphics g) {

    if (this.fontHeight == -1) {
      Rectangle charBounds =
        this.style.getFont().getStringBounds(BracketChars.substring(1,2),
            ((Graphics2D) g).getFontRenderContext()).getBounds();
      this.fontHeight = charBounds.height;
      this.fontWidth = charBounds.width;
      this.fontOffset = charBounds.y;
    }
    
    if (this.orientation == Orientation.west ||
        this.orientation == Orientation.east) {
      this.area.height = 0;
      calcWidth();
    } else {
      this.area.width = 0;
      calcHeight();      
    }
    // adjust padding parameters
    super.adjustSize(g);
  } // end adjustSize
  
  public void growTo(int width, int height) {
    this.area.width = width;
    this.area.height = height;
  }

} // end BracketNode