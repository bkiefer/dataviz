package de.dfki.lt.loot.gui.adapters;

import de.dfki.lt.loot.Pair;

public interface MapAdapterIterator {

  public abstract boolean hasNext();

  public abstract Pair<Object, Object> next();

}
