package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class UIBitmap extends UIBase
{

	public UIBitmap(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}
	
	/**
	 * ��ȡλͼID
	 * @return
	 */
	public String getBitmapID()
	{
		return getAttribute("bitmap");
	}

	/**
	 * ����λͼID
	 * @return
	 */
	public void setBitmapID(String value)
	{
		setAttribute("bitmap",value);
	}
}
