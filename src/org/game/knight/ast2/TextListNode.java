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
		return null;
	}
	
	public int size()
	{
		return getChildren().size();
	}
	
	public TextNode get(int index)
	{
		return (TextNode)getChildren().get(index);
	}

	public TextNode find(String id)
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
