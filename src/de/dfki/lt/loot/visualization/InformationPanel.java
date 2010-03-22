/*
 * 
 */
package de.dfki.lt.loot.visualization;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;


/**
 * The Class InformationPanel. This Class is used to Create a JPanel ( contained by a JScrollPane )
 * for displaying informations about a graph or someting else. 
 * @author chris
 */
public class InformationPanel extends JScrollPane {

	/** The _info panel. */
	private JPanel _infoPanel;
	
	/**
	 * Instantiates a new information panel.<br />
	 * The parameters of this JPanel are in this constructor : <br/>
	 * .) Background Color : 237, 237, 237.
	 * .) TitelBorder : "Informations".
	 * .) Size : 200, 600.
	 * The parameters for the JScrollPane : <br/>
	 * .) Background Color : 237, 237, 237.
	 * .) Preferred Size : 200, 600.
	 * 
	 */
	public InformationPanel()
	{
		super();
		_infoPanel = new JPanel();
		_infoPanel.setBackground(new Color(237, 237, 237));
		this.setBorder(new TitledBorder("Informations"));
		this.setSize(new Dimension(200, 600));
		//_scrollPanel.setBorder(new TitledBorder("Informations"));
		this.setPreferredSize(new Dimension(200, 600));
		this.setBackground(new Color(237, 237, 237));
		this.setViewportView(_infoPanel);
	}
	
	/**
	 * Change the informations of the Information Panel.<br/>
	 * the old component will be removed.
	 * 
	 * @param infos the infos, by JComponent.
	 */
	public void changeInfo(JComponent infos)
	{
		infos.setBackground(new Color(237, 237, 237));
		_infoPanel.removeAll();
		_infoPanel.add(infos);
		this.setViewportView(_infoPanel);
		
	}
	
	/**
	 * Getter for the informations Panel
	 * 
	 * @return the informations Panel
	 */
	public JPanel getInfoPanel(){
		return _infoPanel;
	}

}
