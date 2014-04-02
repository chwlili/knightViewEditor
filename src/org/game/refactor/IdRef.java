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
	 * ���캯��
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
	 * ��ȡ������
	 * @return
	 */
	public IFile getOwner()
	{
		return dom.getFile();
	}
	
	/**
	 * ��ȡĿ��
	 * @return
	 */
	public IdDef getTarget()
	{
		return dom.findIDDef(this);
	}
	
	/**
	 * ����
	 * @return
	 */
	public int getType()
	{
		return type;
	}
	
	/**
	 * ����
	 * @return
	 */
	public String getText()
	{
		return id;
	}
	
	/**
	 * ��ʼ����
	 * @return
	 */
	public int getStart()
	{
		return start;
	}
	
	/**
	 * ��������
	 * @return
	 */
	public int getStop()
	{
		return stop;
	}
	
	/**
	 * ƫ��
	 * @return
	 */
	public int getOffset()
	{
		return start;
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
	 * �Ƿ�Ϊͼ������
	 * @return
	 */
	public boolean isBitmapRef()
	{
		return type==BMP;
	}
	
	/**
	 * �Ƿ�Ϊ9��ͼ����
	 * @return
	 */
	public boolean isBitmapRenderRef()
	{
		return type==BITMAP_RENDER;
	}
	
	/**
	 * �Ƿ�Ϊ�˾�����
	 * @return
	 */
	public boolean isFilterRef()
	{
		return type==FILTER;
	}
	
	/**
	 * �Ƿ�Ϊ��ʽ����
	 * @return
	 */
	public boolean isFormatRef()
	{
		return type==FORMAT;
	}
	
	/**
	 * �Ƿ�Ϊ��������
	 * @return
	 */
	public boolean isTextRef()
	{
		return type==TXT;
	}
	
	/**
	 * �Ƿ�Ϊ�ؼ�����
	 * @return
	 */
	public boolean isControlRef()
	{
		return type==CONTROL;
	}
}
