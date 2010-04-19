package de.dfki.lt.loot;

public class KeyPair<KEYTYPE, VALUETYPE> extends Pair<KEYTYPE, VALUETYPE> {

	public KeyPair(KEYTYPE first, VALUETYPE second) {
		super(first, second);
	}
	
	public String toString()
	  {
		  String toReturn = "" + getFirst(); 
		  return (toReturn);
	  }

}
