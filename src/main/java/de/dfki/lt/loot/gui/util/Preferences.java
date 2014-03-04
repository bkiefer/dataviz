package de.dfki.lt.loot.gui.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class Preferences {
  protected static final Logger logger = Logger.getLogger(Preferences.class);

  /** Where the preferences are loaded / saved */
  private File _prefFile;

  /** The modification time of the preferences file when it was loaded */
  private long _prefFileModified = -1;

  /** contents */
  private LinkedHashMap<String, String> _preferences;

  private Set<String> _changed;

  /** Recently loaded project files */
  protected InputHistory _recentFiles;

  public Preferences(File prefFile) {
    _prefFile = prefFile;
    load();
  }

  public boolean contains(String key) {
    return _preferences == null ? false
                                : _preferences.containsKey(key);
  }

  public String get(String key) {
    return _preferences == null ? null : _preferences.get(key);
  }

  public int getInt(String key) {
    if (_preferences == null) return 0;
    String val = _preferences.get(key);
    try {
      return Integer.parseInt(val);
    }
    catch (NumberFormatException nex) {
      logger.error("Invalid number for "+key+": " + val);
    }
    return 0;
  }

  public void put(String key, String value) {
    if(_preferences == null) {
      _preferences = new LinkedHashMap<String, String>();
      _changed = new HashSet<String>();
    }
    _preferences.put(key, value);
    _changed.add(key);
  }

  public InputHistory recentFiles() {
    return _recentFiles;
  }

  public void recentFiles(int size) {
    _recentFiles = new InputHistory(size);
  }

  protected void installPreferences(
      Map<String, LinkedHashMap<String, String>> sections){
    _preferences = sections.get("Settings");
    if (sections.containsKey("RecentFiles")) {
      int size = getInt("history_size");
      _recentFiles = new InputHistory(size == 0 ? 5 : size);
      _recentFiles.addAll(sections.get("RecentFiles").keySet());
    }
  }

  private LinkedHashMap<String, LinkedHashMap<String, String>>
  loadPreferences() throws FileNotFoundException, IOException {
    return IniFileReader.readIniFile(_prefFile);
  }

  public void load() {
    try {
      if (_prefFile != null && _prefFile.isFile()) {
        _prefFileModified = _prefFile.lastModified();
        LinkedHashMap<String, LinkedHashMap<String, String>> sections =
            IniFileReader.readIniFile(_prefFile);
        if (sections != null)
          installPreferences(sections);
      }
    }
    catch (IOException ex) {
      logger.error("Problem loading preferences: " + ex);
    }
  }

  public void save() {
    try {
      // TODO: maybe create directory??
      if (_prefFile != null) {
        if (_prefFile.exists()) {
          long modification = _prefFile.lastModified();
          if (modification != _prefFileModified) {
            // this is be merged such that only modified preferences are stored
            Map<String, LinkedHashMap<String, String>> sections =
                loadPreferences();
            // add all prefs not in here, supersede all that have been changed
            LinkedHashMap<String, String> prefs = sections.get("Settings");
            if (prefs != null)
              for (Map.Entry<String, String> entry : prefs.entrySet()) {
                // if the value is new or has changed somewhere else, take it
                if (! _preferences.containsKey(entry.getKey())
                    || (! get(entry.getKey()).equals(entry.getValue())
                        && ! _changed.contains(entry.getKey()))) {
                  put(entry.getKey(), entry.getValue());
                }
              }
            // merge the recent files
            _recentFiles.merge(sections.get("RecentFiles").keySet());
          }
        }
        IniFileWriter pref = null;
        try {
          pref = new IniFileWriter(_prefFile);
          pref.writeMap("Settings", _preferences);
          pref.writeList("RecentFiles", _recentFiles);
        } finally {
          if (pref != null)
            pref.close();
        }
      }
    }
    catch (IOException ioex) {
      logger.error("Problem storing preferenes: " + ioex);
    }
  }
}
