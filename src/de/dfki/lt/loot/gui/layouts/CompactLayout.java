package de.dfki.lt.loot.gui.layouts;

public class CompactLayout extends AbstractLayout {

  public CompactLayout() {
    addLayout(new CompactMapLayout());
    addLayout(new CompactConsLayout());
    addLayout(new SimpleTreeLayout());
    addLayout(new BasicAtomLayout());
    addDecorator(new FailureDecorator());
  }

}
