package de.dfki.lt.loot.gui;

import java.util.HashMap;

import de.dfki.lt.loot.gui.adapters.ModelAdapter;

public class ModelAdapterFactory {

	/*
  private static final Logger logger =
    Logger.getLogger(ModelAdapterFactory.class);
*/


  @SuppressWarnings("unchecked")
  private static HashMap<Class, Class> _prototypes =
    new HashMap<Class, Class>();

  @SuppressWarnings("unchecked")
  public static void register(Class objClass, Class adapterClass) {
    _prototypes.put(objClass, adapterClass);
  }

  @SuppressWarnings("unchecked")
  public static ModelAdapter getAdapter(Object o) {
    Class ModelAdapterClass = _prototypes.get(o.getClass());
    try {
      return (ModelAdapter) ModelAdapterClass.newInstance();
    }
    catch (InstantiationException iex) {
      //logger.warn("Could not create Instance for " + ModelAdapterClass);
    }
    catch (IllegalAccessException iaex) {
     // logger.warn("Could not create Instance for " + ModelAdapterClass);
    }
    return null;
  }
}
