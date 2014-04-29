package org.game.knight.ast;

import java.util.ArrayList;

import org.game.knight.ast.AST.Token;

public class DefineGridImgTag extends DefineTag
{
	public DefineGridImgTag(AST ast, boolean complex, Token start, Token stop, Token name, ArrayList<Attribute> attributes, ArrayList<Object> children)
	{
		super(ast, complex, start, stop, name, attributes, children);
	}

}
