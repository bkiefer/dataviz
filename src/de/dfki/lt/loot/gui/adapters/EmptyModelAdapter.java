package de.dfki.lt.loot.gui.adapters;

import java.util.Iterator;

public class EmptyModelAdapter extends ModelAdapter {

  @Override
  public int facets(Object model) { return 0; }

  @Override
  public Iterator getSubModels(Object model) { return null; }

}
