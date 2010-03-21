/*
 * 
 */
package de.dfki.lt.loot.visualization;

import javax.swing.JFrame;

/**
 * The Class VisualizationFrame. A classical Frame to display the main visualisation.<br/>
 * 
 * @autor chris
 */
public class VisualizationFrame extends JFrame {

	/**
	 * Instantiates a new visualization frame.<br/>
	 * With the following options :<br/>
	 * � Location relativ to : null<br/>
	 * � Size : 800, 600<br/>
	 * � ContentPane VisualizationPanel.getView()<br/>
	 * � Default Clode Op : JFrame.EXIT_ON_CLOSE<br/>
	 * � Resizable : true<br/>
	 * � Visible : true<br/>
	 * 
	 * @param visualization the visualization
	 */
	public VisualizationFrame(VisualizationAdapter visualization)
	{
		this.setTitle("DebbugViewer");
    	
    	
    	//Nous allons maintenant dire � notre objet de se positionner au centre
    	this.setLocationRelativeTo(null);
    	//this.setSize(500, 500);
    	this.setSize(800, 600);
   	
    	//On pr�vient notre JFrame que ce sera notre JPanel qui sera son contentPane
    	this.setContentPane(visualization.getView());
    	
    	 	
    	//Terminer le processus lorsqu'on clique sur "Fermer"
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	// Non modifiable
    	this.setResizable(true);

    	this.setVisible(true);
	}
}
