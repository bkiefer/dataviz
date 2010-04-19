package de.dfki.lt.loot.visualization.nodes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;


public class ClassicNodeView extends JPanel {

	public ClassicNodeView(Object name, Object data){
		
		//TODO Statisch
		
		Rectangle rec = new Rectangle(20, 20, 300, 300);
		
		
		this.setBorder(new TitledBorder(name.toString()));
		this.setBackground(new Color(237, 237, 237));
		this.setLayout(new BorderLayout());
		JTextArea txtArea = new JTextArea(data.toString());
		txtArea.setBackground(Color.GREEN);
		this.add(txtArea, BorderLayout.CENTER);
		this.setBounds(rec.getBounds());	
	}
}
