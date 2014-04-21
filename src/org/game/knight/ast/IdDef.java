package org.game.knight.ast;

import org.eclipse.core.resources.IFile;

public class IdDef
{
	private IFile file;
	private String text;
	private int start;
	private int stop;
	private Object ref;

	/**
	 * 构造函数
	 * 
	 * @param file
	 * @param id
	 * @param start
	 * @param stop
	 */
	public IdDef(IFile file, String text, int start, int stop)
	{
		this.file = file;
		this.text = text;
		this.start = start;
		this.stop = stop;
		this.ref = null;
	}

	/**
	 * 构造函数
	 * 
	 * @param file
	 * @param id
	 * @param start
	 * @param stop
	 * @param ref
	 */
	public IdDef(IFile file, String text, int start, int stop, Object ref)
	{
		this(file, text, start, stop);
		this.ref = ref;
	}

	/**
	 * 所属文件
	 * 
	 * @return
	 */
	public IFile getFile()
	{
		return file;
	}

	/**
	 * ID内容
	 * 
	 * @return
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * 开始位置
	 * 
	 * @return
	 */
	public int getStart()
	{
		return start;
	}

	/**
	 * 结束位置
	 * 
	 * @return
	 */
	public int getStop()
	{
		return stop;
	}

	/**
	 * 开始位置
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
	 * 重定向引用
	 * 
	 * @return
	 */
	public Object getRef()
	{
		return ref;
	}
}
