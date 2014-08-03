package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.core.resources.IFile;

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
	
	/**
	 * 获取图像文件
	 * @return
	 */
	public IFile getBitmapFile()
	{
		String bmpID=getBitmapID();
		if(bmpID!=null && bmpID.isEmpty()==false)
		{
			BitmapNode node=getDocument().findBitmap(bmpID);
			if(node!=null)
			{
				return node.getFile();
			}
		}
		return null;
	}
}
