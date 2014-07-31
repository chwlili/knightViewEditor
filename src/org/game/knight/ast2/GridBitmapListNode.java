package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class GridBitmapListNode extends BaseTagNode
{

	public GridBitmapListNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		if("bitmapReader".equals(getTagName(antlrNode)))
		{
			return new GridBitmapNode(antlrNode);
		}
		return super.initChild(antlrNode);
	}

}
