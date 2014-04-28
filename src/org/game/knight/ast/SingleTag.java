package org.game.knight.ast;

import java.util.ArrayList;
import java.util.Hashtable;

import org.game.knight.ast.AST.Token;

public class SingleTag extends AbsTag
{
	private Token name;
	private ArrayList<Attribute> attributes;
	private Hashtable<String, Attribute> attributeTable;

	public SingleTag(int type, Token first, Token last, Token name, ArrayList<Attribute> attributes)
	{
		super(type, first, last);

		this.name = name;
		this.attributes = attributes;
		this.attributeTable = new Hashtable<String, Attribute>();

		if (attributes != null)
		{
			for (Attribute attribute : attributes)
			{
				attributeTable.put(attribute.getNameToken().text, attribute);
			}
		}
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return
	 */
	public String getName()
	{
		return name.text;
	}

	/**
	 * ��ȡ�����б�
	 * 
	 * @return
	 */
	public ArrayList<Attribute> getAttributes()
	{
		return attributes;
	}

	/**
	 * �Ƿ���ָ��������
	 * 
	 * @param name
	 * @return
	 */
	public boolean hasAttribute(String name)
	{
		return attributeTable.containsKey(name);
	}

	/**
	 * ��ȡ����
	 * 
	 * @param name
	 * @return
	 */
	public Attribute getAttribute(String name)
	{
		if (attributeTable.containsKey(name))
		{
			return attributeTable.get(name);
		}
		return null;
	}

	/**
	 * ��ȡ����ֵ
	 * 
	 * @param name
	 * @return
	 */
	public String getAttributeValue(String name)
	{
		if (attributeTable.containsKey(name))
		{
			return attributeTable.get(name).getValue();
		}
		return null;
	}
}
