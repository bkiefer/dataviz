package de.dfki.lt.loot.gui.layouts;

public class CompactLayout extends AbstractLayout {

  private SimpleTreeLayout stl;

  public CompactLayout() {
    addLayout(new CompactMapLayout());
    addLayout(new CompactConsLayout());
    addLayout(stl = new SimpleTreeLayout());
    addLayout(new BasicAtomLayout());
    addDecorator(new FailureDecorator());
  }

  /** If horizontal is true, this algorithm layouts from right to left,
   *  otherwise from top to bottom.
   */
  public void setTreeHorizontal(boolean horizontal) {
    stl.setHorizontal(horizontal);
  }

  /** @param levelDistance the levelDistance to set */
  public void setTreeLevelDistance(int levelDistance) {
    stl.setLevelDistance(levelDistance);
  }

  /** @param nodeDistance the nodeDistance to set */
  public void setTreeNodeDistance(int nodeDistance) {
    stl.setNodeDistance(nodeDistance);
  }

}
