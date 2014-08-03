package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.core.resources.IFile;

public class UIImage extends UIBase
{
	/**
	 * 构造函数
	 * @param antlrNode
	 */
	public UIImage(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}
	
	/**
	 * 获取位图ID
	 * @return
	 */
	public String getBitmapID()
	{
		return getAttribute("bmp");
	}

	/**
	 * 设置位图ID
	 * @return
	 */
	public void setBitmapID(String value)
	{
		setAttribute("bmp",value);
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

	/**
	 * 是否有九宫格
	 * @return
	 */
	public boolean hasGrid()
	{
		String value=getAttribute("grid");
		if(value!=null && value.trim().isEmpty())
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 删除九宫格
	 */
	public void delGrid()
	{
		setAttribute("grid", "");
	}
	
	/**
	 * 设置九宫格
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setGrid(int left,int top,int right,int bottom)
	{
		setAttribute("grid", left+","+top+","+right+","+bottom);
	}
	
	/**
	 *  九宫格左
	 * @return
	 */
	public int getGridLeft()
	{
		return getGrid(0);
	}
	
	/**
	 *  九宫格上
	 * @return
	 */
	public int getGridTop()
	{
		return getGrid(1);
	}
	
	/**
	 *  九宫格右
	 * @return
	 */
	public int getGridRight()
	{
		return getGrid(2);
	}
	
	/**
	 *  九宫格下
	 * @return
	 */
	public int getGridBottom()
	{
		return getGrid(3);
	}
	
	/**
	 * 获取九宫格元素
	 * @param index
	 * @return
	 */
	private int getGrid(int index)
	{
		String string=getAttribute("grid");
		if(string==null)
		{
			return 0;
		}
		
		int l=0;
		int r=string.length();
		int c=0;
		
		for(int i=0;i<index;i++)
		{
			c=string.indexOf(",",l);
			if(c!=-1)
			{
				l=c+1;
			}
			else
			{
				l=string.length();
				break;
			}
		}
		
		c=string.indexOf(",",l);
		if(c!=-1)
		{
			r=c;
		}
		
		if(r>l)
		{
			try
			{
				return Integer.parseInt(string.substring(l,r));
			}
			catch(Error err)
			{
			}
		}
		
		return 0;
	}
}
