package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.core.resources.IFile;

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
	
	/**
	 * ��ȡ���е��ļ�
	 * @return
	 */
	public IFile getFile()
	{
		if(hasAttribute("dependId"))
		{
			BitmapNode node=getDocument().findBitmap(getDependID());
			if(node!=null && node!=this)
			{
				return node.getFile();
			}
			return null;
		}
		else
		{
			return getDocument().resolveFile(getSrc());
		}
	}
}
