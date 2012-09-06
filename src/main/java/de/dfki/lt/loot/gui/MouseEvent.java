package de.dfki.lt.loot.gui;

public class MouseEvent {
  public MouseEvent(DrawingPanel drawingPanel, java.awt.event.MouseEvent e) {
    panel = drawingPanel;
    awtEvent = e;
  }

  public java.awt.event.MouseEvent awtEvent;

  public DrawingPanel panel;
  
  
}
