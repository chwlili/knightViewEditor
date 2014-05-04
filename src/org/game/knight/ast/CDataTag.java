package org.game.knight.ast;

import org.game.knight.ast.AST.Token;

public class CDataTag
{
	private Token start;
	private Token stop;
	private Token text;
	
	public CDataTag(Token start,Token stop,Token text)
	{
		this.start=start;
		this.stop=stop;
		this.text=text;
	}
	
	public String getText()
	{
		return text.text;
	}
}
