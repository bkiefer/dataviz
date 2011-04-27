package de.dfki.lt.loot.gui;

/*Kevin Boone, August 1999
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
class FontChooser
This program has only one class, which is a subclass of Applet as usual.
Note that we define the class to `implememts ItemListener'. This
specifies it as being able to receive events from the list component.
When the user clicks on an item in the list, this program receives
an ItemEvent event. This causes the operation itemStateChange to
be executed.
 */

@SuppressWarnings("serial")
public class FontChooser extends JFrame implements ItemListener {

  /** default sample text */
  private static final String DEF_SAMPLE =
    "The quick brown fox jumps over the lazy dog";

  /* the content of this string will appear as sample text at the top of the
   * font chooser, or DEF_SAMPLE, if this is null.
   */
  private String _sampleText;

  /**
   * The display consists of two parts: the list of fonts (fontList) and the
   * font display area (fontDisplayLabel). Since the display shows text, it is
   * implemented as a label.
   *
   * These objects have to be stored as attributes, as they are created in the
   * constructor but used in the operation itemStateChange.
   */
  private Label fontDisplayLabel;
  private List fontList;

  /** The current, resp. chosen font */
  private Font _font;


  public FontChooser() {
    this(null);
  }

  /** FontChooser constructor
   * @param sampleText the content of this string will appear as sample text
   *                   at the top of the font chooser
   */
  public FontChooser(String sampleText) {
    super("Select Font");

    _sampleText = sampleText;
    JPanel contentPane = new JPanel(new BorderLayout());
    contentPane.setLayout(new BorderLayout());
    this.setContentPane(contentPane);

    // A BorderLayout is probably the right thing to use here. We will put the
    // main
    // part of the display (the font list) in the middle, and the text at the
    // top
    //setLayout(new BorderLayout());

    // This very ugly line retrieves the list of font typefaces as an array of
    // Strings
    String listOfFonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getAvailableFontFamilyNames();

    // Create the font list object, and put the names of the typefaces in it
    this.fontList = new List();
    for (int i = 0; i < listOfFonts.length; i++)
      this.fontList.add(listOfFonts[i]);
    contentPane.add(this.fontList, BorderLayout.CENTER);

    // We now use addItemListener to specify that `this' (that is, this applet)
    // will handle events from the list
    this.fontList.addItemListener(this);

    // Now create the font display area. It starts of empty, as the user has not
    // selected a font yet
    this.fontDisplayLabel = new Label(DEF_SAMPLE);
    _font = this.fontDisplayLabel.getFont();
    contentPane.add(this.fontDisplayLabel, BorderLayout.NORTH);

    // display the frame
    this.pack();
    this.setVisible(true);
  }

  /**itemStateChanged This operation is called whenever the user clicks on a
   * font name (that is, any item in the font list
   */
  public void itemStateChanged(ItemEvent e) {
    // Only proceed if an item is being selected. This operation is also
    // called for the item that is being de-selected. We don't want
    // todo anything special about that
    if (e.getStateChange() == ItemEvent.SELECTED) {
      // Get the name of the typeface from the list
      String fontName = this.fontList.getSelectedItem();

      // Create a font with that name
      _font = new Font(fontName, Font.PLAIN, 14);

      // Set the font display label to the selected
      // font, and insert the name as its text
      this.fontDisplayLabel.setText(
          _sampleText == null ? DEF_SAMPLE : _sampleText);
      this.fontDisplayLabel.setFont(_font);
    }
  }

  /** Return the chosen font */
  public Font getChosenFont() {
    return _font;
  }
}
