package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class DependListNode extends BaseTagNode
{
	/**
	 * ���캯��
	 * @param antlrNode
	 */
	public DependListNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		if("depend".equals(getTagName(antlrNode)))
		{
			return new DependNode(antlrNode);
		}
		return null;
	}
	
	public int size()
	{
		return getChildren().size();
	}
	
	public DependNode get(int index)
	{
		return (DependNode)getChildren().get(index);
	}
}
