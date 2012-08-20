package de.dfki.lt.loot.gui.util;

import java.io.File;

public interface FileProcessor {
  /** return <code>true</code> if processing succeeded, <code>false</code>
   *  otherwise.
   */
  public abstract boolean processFile(File toProcess);
}
