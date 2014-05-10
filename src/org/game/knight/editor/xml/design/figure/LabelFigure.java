package org.game.knight.editor.xml.design.figure;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;

public class LabelFigure extends FlowPage
{
	private String text;
	
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
}
