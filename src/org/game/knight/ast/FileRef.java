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
	 * ���캯��
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
	 * �����ļ�
	 * @return
	 */
	public IFile getFile()
	{
		return file;
	}
	
	/**
	 * ��������
	 * @return
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * ��ʼλ��
	 * @return
	 */
	public int getStart()
	{
		return start;
	}
	
	/**
	 * ����λ��
	 * @return
	 */
	public int getStop()
	{
		return stop;
	}
	
	/**
	 * ����
	 * @return
	 */
	public int getLength()
	{
		return stop-start+1;
	}
	
	/**
	 * ��ȡĿ��·��
	 * @return
	 */
	public String getTargetURL()
	{
		return targetURL;
	}
}
