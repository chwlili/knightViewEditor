package org.game.knight.ast;

import java.util.ArrayList;

import org.game.knight.ast.AST.Token;

public class DefineTextTag extends DefineTag
{

	public DefineTextTag(AST ast, boolean complex, Token start, Token stop, Token name, ArrayList<Attribute> attributes, ArrayList<Object> children)
	{
		super(ast, complex, start, stop, name, attributes, children);
	}
	
	public String getText()
	{
		if(getChildren()!=null)
		{
			StringBuilder stream=new StringBuilder();
			for(Object child:getChildren())
			{
				if(child instanceof Token)
				{
					stream.append(((Token)child).text.trim());
				}
				else if(child instanceof CDataTag)
				{
					CDataTag cdata=(CDataTag)child;
					stream.append(cdata.getText());
				}
			}
			return stream.toString();
		}
		return "";
	}
}
