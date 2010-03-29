/*
 * 
 */
package de.dfki.lt.loot.visualization;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import de.dfki.lt.loot.visualization.graph.Viewer;

/**
 * The Class VisualizationFrame. A classical Frame to display the main visualisation.<br/>
 * 
 * @author chris
 */
public class VisualizationFrame extends JFrame {

	private VisualizationPanel _viPanel;
	
	/**
	 * Instantiates a new visualization frame.<br/>
	 * With the following options :<br/>
	 * .) Location relativ to : null<br/>
	 * .) Size : 800, 600<br/>
	 * .) ContentPane VisualizationPanel.getView()<br/>
	 * .) Default Clode Op : JFrame.EXIT_ON_CLOSE<br/>
	 * .) Resizable : true<br/>
	 * .) Visible : true<br/>
	 * 
	 * @param visualization the visualization
	 */
	public VisualizationFrame(VisualizationAdapter visualization)
	{
		this.setTitle("DebbugViewer");
    	
    	
    	//Nous allons maintenant dire à notre objet de se positionner au centre
    	this.setLocationRelativeTo(null);
    	//this.setSize(500, 500);
    	this.setSize(800, 600);
    	
    	_viPanel = new VisualizationPanel(visualization);
    	//On prévient notre JFrame que ce sera notre JPanel qui sera son contentPane
    	this.setContentPane(_viPanel.getView());
    	_viPanel.combo().addItemListener(new Item());
    	
    	 	
    	//Terminer le processus lorsqu'on clique sur "Fermer"
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	// Non modifiable
    	this.setResizable(true);

    	this.setVisible(true);
	}
	
	/**
     * The Class ItemState.
     */
    class Item implements ItemListener{
    	
            /**
             * Item state changed.
             * 
             * @param e the ItemEvent
             */
            public void itemStateChanged(ItemEvent e) {
            	/*
            	if(e.getStateChange() == ItemEvent.SELECTED)
            	{
            		
            		JFrame frame = new JFrame();
            		frame.setTitle("DebbugViewer2");
                	
                	
                	//Nous allons maintenant dire à notre objet de se positionner au centre
                	frame.setLocationRelativeTo(null);
                	//this.setSize(500, 500);
                	frame.setSize(800, 600);
                	
                
                	//On prévient notre JFrame que ce sera notre JPanel qui sera son contentPane
                	frame.setContentPane(_viPanel.getVisu());
                	//_viPanel.combo().addItemListener(new Item());
                	
                	 	
                	//Terminer le processus lorsqu'on clique sur "Fermer"
                	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                	// Non modifiable
                	frame.setResizable(true);

                	frame.setVisible(true);
            	}
            	*/
            	if(e.getStateChange() == ItemEvent.SELECTED)
            	{
            		System.out.println("TETETETETETETETETETETETETETEt");
            		
                    
            	}
            }               
    }
}
