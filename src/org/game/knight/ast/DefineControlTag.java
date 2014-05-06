package org.game.knight.ast;

import java.util.ArrayList;

import org.game.knight.ast.AST.Token;

public class DefineControlTag extends DefineTag
{
	private boolean root;
	
	public DefineControlTag(AST ast, boolean complex, Token start, Token stop, Token name, ArrayList<Attribute> attributes, ArrayList<Object> children,Boolean root)
	{
		super(ast, complex, start, stop, name, attributes, children);
		
		this.root=root;
	}
	
	public Boolean isRoot()
	{
		return root;
	}

}
