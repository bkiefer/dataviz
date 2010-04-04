/*
 * 
 */
package de.dfki.lt.loot.gui.jgraphviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.dfki.lt.loot.HighLight;
import de.dfki.lt.loot.visualization.InformationPanel;
import de.dfki.lt.loot.visualization.edges.DataGraphEdge;
import de.dfki.lt.loot.visualization.graph.Viewer;
import de.dfki.lt.loot.visualization.nodes.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphViewer.
 * 
 * @param <I> the generic type of ID
 * @param <D> the generic type of Data
 */
public class GraphViewer<I, D> extends JPanel implements Viewer{
	
	/** The name of this visualization. */
	protected String name; 
	
	/** The scroll. */
	protected JScrollPane scroll;
	
	/** The j graph. */
	protected JGraph jGraph = null;
	
	/** The j graph model. */
	protected DefaultGraphModel jGraphModel = null;
	
	/** The all cell list. */
	private HashMap<String, JComponentNode<I, D>> allCellList = new HashMap<String, JComponentNode<I, D>>();
	
	/**
	 * Instantiates a new graph viewer.
	 * 
	 * @param root the root
	 * @param nodes the nodes
	 * @param edges the edges
	 * @param info the info
	 */
	public GraphViewer(String root, HashMap<String, Node<I, D>> nodes, Vector<DataGraphEdge> edges, InformationPanel info)
	{
		name = "JGraph Viewer";
		
		//super("JGraph");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initGraph(root, nodes, edges, info);
	}
	
	/**
	 * Inits the graph.
	 * 
	 * @param nodes the nodes
	 * @param edges the edges
	 * @param info the info
	 */
	private void initGraph(String root, HashMap<String, Node<I, D>> nodes, Vector<DataGraphEdge> edges, InformationPanel info)
	{
		//TODO WHAT is a model ?
		jGraphModel = new DefaultGraphModel();
		
		jGraph = new JGraph(jGraphModel);
		//scroll = new JScrollPane(jGraph);
		//scroll.setBorder(null);
		//scroll.setWheelScrollingEnabled(true);
		//jGraph.setGridColor(Color.lightGray);
		//jGraph.setGridMode(JGraph.LINE_GRID_MODE);
		//jGraph.setGridSize(20);
		//jGraph.setGridEnabled(true);
		//jGraph.setGridVisible(true);
		jGraph.setBackground(new Color(237, 237, 237));
		jGraph.setHandleColor(Color.red);
		jGraph.setSelectionEnabled(true);
		jGraph.setVolatileOffscreen(true);
		//jGraph.setAutoscrolls(true);
		
		
		setLayout(new BorderLayout());
		addGraphLayout(root, nodes, edges);
		
		jGraph.addKeyListener(new KeyBordListener());
		jGraph.addGraphSelectionListener(new GraphSelection(info));
		setSize(600, 600);
		setVisible(true);
	}
	
	/**
	 * Gets the view.
	 * 
	 * @return the view
	 */
	public JPanel getView()
	{
		return this;
	}
	
	/**
	 * Adds the graph layout.
	 * 
	 * @param nodes the nodes
	 * @param edges the edges
	 */
	private void addGraphLayout(String root, HashMap<String, Node<I, D>> nodes, Vector<DataGraphEdge> edges)
	{
		jGraphModel.addGraphModelListener(new GraphMode());
		jGraph.getGraphLayoutCache().setFactory( new JComponentCellViewFactory());
		
		
		insertElements(nodes, edges);
		
		Object[] roots = new Object[1];
		
		roots[0] = allCellList.get(root);
		JGraphFacade facade = new JGraphFacade(jGraph, roots); // Pass the facade the JGraph instance
		JGraphLayout layout = new JGraphHierarchicalLayout(); // Create an instance of the appropriate layout
		layout.run(facade); // Run the layout on the facade. Note that layouts do not implement the Runnable interface, to avoid confusion
		Map nested = facade.createNestedMap(true, true); // Obtain a map of the resulting attribute changes from the facade
		jGraph.getGraphLayoutCache().edit(nested); // Apply the results to the actual graph
		
		add(jGraph, BorderLayout.CENTER);
	}
	
