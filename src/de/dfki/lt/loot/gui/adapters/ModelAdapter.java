/*
 * 
 */


package de.dfki.lt.loot.gui.adapters;

import java.util.Iterator;

// TODO: Auto-generated Javadoc
/**
 * The Class ModelAdapter.
 */
public abstract class ModelAdapter {

  /** The Constant ALL. */
  public static final int NONE = 0,
  NULL = 1, ATOM = NULL << 1, CONS = ATOM << 1, TREE = CONS << 1,
  MAP = TREE << 1, MATRIX = MAP << 1,
  ALL = (MATRIX << 1) -1 ;

  /**
   * Sets the top model.
   * 
   * @param model the new top model
   */
  public void setTopModel(Object model) {}

  /**
   * Facets.
   * 
   * @param model the model
   * @return the int
   */
  public abstract int facets(Object model);

  /**
   * Gets the attribute.
   * 
   * @param model the model
   * @param name the name
   * @return the attribute
   */
  public String getAttribute(Object model, String name) {
    return null;
  }

  /* -- ConsFacet -- */

  /**
   * Gets the first.
   * 
   * @param model the model
   * @return the first
   */
  public Object getFirst(Object model){
    throw new UnsupportedOperationException();
  };

  /**
   * Gets the rest.
   * 
   * @param model the model
   * @return the rest
   */
  public Object getRest(Object model){
    throw new UnsupportedOperationException();
  };

  /* -- MapFacet -- */

  /**
   * Map iterator.
   * 
   * @param model the model
   * @return the map adapter iterator
   */
  public MapAdapterIterator mapIterator(Object model){
    throw new UnsupportedOperationException();
  };

  /* -- TreeFacet -- */

  /**
   * Gets the root node.
   * 
   * @param model the model
   * @return the root node
   */
  public Object getRootNode(Object model){
    throw new UnsupportedOperationException();
  };

  /**
   * Gets the tree daughters.
   * 
   * @param model the model
   * @return the tree daughters
   */
  public Iterator getTreeDaughters(Object model){
    throw new UnsupportedOperationException();
  };

}
