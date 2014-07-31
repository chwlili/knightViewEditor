package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class FilterListNode extends BaseTagNode
{

	public FilterListNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		if("filter".equals(getTagName(antlrNode)))
		{
			return new FilterNode(antlrNode);
		}
		return super.initChild(antlrNode);
	}

}
