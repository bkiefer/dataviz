/*
 * 
 */
package de.dfki.lt.loot.gui.nodes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

// TODO: Auto-generated Javadoc
/**
 * This is the <code>CompositeNode</code> class. <code>CompositeNodes</code>
 * are <code>GraphicalNodes</code> with a number of subNodes (which are
 * <code>GraphicalNodes</code>, as well).
 *
 * @author Tassilo Barth
 * @author Pia Mennig
 * @author Antonia Scheidel
 */
public class TabularNode extends GraphicalNode {

  // This node's table of subNodes, the rows are the inner Lists, i.e.,
  // iterating over rows means iterating over nodeTable
  /** The node table. */
  private ArrayList<GraphicalNode> nodeTable;

  /** the list of row format specifications, must be same size as nodeTable valid chars are W   n   N \ | / w - c - e / | \ S   s   E north, northEast, east, southEast, south, Southwest, West and center, respectively length of strings must confirm to length of row. */
  private List<String> specs;
  
  /** The default spec. */
  private String defaultSpec;

  /** determines which dimension is fixed in the beginning, if this is true, then the initial spec determines row count, else column count. */
  private boolean rowOriented;
  
  /** The col count. */
  private int rowCount, colCount;

  /**
   * Gets the node.
   * 
   * @param row the row
   * @param col the col
   * @return the node
   */
  private GraphicalNode getNode(int row, int col) {
    return nodeTable.get(
        rowOriented ?
        row * colCount + col :
        col * rowCount + row);
  }

  /**
   * Gets the spec.
   * 
   * @param row the row
   * @param col the col
   * @return the spec
   */
  private char getSpec(int row, int col) {
    return (rowOriented ?
        specs.get(row).charAt(col) : specs.get(col).charAt(row));
  }

  /**
   * Instantiates a new tabular node.
   * 
   * @param rows the rows
   * @param defSpec the def spec
   * @param size the size
   */
  public TabularNode(boolean rows, String defSpec, int size) {
    defaultSpec = defSpec;
    nodeTable = new ArrayList<GraphicalNode>(defaultSpec.length() * size);
    rowOriented = rows;
    if (rowOriented) {
      colCount = this.defaultSpec.length();
      rowCount = 0;
    } else {
      colCount = 0;
      rowCount = this.defaultSpec.length();
    }
    this.specs = new ArrayList<String>(size);
  }

  /**
   * Instantiates a new tabular node.
   * 
   * @param rows the rows
   * @param defSpec the def spec
   */
  public TabularNode(boolean rows, String defSpec) {
    this(rows, defSpec, 5);
  }

  /**
   * Add new row, with given row spec.
   * 
   * @param spec the spec
   */
  public void startNext(String spec) {
    if (this.defaultSpec.length() != spec.length())
      throw new IllegalArgumentException();
    // fill current row
    while (nodeTable.size() < rowCount * colCount) nodeTable.add(null);
    if (rowOriented)
      ++rowCount;
    else
      ++colCount;
    this.specs.add(spec);
  }

  /**
   * Add new row, with default row spec.
   */
  public void startNext() {
    startNext(this.defaultSpec);
  }

  /**
   * Adds another <code>GraphicalNode</code> to the current row of this
   * <code>TabularNode</code>. subNode may be null to get empty cells.
   * 
   * @param subNode the sub node
   */
  @Override
  public void addNode(GraphicalNode subNode) {
    // check that the current row/column is not exceeded
    if (nodeTable.size() == rowCount * colCount)
      throw new IllegalArgumentException();

    nodeTable.add(subNode);
    // set the new subnode's parentNode
    if (subNode != null) subNode.setParentNode(this);
  }

  /**
   * Remove the given subNode from my sub-nodes list.
   * 
   * @param subNode the sub node
   */
  @Override
  public void removeNode(GraphicalNode subNode) {
    this.nodeTable.remove(subNode);
    subNode.setParentNode(null);
  }

  /**
   * Replace node old by newNode.
   * 
   * @param old the old
   * @param newNode the new node
   */
  @Override
  public void exchangeNode(GraphicalNode old, GraphicalNode newNode) {
    ListIterator<GraphicalNode> li = nodeTable.listIterator();
    while (li.hasNext()) {
      GraphicalNode node = li.next();
      if (node == old) {
        li.set(newNode);
        old.setParentNode(null);
        return;
      }
    }
    throw new IllegalArgumentException("subnode not contained in this node");
  }

