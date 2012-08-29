package de.dfki.lt.loot.gui.util;

import java.io.StringWriter;


public class FileAssociation {
  private String[] _extensions;
  private ObjectHandler _reader;
  
  public FileAssociation(ObjectHandler reader, String ... extensions) {
    _reader = reader;
    _extensions = extensions;
  }
  
  public String[] getExtensions() { return _extensions; }

  public ObjectHandler getReader() { return _reader; }

  public String toString() {
    StringWriter sw = new StringWriter();
    boolean first = true;
    sw.append('<');
    for(String ext : _extensions) {
      if (! first) sw.append(',');
      else first = false;
      sw.append(ext);
    }
    sw.append("> -> ").append(_reader.toString());
    return sw.toString();
  }
}