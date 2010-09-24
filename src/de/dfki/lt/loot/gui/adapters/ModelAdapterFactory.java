package de.dfki.lt.loot.gui.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class ModelAdapterFactory {

  /** Register built-in adapters */
  static {
    init();
    DOMAdapter.init();
    CollectionsAdapter.init();
  }

  @SuppressWarnings("unchecked")
  private static void init() {
    logger = Logger.getLogger(ModelAdapterFactory.class);
    _prototypes = new HashMap<Class, Class>();
    _classes = new ArrayList<Class>();
  }

  private static Logger logger;

  @SuppressWarnings("unchecked")
  private static HashMap<Class, Class> _prototypes;

  @SuppressWarnings("unchecked")
  private static ArrayList<Class> _classes;


  @SuppressWarnings("unchecked")
  private static class ClassComparator implements Comparator<Class> {;
    @Override
    public int compare(Class o1, Class o2) {
      if (o1 == o2) return 0;
      Class curr = o1.getSuperclass();
      while (curr != null) {
        if (curr == o2) return -1;
        curr = curr.getSuperclass();
      }
      curr = o2.getSuperclass();
      while (curr != null) {
        if (curr == o1) return 1;
        curr = curr.getSuperclass();
      }
      // incomparable
      return 0;
    }
  }


  @SuppressWarnings("unchecked")
  public static void register(Class objClass, Class adapterClass) {
    _classes.add(objClass);
    Collections.sort(_classes, new ClassComparator());
    _prototypes.put(objClass, adapterClass);
  }

  @SuppressWarnings("unchecked")
  public static ModelAdapter getAdapter(Object o) {
    if (o == null) {
      return new ModelAdapter() {

        @Override
        public int facets(Object model) {
          // TODO Auto-generated method stub
          return ModelAdapter.NULL;
        }
      };
    }

    for (Class c : _classes) {
      if (c.isInstance(o)) {
        Class ModelAdapterClass = _prototypes.get(c);
        assert(ModelAdapterClass != null);
        try {
          return (ModelAdapter) ModelAdapterClass.newInstance();
        }
        catch (InstantiationException iex) {
          logger.warn("Could not create Instance for " + ModelAdapterClass);
        }
        catch (IllegalAccessException iaex) {
          logger.warn("Could not create Instance for " + ModelAdapterClass);
        }
      }
    }
    return null;
  }
}
