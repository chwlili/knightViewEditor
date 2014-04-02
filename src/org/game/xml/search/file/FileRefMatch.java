package org.game.xml.search.file;

import org.eclipse.search.ui.text.Match;

public class FileRefMatch extends Match
{
	private String text;

	public FileRefMatch(Object element, String text, int offset, int length)
	{
		super(element, offset, length);

		this.text = text;
	}

	public String getText()
	{
		return text;
	}
}
