/*
 * 
 */
package de.dfki.lt.loot.visualization;

/**
 * The Enum Type. Used by the VisualisationPanel to handel the Viewer. 
 * @autor chris
 */
public enum Type {
	
 /** The DAGGRAPH. The Graph is a DAG ( Directed Acyclique Graph)*/
 DAGGRAPH, 

 /** The JGRAPH. The Viewer used is the JGraph Viewer : hierarchical layout ( good for DAG graph) */
 JGRAPH, 
 
 /** The GRAPH. A classic Graph*/
 GRAPH, 
 
 /** The TREE. A classic tree */
 TREE, 
 
 /** The INDIVIDUAL. Only individual Nodes are to be displayed*/
 INDIVIDUAL;

}
