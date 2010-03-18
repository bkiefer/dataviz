/*
 * 
 */
package JGraph;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.organic.JGraphFastOrganicLayout;

import de.dfki.lt.loot.visualization.edges.DataGraphEdge;
import de.dfki.lt.loot.visualization.nodes.GraphNode;

// TODO: Auto-generated Javadoc
/**
 * The Class JGraphViewer.
 */
public class JGraphViewer {
	
	/**
	 * Instantiates a new j graph viewer.
	 * 
	 * @param root the root
	 * @param nodes the nodes
	 * @param edges the edges
	 */
	public JGraphViewer(GraphNode root, HashMap<String, GraphNode> nodes, Vector<DataGraphEdge> edges){
		final GraphModel model = new DefaultGraphModel();
		final GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
		final JGraph graph = new JGraph(model, view);
		final DefaultGraphCell[] cells = new DefaultGraphCell[3];
		cells[0] = new DefaultGraphCell(new String("Hello"));
		/*
		GraphConstants.setBounds(cells[0].getAttributes(), new Rectangle2D.Double(20,20,40,20));
		GraphConstants.setGradientColor( cells[0].getAttributes(),Color.orange);
		GraphConstants.setOpaque(cells[0].getAttributes(), true);
		*/
		final DefaultPort port0 = new DefaultPort();
		cells[0].add(port0);
		cells[1] = new DefaultGraphCell(new String("World"));
		/*
		GraphConstants.setBounds(cells[1].getAttributes(), new Rectangle2D.Double(140,140,40,20));
		GraphConstants.setGradientColor( cells[1].getAttributes(), Color.red);
		GraphConstants.setOpaque(cells[1].getAttributes(), true);
		*/
		final DefaultPort port1 = new DefaultPort();
		cells[1].add(port1);
		final DefaultEdge edge = new DefaultEdge();
		edge.setSource(cells[0].getChildAt(0));
		edge.setTarget(cells[1].getChildAt(0));
		cells[2] = edge;
		final int arrow = GraphConstants.ARROW_CLASSIC;
		GraphConstants.setLineEnd(edge.getAttributes(), arrow);
		GraphConstants.setEndFill(edge.getAttributes(), true);
		graph.getGraphLayoutCache().insert(cells);
		
		JGraphFacade facade = new JGraphFacade(graph);
		JGraphLayout layout = new JGraphFastOrganicLayout();
		layout.run(facade);
		Map nested = facade.createNestedMap(true, true);
		graph.getGraphLayoutCache().edit(nested);
		
		final JFrame frame = new JFrame();
		frame.getContentPane().add(new JScrollPane(graph));
		frame.pack();
		frame.setVisible(true);
	}

}
