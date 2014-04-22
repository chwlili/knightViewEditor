package org.game.knight.ast;

import java.util.ArrayList;

import org.game.knight.ast.AST.Token;

public class Dom
{
	private ArrayList<Token> tokens;
	private ArrayList<Object> children;

	public Dom(ArrayList<Token> tokens, ArrayList<Object> children)
	{
		this.tokens = tokens;
		this.children = children;
	}

	public ArrayList<Token> getTokens()
	{
		return tokens;
	}

	public ArrayList<Object> getChildren()
	{
		return children;
	}
}
