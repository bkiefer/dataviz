package de.dfki.lt.loot.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.HashMap;

/**
 * This is the <code>Style</code> class. There are four different Styles:
 * 'type', 'feature' and 'bracket' and the default style.
 * 
 */
public class Style {

  private Font font;
  private Color fgCol;
  private Color bgCol;
  private Padding padding;
  private Stroke stroke;

  private static HashMap<String, Style> styleMap;
  private static Style defaultStyle = new Style();

  // This is the style map. Contains style information about the three styles.
  static {
    styleMap = new HashMap<String, Style>();
    styleMap.put("type", new Style(new Font("DejaVu Sans", Font.BOLD, 11),
        Color.BLUE, null, new Padding(0, 0, 1), null));
    styleMap.put("fwfailure", new Style(new Font("DejaVu Sans", Font.BOLD, 14),
        Color.RED, null, new Padding(0, 0, 1), null));
    styleMap.put("bwfailure", new Style(new Font("DejaVu Sans", Font.BOLD, 14),
        Color.GREEN, null, new Padding(0, 0, 1), null));
    styleMap.put("feature", new Style(new Font("DejaVu Sans", Font.PLAIN, 11),
        null, null, new Padding(0, 0, 1), null));
    styleMap.put("bracket", new Style(new Font("DejaVu Sans", Font.PLAIN, 14),
        null, null, new Padding(0, 0, 2), null));
    styleMap.put("coref", new Style(new Font("DejaVu Sans", Font.BOLD, 10),
        null, null, new Padding(2, 1, 1), null));
    styleMap.put("leantype", new Style(new Font("DejaVu Sans", Font.PLAIN, 10),
        null, null, new Padding(0, 0, 1), null));
    styleMap.put("subscript", new Style(new Font("DejaVu Sans", Font.BOLD, 8),
        Color.RED, null, new Padding(0, 0, 0), null));
    styleMap.put("symbol", new Style(new Font("OpenSymbol", Font.PLAIN, 22),
        null, null, new Padding(0, 0, 0), null));
  }

  /**
   * Must not be called from the outside, thus private. Is called once to create
   * the default <code>Style</code> object.
   */
  private Style() {
    this.font = new Font("Monospaced", Font.PLAIN, 10);
    this.fgCol = Color.BLACK;
    this.bgCol = Color.WHITE;
    this.padding = new Padding();
    this.stroke = new BasicStroke();
  }

  /**
   * There are only ever going to be three non-default <code>Style</code>
   * Objects: They are created by using the styleMap, so this constructor is
   * private as well.
   * 
   * @param aFont
   * @param foreground
   * @param background
   * @param aPadding
   * @param aLineWidth
   * @param aStroke
   */
  private Style(Font aFont, Color foreground, Color background,
      Padding aPadding, Stroke aStroke) {
    this.font =
        (null == aFont ? new Font("Monospaced", Font.PLAIN, 12) : aFont);
    this.fgCol = (null == foreground ? Color.BLACK : foreground);
    this.bgCol = (null == background ? Color.WHITE : background);
    this.padding = (aPadding == null ? new Padding() : aPadding);
    this.stroke = (null == aStroke ? new BasicStroke() : aStroke);
  }

  public static void add(String name,
      Font aFont, Color foreground, Color background,
      Padding aPadding, Stroke aStroke) {
    styleMap.put(name, 
        new Style(aFont, foreground, background, aPadding, aStroke));
  }
  /**
   * Looks up the given style name in the styleMap and returns the appropriate
   * <code>Style</code> object
   * 
   * @param name
   * @return the desired <code>Style</code> object
   */
  public static Style get(String name) {
    if (null == name || 0 == name.length()) {
      return defaultStyle;
    }
    Style ret = Style.styleMap.get(name);
    if (null == ret) {
      ret = defaultStyle;
    }
    return ret;
  }

  // Getters and Setters:
  /** @return the font */
  public Font getFont() { return this.font; }

  /** @param aFont the font to set */
  public void setFont(Font aFont) { this.font = aFont; }

  /** @return the foregroundColour */
  public Color getForegroundColour() { return this.fgCol; }

  /** @param aForegroundColour the foregroundColour to set */
  public void setForegroundColour(Color aColor) { this.fgCol = aColor; }

  /** @return the backgroundColour */
  public Color getBackgroundColour() { return this.bgCol; }

  /** @param aBackgroundColour the backgroundColour to set */
  public void setBackgroundColour(Color aColor) { this.bgCol = aColor; }

  /** @return the padding */
  public Padding getPadding() { return this.padding; }

  /** @param aPadding the padding to set */
  public void setPadding(Padding aPadding) { this.padding = aPadding; }

  /** @return the stroke */
  public Stroke getStroke() { return this.stroke; }

  /** @param aStroke the stroke to set */
  public void setStroke(Stroke aStroke) { this.stroke = aStroke; }
  // END Getters and Setters
  
  /** Adjust Graphics properties according to style.
   * @param  g the current graphics context
   * @return the incoming object. Might also be a copy if further changes made
   *         that necessary
   */
  public Graphics setStyle(Graphics g) {

    // set properties of our Graphic object according to style
    if (null != this.getFont()) { g.setFont(this.getFont()); }

    // override default foreground color if given in style object
    if (null != this.getForegroundColour()) {
      g.setColor(this.getForegroundColour());
    } else {
      // default foreground color is black
      g.setColor(Color.black);
    }

    // override stroke if given in style object
    if (null != this.getStroke()) {
      ((Graphics2D) g).setStroke(this.getStroke());
    }

    // return changed Graphics object
    return g;
  }

}
