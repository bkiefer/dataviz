package de.dfki.lt.loot.gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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
    styleMap.put("text", new Style(new Font("DejaVu Sans", Font.PLAIN, 11),
        null, null, null, null));
    styleMap.put("type", new Style(new Font("DejaVu Sans", Font.BOLD, 11),
        Color.BLUE, Color.LIGHT_GRAY, new Padding(0, 0, 1), null));
    styleMap.put("fwfailure", new Style(new Font("DejaVu Sans", Font.BOLD, 14),
        Color.RED, Color.LIGHT_GRAY, new Padding(0, 0, 1), null));
    styleMap.put("bwfailure", new Style(new Font("DejaVu Sans", Font.BOLD, 14),
        Color.GREEN, Color.LIGHT_GRAY, new Padding(0, 0, 1), null));
    styleMap.put("feature", new Style(new Font("DejaVu Sans", Font.PLAIN, 11),
        null, null, new Padding(0, 0, 1), null));
    styleMap.put("bracket", new Style(new Font("DejaVu Sans Mono", Font.PLAIN, 14),
        null, null, new Padding(0, 0, 1), null));
    styleMap.put("squarebracket", new Style(new Font("DejaVu Sans Mono", Font.PLAIN, 10),
        null, null, new Padding(0, 0, 1), null));
    styleMap.put("coref", new Style(new Font("DejaVu Sans", Font.BOLD, 10),
        null, null, new Padding(2, 1, 1), null));
    styleMap.put("leantype", new Style(new Font("DejaVu Sans", Font.PLAIN, 10),
        null, null, new Padding(0, 0, 1), null));
    styleMap.put("subscript", new Style(new Font("DejaVu Sans", Font.BOLD, 8),
        Color.RED, null, new Padding(0, 0, 0), null));
    styleMap.put("symbol", new Style(new Font("OpenSymbol", Font.PLAIN, 22),
        null, null, new Padding(0, 0, 0), null));
  }


  private Color complementaryColor(Color color) {
    return new Color(255 - color.getRed(), 255 - color.getGreen(),
        255 - color.getBlue(), color.getAlpha());
  }

  /**
   * Must not be called from the outside, thus private. Is called once to create
   * the default <code>Style</code> object.
   */
  private Style() {
    this.font = new Font("Monospaced", Font.PLAIN, 10);
    this.fgCol = Color.BLACK;
    this.bgCol = complementaryColor(this.fgCol);
    this.padding = new Padding(0,0,0);
    this.stroke = new BasicStroke();
    /*
    this.padding = new Padding(2,2,2);
    float[] defaultDash = { 10f, 10f };
    this.stroke = new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke
        .JOIN_BEVEL, 100.0f, defaultDash, 0);
    */
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
    /*
    this.padding.border = 1;
    this.padding.margin = 4;
    this.padding.padding = 1;
    */
    this.stroke = (null == aStroke ? new BasicStroke() : aStroke);
  }

  public Style copy() {
    return new Style(font, fgCol, bgCol, padding, stroke);
  }

  public static void add(String name,
      Font aFont, Color foreground, Color background,
      Padding aPadding, Stroke aStroke) {
    Style style = new Style(aFont, foreground, background, aPadding, aStroke);
    if (name == null || 0 == name.length()) {
      defaultStyle = style;
    } else {
      styleMap.put(name, style);
    }
  }

  public static boolean contains(String name) {
    return (null != name && 0 != name.length()
        && Style.styleMap.get(name) != null);
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
  public Color getForegroundColour() {
    return (this.fgCol != null ? this.fgCol : Color.BLACK);
  }

  /** @param aForegroundColour the foregroundColour to set */
  public void setForegroundColour(Color aColor) { this.fgCol = aColor; }

  /** @return the backgroundColour */
  public Color getBackgroundColour() { return this.bgCol; }

  public Color invertedColor() {
    return complementaryColor(this.getForegroundColour());
  }

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
  public Graphics setStyle(Graphics gPlain, boolean inverted) {
    Graphics2D g = (Graphics2D) gPlain;
    // set properties of our Graphic object according to style
    if (null != this.getFont()) { g.setFont(this.getFont()); }

    // override default foreground color if given in style object
    g.setPaint(inverted
        ? complementaryColor(this.getForegroundColour())
        : this.getForegroundColour());

    // override stroke if given in style object
    if (null != this.getStroke()) {
      g.setStroke(this.getStroke());
    }

    // return changed Graphics object
    return g;
  }

  public static void increaseDefaultFontSize(int points) {
    for (Style entry : styleMap.values()) {
      Font f = entry.font;
      entry.font = new Font(f.getName(), f.getStyle(),
          (int)(f.getSize() + points));
    }
  }

  public static void increaseDefaultFontSize(double factor) {
    for (Style entry : styleMap.values()) {
      Font f = entry.font;
      entry.font = new Font(f.getName(), f.getStyle(),
          (int)(f.getSize() * factor));
    }
  }

}
