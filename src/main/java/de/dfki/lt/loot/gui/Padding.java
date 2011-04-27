package de.dfki.lt.loot.gui;

public class Padding {
  public int padding;
  public int border;
  public int margin;

  public Padding() {
    this.padding = this.border = this.margin = 0;
  }

  public Padding(int marg, int bord, int pad) {
    this.padding = pad;
    this.border = bord;
    this.margin = marg;
  }

  public int getOffset() { return this.padding + this.border + this.margin; }
}
