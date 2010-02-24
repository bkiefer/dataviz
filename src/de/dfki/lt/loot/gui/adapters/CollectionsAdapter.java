package de.dfki.lt.loot.gui.adapters;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.dfki.lt.loot.Pair;

public class CollectionsAdapter extends ModelAdapter {

  Iterator _currentListIterator;

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

  @Override
  public Iterator getSubModels(Object model) {
    if (model instanceof Map) {
      return ((Map)model).values().iterator();
    }
    if (model instanceof List) {
      return ((List)model).iterator();
    }
    return null;
  }

  /* -- ConsFacet -- */

  @Override
  public Object getFirst(Object model){
    if (_currentListIterator == null) {
      _currentListIterator = ((List)model).iterator();
    }
    return (_currentListIterator.hasNext()
        ? _currentListIterator.next()
        : null);  // null should never have to be returned
  };

  @Override
  public Object getRest(Object model){
    return _currentListIterator;
  };

  /* -- MapFacet -- */
  private class CollectionsMapAdapterIterator implements MapAdapterIterator {

    private Iterator _entryIterator;

    CollectionsMapAdapterIterator(Map map) {
      _entryIterator = map.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
      return _entryIterator.hasNext();
    }

    @Override
    public Pair<String, Object> next() {
      Map.Entry entry = (Map.Entry) _entryIterator.next();
      return new Pair<String, Object>(
          entry.getKey().toString(),
          entry.getValue());
    }
  }

  @Override
  public MapAdapterIterator mapIterator(Object model){
    return new CollectionsMapAdapterIterator((Map)model);
  };

}
