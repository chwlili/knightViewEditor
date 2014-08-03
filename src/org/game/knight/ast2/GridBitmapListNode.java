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
		return null;
	}

	public int size()
	{
		return getChildren().size();
	}
	
	public GridBitmapNode get(int index)
	{
		return (GridBitmapNode)getChildren().get(index);
	}
	
	public GridBitmapNode find(String id)
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
