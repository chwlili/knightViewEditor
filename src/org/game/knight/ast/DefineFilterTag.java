package org.game.knight.ast;

import java.util.ArrayList;

import org.game.knight.ast.AST.Token;

public class DefineFilterTag extends DefineTag
{

	public DefineFilterTag(AST ast, boolean complex, Token start, Token stop, Token name, ArrayList<Attribute> attributes, ArrayList<Object> children)
	{
		super(ast, complex, start, stop, name, attributes, children);
	}

	public String getID()
	{
		if (hasAttribute("id"))
		{
			return getAttribute("id").getValue();
		}
		return null;
	}

}
