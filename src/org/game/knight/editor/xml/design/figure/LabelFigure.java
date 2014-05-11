package org.game.knight.editor.xml.design.figure;

import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public class LabelFigure extends FlowPage
{
	private String text;
	
	private ResourceManager manager;
	
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text=text;
		
		parseLines();
	}
	
	private void parseLines()
	{
		removeAll();
		
		this.setHorizontalAligment(PositionConstants.CENTER);
		
		TextFlow flow=new TextFlow(text);
		flow.setLayoutManager(new ParagraphTextLayout(flow,ParagraphTextLayout.WORD_WRAP_SOFT));
		add(flow);
	}
	
	private void partHTML(String text)
	{
		ArrayList<Font> fonts=new ArrayList<Font>();
		ArrayList<Color> colors=new ArrayList<Color>();
		
		Stack<Font> fontStack=new Stack<Font>();
		Stack<Color> colorStack=new Stack<Color>();
		
		fontStack.push(getFont());
		colorStack.push(getForegroundColor());
		
		int index=0;
		while(index<text.length())
		{
			char currChar=text.charAt(index);
			
			index++;
		}
	}
}
