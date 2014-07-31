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
	 * ͼ���ַ
	 * @return
	 */
	public String getSrc()
	{
		return getAttribute("src");
	}
	
	/**
	 * �ض���ID
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
