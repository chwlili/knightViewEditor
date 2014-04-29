package org.game.knight.ast;

import java.util.ArrayList;

import org.game.knight.ast.AST.Token;

public class ImportXmlTag extends AbsTag
{
	public ImportXmlTag(AST ast, boolean complex, Token start, Token stop, Token name, ArrayList<Attribute> attributes, ArrayList<Object> children)
	{
		super(ast, complex, start, stop, name, attributes, children);
	}

	public String getSrc()
	{
		if (hasAttribute("src"))
		{
			return getAttribute("src").getValue();
		}
		return null;
	}
}
