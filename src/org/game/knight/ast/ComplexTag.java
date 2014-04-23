package org.game.knight.ast;

import java.util.ArrayList;
import java.util.Hashtable;

import org.game.knight.ast.AST.Token;

public class ComplexTag extends Tag
{
	private ArrayList<Object> children;
	private Hashtable<String, TagAttribute> attributeTable;

	public ComplexTag(Token start, Token stop, Token name, ArrayList<TagAttribute> attributes, ArrayList<Object> children)
	{
		super(start, stop, name, attributes);
		
		this.children = children;
		if (attributes != null)
		{
			for (TagAttribute attribute : attributes)
			{
				attributeTable.put(attribute.getNameToken().text, attribute);
			}
		}
	}
	/**
	 * 获取子级列表
	 * @return
	 */
	public ArrayList<Object> getChildren()
	{
		return children;
	}
}
