package org.game.knight.ast;

import org.eclipse.core.resources.IFile;

public class FileRef
{
	private IFile file;
	private String text;
	private int start;
	private int stop;
	private String targetURL;
	
	/**
	 * 构造函数
	 * @param owner
	 * @param text
	 * @param start
	 * @param stop
	 * @param filePath
	 */
	public FileRef(IFile file,String text,int start,int stop,String targetURL)
	{
		this.file=file;
		this.text=text;
		this.start=start;
		this.stop=stop;
		this.targetURL=targetURL;
	}
	
	/**
	 * 所属文件
	 * @return
	 */
	public IFile getFile()
	{
		return file;
	}
	
	/**
	 * 文字内容
	 * @return
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * 开始位置
	 * @return
	 */
	public int getStart()
	{
		return start;
	}
	
	/**
	 * 结束位置
	 * @return
	 */
	public int getStop()
	{
		return stop;
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
	 * 获取目标路径
	 * @return
	 */
	public String getTargetURL()
	{
		return targetURL;
	}
}
