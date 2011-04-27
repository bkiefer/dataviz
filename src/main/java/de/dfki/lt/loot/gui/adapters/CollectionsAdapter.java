package de.dfki.lt.loot.gui.adapters;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import de.dfki.lt.loot.Pair;

@SuppressWarnings("unchecked")
public class CollectionsAdapter extends ModelAdapter {

  public static void init() {
    ModelAdapterFactory.register(List.class, CollectionsAdapter.class);
    ModelAdapterFactory.register(Map.class, CollectionsAdapter.class);
    ModelAdapterFactory.register(Iterator.class, CollectionsAdapter.class);
  }

  // Has to be a stack of iterators to handle nested lists.
  Stack<Iterator> _activeIterators = new Stack<Iterator>();

  @Override
  public int facets(Object model) {
    if (model instanceof Map) {
      return MAP;
    }
    if (model instanceof List) {
      return (((List) model).isEmpty() ? NULL : CONS);
    }
    if (model instanceof Iterator) {
      return (((Iterator)model).hasNext() ? CONS : NULL);
    }
    return ATOM;
  }

  /* -- ConsFacet -- */

  @Override
  public Object getFirst(Object model) {
    if (model instanceof List)
      _activeIterators.push(((List) model).iterator());
    if (model instanceof Iterator &&
        (_activeIterators.isEmpty() || _activeIterators.peek() != model))
      _activeIterators.push((Iterator) model);

    assert(! _activeIterators.isEmpty() && _activeIterators.peek().hasNext());
    return _activeIterators.peek().next();
  };

  @Override
  public Object getRest(Object model) {
    assert(! _activeIterators.isEmpty() &&
        ((model instanceof List) || model == _activeIterators.peek()));
    Iterator result = _activeIterators.peek();
    if (!result.hasNext())
      _activeIterators.pop();
    return result;
  };

  /* -- MapFacet -- */
  protected class CollectionsMapAdapterIterator implements MapAdapterIterator {

    private Iterator _entryIterator;

    public CollectionsMapAdapterIterator(Map map) {
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
