package de.dfki.lt.loot.visualization.nodes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import de.dfki.lt.loot.HighLighter;
import de.dfki.lt.loot.Pair;
import de.dfki.lt.loot.KeyPair;
import de.dfki.lt.loot.visualization.exceptions.NodeViewException;

public class FeatureView extends JPanel {
	public FeatureView(Object name, Object data, Object parentNodeName, HighLighter high) throws NodeViewException{
		
		Rectangle rec = new Rectangle(20, 20, 300, 300);
		
		
		this.setBorder(new TitledBorder(name.toString()));
		this.setBackground(new Color(237, 237, 237));
		this.setLayout(new BorderLayout());
		JTable table;
		if(data instanceof Pair<?, ?> )
		{
			Pair<?, ?> feature = (Pair<?, ?>)data;
			Object[] tableName = {"Feature", "Result"};
			
			KeyPair<Object, Integer> tableFeature = new KeyPair<Object, Integer>(feature.getFirst(), 1);
			KeyPair<Object, Integer> tableResult = new KeyPair<Object, Integer>(feature.getSecond(), 1);
			if(high != null)
			{
				System.out.println(feature.getFirst() + " - " + feature.getSecond() + " - " + parentNodeName);
				if(high.isHighLighted(feature.getFirst(), feature.getSecond(), parentNodeName))
				{
					tableFeature.setSecond(0);
					tableResult.setSecond(0);
					System.out.println("Test1");
				}
				else 
				{
					if(high.isFeatureHighLighted(feature.getFirst()))
					{
						tableFeature.setSecond(0);
						System.out.println("Test2");
					}
					if(high.isResultHighLighted(feature.getSecond()))
					{
						tableResult.setSecond(0);
						System.out.println("Test3");
					}
				}
				
			}
			Object[][] featureTable = {{tableFeature, tableResult}};
			table = new JTable(featureTable, tableName);
			table.setDefaultRenderer(table.getColumnClass(0), new HighLightRenderer());
			this.add(table, BorderLayout.CENTER);
		}
		else
			throw new NodeViewException("Data Type has to be Pair<?, ?> for the featured Node View");

		//JTextArea txtArea = new JTextArea(node.getData().toString());
		
		this.setBounds(rec.getBounds());
	}
	
	private class HighLightRenderer extends DefaultTableCellRenderer {
		
		public Component getTableCellRendererComponent(JTable table,
                                              Object value,
                                              boolean isSelected,
                                              boolean hasFocus,
                                              int row,
                                              int column)
		{
			Component c = 
				super.getTableCellRendererComponent(table, value,
													isSelected, hasFocus,
													row, column);
			
				if(value instanceof KeyPair<?,?> )
				{
					KeyPair<?,?> valueOfCellKeyPair = (KeyPair<?,?>) value;
					if((Integer)valueOfCellKeyPair.getSecond() == 0)
					{
						c.setBackground(Color.RED);
						System.out.println("!!!! RED !!!!");
						System.out.println(value.toString() + "row : " + row + " - Col : " + column);
					}
					else
						c.setBackground(Color.GREEN);
				} else
					try {
						throw new NodeViewException("Nothing Works !!!");
					} catch (NodeViewException e) {
						e.printStackTrace();
					}
				
				return c;
		}
}
}
