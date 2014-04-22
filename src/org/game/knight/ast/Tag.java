package org.game.knight.ast;

import java.util.ArrayList;

import org.game.knight.ast.AST.Token;

public class Tag
{
	private Token start;
	private Token stop;
	private Token name;
	private ArrayList<TagAttribute> attributes;
	private ArrayList<Object> children;

	public Tag(Token start, Token stop, Token name, ArrayList<TagAttribute> attributes, ArrayList<Object> children)
	{
		this.start = start;
		this.stop = stop;
		this.name = name;
		this.attributes = attributes;
		this.children = children;
	}
}
