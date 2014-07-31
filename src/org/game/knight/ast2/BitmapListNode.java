package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class BitmapListNode extends BaseTagNode
{
	/**
	 * ¹¹Ôìº¯Êý
	 * @param antlrNode
	 */
	public BitmapListNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		if("bitmap".equals(getTagName(antlrNode)))
		{
			return new BitmapNode(antlrNode);
		}
		return super.initChild(antlrNode);
	}
}
