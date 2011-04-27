package de.dfki.lt.loot.gui.util;

import java.util.Deque;
import java.util.LinkedList;

public class LimitedDeque<E> extends LinkedList<E> implements Deque<E> {

  private static final long serialVersionUID = -9162952051346693993L;

  private int _maxSize;

  public LimitedDeque(int maxSize) {
    setMaxSize(maxSize);
  }

  public LimitedDeque() {
    this(Integer.MAX_VALUE);
  }

  public void setMaxSize(int maxSize) {
    _maxSize = maxSize;
  }

  public int getMaxSize() {
    return _maxSize;
  }

  @Override
  public boolean offer(E elt) {
    if (size() >= _maxSize) return false;
    return super.offer(elt);
  }

}
