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
		return null;
	}
	
	public int size()
	{
		return getChildren().size();
	}

	public SwfNode get(int index)
	{
		return (SwfNode)getChildren().get(index);
	}
	
	public SwfNode find(String id)
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
