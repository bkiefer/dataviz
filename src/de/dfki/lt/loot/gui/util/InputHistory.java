package de.dfki.lt.loot.gui.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**************************************************************************
 * A class to manage input histories, including writing to an reading from
 * file.
 **************************************************************************/
public class InputHistory implements HistoryModel {

  private static final String newLine = System.getProperty("line.separator");

  private static final int DEFAULT_MAX_SIZE = 10;

  /** The maximal size of the history */
  private int _maxHistorySize;

  /** The input history */
  private Deque<String> _history;

  /** Did the history change since the last load */
  private boolean _historyChanged;

  /** List of ChangeListeners */
  private List<HistoryListener> _changeListeners;

  public InputHistory(int maxSize) {
    _maxHistorySize = maxSize;
    _history = new LimitedDeque<String>(_maxHistorySize);
    _historyChanged = false;
    _changeListeners = new ArrayList<HistoryListener>(2);
  }

  public InputHistory() {
    this(DEFAULT_MAX_SIZE);
  }

  /** Internal change methods that also call the listeners */

  private void fireHistoryChanged() {
    _historyChanged = true;
    for (HistoryListener l : _changeListeners) {
      l.modelChanged();
    }
  }

  private void removeFirst() {
    _historyChanged = true;
    _history.removeFirst();
    for (HistoryListener l : _changeListeners) {
      l.removeFirst();
    }
  }

  private void addLast(String currentText) {
    _historyChanged = true;
    add(currentText);
    for (HistoryListener l : _changeListeners) {
      l.addLast(currentText);
    }
  }

  private void fireFileChanged(File f) {
    for (HistoryListener l : _changeListeners) {
      l.fileChanged(f);
    }
  }

  public void addListener(HistoryListener hl) {
    _changeListeners.add(hl);
  }

  /** Has the history changed since the last load/save?
   * (something worth saving?)
   */
  public boolean hasHistoryChanged() {
    return _historyChanged;
  }


  /** clear the history and its view */
  public void clear() {
    if (! _history.isEmpty()) {
      _history.clear();
      fireHistoryChanged();
    }
  }


  /** Fulfill the Iterable<String> interface */
  public Iterator<String> iterator() {
    return _history.iterator();
  }


  /** Add a new item to the history. If too many history items have been stored
   *  already, old ones will be deleted.
   *
   *  This also takes care about changes in the view.
   */
  public void add(String currentText) {
    if (! _history.contains(currentText)) {
      while (! _history.offer(currentText)) {
        removeFirst();
      }
      addLast(currentText);
    }
  }

  /** Add the items in the given iterable to the history. If too many history
   *  items have been stored already, old ones will be deleted.
   *
   *  This also takes care about changes in the view.
   */
  public void addAll(Iterable<String> container) {
    for(String s : container) {
      add(s);
    }
  }


  /** Save the history to a file that separates the single inputs by empty lines.
   *  Therefore, empty lines are stripped from the input
   */
  public void save(File f) throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(f));
    for(String input : _history) {
      // remove empty lines from the strings
      input.replaceAll("(\r?\\n)*\r?\n", newLine);
      out.append(input);
      // separate with empty line
      out.newLine(); out.newLine();
    }
    out.close();
    fireFileChanged(f);
    _historyChanged = false;
  }


  /** get the history from the given file. If the argument historyFile
   *  represents a directory, it is only stored internally as default directory
   *  for the next load or save operation.
   */
  public void load(File historyFile) throws IOException {
    if (historyFile != null) {
      try {
        if (historyFile.isFile()) {
          _history.clear();
          BufferedReader in = new BufferedReader(new FileReader(historyFile));
          String nextLine = null;
          StringBuilder nextInput = new StringBuilder();;
          boolean spaceLeft = true;
          while (spaceLeft && (nextLine = in.readLine()) != null) {
            nextLine = nextLine.trim();
            if (nextLine.isEmpty()) {
              spaceLeft = _history.offer(nextInput.toString());
              nextInput = new StringBuilder();
            } else {
              if (nextInput.length() > 0) {
                nextInput.append(newLine);
              }
              nextInput.append(nextLine);
            }
          }
          if (nextInput.length() > 0) {
            _history.add(nextInput.toString());
          }
        }
        fireFileChanged(historyFile);
      }
      finally {
        fireHistoryChanged();
        _historyChanged = false;
      }
    }
  }
}
