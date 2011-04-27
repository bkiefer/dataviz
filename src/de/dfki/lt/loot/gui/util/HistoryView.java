package de.dfki.lt.loot.gui.util;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/***********************************************************************
 * A class to provide a view for input and other histories, including
 * opening file dialogs, etc.
 *
 * Currently, this is implemented using a JMenu
 ***********************************************************************/
public class HistoryView implements HistoryListener {

  private HistoryModel _model;

  /** The last history file */
  private File _historyFile;

 /** Menu displaying the last inputs */
  private JMenu _historyMenu;

  /** The parent component for the dialogs */
  private Component _parent;

  /** The listener that listens for menu item selection */
  private ActionListener _menuItemListener;

  /** If true, the item added last will be at the top of the menu */
  private boolean _newestUp;

  public HistoryView(String label, HistoryModel model, Component parent,
    ActionListener menuItemListener, boolean newestUp) {
    _model = model;
    _model.addListener(this);
    _historyMenu = new JMenu(label);
    _parent = parent;
    _menuItemListener = menuItemListener;
    _newestUp = newestUp;
    modelChanged();
  }

  public HistoryView(String label, HistoryModel model, Component parent,
      ActionListener menuItemListener) {
    this(label, model, parent, menuItemListener, true);
  }

  private void checkRemovedLast() {
    if (_historyMenu.getItemCount() == 0) {
      JMenuItem it = new JMenuItem("Empty");
      it.setEnabled(false);
      _historyMenu.add(it);
    }
  }

  private void checkAddingFirst() {
    if (_historyMenu.getItemCount() == 1) {
      if (_historyMenu.getItem(0).getText().equals("Empty")) {
        _historyMenu.remove(0);
      }
    }
  }

  // *************************************************************************
  // Implement the HistoryListener interface
  // *************************************************************************

  public void addLast(String currentText) {
    checkAddingFirst();
    JMenuItem item = new JMenuItem(currentText);
    item.addActionListener(_menuItemListener);
    if (_newestUp) {
      _historyMenu.add(item, 0);
    } else {
      _historyMenu.add(item);
    }
  }

  public void modelChanged() {
    _historyMenu.removeAll();
    for (String input : _model) {
      addLast(input);
    }
    checkRemovedLast();
  }

  public void removeFirst() {
    if (_newestUp) {
      _historyMenu.remove(_historyMenu.getItemCount() - 1);
    } else {
      _historyMenu.remove(0);
    }
    checkRemovedLast();
  }

  public void remove(int pos) {
    if (_newestUp) {
      pos = _historyMenu.getItemCount() - pos - 1;
    }
    _historyMenu.remove(pos);
    checkRemovedLast();
  }

  /** Store the last file that has been loaded/stored for this view */
  public void fileChanged(File hist) {
    _historyFile = hist;
  }

  private JFileChooser getHistoryFileChooser() {
    File hist = _historyFile;
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(hist);
    if (hist.isFile()) {
      fc.setSelectedFile(hist);
    }
    return fc;
  }

  private File fileChooserLoop(JFileChooser fc, int returnVal) {
    File hist = null;
    do {
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        hist = fc.getSelectedFile();
      }
    }
    while (hist == null && returnVal != JFileChooser.CANCEL_OPTION);
    return hist;
  }


  /** Return the last load/save location. May be a directory, then, no file has
   *  been loaded/saved up to this point.
   */
  public File getHistoryFile() {
    return _historyFile;
  }


  /** Open a file dialog and read the selected file. Returns false if the
   *  user pressed CANCEL
   */
  public boolean loadInteractively() throws IOException {
    JFileChooser fc = getHistoryFileChooser();
    File hist = fileChooserLoop(fc, fc.showOpenDialog(_parent));
    _model.load(hist);
    _historyFile = hist;
    return hist != null;
  }


  /** Open a file dialog and save the history to the selected file. Returns
   *  false if the user pressed CANCEL
   */
  public boolean saveInteractively() throws IOException {
    // create file chooser for txt files
    JFileChooser fc = getHistoryFileChooser();
    File hist = fileChooserLoop(fc, fc.showSaveDialog(_parent));
    if (hist == null)
      return false;
    _historyFile = hist;
    _model.save(_historyFile);
    return true;
  }

  public JMenu getMenu() {
    return _historyMenu;
  }
}
