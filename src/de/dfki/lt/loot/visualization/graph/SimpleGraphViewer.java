/*
 * 
 */
package de.dfki.lt.loot.visualization.graph;

import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;

import de.dfki.lt.loot.visualization.InformationPanel;
import de.dfki.lt.loot.visualization.VisualizationAdapter;
import de.dfki.lt.loot.visualization.edges.DataGraphEdge;
import de.dfki.lt.loot.visualization.mouse.Mouse;
import de.dfki.lt.loot.visualization.nodes.GraphNode;
import de.dfki.lt.loot.visualization.nodes.Node;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;


// TODO: Auto-generated Javadoc
/**
 * The Class SimpleGraphViewer.
 */
public class SimpleGraphViewer implements Viewer{
	
	/** The name of this visualization. */
	private String _name; 
	
	/** The _graph. */
	private SimpleGraph _graph;
	
	/** The _layout. */
	private DAGLayout<Node, DataGraphEdge> _layout;
	
	/** The _server. */
	private VisualizationViewer<Node, DataGraphEdge> _server;
	
	/** The Adapter. */
	private VisualizationAdapter _adapter;	
	
	/**
	 * Instantiates a new simple graph viewer.
	 * 
	 * @param root the root
	 * @param nodes the nodes
	 * @param edges the edges
	 * @param informationPanel the information panel
	 */
	public SimpleGraphViewer(VisualizationAdapter adapter, InformationPanel informationPanel)
	{
		
		_adapter = adapter;
		
		_name = "Jung Viewer";
		_graph = new SimpleGraph(_adapter.getNodes(), _adapter.getEdges());
		//_forest = new DelegateForest(_graph.getGraph());
		_layout = new DAGLayout<Node, DataGraphEdge>(_graph.getGraph());
		_layout.setSize(new Dimension(600, 600));
		_layout.setRoot(_adapter.getNodes().get(_adapter.getRoot()));
		_layout.setForceMultiplier(200);
		_layout.setStretch(1.5);
		_layout.setRepulsionRange(1200);
		
		 
		 
		//_layout.setSize(new Dimension(300, 300));
		_server = new VisualizationViewer<Node, DataGraphEdge>(_layout);
		
		_server.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Node>());
		_server.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<DataGraphEdge>());
		_server.getRenderContext().setVertexShapeTransformer(new VertexShapeAspect<Node, DataGraphEdge>());
		_server.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, DataGraphEdge>());
		_server.getRenderContext().setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<Node,DataGraphEdge>(.5, .5));
		_server.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		_server.getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(false);
		_server.addKeyListener(new KeyBordListener());
		Mouse<GraphNode, DataGraphEdge> mouse;
		
		if(informationPanel == null)
		{
			mouse = new Mouse<GraphNode, DataGraphEdge>();
		}
		else{
			mouse = new Mouse<GraphNode, DataGraphEdge>(informationPanel);
		}
		_server.addKeyListener(mouse.getModeKeyListener());
		
		/*
		// Create a graph mouse and add it to the visualization viewer
        EditingModalGraphMouse gm = new EditingModalGraphMouse(_server.getRenderContext(), 
                 GraphNodeFactory.getInstance(),
                DataGraphEdgeFactory.getInstance()); 
                */
		
		
		
		
		_server.setGraphMouse(mouse);
		//_server.addKeyListener();
		
		/*
		// Create a graph mouse and add it to the visualization component 
		PluggableGraphMouse gm = new PluggableGraphMouse();
		gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(),  0, 1.1f, 0.9f));
		gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1_MASK));
		*/
	}
	
	/**
	 * Gets the view.
	 * 
	 * @return the view
	 */
	public JPanel getView()
	{
		return _server;
	}
	
    /**
     * The Class VertexShapeAspect.
     * 
     * @param <V> the value type
     * @param <E> the element type
     */
    private final static class VertexShapeAspect<V,E>
    extends AbstractVertexShapeTransformer <V>
    implements Transformer<V,Shape>  {
        
        /**
         * Instantiates a new vertex shape aspect.
         */
        public VertexShapeAspect()
        {
            setSizeTransformer(new Transformer<V,Integer>() {

				public Integer transform(V v) {
		                return 120;

				}});
            setAspectRatioTransformer(new Transformer<V,Float>() {

				public Float transform(V v) {
		                return (float)(0.5);
		            }
				});
        }
        
        /* (non-Javadoc)
         * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
         */
        public Shape transform(V v)
        {
            	
                return factory.getRoundRectangle(v);
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
    private class KeyBordListener implements KeyListener{
    	
	    /* (non-Javadoc)
	     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	     */
	    @Override
		public void keyTyped(final KeyEvent e) {
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
		 */
		@Override
		public void keyReleased(final KeyEvent e) {
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
		 */
		@Override
		public void keyPressed(final KeyEvent e) {

			if((e.getKeyCode() == 82) && e.isControlDown())
			{
				_layout = new DAGLayout<Node, DataGraphEdge>(_graph.getGraph());
				_layout.setSize(new Dimension(600, 600));
				
				_layout.setRoot(_adapter.getNodes().get(_adapter.getRoot()));
				_layout.setForceMultiplier(200);
				_layout.setStretch(1.5);
				_layout.setRepulsionRange(1200);
				_server.setGraphLayout(_layout);
				_server.repaint();
			}
			else if ((e.getKeyCode() == 83) && e.isControlDown())
			{
				_server.setGraphLayout(new FRLayout2<Node, DataGraphEdge>(_graph.getGraph()));
				_server.repaint();
			}
			
		}
    	
    }

	@Override
	public int setHightLight(String[] toLight) {
		// TODO Not implemented now
		return 0;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public void setName(String name) {
		_name = name;
		
	}
	
	/**
	 * To String.
	 * 
	 * @return The name of this visualization. Usefull for the Menu creation.
	 */
	public String toString()
	{
		return _name;
	}

	@Override
	public VisualizationAdapter getAdapter() {
		
		return null;
	}
	

}
