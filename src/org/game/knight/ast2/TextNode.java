package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class TextNode extends BaseTagNode
{

	public TextNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	public String getID()
	{
		return getAttribute("id");
	}
	
	public String getText()
	{
		return antlrNode.getText();
	}
}
