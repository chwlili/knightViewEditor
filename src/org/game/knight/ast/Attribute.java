package org.game.knight.ast;

public class Attribute
{
	private AST.Token start;
	private AST.Token stop;
	private AST.Token name;
	private AST.Token value;
	
	public Attribute(AST.Token start,AST.Token stop,AST.Token name,AST.Token value)
	{
		this.start=start;
		this.stop=stop;
		this.name=name;
		this.value=value;
	}
	
	public AST.Token getStartToken()
	{
		return start;
	}

	public AST.Token getStopToken()
	{
		return stop;
	}

	public AST.Token getNameToken()
	{
		return name;
	}

	public AST.Token getValueToken()
	{
		return value;
	}
	
	/**
	 * 获取属性名称
	 * @return
	 */
	public String getName()
	{
		return name.text;
	}
	
	/**
	 * 获取属性值
	 * @return
	 */
	public String getValue()
	{
		return value.text;
	}
}
