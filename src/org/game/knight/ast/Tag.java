package org.game.knight.ast;

import java.util.ArrayList;
import java.util.Hashtable;

import org.game.knight.ast.AST.Token;

public class Tag
{
	private Token first;
	private Token last;
	private Token name;
	private ArrayList<TagAttribute> attributes;
	private Hashtable<String, TagAttribute> attributeTable;

	public Tag(Token first, Token last, Token name, ArrayList<TagAttribute> attributes)
	{
		this.first = first;
		this.last = last;
		this.name = name;
		this.attributes = attributes;
		this.attributeTable = new Hashtable<String, TagAttribute>();
		
		if (attributes != null)
		{
			for (TagAttribute attribute : attributes)
			{
				attributeTable.put(attribute.getNameToken().text, attribute);
			}
		}
	}

	public Token getFirstToken()
	{
		return first;
	}

	public Token getLastToken()
	{
		return last;
	}

	public Token getNameToken()
	{
		return name;
	}
	
	/**
	 * 获取标记名称
	 * @return
	 */
	public String getName()
	{
		return name.text;
	}
	
	/**
	 * 获取属性列表
	 * @return
	 */
	public ArrayList<TagAttribute> getAttributes()
	{
		return attributes;
	}
	
	/**
	 * 是否是指定的属性
	 * @param name
	 * @return
	 */
	public boolean hasAttribute(String name)
	{
		return attributeTable.contains(name);
	}
	
	/**
	 * 获取属性
	 * @param name
	 * @return
	 */
	public TagAttribute getAttributeToken(String name)
	{
		if(attributeTable.contains(name))
		{
			return attributeTable.get(name);
		}
		return null;
	}
	
	/**
	 * 获取属性值
	 * @param name
	 * @return
	 */
	public String getAttributeValue(String name)
	{
		if(attributeTable.contains(name))
		{
			return attributeTable.get(name).getValue();
		}
		return null;
	}
}
