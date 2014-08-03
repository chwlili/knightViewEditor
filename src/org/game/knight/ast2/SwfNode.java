package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class SwfNode extends BaseTagNode
{

	public SwfNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}
	
	public String getID()
	{
		return getAttribute("id");
	}

}
