package de.dfki.lt.loot;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.LayeredHighlighter;

/**
 * HighLight
 * 
 * Abstract class to highlight a JTextArea with the given search regex (post and pre) or the default one<br\>
 * If you search more then one String, take care, may be part of the searched Strings already been colored.
 *  
 *  @author chris
 */
public abstract class HighLight {

	private static final String DEFAULT_REGEX =  "(($)|(^)|([^a-z[A-Z[0-9]]]))";
	private static String _preRegex;
	private static String _postRegex;
	private static Color[] _colors = {Color.RED};
	
	/**
	 * The area highlighter witch colored also in the words.<br/>
	 * The REGEX in use is "".
	 *  
	 * @param area the JTextArea that will be modified
	 * @param toHighLight the searched Strings
	 * @return JTextArea the modified aera
	 */
	public static JTextArea areaHighLightWordFracture(JTextArea area, String[] toHighLight)
	{
		return areaHighLight(area, toHighLight, "", "");
	}
	
	/**
	 * The default highlighter: Word sensitiv.<br/>
	 * The pre and post REGEX in use is the DEFAULT_REGEX : "(($)|(^)|([^a-z[A-Z[0-9]]]))".
	 * 
	 * @param area the JTextArea that will be modified
	 * @param toHighLight the searched Strings
	 * @return JTextArea the modified aera
	 */
	public static JTextArea areaHighLight (JTextArea area, String[] toHighLight)
	{
		return areaHighLight(area, toHighLight, DEFAULT_REGEX, DEFAULT_REGEX);
	}
	
	/**
	 * A highlighter that gives you the possibility to pass your own REGEX ( the same for post and pre searched word)
	 * 
	 * @param area the JTextArea that will be modified
	 * @param toHighLight the searched Strings
	 * @param prePostRegex The REGEX that will be added to the searched words at the end and the begining
	 * @return JTextArea the modified aera
	 */
	public static JTextArea areaHighLight (JTextArea area, String[] toHighLight, String prePostRegex)
	{
		return areaHighLight(area, toHighLight, prePostRegex, prePostRegex);
	}
	
	/**
	 * A highlighter that gives you the possibility to pass your own REGEX.<br/>
	 * One at the begining one at the end.
	 * @param <LayeredHighlightInfo>
	 * 
	 * @param area the JTextArea that will be modified
	 * @param toHighLight the searched Strings
	 * @param preRegex The REGEX that will be added to the searched words at the begining
	 * @param postRegex The REGEX that will be added to the searched words at the end
	 * @return JTextArea the modified aera
	 */
	public static <LayeredHighlightInfo> JTextArea areaHighLight (JTextArea area, String[] toHighLight, String preRegex, String postRegex)
	{
		Color colorToUse = _colors[0];
		_preRegex = preRegex;
		_postRegex = postRegex;
		Highlighter h = area.getHighlighter();
		h.removeAllHighlights();
		String text = area.getText(); 
		Pattern search;
		Matcher in;
		int start, end;
		for(int i = 0; i < toHighLight.length; i++)
		{
			String toSearch = _preRegex + toHighLight[i] + _postRegex;
			search = Pattern.compile(toSearch);
			in = search.matcher(text);
			
			if(i > _colors.length)
				colorToUse = _colors[0];
			else
				colorToUse = _colors[i];
			while(in.find())
			{
				try {
					start = in.start();
					end = in.end();
					if(start != 0)
						start++;
					if(end != text.length())
						end--;
					h.addHighlight(start, end, new DefaultHighlighter.DefaultHighlightPainter(colorToUse));
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
		return area;		
	}
	
	/**
	 * Setter for the Color Table. The other function will use as much colors as possible.<br\>
	 * If there are not enough Colors in the table, the first one will be used for the rest of he words.<br\>
	 * The default Color is Color.RED.
	 * @param colors the color set you want to use
	 */
	public void setColors(Color[] colors)
	{
		_colors = colors;
	}
	
	/**
	 * Getter for the color set.
	 * @return Color[] the color set in use
	 */
	public Color[] getColors()
	{
		return _colors;
	}
}
