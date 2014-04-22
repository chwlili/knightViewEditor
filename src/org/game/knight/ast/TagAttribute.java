package org.game.knight.ast;

public class TagAttribute
{
	private AST.Token start;
	private AST.Token stop;
	private AST.Token name;
	private AST.Token value;
	
	public TagAttribute(AST.Token start,AST.Token stop,AST.Token name,AST.Token value)
	{
		this.start=start;
		this.stop=stop;
		this.name=name;
		this.value=value;
	}
	
	public AST.Token getStart()
	{
		return start;
	}

	public AST.Token getStop()
	{
		return stop;
	}

	public AST.Token getName()
	{
		return name;
	}

	public AST.Token getValue()
	{
		return value;
	}
}
