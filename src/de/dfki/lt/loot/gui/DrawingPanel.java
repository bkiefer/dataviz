package de.dfki.lt.loot.gui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.layouts.Layout;
import de.dfki.lt.loot.gui.nodes.EmptyNode;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

/**
 * This is the <code>DrawingPanel</code>.
 *
 * @author Tassilo Barth
 * @author Pia Mennig
 * @author Antonia Scheidel
 */
@SuppressWarnings("serial")
public class DrawingPanel extends JPanel {

  // The root node of the structure
  private GraphicalNode _root = null;

  /** The object associated with this panel */
  private Object _model;

  /** The view context, a mapping from model nodes to GraphicalNodes */
  private ViewContext _context;

  /** A dynamic adapter to mediate between a model structure and the layout */
  private ModelAdapter _adapter;

  /** An object transforming the model into a graphical representation */
  private Layout _layout;

  /** a private field that indicates if a graphical representation has been
   *  generated for the last model that has been set.
   */
  private boolean _unadjusted;

  /** Mediates between user input and model */
  @SuppressWarnings("unused")
  private Controller _controller;

  /**
   * Create a new <code>DrawingPanel</code> with a specified layout.
   *
   * @param aLayout:
   *          mandatory!
   */
  public DrawingPanel(Layout aLayout, ModelAdapter adapter) {
    if (aLayout == null || adapter == null) {
      throw new IllegalArgumentException("Layout and Adapter must be given");
    }
    this._layout = aLayout;
    this._adapter = adapter;
    this._controller = new Controller(this);
    this._model = null;
    this._context = null;
  }

  /** set the top model node for this panel */
  public void setModel(Object aModel) {
    if (aModel != null) {
      this._model = aModel;
      _context = new ViewContext(aModel, _adapter);
      this._root = this._layout.computeView(this._model, _context);

      this.setSize(this._root.getRect().height, this._root.getRect().width);
    }
    else {
      this._root = new EmptyNode();
      this.setSize(0,0);
    }
    this._unadjusted = true;
    this.repaint();
  }

  public Object getModel() { return _model; }

  public GraphicalNode getMainView(Object model) {
    return (_context == null ? null : _context.getRepresentative(model));
  }

  public List<GraphicalNode> getOtherViews(Object model) {
    return (_context == null ? null : _context.equivalenceClass(model));
  }

  public GraphicalNode getRoot() {
    return _root;
  }

  /*
   * (non-Javadoc)
   *
   * @see javax.swing.JComponent#getPreferredSize()
   */
  @Override
  public Dimension getPreferredSize() {
    // if the root node's size has not yet been adjusted, return a 300x300
    // Dimension
    if (null == this._root || this._root.getRect().width == 0 ||
        this._root.getRect().height == 0) {
      return new Dimension(100, 100);
    }
    // else return the root node's new size
    return new Dimension(this._root.getRect().width, this._root.getRect().height);
  }

  public int getDefaultTextHeight() {
    Graphics g = this.getGraphics();
    assert(g != null);
    FontMetrics fm = g.getFontMetrics(Style.get(null).getFont());
    return fm.getHeight();
  }

  /*
   * (non-Javadoc)
   *
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  public void paintComponent(Graphics g) {
    // call super()
    super.paintComponent(g);

    // create a copy of g as we need to make some changes to the Graphics
    // object
    // but need to leave the original one untouched
    Graphics guiG = g.create();
    if (this._root != null) {
      if (this._unadjusted) {
        this._root.adjustSize(guiG);
        this._unadjusted = false;
      }
      this._root.paint(this._root.getRect(), guiG);
    }
  }
}
