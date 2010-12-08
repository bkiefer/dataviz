package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import de.dfki.lt.loot.gui.Style;

/**
 * This is the TextNode class. A TextNode's content is just a String.
 *
 * @author Tassilo Barth
 * @author Pia Mennig
 * @author Antonia Scheidel
 */
public class TextNode extends BasicNode {

  // this is the String content of the TextNode
  private String text;
  // eventually nonzero offsets because of baseline
  private int xOffset, yOffset;

  /** The default constructor: Creates a new instance of <code>TextNode</code>
   * @param content
   *          The TextNode's content
   */
  public TextNode(String content) { this.text = content; }

  /** The default constructor: Creates a new instance of <code>TextNode</code>
   * @param content
   *          The TextNode's content
   */
  public TextNode(String content, Style aStyle) {
    super(aStyle);
    this.text = content;
  }

  // Begin Getters and Setters:
  /** @return the TextNode's content String */
  public String getText() { return this.text; }

  /** @param text
   *         The String we want to assign to this TextNode's <code>text</code>
   */
  public void setText(String content) { this.text = content; }

  /** draws a String (formatted by g) from text and origin
   * @param g the graphics context
   * @param inner the inner rectangle (padding already treated correctly)
   */
  @Override
  protected void paintAbsolute(Rectangle inner, Graphics g, boolean inverted) {
    Graphics2D g2d = (Graphics2D)g;
    int offset = this.getStyle().getPadding().getOffset();

    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        //RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
        //RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        //RenderingHints.VALUE_TEXT_ANTIALIAS_GASP
        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB
    );
    /*
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
        //RenderingHints.VALUE_RENDER_SPEED
        RenderingHints.VALUE_RENDER_QUALITY
    );
    g2d.setRenderingHint(RenderingHints.KEY_DITHERING,
        //RenderingHints.VALUE_DITHER_DISABLE
        RenderingHints.VALUE_DITHER_ENABLE
    );
    */

    g.drawString(this.text,
        inner.x + this.xOffset + offset,
        inner.y + this.yOffset + offset);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.unisb.loot.gui.BasicNode#adjustSize(java.awt.Graphics)
   */
  @Override
  public void adjustSize(Graphics g) {
    assert (this.style.getFont() != null);
    Rectangle charBounds =
        this.style.getFont().getStringBounds(this.text,
            ((Graphics2D) g).getFontRenderContext()).getBounds();

    // adjust offsets
    this.xOffset = charBounds.x ;
    this.yOffset = -charBounds.y ;

    // add offset to Node's size
    this.area.width = charBounds.width;
    this.area.height = charBounds.height;
    // treat padding parameters like in all other basic nodes
    super.adjustSize(g);
  }

}
