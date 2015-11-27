package de.dfki.lt.loot.gui.util;

import javax.swing.filechooser.FileFilter;


public abstract class FileProcessorAdapter implements FileProcessor {

  public FileAssociation getAssociation(String extension) { return null; }

  public FileFilter getFileFilter() { return null; }

}
