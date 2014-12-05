package de.dfki.lt.loot.gui.util;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileFilter;

import de.dfki.lt.loot.gui.MainFrame;


public interface FileProcessor {
  /** return <code>true</code> if processing succeeded, <code>false</code>
   *  otherwise.
   */
  public abstract boolean processFile(File toProcess, MainFrame mf)
      throws IOException;

  /** Return a file association for the given extension (optional method)
   *  return null if no association found
   */
  public abstract FileAssociation getAssociation(String extension);

  /** Return a file filter for the open dialogue (optional method)
   * return null for no filter
   */
  public FileFilter getFileFilter();

}
