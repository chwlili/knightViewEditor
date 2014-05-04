package org.game.knight.ast;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.game.knight.ast.AST.Token;

public class DefineGridImgTag extends DefineTag
{
	public DefineGridImgTag(AST ast, boolean complex, Token start, Token stop, Token name, ArrayList<Attribute> attributes, ArrayList<Object> children)
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
		if(hasAttribute("bmp"))
		{
			IdRef idRef=getAST().getLinks().getIdRef(getAttribute("bmp").getValueToken().start);
			if(idRef!=null)
			{
				IdDef idDef=idRef.getTarget();
				Object refTo=idDef.getRef();
				if(refTo instanceof FileRef)
				{
					FileRef fileRef=(FileRef)refTo;
					return ASTManager.getSourceFile(fileRef.getFile().getProject(),fileRef.getTargetURL());
				}
			}
		}
		return null;
	}

}
