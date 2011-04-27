package de.dfki.lt.loot.gui.layouts;

public abstract class FacetLayoutBase implements FacetLayout {
  // The meta (dispatching) facet layout
  public FacetLayout _meta;

  public void register(FacetLayout metaLayout) {
    _meta = metaLayout;
  }
}
