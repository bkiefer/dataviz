package de.dfki.lt.loot.gui.util;

import javax.swing.filechooser.FileFilter;


public abstract class FileProcessorAdapter implements FileProcessor {

  @Override
  public FileAssociation getAssociation(String extension) { return null; }

  @Override
  public FileFilter getFileFilter() { return null; }

}
