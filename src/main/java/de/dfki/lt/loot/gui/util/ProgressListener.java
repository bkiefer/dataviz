package de.dfki.lt.loot.gui.util;

public interface ProgressListener {

  public void setMaximum(int max);

  public void progress(int count);
}
