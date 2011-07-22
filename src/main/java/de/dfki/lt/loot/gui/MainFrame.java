package de.dfki.lt.loot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.dfki.lt.loot.gui.adapters.CollectionsAdapter;
import de.dfki.lt.loot.gui.adapters.DOMAdapter;
import de.dfki.lt.loot.gui.adapters.EmptyModelAdapter;
import de.dfki.lt.loot.gui.adapters.ModelAdapter;
import de.dfki.lt.loot.gui.adapters.ModelAdapterFactory;
import de.dfki.lt.loot.gui.layouts.CompactLayout;
import de.dfki.lt.loot.gui.util.HistoryView;
import de.dfki.lt.loot.gui.util.IniFileReader;
import de.dfki.lt.loot.gui.util.IniFileWriter;
import de.dfki.lt.loot.gui.util.InputHistory;

/**
 * <code>MainFrame</code> defines the main window of the application.
 *
 * @author Joerg Steffen, DFKI
 * @author Bernd Kiefer, DFKI
 * @author Java 2 Kurs people
 * @version $Id: MainFrame.java 222 2009-01-29 13:51:16Z jost02 $
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

  /** This contains the currently open frames. */
  protected static List<MainFrame> _openFrames = new ArrayList<MainFrame>();

  /** These listeners are called when the last frame has been closed */
  protected static List<CloseAllListener> clAll =
    new LinkedList<CloseAllListener>();

  /** This contains the current directory. */
  protected File _currentDir;

  protected Object[][] actionSpecs() {
    Object [][] results = {
      {"New", "window-new", "New Frame", "New Frame",
        new Runnable() { public void run() { newFrame(); } } },
      {"Open", "document-open", "Open", "Open File",
        new Runnable() { public void run() { openFileDialog(); } } },
      {"Select Font", "go-next", "Select Font", "Select Font",
        new Runnable() { public void run() { chooseFont(); } } },
      {"Close", "window-close", "Close", "Close Window",
        new Runnable() { public void run() { close(); } } },
      {"Quit", "application-exit", "Quit", "Quit",
        new Runnable() { public void run() { closeAll(); } } },
    };
    return results;
  }

  protected Object[][] menuSpecs() {
    Object [][] results = {
    { "New",
      KeyStroke.getKeyStroke(Character.valueOf('n'), InputEvent.ALT_DOWN_MASK),
      new Runnable() { public void run() { newFrame(); } }
    },
    { "Open", KeyEvent.VK_O,
      new Runnable() { public void run() { openFileDialog(); } }
    },
    { "Close",
      KeyStroke.getKeyStroke(Character.valueOf('w'),
          InputEvent.META_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
      new Runnable() { public void run() { close(); } }
    },
    { null },
    { "Recent Files", null, null },
    { null },
    { "Select Font", null,
      new Runnable() { public void run() { chooseFont(); } }
    },
    /*
    { "Load History", null,
      new Runnable() { public void run() { loadHistory(); } } },
    { "Save History", null,
      new Runnable() { public void run() { saveHistory(); } } },
    { "Clear History", null,
      new Runnable() { public void run() { _history.clear(); } } },
    { null },
    */
    {"Exit",
      KeyStroke.getKeyStroke(Character.valueOf('a'),
          InputEvent.ALT_DOWN_MASK),
      new Runnable() { public void run() { closeAll(); } }
    }
    };
    return results;
  }

  /* *************************************************************************
   * fields for GUI elements
   * *************************************************************************/

  /** Should the history be saved without asking? */
  protected boolean _saveHistoryByDefault = false;

  /** Saved Input history, also managing view */
  protected InputHistory _history = null;

  /** View displaying the last inputs */
  protected HistoryView _historyView = null;

  /** Global preferences
   *  Valid are: tracing (all|match|action)
   *             traceWindow (fourQuadrants|tabbed)
   *             emacs (yes|no)
   */
  protected HashMap<String, String> _preferences;

  /** Recently loaded project files */
  protected InputHistory _recentFiles;

  /** Font chooser dialog */
  protected FontChooser f;

  /** Action Buttons */
  protected ArrayList<JButton> _actionButtons;

  protected static String _defaultFont = "Monospace";
  protected static String _iconPath = null;

  protected Font _textFont = new Font(_defaultFont, 0, 12);

  protected Dimension _preferredSize = new Dimension(600, 600);

  /** displays error and status information */
  protected JLabel _statusLine;

  /** This contains the content area. */
  protected DrawingPanel _contentArea;

  /* **********************************************************************
   * Window closing functionality
   * ********************************************************************** */

  public interface CloseAllListener {
    public abstract void allClosed();
  }

  protected class ReleaseSemaphoreOnCloseAll implements CloseAllListener {
    Semaphore mySem;

    public ReleaseSemaphoreOnCloseAll(Semaphore sem) { mySem = sem; }

    public void allClosed() { mySem.release(); }
  }

  /** <code>Terminator</code> defines action to be done when closing a frame.
   */
  protected class Terminator extends WindowAdapter {
    /** This creates a new instance of <code>Terminator</code>. */
    public Terminator() {
      super();
    }

    /** This method is called when the user initiated closing the window. At
     *  this point, this is only a request, not the real closing, and the close
     *  operation can still be cancelled, provided the defaultCloseOperation is
     *  set properly.
     */
    @Override
    public void windowClosing(WindowEvent we) {
      if (_history != null && _history.hasHistoryChanged()) {
        if (_saveHistoryByDefault) {
          saveHistoryMaybeAsk();
        } else {
          int reaction = JOptionPane.showConfirmDialog(MainFrame.this,
              "Save History to File?","Save History",
              JOptionPane.YES_NO_CANCEL_OPTION);
          switch (reaction) {
          case JOptionPane.CANCEL_OPTION: return; // do not close window
          case JOptionPane.NO_OPTION:
            break;
          case JOptionPane.YES_OPTION:
            saveHistoryMaybeAsk();
            break;
          }
        }
      }
      we.getWindow().dispose();
    }

    /** Now the window will definitely be closed. Do all necessary cleanup.
     */
    @Override
    public void windowClosed(WindowEvent we) {
      // find the frame for this window and remove it form the list;
      // make sure this is thread safe
      synchronized (MainFrame._openFrames) {
        Window win = we.getWindow();
        _openFrames.remove(win);
      }
      savePreferences();
      // if this was the last frame, completely shut down the application
      if (MainFrame._openFrames.isEmpty()) {
        for (CloseAllListener caListener : clAll) {
          caListener.allClosed();
        }
      }
    }
  }

  public void registerCloseAllListener(CloseAllListener caListener) {
    clAll.add(0, caListener);
  }

  public void releaseOnAllClosed(Semaphore sem) {
    registerCloseAllListener(new ReleaseSemaphoreOnCloseAll(sem));
  }

  /** a method to close a frame from within the program */
  public void close() {
    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

  /** a method to close all frames from within the program */
  protected void closeAll() {
    synchronized (MainFrame._openFrames) {
      for (MainFrame frame : MainFrame._openFrames) {
        frame.close();
      }
    }
  }

  /* **********************************************************************
   * Constructors
   * ********************************************************************** */

  protected MainFrame(String title, File currDir, DrawingPanel dp) {
    super(title);
    this._currentDir = currDir;
    this._contentArea = dp;
    this.initFrame();
  }

  public MainFrame(String title) {
    this(title, new File("."), new DrawingPanel(new CompactLayout(), null));
  }

  public MainFrame(String title, DrawingPanel dp) {
    this(title, new File("."), dp);
  }

  protected MainFrame() {}


  /* **********************************************************************
   *  Communicate things to the user
   * ********************************************************************** */

  /** Clear the status line (a line at the bottom of the window for status
   *  messages).
   */
  public void clearStatusLine() {
    setStatusLine(" ");
  }

  /** Put the given message into the status line with black (default) text color
   */
  public void setStatusLine(String msg) {
    setStatusLine(msg, Color.BLACK);
  }

  /** Put the given message into the status line with the text color given by
   *  col
   */
  public void setStatusLine(String msg, Color col) {
    _statusLine.setForeground(col);
    _statusLine.setText(msg);
  }

  /* **********************************************************************
   * Action methods
   * ********************************************************************** */

  /** Create a new frame for a possibly different project */
  protected void newFrame() {
    @SuppressWarnings("unused")
    MainFrame newFrame = new MainFrame("New Window");
  }

  protected void chooseFont() {
    FontChooser f = new FontChooser();
    _textFont = f.getChosenFont();
  }

  protected void setInput(String input) {
    throw new UnsupportedOperationException();
  }

  // **************************************************************************
  // History handling
  // **************************************************************************

  protected boolean loadHistory() {
    try {
      _historyView.loadInteractively();
    }
    catch (IOException ioex) {
      reportProblem("Problem laoding history file: "
          + ioex.getLocalizedMessage());
      return false;
    }
    return true;
  }

  protected boolean saveHistory() {
    try {
      _historyView.saveInteractively();
    }
    catch (IOException ioex) {
      reportProblem("Problem saving history file: "
          + ioex.getLocalizedMessage());
      return false;
    }
    return true;
  }

  protected boolean saveHistoryMaybeAsk() {
    try {
      if (_historyView.getHistoryFile() != null) {
        _history.save(_historyView.getHistoryFile());
        return true;
      }
      return _historyView.saveInteractively();
    }
    catch (IOException ioex) {
      reportProblem("Problem saving history file: "
          + ioex.getLocalizedMessage());
    }
    return false;
  }

  /* **********************************************************************
   * Initialization / Creation
   * ********************************************************************** */

  protected File getPreferencesFile() {
    return new File(_currentDir, ".preferences");
  }

  protected File getResourcesDir() {
    return _currentDir;
  }

  protected void loadPreferences() {
    File prefFile = getPreferencesFile();
    if (prefFile != null && prefFile.isFile()) {
      try {
        LinkedHashMap<String, LinkedHashMap<String, String>> sections =
          IniFileReader.readIniFile(prefFile);
        _preferences = sections.get("Settings");
        if (sections.containsKey("RecentFiles")) {
          _recentFiles = new InputHistory(5);
          _recentFiles.addAll(sections.get("RecentFiles").keySet());
        }
      }
      catch (IOException ioex) {
        Logger.getRootLogger().warn("Error reading preferences file: "
            + ioex.getLocalizedMessage());
      }
    }
  }

  protected void savePreferences() {
    File prefFile = getPreferencesFile();
    if (prefFile != null) {
      IniFileWriter pref = null;
      try {
        try {
          pref = new IniFileWriter(prefFile);
          pref.writeMap("Settings", _preferences);
          pref.writeList("RecentFiles", _recentFiles);
        } finally {
          if (pref != null)
            pref.close();
        }
      } catch (IOException ioex) {
        Logger.getRootLogger().warn(
            "Error writing preferences file: " + ioex.getLocalizedMessage());
      }
    }
  }

  public static JButton newButton(String imageName, String actionCommand,
    String toolTipText, String altText, ActionListener l) {
    // Look for the image.
    String imgLocation = _iconPath + "24x24/actions/" + imageName + ".png";
    // URL imageURL = MainFrame.class.getResource(imgLocation);
    String imageURL = null;
    if (new File(imgLocation).exists()) {
      imageURL = imgLocation;
    }

    // Create and initialize the button.
    JButton button = new JButton();
    button.setActionCommand(actionCommand);
    button.setToolTipText(toolTipText);
    button.addActionListener(l);

    if (imageURL != null) { // image found
      button.setIcon(new ImageIcon(imageURL, altText));
    }
    else { // no image found
      button.setText(altText);
      // System.err.println("Resource not found: " + imgLocation);
    }

    return button;
  }

  /** This initializes the menu bar. */
  protected JToolBar newMainToolBar() {
    _actionButtons = new ArrayList<JButton>();
    JToolBar toolBar = new JToolBar("Content Planner Tools");
    Object [][] specs = actionSpecs();
    if (specs == null) return null;
    for (Object[] spec : specs) {
      if (spec[1] != null) {
        final Runnable r = (Runnable) spec[4];
        JButton newButton =
          newButton((String) spec[1],(String) spec[0],
              (String) spec[2],(String) spec[3],
              new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                   clearStatusLine(); r.run();
                 }
              }
          );
        _actionButtons.add(newButton);
        toolBar.add(newButton);
      }
    }
    return toolBar;
  }


  /** Create a new menu item out from name and key spec and add it to the
   *  given menu
   */
  protected void newMenuItem(JMenu menu, String name, Object key,
    Object l) {
    if (l instanceof ActionListener) {
      JMenuItem newItem = new JMenuItem(name);
      if (key instanceof Integer)
        newItem.setMnemonic((Integer) key);
      if (key instanceof KeyStroke)
        newItem.setAccelerator((KeyStroke) key);
      newItem.addActionListener((ActionListener) l);
      menu.add(newItem);
    } else {
      menu.add((JMenu) l);
    }
  }


  /** This initializes the menu bar. */
  protected void newMainMenuBar() {
    // create the menu bar
    JMenuBar menuBar = new JMenuBar();
    menuBar.setOpaque(true);
    menuBar.setPreferredSize(new Dimension(400, 20));

    // create the 'File' menu
    JMenu menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_F);
    menuBar.add(menu);

    for (Object[] spec : menuSpecs()) {
      if (spec[0] != null) {
        if (spec[2] != null) {
          final Runnable r = (Runnable) spec[2];
          newMenuItem(menu, (String) spec[0], spec[1], new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { r.run(); }
          });
        } else {
          if (_recentFiles == null) {
            _recentFiles = new InputHistory(5);
          }
          newMenuItem(menu, (String) spec[0], spec[1],
              new HistoryView("Recent Files", _recentFiles, null,
                  new ActionListener() {
                    @Override
                public void actionPerformed(ActionEvent e) {
                  String fileName = e.getActionCommand();
                  MainFrame.this.openFile(new File(fileName));
                }
              }).getMenu());
        }
      } else {
        menu.addSeparator();
      }
    }

    if (_history != null) {
      _historyView = new HistoryView("Recent Input", _history,
          this, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          setInput(e.getActionCommand());
        }
      });

      menuBar.add(_historyView.getMenu());
    }

    // add menu bar to main frame
    this.setJMenuBar(menuBar);
  }

  protected Component newContentPane() {
    return this._contentArea.wrapScrollable();
  }

  protected void initFrame() {
    try {
      _iconPath = getResourcesDir().getAbsolutePath() + "/icons/";
      String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
      UIManager.setLookAndFeel(lookAndFeel);
      if (! new File(_iconPath).isDirectory()) {
        if (lookAndFeel.contains("GTK")) {
          _iconPath = "/usr/share/icons/gnome/";
        }
      }
    } catch (ClassNotFoundException e) {
      // well, we're content with everything we get
    } catch (InstantiationException e) {
      // well, we're content with everything we get
    } catch (IllegalAccessException e) {
      // well, we're content with everything we get
    } catch (UnsupportedLookAndFeelException e) {
      // well, we're content with everything we get
    }

    // use native windowing system to position new frames
    this.setLocationByPlatform(true);
    // set preferred size
    this.setPreferredSize(_preferredSize);
    // set handler for closing operations
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.addWindowListener(new Terminator());

    // create content panel and add it to the frame
    JPanel contentPane = new JPanel(new BorderLayout());
    this.setContentPane(contentPane);

    // create menu bar
    this.newMainMenuBar();
    JToolBar toolBar = newMainToolBar();
    if (toolBar != null) {
      this.add(toolBar, BorderLayout.NORTH);
    }
    // add statusline
    _statusLine = new JLabel();
    contentPane.add(_statusLine, BorderLayout.SOUTH);
    clearStatusLine();

    // add this frame to the list of open frames
    MainFrame._openFrames.add(this);

    contentPane.add(newContentPane(), BorderLayout.CENTER);

    // display the frame
    this.pack();
    this.setVisible(true);
  }

  public boolean openFile(File toRead) {
    Object fileContent = readFileContent(toRead);
    if (fileContent != null) {
      ModelAdapter adapt = ModelAdapterFactory.getAdapter(fileContent);
      if (adapt != null) {
        setModel(fileContent, adapt);
      } else {
        setModel(fileContent);
      }
      return true;
    }
    else {
      errorDialog("File content of " + toRead + " could not be read");
    }
    return false;
  }

  protected FileNameExtensionFilter getFileFilter() {
    return new FileNameExtensionFilter("txt/xml files only", "txt", "xml");
  }

  protected void openFileDialog() {
    // create file chooser for txt files
    JFileChooser fc = new JFileChooser();
    FileNameExtensionFilter fexf = getFileFilter();
    if (fexf != null) {
      fc.addChoosableFileFilter(fexf);
    }
    fc.setCurrentDirectory(_currentDir);
    int returnVal = -1;
    boolean success = false;
    do {
      returnVal = fc.showOpenDialog(MainFrame.this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        // update current directory
        _currentDir = fc.getSelectedFile().getParentFile();
        // get the object read from this file
        File toRead = fc.getSelectedFile();
        success = openFile(toRead);
      }
    } while (! success && returnVal != JFileChooser.CANCEL_OPTION);
  }


  public void setModel(Object model, ModelAdapter adapt) {
    this._contentArea.setModel(model, adapt);
  }

  public void setModel(Object model) {
    if (_contentArea.getAdapter() != null) {
      _contentArea.setModel(model);
    } else {
      _contentArea.setModel(model, ModelAdapterFactory.getAdapter(model));
    }
  }

  public static Document readXmlFile(File xmlFile) {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(xmlFile);
      doc.getDocumentElement().normalize();
      return doc;
    }
    catch (ParserConfigurationException e2) {
      e2.printStackTrace();
    }
    catch (SAXException e1) {
      e1.printStackTrace();
    }
    catch (IOException e1) {
      e1.printStackTrace();
    }
    return null;
  }

  public Object readFileContent(File fileToOpen) {
    // add some smart code to assess the file type and call the right `open'
    // method
    if (! fileToOpen.exists()) {
      errorDialog("No such File: " + fileToOpen);
      return null;
    }
    if (fileToOpen.getName().endsWith(".xml")) {
      return readXmlFile(fileToOpen);
    }
    return null;
  }

  protected void errorDialog(String string) {
    JOptionPane.showMessageDialog(this, string, "Error",
        JOptionPane.ERROR_MESSAGE);
  }

  protected void warningDialog(String string) {
    JOptionPane.showMessageDialog(this, string, "Error",
        JOptionPane.WARNING_MESSAGE);
  }

  protected void reportProblem(String msg) {
    warningDialog(msg);
  }

  public static void main(String args[]) {
    DOMAdapter.init();
    CollectionsAdapter.init();
    @SuppressWarnings("unused")
    MainFrame mf = new MainFrame("Main Window");
  }
}
