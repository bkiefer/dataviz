package de.dfki.lt.loot.gui.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.dfki.lt.loot.gui.MainFrame;

public interface ObjectHandler {
  boolean process(File f, InputStream in, MainFrame mf) throws IOException ;
}
