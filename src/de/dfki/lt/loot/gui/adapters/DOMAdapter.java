package de.dfki.lt.loot.gui.adapters;

import java.util.Iterator;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import de.dfki.lt.loot.Pair;

public class DOMAdapter extends ModelAdapter {

  public static void init() {
    ModelAdapterFactory.register(Document.class, DOMAdapter.class);
  }

  @Override
  public int facets(Object model) {
    if (model instanceof Document) {
      return ModelAdapter.TREE;
    }
    if (model instanceof String) {
      return ModelAdapter.ATOM;
    }
    if (model instanceof MapMarker) {
      return ModelAdapter.MAP;
    }
    return ModelAdapter.TREE | ModelAdapter.MAP;
  }

  @Override
  public String getAttribute(Object model, String name) {
    if (name == "type") {
      if (model instanceof Element)
        return ((Element) model).getNodeName();
      if (model instanceof MapMarker)
        return ((MapMarker) model)._root.getNodeName();
    }
    return null;
  }

  /* -- MapFacet -- */

  private class AttributeIterator implements MapAdapterIterator {
    private NamedNodeMap _map;
    private int _current;

    public AttributeIterator(Element elt) {
      _map = elt.getAttributes();
      _current = 0;
    }

    @Override
    public boolean hasNext() {
      return _current < _map.getLength();
    }

    @Override
    public Pair<String, Object> next() {
      Attr node = (Attr) _map.item(_current);
      ++_current;
      return new Pair<String, Object>(node.getName(), node.getValue());
    }

  }

  @Override
  public MapAdapterIterator mapIterator(Object model){
    if (model instanceof MapMarker) {
      return new AttributeIterator(((MapMarker) model)._root);
    }
    return null;
  };

  /* -- TreeFacet -- */

  private class MapMarker {
    Element _root;
    MapMarker(Element root) { _root = root; }
  }

  @Override
  public Object getRootNode(Object model){
    if (model instanceof Document) {
      return new MapMarker(((Document) model).getDocumentElement());
    }
    if (model instanceof Element) {
      return new MapMarker((Element) model);
    }
    return null;
  };

  @SuppressWarnings("unchecked")
  private class NodeListIterable implements Iterable {
    private NodeList _list;
    private int _current;

    private void advance() {
      while (! (_list.item(_current) instanceof Element)
          && _current < _list.getLength()) {
        ++_current;
      }
    }

    public NodeListIterable(NodeList list) {
      _list = list;
      _current = 0;
      advance();
    }

    @Override
    public Iterator iterator() {
      return new Iterator() {
        public boolean hasNext() {
          return _current < _list.getLength();
        }
        public Object next() {
          Object result = _list.item(_current);
          ++_current;
          advance();
          return result;
        }
        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public Iterable getTreeDaughters(Object model){
    if (model instanceof Document) {
      model = ((Document) model).getDocumentElement();
    }
    NodeList list = ((Element) model).getChildNodes();
    return new NodeListIterable(list);
  };
}
