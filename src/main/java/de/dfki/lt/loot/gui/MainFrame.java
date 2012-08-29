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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.zip.GZIPInputStream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.dfki.lt.loot.gui.util.FileAssociation;
import de.dfki.lt.loot.gui.util.FileProcessor;
import de.dfki.lt.loot.gui.util.GenericFileProcessor;
import de.dfki.lt.loot.gui.util.HistoryView;
import de.dfki.lt.loot.gui.util.IniFileReader;
import de.dfki.lt.loot.gui.util.IniFileWriter;
import de.dfki.lt.loot.gui.util.InputHistory;
import de.dfki.lt.loot.gui.util.ObjectHandler;
import de.dfki.lt.loot.gui.util.ProgressListener;

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
  
  private static final Logger logger = Logger.getLogger(MainFrame.class);

  /** This contains the currently open frames. */
  protected static List<MainFrame> _openFrames = new ArrayList<MainFrame>();

  /** These listeners are called when the last frame has been closed */
  protected static List<CloseAllListener> clAll =
    new LinkedList<CloseAllListener>();

  /** This contains the current directory. */
  protected File _currentDir;

  /* *************************************************************************
   * Button and Menu specifications
   * 
   * Implemented as methods to avoid the bogus fields in derived classes
   * *************************************************************************/

  private static String iconUrl(String name) {
    String imgLocation = _iconPath + "24x24/actions/" + name + ".png";
    // URL imageURL = MainFrame.class.getResource(imgLocation);
    if (new File(imgLocation).exists()) {
      return imgLocation;
    }
    return null;
  }
  
  public static class RunnableAction extends AbstractAction {
    private Runnable _r;
    
    public RunnableAction(String title, String iconName, String toolTipText,
        String altText, Object key, Runnable r) {
      super(title);
      if (iconName != null) {
        String iconFileName = iconUrl(iconName);
        if (iconFileName != null) {
          putValue(SMALL_ICON, new ImageIcon(iconFileName, altText));
        }
      }
      putValue(SHORT_DESCRIPTION, toolTipText);
      if (key != null)
        putValue(ACCELERATOR_KEY, key);
      _r = r;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
      _r.run();
    }
  }
  
  protected class MyMenu extends JMenu {
    public MyMenu(String title, Object key, Object ... menuItems) {
      super(title);
      if (key != null)
        setMnemonic((Integer)key);
      
      for (Object spec : menuItems) {
        if (spec == null) {
          addSeparator();
        } else if (spec instanceof RunnableAction) {
          JMenuItem item = add((Action) spec);
          item.setIcon(null);  // currently, no icons for menus
        } else if (spec instanceof JMenuItem) {
          add((JMenuItem) spec);
        }
      }
    }
  }
  
  protected RunnableAction newAction() {
    return new RunnableAction(
      "New", "window-new", "New Frame", "New Frame",
      KeyStroke.getKeyStroke(Character.valueOf('n'), InputEvent.ALT_DOWN_MASK),
      new Runnable() { public void run() { newFrame(); } });
  }
    
  protected RunnableAction closeAction() {
    return new RunnableAction(
      "Close", "window-close", "Close", "Close Window",
      KeyStroke.getKeyStroke(Character.valueOf('w'),
          InputEvent.META_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
          new Runnable() { public void run() { close(); } });
  }
    
  protected RunnableAction openAction() {
    return new RunnableAction(
      "Open", "document-open", "Open", "Open File",
      KeyStroke.getKeyStroke((char)KeyEvent.VK_O),
      new Runnable() { public void run() { openFileDialog(_fileProcessor); } });
  }
  
  protected RunnableAction chooseFontAction() {
    return new RunnableAction(
      "Select Font", "go-next", "Select Font", "Select Font", null,
      new Runnable() { public void run() { chooseFont(); } });
  }
  
  protected RunnableAction exitAction() {
    return new RunnableAction(
      "Quit", "application-exit", "Quit", "Quit",
      KeyStroke.getKeyStroke(Character.valueOf('a'), InputEvent.ALT_DOWN_MASK),
      new Runnable() { public void run() { closeAll(); } });
  }
  
  protected RunnableAction loadHistoryAction() {
    return new RunnableAction(
      "Load History", null, "Load History", "Load History", null,
      new Runnable() { public void run() { loadHistory(); } });
  }
  
  protected RunnableAction saveHistoryAction() {
    return new RunnableAction(
      "Save History", null, "Save History", "Save History", null,
      new Runnable() { public void run() { saveHistory(); } });
  }
  
  protected RunnableAction clearHistoryAction() {
    return new RunnableAction(
      "Clear History", null, "Clear History", "Clear History", null,
      new Runnable() { public void run() { _history.clear(); } });
  }
  
  protected JMenu recentFiles() {
    return new HistoryView("Recent Files", _recentFiles, null,
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            String fileName = e.getActionCommand();
            try {
              _fileProcessor.processFile(new File(fileName));
            } catch (IOException ex) {
              errorDialog("Problem during file processing:\n" + ex);
            }
          }
        }).getMenu();
  }
  
  protected RunnableAction[] actionSpecs() {
    RunnableAction[] results = {
        newAction(), openAction(), chooseFontAction(), closeAction(),
        exitAction(),
    };
    return results;
  }

  protected MyMenu[] menuSpecs(RunnableAction[] toolBarAction) {
    Object[] fileSpecs = {
    toolBarAction[0],
    toolBarAction[1],
    toolBarAction[3],
    null,
    recentFiles(),
    null,
    toolBarAction[2],
    // loadHistoryAction, saveHistoryAction, clearHistoryAction, separator
    toolBarAction[4],
    };
    MyMenu[] menuBarSpecs = {
        new MyMenu("File", KeyEvent.VK_F, fileSpecs),
    };
    return menuBarSpecs;
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

  /** Name of tool bar containing the action buttons */
  protected String _toolBarName = "Main Tools";

  /** Action Buttons */
  protected ArrayList<JButton> _actionButtons = new ArrayList<JButton>();

  protected static String _defaultFont = "Monospace";
  protected static String _iconPath = null;

  protected Font _textFont = new Font(_defaultFont, 0, 12);

  protected Dimension _preferredSize = new Dimension(600, 600);

  /** displays error and status information */
  protected JLabel _statusLine;

  /** To use if needed to display progress information */
  protected JProgressBar _progressBar;

  /** This contains the content area. */
  protected DrawingPanel _contentArea;
  
  /** A generic file handler */
  protected GenericFileProcessor _fileProcessor;

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
    this(title, new File("."), new DrawingPanel(null, null));
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

  protected void errorDialog(String string) {
    JOptionPane.showMessageDialog(this, string, "Error",
        JOptionPane.ERROR_MESSAGE);
  }

  protected void warningDialog(String string) {
    JOptionPane.showMessageDialog(this, string, "Warning",
        JOptionPane.WARNING_MESSAGE);
  }

  protected void reportProblem(String msg) {
    warningDialog(msg);
  }

  /* **********************************************************************
   * Handling of hideable progress bar (in the bottom (status) part)
   * ********************************************************************** */

  /* Helper method for show/hide */
  private void handleProgressBar(Dimension dim, boolean show) {
    _progressBar.setPreferredSize(dim);
    _progressBar.setMaximumSize(dim);
    _progressBar.setVisible(show);
  }

  /** Show the progress bar */
  public void showProgressBar() {
    handleProgressBar(
        new Dimension(100, (int)(_statusLine.getHeight() * .8)),
        true);
  }

  /** Hide the progress bar */
  public void hideProgressBar() {
    handleProgressBar(new Dimension(0, 0), false);
  }

  /** return a new Listener for the progress bar.
   *  The setMaximum method is for initialization, the progress method is the
   *  `listen' method, so to say.
   */
  public ProgressListener getProgressBarListener() {
    return new ProgressListener() {
      public void setMaximum(int max) {
        _progressBar.setMaximum(max);
        _progressBar.setValue(0);
      }

      public void progress(int value) {
        _progressBar.setValue(value);
      }
    };
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
        logger.warn("Error reading preferences file: "
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
        logger.warn("Error writing preferences file: "
            + ioex.getLocalizedMessage());
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

  /** This returns a newly created tool bar.
   *  @param specs        the Action specifications for this tool bar
   *  @param toolBarName  the name of the tool bar
   *  @param buttons      [IN/OUT] the list of buttons created for this tool
   *                      bar, to be able to access them directly.
   *  TODO this should be removed soon, since it has been replaced by the method
   *  using actions
   */
  public JToolBar newToolBar0(Object [][] specs, String toolBarName,
    List<JButton> buttons) {
    JToolBar toolBar = new JToolBar(toolBarName);
    if (specs == null) return null;
    for (Object[] spec : specs) {
      if (spec[1] != null) {
        final Runnable r = (Runnable) spec[5];
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
        buttons.add(newButton);
        toolBar.add(newButton);
      }
    }
    return toolBar;
  }

  /** This returns a newly created tool bar.
   *  @param specs        the Action specifications for this tool bar
   *  @param toolBarName  the name of the tool bar
   *  @param buttons      [IN/OUT] the list of buttons created for this tool
   *                      bar, to be able to access them directly.
   *                      TODO: this parameter is not needed anymore, since
   *                      the state changes in the buttons should be done via
   *                      the Actions
   */
  public JToolBar newToolBar(RunnableAction[] specs, String toolBarName,
      List<JButton> buttons) {
    JToolBar toolBar = new JToolBar(toolBarName);
    for (RunnableAction spec : specs) {
      JButton newButton = new JButton(spec);
      if (newButton.getIcon() != null) {
        newButton.setText(null);
      }
      buttons.add(newButton);
      toolBar.add(newButton);
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


  /** This initialises the menu bar. */
  protected void newMainMenuBar(RunnableAction[] actions) {
    // create the menu bar
    JMenuBar menuBar = new JMenuBar();
    menuBar.setOpaque(true);
    menuBar.setPreferredSize(new Dimension(400, 20));

    for (MyMenu menu : menuSpecs(actions)) {
      // create the 'File' menu
      menuBar.add(menu);
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

    // add file associations known to this frame class
    addAssociations();
    
    // create content panel and add it to the frame
    JPanel contentPane = new JPanel(new BorderLayout());
    this.setContentPane(contentPane);

    // create menu bar
    if (_recentFiles == null) {
      _recentFiles = new InputHistory(5);
    }
    RunnableAction[] actions = actionSpecs();
    this.newMainMenuBar(actions);
    JToolBar toolBar = newToolBar(actions, _toolBarName, _actionButtons);
    if (toolBar != null) {
      this.add(toolBar, BorderLayout.NORTH);
    }

    // add statusline and optional progress bar
    _statusLine = new JLabel(" ");
    _progressBar = new JProgressBar(0, 0);
    JPanel bottomLine = new JPanel();
    bottomLine.setLayout(new BoxLayout(bottomLine, BoxLayout.LINE_AXIS));
    bottomLine.add(_statusLine);
    bottomLine.add(Box.createHorizontalGlue());
    bottomLine.add(_progressBar);
    _progressBar.setAlignmentY(CENTER_ALIGNMENT);
    _progressBar.setVisible(false);

    contentPane.add(bottomLine, BorderLayout.SOUTH);
    clearStatusLine();

    // add this frame to the list of open frames
    MainFrame._openFrames.add(this);

    contentPane.add(newContentPane(), BorderLayout.CENTER);

    // display the frame
    this.pack();
    this.setVisible(true);
  }

  public void addFileAssociation(ObjectHandler h, String ... extensions) {
    _fileProcessor.addFileAssociation(h, extensions);
  }

  protected void addAssociations() {
    addFileAssociation(new ObjectHandler() {
      @Override
      public boolean process(InputStream in) throws IOException {
        Document d = MainFrame.readXmlFile(in);
        try {
          MainFrame.this.setModel(d);
        } catch (Exception ex) {
          return false;
        }
        return true;
      }
      
      public String toString() { return "Display XML files as tree"; }
    }, "xml");
  }

  protected void openFileDialog(FileProcessor proc) {
    // create file chooser for txt files
    JFileChooser fc = new JFileChooser();
    FileFilter fexf = proc.getFileFilter();
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
        if (! toRead.exists()) {
          errorDialog("No such File: " + toRead);
          success = false;
        } else {
          try {
            success = proc.processFile(toRead);
          } catch (IOException ex) {
            errorDialog("File content of " + toRead + " could not be read:\n"
                + ((ex.getCause() != null)
                    ? ex.getCause().toString() : ex.toString()));
          }
        }
      }
    } while (! success && returnVal != JFileChooser.CANCEL_OPTION);
  }

  public void setModel(Object model) {
    _contentArea.setModel(model);
  }

  public static Document readXmlFile(InputStream xmlFile) {
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

  public static void main(String args[]) {
    @SuppressWarnings("unused")
    MainFrame mf = new MainFrame("Main Window");
  }
}