  /*
   * (non-Javadoc)
   *
   * @see de.unisb.loot.gui.GraphicalNode#paintAbsolute(java.awt.Rectangle,
   *      java.awt.Graphics)
   */
  @Override
  public void paintAbsolute(Rectangle absoluteArea, Graphics g) {
    // call the paint method of every sub node
    for (GraphicalNode subNode : this.nodeTable) {
      if (subNode != null) subNode.paint(absoluteArea, g);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see de.unisb.loot.gui.GraphicalNode#adjustSize(java.awt.Graphics)
   */
  @Override
  public void adjustSize(Graphics g) {
    // finish the last row
    while (nodeTable.size() < rowCount * colCount) nodeTable.add(null);

    assert (this.style != null);
    // determine minimal column widths and row heights
    int[] rowHeights = new int[rowCount];
    Arrays.fill(rowHeights, 0);
    int[] colWidths = new int[colCount];
    Arrays.fill(colWidths, 0);

    // we need two runs for the brackets and assume that is enough, although
    // that might not be the case, but proper convergence could take forever.
    for (int row = 0; row < rowCount; ++row) {
      for (int col = 0; col < colCount; ++col) {
        GraphicalNode subnode = getNode(row, col);
        if (subnode != null) {
          subnode.adjustSize(g);
          rowHeights[row] = Math.max(rowHeights[row], subnode.getRect().height);
          colWidths[col] = Math.max(colWidths[col], subnode.getRect().width);
        }
      }
    }

    for (int row = 0; row < rowCount; ++row) {
      for (int col = 0; col < colCount; ++col) {
        GraphicalNode subnode = getNode(row, col);
        if (subnode != null) {
          subnode.growTo(colWidths[col], rowHeights[row]);
          rowHeights[row] = Math.max(rowHeights[row], subnode.getRect().height);
          colWidths[col] = Math.max(colWidths[col], subnode.getRect().width);
        }
      }
    }

    int totalHeight = 0;
    for (int h : rowHeights) { totalHeight += h; }
    int totalWidth = 0;
    for (int w : colWidths) { totalWidth += w; }

    // Compute the offset as given by the padding.
    int offset = this.style.getPadding().getOffset();

    // Use it to determine maximum width and height of the CompositeNode.
    totalHeight += offset + offset;
    totalWidth += offset + offset;

    // now adjust relative origins, honoring alignment
    int currentY = offset;
    for (int row = 0; row < rowCount; ++row) {
      int currentX = offset;
      for (int col = 0; col < colCount; ++col) {
        GraphicalNode subnode = getNode(row, col);
        char spec = getSpec(row, col);
        if (subnode != null) {
          adjustOrigin(subnode, spec, currentX, currentY,
              colWidths[col], rowHeights[row]);
        }
        currentX += colWidths[col];
      }
      currentY += rowHeights[row];
    }

    // set the CompositeNode's size accordingly
    this.area.setSize(totalWidth, totalHeight);
  }

  /**
   * Adjust the origin of subNode according to the current cell's alignment
   * and area.
   * 
   * @param subNode the sub node
   * @param align the align
   * @param x the x
   * @param y the y
   * @param width the width
   * @param height the height
   */
  private void adjustOrigin(GraphicalNode subNode, char align,
      int x, int y, int width, int height) {
    int xoff = 0, yoff = 0;
    // 0, 0 is the upper left corner.
    // compute horizontal offset
    switch (align) {
    case 'n': // all at the top, centered
    case 's': // bottom, horizontally centered
    case 'c': // centered vertically and horizontally
      xoff = (width - subNode.getRect().width) / 2;
      break;
    case 'N': // all at the top, flush right
    case 'e': // vertically centered, flush right
    case 'E': // bottom, flush right
      xoff = (width - subNode.getRect().width);
      break;
    }
    switch (align) {
    case 'e': // vertically centered, flush right
    case 'w': // vertically centered, flush left
    case 'c': // centered vertically and horizontally
      yoff = (height - subNode.getRect().height) / 2;
      break;
    case 'E': // bottom, flush right
    case 's': // bottom, horizontally centered
    case 'S': // bottom, flush left
      yoff = (height - subNode.getRect().height);
      break;
    } // end switch
    subNode.setOrigin(x + xoff, y  + yoff);
  } // end method


  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.nodes.GraphicalNode#getChildContainingPoint(java.awt.Point)
   */
  @Override
  protected GraphicalNode getChildContainingPoint(Point p) {
    for (GraphicalNode child : nodeTable) {
      if (child != null && child.area.contains(p)) {
        p.x -= child.area.x;
        p.y -= child.area.y;
        return child.getChildContainingPoint(p);
      }
    }
    return this;
  }
} // end class

