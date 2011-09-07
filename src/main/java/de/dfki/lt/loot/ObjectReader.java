package de.dfki.lt.loot;

import java.io.IOException;
import java.io.InputStream;

public interface ObjectReader {
  Object read(InputStream in) throws IOException ;
}
