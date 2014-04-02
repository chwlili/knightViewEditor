package org.game.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;

public class ViewXmlScanner implements ITokenScanner
{
	// character offset
	private int offset;
	// last token returned by nextToken()
	private DomToken lastToken;
	// token list
	private DomManager manager;

	public ViewXmlScanner()
	{
		offset = 0;
	}

	public int getTokenLength()
	{
		return lastToken.stop-lastToken.start+1;
	}

	public int getTokenOffset()
	{
		return lastToken.start;
	}
	public IToken nextToken()
	{
		if (lastToken == null)
		{
			lastToken = manager.getTokenByOffset(offset);
		}
		else
		{
			lastToken = manager.getNextToken(lastToken);
		}

		if (lastToken == null)
		{
			return Token.EOF;
		}

		return new Token(lastToken.attribute);
	}

	public void setRange(IDocument document, int offset, int length)
	{
		this.offset = offset;
		
		this.lastToken = null;
		this.manager = DomManager.getDomManager(document);
	}
}
