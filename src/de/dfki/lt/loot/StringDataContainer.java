/*
 * 
 */
package de.dfki.lt.loot;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Class StringDataContainer.
 */
public class StringDataContainer implements DataContainer<String, String> {

	/** The _pairs. */
	Vector<Pair<String, String>> _pairs;
	
	/**
	 * Instantiates a new string data container.
	 */
	public StringDataContainer()
	{
		_pairs = new Vector<Pair<String, String>>();
	}
	
	
	/**
	 * Instantiates a new string data container.
	 * 
	 * @param pairs the pairs
	 */
	public StringDataContainer(Collection<Pair<String, String>> pairs)
	{
		this();
		_pairs.addAll(pairs);
	}
	
	
	
	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.DataContainer#getLength()
	 */
	@Override
	public int getLength() {
		return _pairs.size();
	}

	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.DataContainer#getPair(int)
	 */
	@Override
	public Pair<String, String> getPair(int i) {
		try
		{
			return _pairs.get(i);
		}
		catch(ArrayIndexOutOfBoundsException ai)
		{
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.DataContainer#getIndex(de.dfki.lt.loot.Pair)
	 */
	@Override
	public int getIndex(Pair<String, String> pair) {
		return _pairs.indexOf(pair);
	}

	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.DataContainer#getPairs()
	 */
	@Override
	public Collection<Pair<String, String>> getPairs() {
		return _pairs;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "";
		Iterator<Pair<String, String>> it = _pairs.iterator();
		while(it.hasNext())
		{
			s += "\n";
			s += it.next().toString();
		}
		return s.substring(1);
	}


	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.DataContainer#add(de.dfki.lt.loot.Pair)
	 */
	@Override
	public void add(Pair<String, String> pair) {
		_pairs.add(pair);	
	}
}
