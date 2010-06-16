package de.dfki.lt.loot.gui.adapters;

public class EmptyModelAdapter extends ModelAdapter {

  private int _facet;

  /** Create an Adapter that will tag everything as the given facet */
  public EmptyModelAdapter(int facet) {
    _facet = facet;
  }

  /** Create a dummy Adapter, because the layout won't use it anyway */
  public EmptyModelAdapter() {
    this(0);
  }

  /** returns the same facet all the time */
  @Override
  public int facets(Object model) { return _facet; }

}
