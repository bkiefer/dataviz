package de.dfki.lt.loot.gui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import de.dfki.lt.loot.gui.MainFrame;


public class GenericFileProcessor implements FileProcessor {
  private static final Logger logger = Logger.getLogger(FileProcessor.class);

  /** Associates file extensions with input readers */
  protected HashMap<String, FileAssociation> _associations =
      new HashMap<String, FileAssociation>();

  protected FileAssociation _default = null;

  public boolean processFile(File toProcess, MainFrame mf) throws IOException {
    // add some smart code to assess the file type and call the right `open'
    // method
    String fileName = toProcess.getName();
    int lastDot = fileName.lastIndexOf('.');
    String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
    boolean uncompress = false;
    if (extension.equals("gz")) {
      uncompress = true;
      int secondLast = fileName.lastIndexOf('.', lastDot - 1);
      extension = fileName.substring(secondLast + 1, lastDot);
    }
    if (! extension.isEmpty()) {
      FileAssociation fa = _associations.get(extension);
      if (fa != null) {
        ObjectHandler r = fa.getReader();
        InputStream in = new FileInputStream(toProcess);
        if (uncompress) {
          in = new GZIPInputStream(in);
        }
        return r.process(toProcess, in, mf);
      }
    }
    return false;
  }

  public FileAssociation getAssociation(String extension) {
    return _associations.get(extension);
  }

  public FileFilter getFileFilter() {
    StringWriter sw = new StringWriter();
    boolean first = true;
    for (String ext : _associations.keySet()) {
      if (! first) sw.append('/');
      else first = false;
      sw.append(ext);
    }
    sw.append(" files only");
    return new FileNameExtensionFilter(sw.toString(),
        _associations.keySet().toArray(new String[0]));
  }

  /** Add a new file association (a mapping from extensions to ObjectHandler)
   *  If extensions is null, the hander is used as default handler if no other
   *  association can be found.
   *
   * @param r the ObjectHandler to treat these files
   * @param extensions the possible extensions to be treated by the handler
   */
  public void addFileAssociation(ObjectHandler r, String ... extensions) {
    if (extensions == null) {
      _default = new FileAssociation(r, extensions);
      return;
    }
    FileAssociation fa = new FileAssociation(r, extensions);
    for (String extension : extensions) {
      if (_associations.containsKey(extension)) {
        logger.warn("Trying to associate " + extension + " with two different" +
            " views " + _associations.get(extension) + " and " + fa
            + ", ignoring the second one");
      } else {
        _associations.put(extension, fa);
      }
    }
  }
}
