package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.core.resources.IFile;

public class UIImage extends UIBase
{
	/**
	 * ���캯��
	 * @param antlrNode
	 */
	public UIImage(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}
	
	/**
	 * ��ȡλͼID
	 * @return
	 */
	public String getBitmapID()
	{
		return getAttribute("bmp");
	}

	/**
	 * ����λͼID
	 * @return
	 */
	public void setBitmapID(String value)
	{
		setAttribute("bmp",value);
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

	/**
	 * �Ƿ��оŹ���
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
	 * ɾ���Ź���
	 */
	public void delGrid()
	{
		setAttribute("grid", "");
	}
	
	/**
	 * ���þŹ���
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
	 *  �Ź�����
	 * @return
	 */
	public int getGridLeft()
	{
		return getGrid(0);
	}
	
	/**
	 *  �Ź�����
	 * @return
	 */
	public int getGridTop()
	{
		return getGrid(1);
	}
	
	/**
	 *  �Ź�����
	 * @return
	 */
	public int getGridRight()
	{
		return getGrid(2);
	}
	
	/**
	 *  �Ź�����
	 * @return
	 */
	public int getGridBottom()
	{
		return getGrid(3);
	}
	
	/**
	 * ��ȡ�Ź���Ԫ��
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
