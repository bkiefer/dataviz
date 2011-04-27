/*
 * 
 */
package de.dfki.lt.loot;

// TODO: Auto-generated Javadoc
/**
 * The Interface PairInterface.
 * 
 * @param <KEYTYPE> the generic type
 * @param <VALUETYPE> the generic type
 */
public interface PairInterface<KEYTYPE, VALUETYPE> {
  
  /**
   * Gets the first.
   * 
   * @return the first
   */
  KEYTYPE getFirst();
  
  /**
   * Gets the second.
   * 
   * @return the second
   */
  VALUETYPE getSecond();
}
