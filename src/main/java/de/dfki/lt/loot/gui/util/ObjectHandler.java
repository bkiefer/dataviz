package de.dfki.lt.loot.gui.util;

import java.io.IOException;
import java.io.InputStream;

public interface ObjectHandler {
  boolean process(InputStream in) throws IOException ;
}
