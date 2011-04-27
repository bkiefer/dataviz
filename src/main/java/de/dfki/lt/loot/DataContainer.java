/*
 * 
 */
package de.dfki.lt.loot;

import java.util.Collection;

// TODO: Auto-generated Javadoc
/**
 * The Interface DataContainer.
 * 
 * @param <KEYTYPE> the generic type
 * @param <VALUETYPE> the generic type
 */
public interface DataContainer<KEYTYPE, VALUETYPE> {
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	public String toString();
	
	/**
	 * Gets the pair.
	 * 
	 * @param i the i
	 * @return the pair
	 */
	public Pair<KEYTYPE, VALUETYPE> getPair(int i);
	
	/**
	 * Gets the index.
	 * 
	 * @param pair the pair
	 * @return the index
	 */
	public int getIndex(Pair<KEYTYPE, VALUETYPE> pair);
	
	/**
	 * Gets the length.
	 * 
	 * @return the length
	 */
	public int getLength();
	
	/**
	 * Gets the pairs.
	 * 
	 * @return the pairs
	 */
	public Collection<Pair<KEYTYPE, VALUETYPE>> getPairs();
	
	/**
	 * Adds the.
	 * 
	 * @param pair the pair
	 */
	public void add(Pair<KEYTYPE, VALUETYPE> pair);
}
