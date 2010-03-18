/*
 * 
 */
package de.dfki.lt.loot.gui.adapters;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.dfki.lt.loot.Pair;

// TODO: Auto-generated Javadoc
/**
 * The Class CollectionsAdapter.
 */
public class CollectionsAdapter extends ModelAdapter {

  /** The _current list iterator. */
  Iterator _currentListIterator;

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.adapters.ModelAdapter#facets(java.lang.Object)
   */
  @Override
  public int facets(Object model) {
    if (model instanceof Map) {
      return ModelAdapter.MAP;
    }
    if (model instanceof List) {
      _currentListIterator = null;
      return (((List)model).isEmpty() ? ModelAdapter.NULL : ModelAdapter.CONS);
    }
    if (model instanceof Iterator) {
      return (((Iterator)model).hasNext()
          ? ModelAdapter.NULL
          : ModelAdapter.CONS);
    }
    return ModelAdapter.ATOM;
  }

  /* -- ConsFacet -- */

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.adapters.ModelAdapter#getFirst(java.lang.Object)
   */
  @Override
  public Object getFirst(Object model){
    if (_currentListIterator == null) {
      _currentListIterator = ((List)model).iterator();
    }
    return (_currentListIterator.hasNext()
        ? _currentListIterator.next()
        : null);  // null should never have to be returned
  };

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.adapters.ModelAdapter#getRest(java.lang.Object)
   */
  @Override
  public Object getRest(Object model){
    return _currentListIterator;
  };

  /* -- MapFacet -- */
  /**
   * The Class CollectionsMapAdapterIterator.
   */
  private class CollectionsMapAdapterIterator implements MapAdapterIterator {

    /** The _entry iterator. */
    private Iterator _entryIterator;

    /**
     * Instantiates a new collections map adapter iterator.
     * 
     * @param map the map
     */
    CollectionsMapAdapterIterator(Map map) {
      _entryIterator = map.entrySet().iterator();
    }

    /* (non-Javadoc)
     * @see de.dfki.lt.loot.gui.adapters.MapAdapterIterator#hasNext()
     */
    @Override
    public boolean hasNext() {
      return _entryIterator.hasNext();
    }

    /* (non-Javadoc)
     * @see de.dfki.lt.loot.gui.adapters.MapAdapterIterator#next()
     */
    @Override
    public Pair<String, Object> next() {
      Map.Entry entry = (Map.Entry) _entryIterator.next();
      return new Pair<String, Object>(
          entry.getKey().toString(),
          entry.getValue());
    }
  }

  /* (non-Javadoc)
   * @see de.dfki.lt.loot.gui.adapters.ModelAdapter#mapIterator(java.lang.Object)
   */
  @Override
  public MapAdapterIterator mapIterator(Object model){
    return new CollectionsMapAdapterIterator((Map)model);
  };

}
