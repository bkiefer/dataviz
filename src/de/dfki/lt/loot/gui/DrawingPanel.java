package de.dfki.lt.loot.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
public class DrawingPanel extends JPanel
implements MouseMotionListener, java.awt.event.MouseListener {

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

  /** The scroll pane we have been wrapped into, if any */
  private JScrollPane _myScrollPane = null;

  /** a private field that indicates if a graphical representation has been
   *  generated for the last model that has been set.
   */
  private boolean _unadjusted;

  /** The list of listeners that are notified of events when entering or leaving
   *  a node
   */
  private List<MouseListener> _nodeListeners;

  /** The node that was at the mouse pointer position in the last event */
  private GraphicalNode _underMouse;

  /** Create a new <code>DrawingPanel</code> with a specified layout.
   * @param aLayout: the object that converts the model into a view
   */
  public DrawingPanel(Layout aLayout, ModelAdapter adapter) {
    if (aLayout == null || adapter == null) {
      throw new IllegalArgumentException("Layout and Adapter must be given");
    }
    _layout = aLayout;
    _adapter = adapter;
    _model = null;
    _context = null;

    _underMouse = null;
    _nodeListeners = new ArrayList<MouseListener>(3);
    setBackground(Color.white);
  }

  public JScrollPane wrapScrollable() {
    _myScrollPane = new JScrollPane(this);
    return _myScrollPane;
  }

  /** set the top model node for this panel */
  public void setModel(Object aModel, ModelAdapter adapt) {
    if (aModel != null) {
      _adapter = adapt;
      _model = aModel;
      _context = new ViewContext(aModel, _adapter);
      _root = _layout.computeView(_model, _context);

      setSize(_root.getRect().height, _root.getRect().width);
    }
    else {
      _root = new EmptyNode();
      setSize(0,0);
    }
    _unadjusted = true;
    repaint();
  }

  /** set the top model node for this panel */
  public void setModel(Object aModel) {
    setModel(aModel, _adapter);
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


  /* @see javax.swing.JComponent#getPreferredSize()
   */
  @Override
  public Dimension getPreferredSize() {
    // if the root node's size has not yet been adjusted, return a 300x300
    // Dimension
    if (null == _root || _root.getRect().width == 0 ||
        _root.getRect().height == 0) {
      return new Dimension(100, 100);
    }
    // else return the root node's new size
    return new Dimension(_root.getRect().width, _root.getRect().height);
  }

  public int getDefaultTextHeight() {
    Graphics g = getGraphics();
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
    if (_root != null) {
      if (_unadjusted) {
        if (_myScrollPane != null) {
          int unitIncrement = getDefaultTextHeight();
          _myScrollPane.getHorizontalScrollBar().setUnitIncrement(unitIncrement);
          _myScrollPane.getVerticalScrollBar().setUnitIncrement(unitIncrement);
        }
        _root.adjustSize(guiG);
        _unadjusted = false;
      }
      _root.paint(_root.getRect(), guiG, false);
    }
  }

  /* **********************************************************************
   * Custom Listener providing implementation
   * *********************************************************************/

  public void addListener(MouseListener l) {
    if (_nodeListeners.isEmpty()) {
      addMouseMotionListener(this);
      addMouseListener(this);
    }
    _nodeListeners.add(l);
  }

  public void removeListener(MouseListener l) {
    _nodeListeners.remove(l);
    if (_nodeListeners.isEmpty()) {
      removeMouseMotionListener(this);
      removeMouseListener(this);
    }
  }

  private void fireMouseEnters(MouseEvent e, GraphicalNode node) {
    for (MouseListener l : _nodeListeners) {
      l.mouseEnters(e, node);
    }
  }

  private void fireMouseLeaves(MouseEvent e, GraphicalNode node) {
    for (MouseListener l : _nodeListeners) {
      l.mouseLeaves(e, node);
    }
  }

  private void fireMouseClicked(MouseEvent e, GraphicalNode node) {
    for (MouseListener l : _nodeListeners) {
      l.mouseClicked(e, node);
    }
  }


  /* **********************************************************************
   * MouseMotionListener implementation
   * *********************************************************************/

  /** A preliminary implementation that has most of the needed functionality
   *  and good performance could easily be extended in a useful way.
   */
  public void mouseMoved(MouseEvent e) {
    Component originator = e.getComponent();
    assert(originator instanceof DrawingPanel);
    DrawingPanel view = (DrawingPanel) e.getComponent();
    assert(originator == view);
    Point p = e.getPoint();

    if (view.getVisibleRect().contains(p) && view.getRoot() != null) {
      GraphicalNode nowPointsTo =
        ((_underMouse == null) ? view.getRoot() : _underMouse)
        .getDeepestIncludingPoint(p);

      if (nowPointsTo != _underMouse) {
        if (_underMouse != null) {
          fireMouseLeaves(e, _underMouse);
        }
        if (nowPointsTo != null) {
          fireMouseEnters(e, nowPointsTo);
        }
        _underMouse = nowPointsTo;
      }
    }
  }

  /** We don't currently support dragging events */
  public void mouseDragged(MouseEvent e) { }

  /* **********************************************************************
   * MouseListener implementation
   * *********************************************************************/

  @Override
  public void mouseClicked(MouseEvent e) {
    Component originator = e.getComponent();
    assert(originator instanceof DrawingPanel);
    DrawingPanel view = (DrawingPanel) e.getComponent();
    assert(originator == view);
    Point p = e.getPoint();
    if (view.getVisibleRect().contains(p) && view.getRoot() != null) {
      // the _underMouse optimization is likely to get no improvement since
      // clicks are rarely very near to each other
      GraphicalNode nowPointsTo = view.getRoot().getDeepestIncludingPoint(p);
      if (nowPointsTo != null) {
        fireMouseClicked(e, nowPointsTo);
      }
    }
  }

  /** currently not used */
  @Override
  public void mouseReleased(MouseEvent e) { }

  /** currently not used */
  @Override
  public void mouseEntered(MouseEvent e) { }

  /** currently not used */
  @Override
  public void mouseExited(MouseEvent e) { }

  /** currently not used */
  @Override
  public void mousePressed(MouseEvent e) { }
}
