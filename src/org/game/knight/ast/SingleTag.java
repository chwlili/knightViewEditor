package org.game.knight.ast;

import java.util.ArrayList;

import org.game.knight.ast.AST.Token;

public class SingleTag
{
	private Token first;
	private Token last;
	private Token name;
	private ArrayList<TagAttribute> attributes;
	
	public SingleTag(Token first, Token last, Token name, ArrayList<TagAttribute> attributes)
	{
		this.first=first;
		this.last=last;
		this.name=name;
		this.attributes=attributes;
	}
	
	public Token getFirst()
	{
		return first;
	}
	
	public Token getLast()
	{
		return last;
	}
	
	public Token getName()
	{
		return name;
	}
	
}
