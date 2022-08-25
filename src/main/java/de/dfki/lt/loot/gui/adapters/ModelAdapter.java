package de.dfki.lt.loot.gui.adapters;

public abstract class ModelAdapter {

  public static final int NONE = 0,
  NULL = 1, ATOM = NULL << 1, SYMBOL = ATOM << 1, CONS = SYMBOL << 1, TREE = CONS << 1,
  MAP = TREE << 1, MATRIX = MAP << 1, GRAPH = MATRIX << 1,
  ALL = (GRAPH << 1) -1 ;

  public void setTopModel(Object model) {}

  public abstract int facets(Object model);

  public String getAttribute(Object model, String name) {
    return null;
  }

  /* -- SymbolFacet -- */

  public String getString(Object model) {
    throw new UnsupportedOperationException();
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

  /** This method should return an object that is to be displayed as node
   *  content, contrasting the model's role as tree node with children.
   *  If the node content is, e.g., a feature structure, the returned object
   *  should fulfill the map facet. In general, it is not a good idea if the
   *  returned object fulfills the tree facet, because that can lead to infinite
   *  recursion.
   */
  public Object getNodeContent(Object model){
    throw new UnsupportedOperationException();
  };

  @SuppressWarnings("rawtypes")
  public Iterable getTreeDaughters(Object model){
    throw new UnsupportedOperationException();
  };

  /* -- Graph -- */

  /** Get all the vertices in this graph */
  @SuppressWarnings("rawtypes")
  public Iterable getVertices(Object model) {
    throw new UnsupportedOperationException();
  }

  /** Get the list of edges out of a vertex. An edge is an object of its own,
   * but besides the target vertex, it can only contain attribute information.
   */
  @SuppressWarnings("rawtypes")
  public Iterable outEdges(Object vertex) {
    throw new UnsupportedOperationException();
  }

  /** The target vertex of an edge. May only be applied to edges */
  public Object target(Object edge) {
    throw new UnsupportedOperationException();
  }
}
