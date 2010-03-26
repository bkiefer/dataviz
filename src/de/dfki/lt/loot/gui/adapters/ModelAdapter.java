

package de.dfki.lt.loot.gui.adapters;

import java.util.Iterator;

public abstract class ModelAdapter {

  public static final int NONE = 0,
  NULL = 1, ATOM = NULL << 1, CONS = ATOM << 1, TREE = CONS << 1,
  MAP = TREE << 1, MATRIX = MAP << 1, GRAPH = MATRIX << 1,
  ALL = (GRAPH << 1) -1 ;

  public void setTopModel(Object model) {}

  public abstract int facets(Object model);

  public String getAttribute(Object model, String name) {
    return null;
  }

  /* -- ConsFacet -- */

  public Object getFirst(Object model){
    throw new UnsupportedOperationException();
  };

  public Object getRest(Object model){
    throw new UnsupportedOperationException();
  };

  /* -- MapFacet -- */

  public MapAdapterIterator mapIterator(Object model){
    throw new UnsupportedOperationException();
  };

  /* -- TreeFacet -- */

  public Object getRootNode(Object model){
    throw new UnsupportedOperationException();
  };

  public Iterator getTreeDaughters(Object model){
    throw new UnsupportedOperationException();
  };

  /* -- Graph -- */

  /** Get all the vertices in this graph */
  public Iterator getVertices() {
    throw new UnsupportedOperationException();
  }

  /** Get the content of a vertex */
  public Object getNode() {
    throw new UnsupportedOperationException();
  }

  /** Get the list of edges out of a vertex. An edge is an object of its own,
   * but besides the target vertex, it can only contain attribute information.
   */
  public Iterator outEdges(Object vertex) {
    throw new UnsupportedOperationException();
  }

  /** The target vertex of an edge. May only be applied to edges */
  public Object target() {
    throw new UnsupportedOperationException();
  }

}
