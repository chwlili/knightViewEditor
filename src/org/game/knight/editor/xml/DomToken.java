package org.game.knight.editor.xml;

import org.eclipse.jface.text.TextAttribute;

public class DomToken
{
	/**
	 * 起始索引
	 */
	public int start;
	
	/**
	 * 结束索引
	 */
	public int stop;
	
	/**
	 * 索引号
	 */
	public int index;
	
	/**
	 * 显增属性
	 */
	public TextAttribute attribute;
	
	/**
	 * 构造函数
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
