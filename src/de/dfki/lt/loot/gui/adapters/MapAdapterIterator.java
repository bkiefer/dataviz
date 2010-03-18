/*
 * 
 */
package de.dfki.lt.loot.gui.adapters;

import de.dfki.lt.loot.Pair;

// TODO: Auto-generated Javadoc
/**
 * The Interface MapAdapterIterator.
 */
public interface MapAdapterIterator {

  /**
   * Checks for next.
   * 
   * @return true, if successful
   */
  public abstract boolean hasNext();

  /**
   * Next.
   * 
   * @return the pair
   */
  public abstract Pair<String, Object> next();

}
