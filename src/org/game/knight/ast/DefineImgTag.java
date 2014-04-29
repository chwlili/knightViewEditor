package org.game.knight.ast;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.game.knight.ast.AST.Token;

public class DefineImgTag extends DefineTag
{
	public DefineImgTag(AST ast, boolean complex, Token start, Token stop, Token name, ArrayList<Attribute> attributes, ArrayList<Object> children)
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
	
	public IFile getImg()
	{
		if(hasAttribute("src"))
		{
			FileRef ref=getAST().getLinks().getFileRef(getAttribute("src").getValueToken().start);
			if(ref!=null)
			{
				return ASTManager.getSourceFile(ref.getFile().getProject(),ref.getTargetURL());
			}
		}
		return null;
	}
}
