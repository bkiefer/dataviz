package de.dfki.lt.loot.gui.util;

import java.io.File;
import java.io.IOException;

public interface HistoryModel extends Iterable<String> {

  public abstract void addListener(HistoryListener l);

  public abstract void load(File f) throws IOException;

  public abstract void save(File f) throws IOException;
}
