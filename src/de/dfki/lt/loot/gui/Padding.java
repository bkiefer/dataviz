/*
 * 
 */
package de.dfki.lt.loot.gui;

// TODO: Auto-generated Javadoc
/**
 * The Class Padding.
 */
public class Padding {
  
  /** The padding. */
  public int padding;
  
  /** The border. */
  public int border;
  
  /** The margin. */
  public int margin;

  /**
   * Instantiates a new padding.
   */
  public Padding() {
    this.padding = this.border = this.margin = 0;
  }

  /**
   * Instantiates a new padding.
   * 
   * @param marg the marg
   * @param bord the bord
   * @param pad the pad
   */
  public Padding(int marg, int bord, int pad) {
    this.padding = pad;
    this.border = bord;
    this.margin = marg;
  }

  /**
   * Gets the offset.
   * 
   * @return the offset
   */
  public int getOffset() { return this.padding + this.border + this.margin; }
}
