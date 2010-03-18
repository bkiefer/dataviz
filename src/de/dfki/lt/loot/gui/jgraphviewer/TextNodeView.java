/*
 * 
 */
package de.dfki.lt.loot.gui.jgraphviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;

import de.dfki.lt.loot.visualization.nodes.GraphNode;

// TODO: Auto-generated Javadoc
/**
 * The Class TextNodeView.
 */
@SuppressWarnings("serial")
public class TextNodeView extends NodeView
	{
	
	/** The _comp. */
	private static Component _comp = null;
	
	/** The renderer. */
	private static CompViewRenderer renderer = new CompViewRenderer();
	
	/**
	 * The Class CompViewRenderer.
	 */
	private static class CompViewRenderer extends VertexRenderer
	{	
		
		/**
		 * Instantiates a new comp view renderer.
		 */
		public CompViewRenderer()
		{
			super();
		}	

		/* (non-Javadoc)
		 * @see org.jgraph.graph.VertexRenderer#getRendererComponent(org.jgraph.JGraph, org.jgraph.graph.CellView, boolean, boolean, boolean)
		 */
		public Component getRendererComponent(JGraph jGraph, CellView cellView, boolean selected, boolean focused,
				boolean preview)
		{
			
			
			
			//DefaultGraphCell cell = (DefaultGraphCell) cellView.getCell();
			JComponentNode jcell = (JComponentNode)cellView.getCell();
			//JComponentNode jcell = (JComponentNode)cell.getUserObject();
		
		
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(jcell.getId().toString()));
			panel.setBackground(new Color(237, 237, 237));
			panel.setLayout(new BorderLayout());
			JTextArea txtArea = new JTextArea(jcell.getData().toString());
			txtArea.setBackground(Color.GREEN);
			panel.add(txtArea, BorderLayout.CENTER);
			_comp = panel;
			_comp.setBounds(((Rectangle2D) jcell.getAttributes().get(GraphConstants.BOUNDS)).getBounds());
			
			//_comp.addMouseListener(new InformationListener());
			
			return _comp;
		}
		
	}

	/**
	 * Instantiates a new text node view.
	 * 
	 * @param cell the cell
	 */
	public TextNodeView(Object cell)
	{
		super(cell);
	}

	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.gui.jgraphviewer.NodeView#getRenderer()
	 */
	@Override
	public CellViewRenderer getRenderer()
	{
		return renderer;
	}
	
	/* (non-Javadoc)
	 * @see de.dfki.lt.loot.gui.jgraphviewer.NodeView#getComponent()
	 */
	public Component getComponent()
	{
		return _comp;
	}
	
	
}

