package de.dfki.lt.loot.gui.jgraphviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import de.dfki.lt.loot.visualization.nodes.Node;

public class ClassicNodeView extends JPanel {

	public ClassicNodeView(Node<?, ?> node){
		
		//TODO Statisch		
		this.setBorder(new TitledBorder(node.getId().toString()));
		this.setBackground(new Color(237, 237, 237));
		this.setLayout(new BorderLayout());
		//this.add((JPanel)node.getData(), BorderLayout.CENTER);
		
		JTextArea txtArea = new JTextArea(node.getData().toString());
		txtArea.setBackground(Color.GREEN);
		this.add(txtArea, BorderLayout.CENTER);

		
		
	}
}
