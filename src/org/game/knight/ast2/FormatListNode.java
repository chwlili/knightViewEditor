package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class FormatListNode extends BaseTagNode
{

	public FormatListNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		if("format".equals(getTagName(antlrNode)))
		{
			return new FormatNode(antlrNode);
		}
		return super.initChild(antlrNode);
	}

}
