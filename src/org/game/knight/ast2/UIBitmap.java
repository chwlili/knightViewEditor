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
	
	/**
	 * ��ȡͼ���ļ�
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
