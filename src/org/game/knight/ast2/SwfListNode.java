package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class SwfListNode extends BaseTagNode
{

	public SwfListNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		if("swf".equals(getTagName(antlrNode)))
		{
			return new SwfNode(antlrNode);
		}
		return super.initChild(antlrNode);
	}

}
