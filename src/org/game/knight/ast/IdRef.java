package org.game.knight.ast;

import org.eclipse.core.resources.IFile;

public class IdRef
{
	public static final int REF_Bitmap = 1;
	public static final int REF_BitmapRenderer = 2;
	public static final int REF_Filter = 3;
	public static final int REF_Format = 4;
	public static final int REF_Text = 5;
	public static final int Ref_Control = 6;

	private FileAst ast;
	private int type;
	private String text;
	private int start;
	private int stop;

	/**
	 * 构造函数
	 * 
	 * @param type
	 * @param id
	 * @param start
	 * @param stop
	 */
	public IdRef(FileAst ast, int type, String text, int start, int stop)
	{
		this.ast = ast;
		this.type = type;
		this.text = text;
		this.start = start;
		this.stop = stop;
	}

	/**
	 * 所属文件
	 * 
	 * @return
	 */
	public IFile getFile()
	{
		return ast.getFile();
	}

	/**
	 * 查找定义
	 * 
	 * @return
	 */
	public IdDef getTarget()
	{
		return ast.findIDDef(this);
	}

	/**
	 * 内容
	 * 
	 * @return
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * 起始索引
	 * 
	 * @return
	 */
	public int getStart()
	{
		return start;
	}

	/**
	 * 结束索引
	 * 
	 * @return
	 */
	public int getStop()
	{
		return stop;
	}

	/**
	 * 偏移
	 * 
	 * @return
	 */
	public int getOffset()
	{
		return start;
	}

	/**
	 * 长度
	 * 
	 * @return
	 */
	public int getLength()
	{
		return stop - start + 1;
	}

	/**
	 * 是否为图像引用
	 * 
	 * @return
	 */
	public boolean isBitmapRef()
	{
		return type == REF_Bitmap;
	}

	/**
	 * 是否为9宫图引用
	 * 
	 * @return
	 */
	public boolean isBitmapRenderRef()
	{
		return type == REF_BitmapRenderer;
	}

	/**
	 * 是否为滤镜引用
	 * 
	 * @return
	 */
	public boolean isFilterRef()
	{
		return type == REF_Filter;
	}

	/**
	 * 是否为格式引用
	 * 
	 * @return
	 */
	public boolean isFormatRef()
	{
		return type == REF_Format;
	}

	/**
	 * 是否为文字引用
	 * 
	 * @return
	 */
	public boolean isTextRef()
	{
		return type == REF_Text;
	}

	/**
	 * 是否为控件引用
	 * 
	 * @return
	 */
	public boolean isControlRef()
	{
		return type == Ref_Control;
	}
}
