package org.game.knight.ast;

import java.util.ArrayList;

import org.game.knight.ast.AST.Token;

public class DefineFormatTag extends DefineTag
{

	public DefineFormatTag(AST ast, boolean complex, Token start, Token stop, Token name, ArrayList<Attribute> attributes, ArrayList<Object> children)
	{
		super(ast, complex, start, stop, name, attributes, children);
	}

}
