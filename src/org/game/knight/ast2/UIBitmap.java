package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class UIBitmap extends UIBase
{

	public UIBitmap(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}
	
	/**
	 * 获取位图ID
	 * @return
	 */
	public String getBitmapID()
	{
		return getAttribute("bitmap");
	}

	/**
	 * 设置位图ID
	 * @return
	 */
	public void setBitmapID(String value)
	{
		setAttribute("bitmap",value);
	}
}
