package de.dfki.lt.loot;

public interface HighLighter {

	public boolean isHighLighted(Object inFeature, Object result, Object parentNodeName);
	public boolean isHighLighted(Object inFeature, Object result);
	public boolean isFeatureHighLighted(Object feature);
	public boolean isResultHighLighted(Object result);

}
