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

  private static void init() {
    logger = Logger.getLogger(ModelAdapterFactory.class);
    _adapterPrototypes = new HashMap<Class<?>, Class<? extends ModelAdapter>>();
    _layoutPrototypes = new HashMap<Class<?>, Class<? extends Layout>>();
    _listenerPrototypes = new HashMap<Class<?>, Class<? extends MouseListener>>();
    _classes = new ArrayList<Class<?>>();
  }

  private static Logger logger;

  private static HashMap<Class<?>, Class<? extends ModelAdapter>> _adapterPrototypes;
  private static HashMap<Class<?>, Class<? extends Layout>> _layoutPrototypes;
  private static HashMap<Class<?>, Class<? extends MouseListener>> _listenerPrototypes;

  private static List<Class<?>> _classes;


  private static class ClassComparator implements Comparator<Class<?>> {;
    @Override
    public int compare(Class<?> o1, Class<?> o2) {
      if (o1 == o2) return 0;
      Class<?> curr = o1.getSuperclass();
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
  public static void registerLayout(Class objClass,
      Class<? extends Layout> layoutClass) {
    if (!_classes.contains(objClass)) {
      _classes.add(objClass);
      Collections.sort(_classes, new ClassComparator());
    }
    _layoutPrototypes.put(objClass, layoutClass);
  }

  @SuppressWarnings("rawtypes")
  public static void registerAdapter(Class objClass,
      Class<? extends ModelAdapter> adapterClass) {
    if (!_classes.contains(objClass)) {
      _classes.add(objClass);
      Collections.sort(_classes, new ClassComparator());
    }
    _adapterPrototypes.put(objClass, adapterClass);
  }

  @SuppressWarnings("rawtypes")
  public static void registerListener(Class objClass,
      Class<? extends MouseListener> listenerClass) {
    if (!_classes.contains(objClass)) {
      _classes.add(objClass);
      Collections.sort(_classes, new ClassComparator());
    }
    _listenerPrototypes.put(objClass, listenerClass);
  }

  @SuppressWarnings("rawtypes")
  private static <T>
  T getPrototype(HashMap<Class<?>, Class<? extends T>> protos, Object o) {
    for (Class c : _classes) {
      if (c.isInstance(o)) {
        Class<? extends T> clazz = protos.get(c);
        if (clazz != null) {
          try {
            return clazz.newInstance();
          } catch (InstantiationException iex) {
            logger.warn("Could not create Instance for " + clazz);
          } catch (IllegalAccessException iaex) {
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
    ModelAdapter result = getPrototype(_adapterPrototypes, o);
    return result;
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
    Layout result = getPrototype(_layoutPrototypes, o);
    return result;
  }

  public static MouseListener getListener(Object o) {
    if (o == null) return null;
    MouseListener result = getPrototype(_listenerPrototypes, o);
    return result;
  }
}
