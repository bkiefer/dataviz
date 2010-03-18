/*
 * 
 */
package de.dfki.lt.loot.gui.jgraphviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;



import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;
import com.jgraph.layout.organic.JGraphFastOrganicLayout;

import de.dfki.lt.loot.visualization.InformationPanel;
import de.dfki.lt.loot.visualization.edges.DataGraphEdge;
import de.dfki.lt.loot.visualization.exceptions.GraphNodeException;
import de.dfki.lt.loot.visualization.nodes.GraphNode;
import de.dfki.lt.loot.visualization.nodes.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphViewer.
 * 
 * @param <I> the generic type
 * @param <D> the generic type
 */
public class GraphViewer<I, D> extends JPanel implements MouseListener{
	
	/** The scroll. */
	protected JScrollPane scroll;
	
	/** The j graph. */
	protected JGraph jGraph = null;
	
	/** The j graph model. */
	protected DefaultGraphModel jGraphModel = null;
	
	/** The all cell list. */
	private HashMap<String, DefaultGraphCell> allCellList = new HashMap<String, DefaultGraphCell>();
	
	/**
	 * Instantiates a new graph viewer.
	 * 
	 * @param root the root
	 * @param nodes the nodes
	 * @param edges the edges
	 * @param info the info
	 */
	public GraphViewer(GraphNode root, HashMap<String, GraphNode> nodes, Vector<DataGraphEdge> edges, InformationPanel info)
	{
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
		this.setBorder(new TitledBorder("LogicalForm"));
		initGraph(nodes, edges, info);
	}
	
	/**
	 * Inits the graph.
	 * 
	 * @param nodes the nodes
	 * @param edges the edges
	 * @param info the info
	 */
	private void initGraph(HashMap<String, GraphNode> nodes, Vector<DataGraphEdge> edges, InformationPanel info)
	{
		//TODO WHAT is a model ?
		jGraphModel = new DefaultGraphModel();
		
		jGraph = new JGraph(jGraphModel);
		scroll = new JScrollPane(jGraph);
		scroll.setBorder(null);
		scroll.setWheelScrollingEnabled(true);
		//jGraph.setGridColor(Color.lightGray);
		//jGraph.setGridMode(JGraph.LINE_GRID_MODE);
		//jGraph.setGridSize(20);
		//jGraph.setGridEnabled(true);
		//jGraph.setGridVisible(true);
		jGraph.setBackground(new Color(237, 237, 237));
		jGraph.setHandleColor(Color.red);
		jGraph.setSelectionEnabled(true);
		jGraph.setVolatileOffscreen(true);
		jGraph.setAutoscrolls(true);
		
		
		setLayout(new BorderLayout());
		addGraphLayout(nodes, edges);
		
		jGraph.addKeyListener(new KeyBordListener());
		//jGraph.addMouseWheelListener(new WheelListener());
		jGraph.addMouseListener(this);
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
	private void addGraphLayout(HashMap<String, GraphNode> nodes, Vector<DataGraphEdge> edges)
	{
		jGraphModel.addGraphModelListener(new GraphMode());
		jGraph.getGraphLayoutCache().setFactory( new JComponentCellViewFactory());
		
		
		insertElements(nodes, edges);
		
		Object[] roots = new Object[1];
		roots[0] = allCellList.get("d1");
		JGraphFacade facade = new JGraphFacade(jGraph, roots); // Pass the facade the JGraph instance
		JGraphLayout layout = new JGraphHierarchicalLayout(); // Create an instance of the appropriate layout
		layout.run(facade); // Run the layout on the facade. Note that layouts do not implement the Runnable interface, to avoid confusion
		Map nested = facade.createNestedMap(true, true); // Obtain a map of the resulting attribute changes from the facade
		jGraph.getGraphLayoutCache().edit(nested); // Apply the results to the actual graph
		
		add(scroll, BorderLayout.CENTER);
	}
	
	/**
	 * Insert elements.
	 * 
	 * @param nodes the nodes
	 * @param edges the edges
	 */
	private void insertElements(HashMap<String, GraphNode> nodes, Vector<DataGraphEdge> edges)
	{
		Iterator<GraphNode> it = nodes.values().iterator();
		Iterator<String> itS = nodes.keySet().iterator();
		
		while(it.hasNext())
		{
			GraphNode node = it.next();
			JComponentStringNode jc = null;
			jc = new JComponentStringNode(node);
			jc.add(new DefaultPort());
			jGraph.getGraphLayoutCache().insert(jc);
			String id = itS.next();
			allCellList.put(id, jc);
		}
		
		JComponentStringNode jc;
		try {
			jc = new JComponentStringNode(new GraphNode("Test", "Test"));

			jc.add(new DefaultPort());
			jGraph.getGraphLayoutCache().insert(jc);
			allCellList.put("test", jc);
		} catch (GraphNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		Iterator<DataGraphEdge> it2 = edges.iterator();
		int i = 0;
		while(it2.hasNext())
		{
			DataGraphEdge edgeI = it2.next();
			DefaultEdge edge = new DefaultEdge();
			//System.out.println("In:" + edgeI.getIn() + " - Out:" + edgeI.getOut());
			edge.setSource((allCellList.get(edgeI.getIn())).getChildAt(0));
			edge.setTarget((allCellList.get(edgeI.getOut())).getChildAt(0));
			
			i++;
			int arrow = GraphConstants.ARROW_CLASSIC;
			GraphConstants.setLineEnd(edge.getAttributes(), arrow);
			GraphConstants.setEndFill(edge.getAttributes(), true);
			jGraph.getGraphLayoutCache().insert(edge);
			
			
		}
		
		DefaultEdge edge = new DefaultEdge();
		//System.out.println("In:" + edgeI.getIn() + " - Out:" + edgeI.getOut());
		edge.setSource((allCellList.get("size").getChildAt(0)));
		edge.setTarget((allCellList.get("test")).getChildAt(0));
		
		int arrow = GraphConstants.ARROW_CLASSIC;
		GraphConstants.setLineEnd(edge.getAttributes(), arrow);
		GraphConstants.setEndFill(edge.getAttributes(), true);
		
		jGraph.getGraphLayoutCache().insert(edge);
		
		DefaultEdge edge2 = new DefaultEdge();
		//System.out.println("In:" + edgeI.getIn() + " - Out:" + edgeI.getOut());
		edge2.setSource((allCellList.get("color").getChildAt(0)));
		edge2.setTarget((allCellList.get("test")).getChildAt(0));
		
		GraphConstants.setLineEnd(edge.getAttributes(), arrow);
		GraphConstants.setEndFill(edge.getAttributes(), true);
		
		jGraph.getGraphLayoutCache().insert(edge2);
		
	}
	
	
	
	/**
	 * The Class GraphMode.
	 * 
	 * @return
	 */
	/*
	private DefaultGraphCell getCell(JComponentStringNode jCell)
	{
		DefaultGraphCell cell = new DefaultGraphCell(jCell);
		AttributeMap map = new AttributeMap();
		Rectangle rec = null;
		rec = new Rectangle(20, 20, 300, 300);
				
		GraphConstants.setBounds(map, rec);
		GraphConstants.setGradientColor(map, Color.white.brighter());
		GraphConstants.setBorderColor(map, Color.blue);
		GraphConstants.setBackground(map, Color.RED);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setEditable(map, false);
		cell.setAttributes(map);
		return cell;
	}*/
	
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
			vertex = new TextNodeView(objCell);
			return vertex;
		}
	}
	
