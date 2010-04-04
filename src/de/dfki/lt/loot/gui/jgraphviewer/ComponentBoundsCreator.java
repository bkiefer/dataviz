package de.dfki.lt.loot.gui.jgraphviewer;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class ComponentBoundsCreator{
	
	private static JFrame _frame = new JFrame();
	
	public static void setComponentBounds(Component comp)
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		_frame.setContentPane(new JPanel(new BorderLayout()));
		
		
		_frame.getContentPane().add(comp, BorderLayout.CENTER);
		_frame.pack();
		
		_frame.getContentPane().removeAll();
	}

}
