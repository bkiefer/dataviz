package de.dfki.lt.loot.gui.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import de.dfki.lt.loot.gui.MouseListener;
import de.dfki.lt.loot.gui.ViewContext;
import de.dfki.lt.loot.gui.layouts.CompactLayout;
import de.dfki.lt.loot.gui.layouts.Layout;
import de.dfki.lt.loot.gui.nodes.GraphicalNode;

public class ModelAdapterFactory {

  /** Register built-in adapters */
  static {
    init();
    DOMAdapter.init();
    CollectionsAdapter.init();
    // register a default layout.
    registerLayout(Object.class, CompactLayout.class);
  }

  @SuppressWarnings("rawtypes")
  private static void init() {
    logger = Logger.getLogger(ModelAdapterFactory.class);
    _adapterPrototypes = new HashMap<Class, Class>();
    _layoutPrototypes = new HashMap<Class, Class>();
    _listenerPrototypes = new HashMap<Class, Class>();
    _classes = new ArrayList<Class>();
  }

  private static Logger logger;

  @SuppressWarnings("rawtypes")
  private static HashMap<Class, Class> _adapterPrototypes;
  @SuppressWarnings("rawtypes")
  private static HashMap<Class, Class> _layoutPrototypes;
  @SuppressWarnings("rawtypes")
  private static HashMap<Class, Class> _listenerPrototypes;

  @SuppressWarnings("rawtypes")
  private static List<Class> _classes;


  @SuppressWarnings("rawtypes")
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


  @SuppressWarnings("rawtypes")
  public static void registerLayout(Class objClass, Class layoutClass) {
    if (!_classes.contains(objClass)) {
      _classes.add(objClass);
      Collections.sort(_classes, new ClassComparator());
    }
    _layoutPrototypes.put(objClass, layoutClass);
  }

  @SuppressWarnings("rawtypes")
  public static void registerAdapter(Class objClass, Class adapterClass) {
    if (!_classes.contains(objClass)) {
      _classes.add(objClass);
      Collections.sort(_classes, new ClassComparator());
    }
    _adapterPrototypes.put(objClass, adapterClass);
  }

  @SuppressWarnings("rawtypes")
  public static void registerListener(Class objClass, Class listenerClass) {
    if (!_classes.contains(objClass)) {
      _classes.add(objClass);
      Collections.sort(_classes, new ClassComparator());
    }
    _listenerPrototypes.put(objClass, listenerClass);
  }


  @SuppressWarnings("rawtypes")
  private static Object getPrototype(HashMap<Class, Class> protos, Object o) {
    for (Class c : _classes) {
      if (c.isInstance(o)) {
        Class clazz = protos.get(c);
        if (clazz != null) {
          try {
            return clazz.newInstance();
          }
          catch (InstantiationException iex) {
            logger.warn("Could not create Instance for " + clazz);
          }
          catch (IllegalAccessException iaex) {
            logger.warn("Could not create Instance for " + clazz);
          }
        }
      }
    }
    return null;
  }

  public static ModelAdapter getAdapter(Object o) {
    if (o == null) {
      return new ModelAdapter() {
        @Override
        public int facets(Object model) { return ModelAdapter.NULL; }
      };
    }
    Object result = getPrototype(_adapterPrototypes, o);
    if (! (result instanceof ModelAdapter)) {
      return null;
    }
    return (ModelAdapter) result;
  }

  public static Layout getLayout(Object o) {
    if (o == null) {
      return new Layout() {
        @Override
        public GraphicalNode computeView(Object model, ViewContext context) {
          return null;
        }
      };
    }
    Object result = getPrototype(_layoutPrototypes, o);
    if (! (result instanceof Layout)) {
      return null;
    }
    return (Layout) result;
  }

  public static MouseListener getListener(Object o) {
    if (o == null) return null;

    Object result = getPrototype(_listenerPrototypes, o);
    if (! (result instanceof MouseListener)) {
      return null;
    }
    return (MouseListener) result;
  }
}
