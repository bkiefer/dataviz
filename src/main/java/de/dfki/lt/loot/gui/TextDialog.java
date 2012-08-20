/*
 */

package de.dfki.lt.loot.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TextDialog extends JDialog implements ActionListener,
    PropertyChangeListener {
  private String typedText = null;
  private String action = null;

  private JFrame parent;

  private JTextField textField;

  private JOptionPane optionPane;

  protected String[] btnStrings = { "OK", "Cancel" };
  protected String[] actionStrings = { "OK", "Cancel" };

  /**
   * Returns null if the window was closed; otherwise, returns the string as the
   * user entered it.
   */
  public String getText() {
    return typedText;
  }

  /**
   * Returns null if the window was closed; otherwise, returns the string as the
   * user entered it.
   */
  public void setText(String defaultText) {
    typedText = defaultText;
    textField.setText(typedText);
  }

  /**
   * Returns null if the window was closed; otherwise, returns the action string
   * determined by the button that was pressed.
   */
  public String getAction() {
    return action;
  }

  public void openDialog() {
    setLocationRelativeTo(parent);
    textField.selectAll();
    setVisible(true);
  }

  private int index(Object[] array, Object value) {
    for (int i = 0; i < array.length; ++i) {
      if (array[i].equals(value))
        return i;
    }
    return -1;
  }

  /** Creates the reusable dialog. */
  public TextDialog(JFrame aFrame, String message) {
    super(aFrame, true);
    parent = aFrame;

    setTitle("User question query dialog");

    textField = new JTextField(10);

    // Create an array of the text and components to be displayed.
    Object[] array = { message, textField };

    // Create the JOptionPane.
    optionPane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE,
        JOptionPane.YES_NO_OPTION, null, btnStrings, btnStrings[0]);

    // Make this dialog display it.
    setContentPane(optionPane);

    // Handle window closing correctly.
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent we) {
        /*
         * Instead of directly closing the window, we're going to change the
         * JOptionPane's value property.
         */
        optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
      }
    });

    // Ensure the text field always gets the first focus.
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent ce) {
        textField.requestFocusInWindow();
      }
    });

    // Register an event handler that puts the text into the option pane.
    textField.addActionListener(this);

    // Register an event handler that reacts to option pane state changes.
    optionPane.addPropertyChangeListener(this);
  }

  /** This method handles events for the text field. */
  public void actionPerformed(ActionEvent e) {
    optionPane.setValue(btnStrings[0]);
  }

  /** This method reacts to state changes in the option pane. */
  public void propertyChange(PropertyChangeEvent e) {
    String prop = e.getPropertyName();

    if (isVisible()
        && (e.getSource() == optionPane)
        && (JOptionPane.VALUE_PROPERTY.equals(prop)
            || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
      Object value = optionPane.getValue();

      if (value == JOptionPane.UNINITIALIZED_VALUE) {
        // ignore reset
        return;
      }

      // Reset the JOptionPane's value.
      // If you don't do this, then if the user
      // presses the same button next time, no
      // property change event will be fired.
      optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);


      int index = index(btnStrings, value);
      if (index >= 0) {
        typedText = textField.getText();
        action = actionStrings[index];
      } else { // user closed dialog or clicked cancel
        typedText = null;
        action = null;
      }
      clearAndHide();
    }
  }

  /** This method clears the dialog and hides it. */
  public void clearAndHide() {
    textField.setText(null);
    setVisible(false);
  }
}
