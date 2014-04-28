package org.game.knight.ast;

import org.game.knight.ast.AST.Token;

public class AbsTag
{
	private static int id = 1;

	public static final int Depend = id++;
	public static final int Bitmap = id++;
	public static final int BitmapRenderer = id++;
	public static final int Swf = id++;
	public static final int Filter = id++;
	public static final int Format = id++;
	public static final int Text = id++;
	public static final int Control = id++;

	public static final int DependList = id++;
	public static final int BitmapList = id++;
	public static final int BitmapRendererList = id++;
	public static final int SwfList = id++;
	public static final int FilterList = id++;
	public static final int FormatList = id++;
	public static final int TextList = id++;
	public static final int ControlList = id++;

	private int type;
	private Token start;
	private Token stop;

	/**
	 * 构造函数
	 * 
	 * @param start
	 * @param stop
	 */
	public AbsTag(int type, Token start, Token stop)
	{
		this.type = type;
		this.start = start;
		this.stop = stop;
	}

	/**
	 * 开始位置
	 * 
	 * @return
	 */
	public int getOffset()
	{
		return start.start;
	}

	/**
	 * 内容长度
	 * 
	 * @return
	 */
	public int getLength()
	{
		return stop.stop - start.start + 1;
	}

	/**
	 * 是否为列表
	 * 
	 * @return
	 */
	public boolean isList()
	{
		return DependList <= type && type <= ControlList;
	}

	public boolean isItem()
	{
		return Depend <= type && type <= Control;
	}

	public boolean isDepend()
	{
		return Depend == type;
	}

	public boolean isBitmap()
	{
		return Bitmap == type;
	}

	public boolean isBitmapRenderer()
	{
		return BitmapRenderer == type;
	}

	public boolean isSwf()
	{
		return Swf == type;
	}

	public boolean isFilter()
	{
		return Filter == type;
	}

	public boolean isFormat()
	{
		return Format == type;
	}

	public boolean isText()
	{
		return Text == type;
	}

	public boolean isControl()
	{
		return Control == type;
	}
}
