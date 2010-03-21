/*
 * 
 */
package de.dfki.lt.loot.visualization.mouse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import de.dfki.lt.loot.visualization.InformationPanel;
import de.dfki.lt.loot.visualization.nodes.GraphNode;
import edu.uci.ics.jung.visualization.VisualizationViewer;

// TODO: Auto-generated Javadoc
/**
 * The Class DataMenuItem.
 * 
 * @param <V> the value type
 * @param <E> the element type
 */
@SuppressWarnings("serial")
public class DataMenuItem<V, E> extends JMenuItem implements VertexListener<V, E> {

    /** The vertex. */
    private V vertex;
    
    /** The vis comp. */
    private VisualizationViewer visComp;
    
    /** The _information panel. */
    public InformationPanel _informationPanel;
    
    /** The action listener. */
    private PanelActionListener<V, E> actionListener;

  
    /**
     * Creates a new instance of DataMenuItem.
     * 
     * @param informationPanel the information panel
     */
    public DataMenuItem(InformationPanel informationPanel) {
        super("Node Features");
        _informationPanel = informationPanel;
        /*
        actionListener = new PanelActionListener<V, E>(informationPanel);
        this.addActionListener(actionListener);
       */
     
        this.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                visComp.getPickedVertexState().pick(vertex, false);
                
                
                _informationPanel.changeInfo(new JTextArea(((GraphNode) vertex).getData().toString(), 20, 20));
                
                /*
        		JFrame frame = new JFrame("Node : "+ ((GraphNode) vertex).getId());
        		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        		
        		frame.add(_informationPanel);
        		
        		frame.pack();
        		frame.setVisible(true);
        		frame.setFocusable(true);
        		*/
                visComp.repaint();
            }

        });

    }
    
    
    /**
     * Instantiates a new data menu item.
     */
    public DataMenuItem() {
        super("Node Features");
        this.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                visComp.getPickedVertexState().pick(vertex, false);
                
                
                //TODO Hier wird die Generizität verloren : Das geht nicht
                
        		JFrame frame = new JFrame("Node : "+ ((GraphNode) vertex).getId());
        		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        		JPanel panel = new  JPanel();
        		JTextArea ta = new JTextArea(((GraphNode) vertex).getData().toString(), 20, 20);
        		
        		panel.add(ta);
        		frame.getContentPane().add(panel);
        		
        		frame.pack();
        		frame.setVisible(true);
        		frame.setFocusable(true);
        		
                visComp.repaint();
            }

        });
    }

    /**
     * Implements the VertexMenuListener interface.
     * 
     * @param v the v
     * @param visComp the vis comp
     */
    public void setVertexAndView(V v, VisualizationViewer visComp) {
        this.vertex = v;
        this.visComp = visComp;
        this.setText("Open features");
    }
	
	

}
