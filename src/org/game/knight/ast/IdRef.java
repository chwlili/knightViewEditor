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
	 * ���캯��
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
	 * �����ļ�
	 * 
	 * @return
	 */
	public IFile getFile()
	{
		return ast.getFile();
	}

	/**
	 * ���Ҷ���
	 * 
	 * @return
	 */
	public IdDef getTarget()
	{
		return ast.findIDDef(this);
	}

	/**
	 * ����
	 * 
	 * @return
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * ��ʼ����
	 * 
	 * @return
	 */
	public int getStart()
	{
		return start;
	}

	/**
	 * ��������
	 * 
	 * @return
	 */
	public int getStop()
	{
		return stop;
	}

	/**
	 * ƫ��
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
	 * �Ƿ�Ϊͼ������
	 * 
	 * @return
	 */
	public boolean isBitmapRef()
	{
		return type == REF_Bitmap;
	}

	/**
	 * �Ƿ�Ϊ9��ͼ����
	 * 
	 * @return
	 */
	public boolean isBitmapRenderRef()
	{
		return type == REF_BitmapRenderer;
	}

	/**
	 * �Ƿ�Ϊ�˾�����
	 * 
	 * @return
	 */
	public boolean isFilterRef()
	{
		return type == REF_Filter;
	}

	/**
	 * �Ƿ�Ϊ��ʽ����
	 * 
	 * @return
	 */
	public boolean isFormatRef()
	{
		return type == REF_Format;
	}

	/**
	 * �Ƿ�Ϊ��������
	 * 
	 * @return
	 */
	public boolean isTextRef()
	{
		return type == REF_Text;
	}

	/**
	 * �Ƿ�Ϊ�ؼ�����
	 * 
	 * @return
	 */
	public boolean isControlRef()
	{
		return type == Ref_Control;
	}
}
