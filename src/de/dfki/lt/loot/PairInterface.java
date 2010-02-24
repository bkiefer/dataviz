package de.dfki.lt.loot;

public interface PairInterface<KEYTYPE, VALUETYPE> {
  KEYTYPE getFirst();
  VALUETYPE getSecond();
}
