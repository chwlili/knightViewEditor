package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class TextListNode extends BaseTagNode
{

	public TextListNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		if("text".equals(getTagName(antlrNode)))
		{
			return new TextNode(antlrNode);
		}
		return super.initChild(antlrNode);
	}

}