	/**
	 * The Class JComponentStringNode.
	 */
	private class JComponentStringNode extends JComponentNode<Object, Object>{

		/**
		 * Instantiates a new j component string node.
		 * 
		 * @param node the node
		 */
		public JComponentStringNode(GraphNode node) {
			super(node);
		}
		
	}

	/**
	 * The listener interface for receiving keyBord events.
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
	
	
	
	/*
	private class WheelListener implements MouseWheelListener{

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			
		       int notches = e.getWheelRotation();
		       if (notches < 0) {
		    	   Point2D point = jGraph.fromScreen(new Point(e.getPoint()));
		    	   
		    	   //Point2D point = jGraph.toScreen(e.getPoint());
		    	   System.out.println("Point : " + new Point(e.getPoint()));
		    	   //jGraph.setScale(0.8 * jGraph.getScale(), new Point(300, 300));
		    	   jGraph.scrollPointToVisible(point);
		    	  
		    	   
		           
		       } else {
		    	   //Point2D point = jGraph.fromScreen(new Point(e.getPoint()));
		    	   Point2D point = jGraph.toScreen(e.getPoint());
		    	   //System.out.println("Point : " +  new Point(e.getPoint()));
		    	   //jGraph.setScale(1.2 * jGraph.getScale(), new Point(300, 300));
		    	   jGraph.scrollPointToVisible(point);
		       }
			
		}
		
	}
	*/
	
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
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		//Point point = (Point) jGraph.fromScreen(new Point(e.getPoint()));
		//jGraph = jGraph.toScreen(point);
		
		//Point2D point = jGraph.fromScreen(new Point(e.getPoint()));
		//Point2D point = jGraph.toScreen(e.getPoint());
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}

