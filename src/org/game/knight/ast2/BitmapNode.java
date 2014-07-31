package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class BitmapNode extends BaseTagNode
{

	public BitmapNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}
	
	/**
	 * ID
	 * @return
	 */
	public String getID()
	{
		return getAttribute("id");
	}

	/**
	 * 图像地址
	 * @return
	 */
	public String getSrc()
	{
		return getAttribute("src");
	}
	
	/**
	 * 重定向ID
	 * @return
	 */
	public String getDependID()
	{
		return getAttribute("dependId");
	}
	
//	public String getFilePath()
//	{
//		
//	}
}
