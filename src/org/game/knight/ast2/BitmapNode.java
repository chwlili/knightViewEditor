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
	
	/**
	 * 获取引有的文件
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
