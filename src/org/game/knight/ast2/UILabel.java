package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class UILabel extends UIBase
{

	public UILabel(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	public FormatNode getFormat()
	{
		return getDocument().findFormat(getAttribute("format"));
	}
	
	public boolean isHTML()
	{
		return "true".equals(getAttribute("html"));
	}
	
	public String getText()
	{
		TextNode text=getDocument().findText(getAttribute("text"));
		if(text!=null)
		{
			return text.getText();
		}
		return "???";
	}
}
