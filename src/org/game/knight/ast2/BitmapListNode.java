package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class BitmapListNode extends BaseTagNode
{
	/**
	 * ¹¹Ôìº¯Êı
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
		return null;
	}
	
	public int size()
	{
		return getChildren().size();
	}
	
	public BitmapNode get(int index)
	{
		return (BitmapNode)getChildren().get(index);
	}
	
	public BitmapNode find(String id)
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