	/**
	 * Insert elements.
	 * 
	 * @param nodes the nodes
	 * @param edges the edges
	 */
	private void insertElements(HashMap<String, Node<I, D>> nodes, Vector<DataGraphEdge> edges)
	{
		Iterator<Node<I, D>> it = nodes.values().iterator();
		Iterator<String> itS = nodes.keySet().iterator();
		
		while(it.hasNext())
		{
			Node<I, D> node = it.next();
			JComponentNode<I, D> jc = null;
			jc = new JComponentNode<I, D>(node, new ClassicNodeView(node));
			jc.add(new DefaultPort());
			jGraph.getGraphLayoutCache().insert(jc);
			String id = itS.next();
			allCellList.put(id, jc);
		}
		
		
		for(DataGraphEdge edgeI : edges)
		{
			DefaultEdge edge = new DefaultEdge();
			edge.setSource((allCellList.get(edgeI.getIn())).getChildAt(0));
			edge.setTarget((allCellList.get(edgeI.getOut())).getChildAt(0));
			
			int arrow = GraphConstants.ARROW_CLASSIC;
			GraphConstants.setLineEnd(edge.getAttributes(), arrow);
			GraphConstants.setEndFill(edge.getAttributes(), true);
			jGraph.getGraphLayoutCache().insert(edge);
			
			
		}
		
	}
	
	
	/**
	 * The Class GraphMode.
	 * 
	 * @return
	 */
	private class GraphMode implements GraphModelListener
	{
		
		/* (non-Javadoc)
		 * @see org.jgraph.event.GraphModelListener#graphChanged(org.jgraph.event.GraphModelEvent)
		 */
		@Override
		public void graphChanged(GraphModelEvent e)
		{
			jGraph.clearOffscreen();
		}
	}
	
	/**
	 * A factory for creating JComponentCellView objects.
	 */
	private class JComponentCellViewFactory extends DefaultCellViewFactory
	{
		
		/* (non-Javadoc)
		 * @see org.jgraph.graph.DefaultCellViewFactory#createVertexView(java.lang.Object)
		 */
		protected VertexView createVertexView(Object objCell)
		{
			DefaultGraphCell cell = (DefaultGraphCell) objCell;
			VertexView vertex = null;
			vertex = new NodeViewer(objCell);
			return vertex;
		}
	}
	

	/**
	 * The listener interface for receiving keyBord eves.
	 * The class that is interested in processing a keyBord
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addKeyBordListener<code> method. When
	 * the keyBord event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see KeyBordEvent
	 */
	private class KeyBordListener implements KeyListener
	{

		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if((e.getKeyCode() == 79) && e.isControlDown())
			{	
				jGraph.setScale(0.5 * jGraph.getScale());
			}
			
			if((e.getKeyCode() == 73) && e.isControlDown())
			{
				jGraph.setScale(2 * jGraph.getScale());
			}
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
		 */
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
		 */
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * The Class GraphSelection.
	 */
	private class GraphSelection implements GraphSelectionListener{

		/** The _info. */
		InformationPanel _info;
		
		/**
		 * Instantiates a new graph selection.
		 * 
		 * @param info the info
		 */
		public GraphSelection (final InformationPanel info)
		{
			_info = info;
		}
		
		/* (non-Javadoc)
		 * @see org.jgraph.event.GraphSelectionListener#valueChanged(org.jgraph.event.GraphSelectionEvent)
		 */
		@Override
		public void valueChanged(GraphSelectionEvent e) {
			if(e.getCell() instanceof JComponentNode<?, ?>)
			{	
				JComponentNode<I, D> node = (JComponentNode<I, D>) e.getCell();
				_info.changeInfo(new JTextArea(node.getId() + "\n\n" + node.getData(), 20, 20));
				
			}
			
		}
		
	}
	
	

	@Override
	public int setHightLight(String[] toLight) {
		
		Collection<JComponentNode<I, D>> cells = allCellList.values();
		Iterator<JComponentNode<I, D>> it = cells.iterator();
		while(it.hasNext())
		{
			Component[] comps = it.next().getComp().getComponents();
			for (int i = 0; i < comps.length; i++)
			{
				if( comps[i] instanceof JTextArea)
				{
					System.out.println("IN IN IN");
					comps[i] = HighLight.areaHighLight((JTextArea) comps[i], toLight);
				}
			}
		}	
		return 0;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * To String.
	 * 
	 * @return The name of this visualization. Usefull for the Menu creation.
	 */
	public String toString()
	{
		return name;
	}

}

