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
		return null;
	}

	public int size()
	{
		return getChildren().size();
	}
	
	public FormatNode get(int index)
	{
		return (FormatNode) getChildren().get(index);
	}
	
	public FormatNode find(String id)
	{
		if(id!=null)
		{
			for(int i=size()-1;i>=0;i--)
			{
				if(id.equals(get(i).getID()))
				{
					return get(i);
				}
			}
		}
		return null;
	}
}
