package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class ColorListNode extends BaseTagNode
{

	public ColorListNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		if("color".equals(getTagName(antlrNode)))
		{
			return new ColorNode(antlrNode);
		}
		return super.initChild(antlrNode);
	}
}
