package org.game.knight.editor.xml;

import java.util.ArrayList;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.Color;
import org.game.knight.ast.AST;
import org.game.knight.ast.FileAstManager;

public class ViewXmlScanner implements ITokenScanner
{
	private TextAttribute ProcessColor = new TextAttribute(new Color(null, 255, 0, 0));
	private TextAttribute DtdColor = new TextAttribute(new Color(null, 255, 0, 255));
	private TextAttribute CommentColor = new TextAttribute(new Color(null, 63, 95, 191));
	private TextAttribute CdataColor = new TextAttribute(new Color(null, 0, 0, 0));
	private TextAttribute TagColor = new TextAttribute(new Color(null, 0, 128, 128));
	private TextAttribute AttrColor = new TextAttribute(new Color(null, 127, 0, 127));
	private TextAttribute AttrValueColor = new TextAttribute(new Color(null, 42, 0, 255));
	private TextAttribute DefaultColor = new TextAttribute(new Color(null, 0, 0, 0));

	private AST ast;
	private int offset;
	private AST.Token last;
	private int lastIndex;

	public ViewXmlScanner()
	{
		offset = 0;
	}

	public int getTokenOffset()
	{
		return last.start;
	}

	public int getTokenLength()
	{
		return last.stop - last.start + 1;
	}

	public IToken nextToken()
	{
		if (last == null)
		{
			ArrayList<AST.Token> tokens = ast.getTokens();
			for (int i = 0; i < tokens.size(); i++)
			{
				AST.Token token = tokens.get(i);
				if (token.start <= offset && offset <= token.stop)
				{
					last = token;
					lastIndex = i;
					break;
				}
			}
		}
		else
		{
			int index = lastIndex + 1;
			if (index < ast.getTokens().size())
			{
				last = ast.getTokens().get(index);
				lastIndex = index;
			}
			else
			{
				last = null;
				lastIndex = -1;
			}
		}

		if (last == null)
		{
			return Token.EOF;
		}

		Object color = DefaultColor;
		if (last != null)
		{
			int type=last.type;
			if(type==AST.Token.PROCESS)
			{
				color = ProcessColor;
			}
			else if(type==AST.Token.DTD)
			{
				color=DtdColor;
			}
			else if(type==AST.Token.COMMENT)
			{
				color=CommentColor;
			}
			else if(type==AST.Token.CDATA)
			{
				color=CdataColor;
			}
			else if(type==AST.Token.CDATA_BEGIN || type== AST.Token.CDATA_END)
			{
				color=TagColor;
			}
			else if(type==AST.Token.TAG_L || type==AST.Token.TAG_NAME || type==AST.Token.TAG_SLASH || type==AST.Token.TAG_R)
			{
				color=TagColor;
			}
			else if(type==AST.Token.ATT_NAME || type==AST.Token.ATT_EQUALS || type==AST.Token.ATT_BEGIN || type==AST.Token.ATT_END)
			{
				color=AttrColor;
			}
			else if(type==AST.Token.ATT_VALUE)
			{
				color=AttrValueColor;
			}
		}

		return new Token(color);
	}

	public void setRange(IDocument document, int offset, int length)
	{
		this.offset = offset;
		this.last = null;
		this.ast = FileAstManager.getFileSummay(DomManager.getFileByDocument(document)).getAST();
	}
}
