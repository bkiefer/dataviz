/*
 * 
 */
package de.dfki.lt.loot.visualization;

import javax.swing.JFrame;
import javax.swing.JPanel;

// TODO: Auto-generated Javadoc
/**
 * The Class VisualizationFrame.
 */
public class VisualizationFrame extends JFrame {

	/**
	 * Instantiates a new visualization frame.
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
   	
    	//On prévient notre JFrame que ce sera notre JPanel qui sera son contentPane
    	this.setContentPane(visualization.getView());
    	
    	 	
    	//Terminer le processus lorsqu'on clique sur "Fermer"
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	// Non modifiable
    	this.setResizable(true);

    	this.setVisible(true);
	}
}
