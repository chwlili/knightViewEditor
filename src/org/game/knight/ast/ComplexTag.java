package org.game.knight.ast;

import java.util.ArrayList;
import java.util.Hashtable;

import org.game.knight.ast.AST.Token;

public class ComplexTag extends SingleTag
{
	private ArrayList<Object> children;
	private Hashtable<String, Attribute> attributeTable = new Hashtable<String, Attribute>();

	public ComplexTag(int type, Token start, Token stop, Token name, ArrayList<Attribute> attributes, ArrayList<Object> children)
	{
		super(type, start, stop, name, attributes);

		this.children = children;
		if (attributes != null)
		{
			for (Attribute attribute : attributes)
			{
				attributeTable.put(attribute.getNameToken().text, attribute);
			}
		}
	}

	/**
	 * 获取子级列表
	 * 
	 * @return
	 */
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}
