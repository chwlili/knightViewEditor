package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class DependNode extends BaseTagNode
{

	public DependNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}
	
	public ViewDocument getDependDocument()
	{
		ViewDocument dom=getDocument();
		String path=dom.resolvePath(getAttribute("src"));
	}
}
