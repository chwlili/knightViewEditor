package org.game.knight.editor.xml;

import org.eclipse.jface.text.TextAttribute;

public class DomToken
{
	/**
	 * ��ʼ����
	 */
	public int start;
	
	/**
	 * ��������
	 */
	public int stop;
	
	/**
	 * ������
	 */
	public int index;
	
	/**
	 * ��������
	 */
	public TextAttribute attribute;
	
	/**
	 * ���캯��
	 * @param start
	 * @param stop
	 * @param attribute
	 */
	public DomToken(int start,int stop,TextAttribute attribute)
	{
		this.start=start;
		this.stop=stop;
		this.attribute=attribute;
	}
}
