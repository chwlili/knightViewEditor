package org.game.knight.ast2;

import java.io.IOException;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class DependNode extends BaseTagNode
{

	public DependNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}
	
	public ViewDocument getDependDocument()
	{
		IFile file=getDocument().resolveFile(getAttribute("src"));
		if(file!=null)
		{
			try
			{
				return ViewDocumentFactory.getViewAST(file);
			}
			catch (CoreException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
}
