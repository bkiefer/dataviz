/*
 * 
 */
package de.dfki.lt.loot;

// TODO: Auto-generated Javadoc
/**
 * The Class Pair.
 * 
 * @param <KEYTYPE> the generic type
 * @param <VALUETYPE> the generic type
 */
public class Pair<KEYTYPE, VALUETYPE>
implements PairInterface<KEYTYPE, VALUETYPE> {
  
  /** The _first. */
  private Object _first;
  
  /** The _second. */
  private Object _second;

  /**
   * Instantiates a new pair.
   * 
   * @param first the first
   * @param second the second
   */
  public Pair(KEYTYPE first, VALUETYPE second) {
    _first = first;
    _second = second;
  }

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.PairInterface#getFirst()
   */
  @SuppressWarnings("unchecked")
  public KEYTYPE getFirst() { return (KEYTYPE) _first; }
  
  /* (non-Javadoc)
   * @see de.dfki.lt.loot.PairInterface#getSecond()
   */
  @SuppressWarnings("unchecked")
  public VALUETYPE getSecond() { return (VALUETYPE) _second; }
  
  public void setFirst(KEYTYPE newFirst){ _first = newFirst;}
  
  public void setSecond(VALUETYPE newSecond){ _second = newSecond;}
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
	  return (_first + " : " + _second );
  }
}
