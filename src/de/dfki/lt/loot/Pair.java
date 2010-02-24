package de.dfki.lt.loot;

public class Pair<KEYTYPE, VALUETYPE>
implements PairInterface<KEYTYPE, VALUETYPE> {
  private Object _first;
  private Object _second;

  public Pair(KEYTYPE first, VALUETYPE second) {
    _first = first;
    _second = second;
  }

  public KEYTYPE getFirst() { return (KEYTYPE) _first; }
  public VALUETYPE getSecond() { return (VALUETYPE) _second; }
}
