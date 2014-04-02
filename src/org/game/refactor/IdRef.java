package org.game.refactor;

import org.eclipse.core.resources.IFile;


public class IdRef
{
	public static final int BMP=1;
	public static final int BITMAP_RENDER=2;
	public static final int FILTER=3;
	public static final int FORMAT=4;
	public static final int TXT=5;
	public static final int CONTROL=6;
	
	private FileSummay dom;
	private int type;
	private String id;
	private int start;
	private int stop;
	
	/**
	 * 构造函数
	 * @param type
	 * @param id
	 * @param start
	 * @param stop
	 */
	public IdRef(FileSummay dom,int type,String id,int start,int stop)
	{
		this.dom=dom;
		this.type=type;
		this.id=id;
		this.start=start;
		this.stop=stop;
	}
	
	/**
	 * 获取所有者
	 * @return
	 */
	public IFile getOwner()
	{
		return dom.getFile();
	}
	
	/**
	 * 获取目标
	 * @return
	 */
	public IdDef getTarget()
	{
		return dom.findIDDef(this);
	}
	
	/**
	 * 类型
	 * @return
	 */
	public int getType()
	{
		return type;
	}
	
	/**
	 * 内容
	 * @return
	 */
	public String getText()
	{
		return id;
	}
	
	/**
	 * 起始索引
	 * @return
	 */
	public int getStart()
	{
		return start;
	}
	
	/**
	 * 结束索引
	 * @return
	 */
	public int getStop()
	{
		return stop;
	}
	
	/**
	 * 偏移
	 * @return
	 */
	public int getOffset()
	{
		return start;
	}
	
	/**
	 * 长度
	 * @return
	 */
	public int getLength()
	{
		return stop-start+1;
	}
	
	/**
	 * 是否为图像引用
	 * @return
	 */
	public boolean isBitmapRef()
	{
		return type==BMP;
	}
	
	/**
	 * 是否为9宫图引用
	 * @return
	 */
	public boolean isBitmapRenderRef()
	{
		return type==BITMAP_RENDER;
	}
	
	/**
	 * 是否为滤镜引用
	 * @return
	 */
	public boolean isFilterRef()
	{
		return type==FILTER;
	}
	
	/**
	 * 是否为格式引用
	 * @return
	 */
	public boolean isFormatRef()
	{
		return type==FORMAT;
	}
	
	/**
	 * 是否为文字引用
	 * @return
	 */
	public boolean isTextRef()
	{
		return type==TXT;
	}
	
	/**
	 * 是否为控件引用
	 * @return
	 */
	public boolean isControlRef()
	{
		return type==CONTROL;
	}
}
