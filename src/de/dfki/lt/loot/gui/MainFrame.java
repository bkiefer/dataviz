package de.dfki.lt.loot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

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

  public interface CloseAllListener {
    public abstract void allClosed();
  }

  private class ReleaseSemaphoreOnCloseAll implements CloseAllListener {
    Semaphore mySem;

    public ReleaseSemaphoreOnCloseAll(Semaphore sem) {
      mySem = sem;
    }

    public void allClosed() {
      mySem.release();
    }
  }

  /** <code>Terminator</code> defines action to be done when closing a frame.
   */
  private class Terminator extends WindowAdapter {
    /** This creates a new instance of <code>Terminator</code>. */
    public Terminator() {
      super();
    }

    /**
     * This overrides the <code>windowClosing</code> method of the super class.
     *
     * @param we a <code>WindowEvent</code>
     * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
     */
    @Override
    public void windowClosing(WindowEvent we) {
      we.getWindow().dispose();
    }

    /**
     * This overrides the <code>windowClosed</code> method of the super class.
     *
     * @param we a <code>WindowEvent</code>
     * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
     */
    @Override
    public void windowClosed(WindowEvent we) {

      // find the frame for this window and remove it form the list;
      // make sure this is thread safe
      synchronized (MainFrame.openFrames) {
        Window win = we.getWindow();
        for (MainFrame oneFrame : MainFrame.openFrames) {
          if (oneFrame.equals(win)) {
            MainFrame.openFrames.remove(oneFrame);
            break;
          }
        }
      }
      // if this was the last frame, completely shut down the application
      if (MainFrame.openFrames.isEmpty()) {
        if (clAll == null)
          System.exit(0);
        clAll.allClosed();
      }
    }
  }

  /** This contains the currently open frames. */
  private static List<MainFrame> openFrames = new ArrayList<MainFrame>();

  /** This Listener is called when the last Frame has been closed */
  private static CloseAllListener clAll = null;

  /** This contains the content area. */
  private DrawingPanel contentArea;

  /** This contains the current directory. */
  private File currentDir;

  FontChooser f;

  private void init(DrawingPanel dp) {
    // create content panel and add it to the frame
    JPanel contentPane = new JPanel(new BorderLayout());
    contentPane.setLayout(new BorderLayout());
    this.setContentPane(contentPane);

    // create menu bar
    this.initMenuBar();

    // create scrollable display area
    this.contentArea = dp;
    this.contentArea.setBackground(Color.white);

    JScrollPane theContentArea = new JScrollPane(this.contentArea);
    contentPane.add(theContentArea, BorderLayout.CENTER);

    // add this frame to the list of open frames
    MainFrame.openFrames.add(this);

    // use native windowing system to position new frames
    this.setLocationByPlatform(true);

    // set handler for closing operations
    this.addWindowListener(new Terminator());

    // display the frame
    this.pack();
    int unitIncrement = dp.getDefaultTextHeight();
    theContentArea.getHorizontalScrollBar().setUnitIncrement(unitIncrement);
    theContentArea.getVerticalScrollBar().setUnitIncrement(unitIncrement);
    this.setVisible(true);
  }

  MainFrame(String title, File currDir, DrawingPanel dp) {
    super(title);
    this.currentDir = currDir;
    this.init(dp);
  }

  public MainFrame(String title, Object tfs) {
    super(title);
    if (tfs != null) {
      this.setModel(tfs);
    }
  }

  public <MODEL> MainFrame(String title, DrawingPanel dp) {
    this(title, null, dp);
  }

  public void releaseOnAllClosed(Semaphore sem) {
    clAll = new ReleaseSemaphoreOnCloseAll(sem);
  }

  /** a method to close a frame from within the program */
  public void close() {
    this.dispatchEvent(
        new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

  /**
   * This initializes the menu bar.
   */
  private void initMenuBar() {

    // create the menu bar
    JMenuBar menuBar = new JMenuBar();
    menuBar.setOpaque(true);
    menuBar.setPreferredSize(new Dimension(400, 20));

    // create the 'File' menu
    JMenu menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_F);
    menuBar.add(menu);

    // create menu item 'New'
    JMenuItem newMenuItem = new JMenuItem("New", KeyEvent.VK_N);
    newMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        new MainFrame("New Window", null);
      }
    });
    menu.add(newMenuItem);

    // create menu item 'Open'
    JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
    openMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        // create file chooser for txt files
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(
          new FileNameExtensionFilter("txt/xml files only", "txt", "xml"));
        fc.setCurrentDirectory(MainFrame.this.currentDir);
        int returnVal = fc.showOpenDialog(MainFrame.this);
        Object tfs = null;
        do {
          if (returnVal == JFileChooser.APPROVE_OPTION) {
            // update current directory
            MainFrame.this.currentDir = fc.getSelectedFile().getParentFile();
            // parse the TFS
            tfs = MainFrame.this.openFile(fc.getSelectedFile());
            if (tfs != null) {
              MainFrame.this.setModel(tfs);
            }
          }
        }
        while (tfs == null && returnVal != JFileChooser.CANCEL_OPTION);
      }
    });
    menu.add(openMenuItem);

    if (currentDir == null) {
      newMenuItem.setEnabled(false);
      openMenuItem.setEnabled(false);
    }

    // create menu item 'Close'
    JMenuItem closeMenuItem = new JMenuItem("Close", KeyEvent.VK_C);
    closeMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        MainFrame.this.dispose();
      }
    });
    menu.add(closeMenuItem);

    // create menu item 'Close All'
    JMenuItem exitAllMenuItem = new JMenuItem("Close All", KeyEvent.VK_A);
    exitAllMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        // create a close event for each frame
        synchronized (MainFrame.openFrames) {
          for (MainFrame oneFrame : MainFrame.openFrames) {
            oneFrame.close();
          }
        }
      }
    });
    menu.add(exitAllMenuItem);

    // create menu item 'FontSelect'
    JMenuItem fontMenuItem = new JMenuItem("Select Font");
    fontMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        MainFrame.this.f = new FontChooser();
      }
    });
    menu.add(fontMenuItem);

    // add menu bar to main frame
    this.setJMenuBar(menuBar);
  }


  public void setModel(Object model) {
    (this.contentArea).setModel(model);

    this.contentArea.repaint();
  }

  public Object openFile(File fileToOpen) {
    // add some smart code to assess the file type and call the right `open'
    // method
    return null;
  }
}
