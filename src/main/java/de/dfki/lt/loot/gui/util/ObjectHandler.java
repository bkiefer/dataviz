package de.dfki.lt.loot.gui.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface ObjectHandler {
  boolean process(File f, InputStream in) throws IOException ;
}
