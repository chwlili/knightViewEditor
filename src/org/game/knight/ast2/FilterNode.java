package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class FilterNode extends BaseTagNode
{

	public FilterNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}
	
	public String getID()
	{
		return getAttribute("id");
	}

}
