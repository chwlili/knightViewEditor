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
	 * ���캯��
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
	 * ���캯��
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
	 * �����ļ�
	 * 
	 * @return
	 */
	public IFile getFile()
	{
		return file;
	}

	/**
	 * ID����
	 * 
	 * @return
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * ��ʼλ��
	 * 
	 * @return
	 */
	public int getStart()
	{
		return start;
	}

	/**
	 * ����λ��
	 * 
	 * @return
	 */
	public int getStop()
	{
		return stop;
	}

	/**
	 * ��ʼλ��
	 * 
	 * @return
	 */
	public int getOffset()
	{
		return start;
	}

	/**
	 * ����
	 * 
	 * @return
	 */
	public int getLength()
	{
		return stop - start + 1;
	}

	/**
	 * �ض�������
	 * 
	 * @return
	 */
	public Object getRef()
	{
		return ref;
	}
}
