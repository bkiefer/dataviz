package de.dfki.lt.loot.gui.util;

import java.io.File;

public interface HistoryListener {

  public abstract void removeFirst();

  public abstract void addLast(String what);

  public abstract void modelChanged();

  public abstract void fileChanged(File f);

  public abstract void remove(int pos);

}
