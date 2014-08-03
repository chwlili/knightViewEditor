package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class ColorListNode extends BaseTagNode
{

	public ColorListNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		if("color".equals(getTagName(antlrNode)))
		{
			return new ColorNode(antlrNode);
		}
		return null;
	}
	
	public int size()
	{
		return getChildren().size();
	}
	
	public ColorNode get(int index)
	{
		return (ColorNode)getChildren().get(index);
	}
	
	public ColorNode find(String id)
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
