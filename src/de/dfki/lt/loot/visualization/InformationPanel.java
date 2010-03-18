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


// TODO: Auto-generated Javadoc
/**
 * The Class InformationPanel.
 */
public class InformationPanel extends JScrollPane {

	/** The _info panel. */
	private JPanel _infoPanel;
	
	/**
	 * Instantiates a new information panel.
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
	 * Change info.
	 * 
	 * @param infos the infos
	 */
	public void changeInfo(JComponent infos)
	{
		infos.setBackground(new Color(237, 237, 237));
		_infoPanel.removeAll();
		_infoPanel.add(infos);
		this.setViewportView(_infoPanel);
		
	}

}
