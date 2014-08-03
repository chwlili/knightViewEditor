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
		return null;
	}

	public int size()
	{
		return getChildren().size();
	}
	
	public FilterNode get(int index)
	{
		return (FilterNode)getChildren().get(index);
	}
	
	public FilterNode find(String id)
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
