package org.game.knight.search;

import org.eclipse.search.ui.text.Match;

public class SearchMatch extends Match
{
	private String text;

	public SearchMatch(Object element, String text, int offset, int length)
	{
		super(element, offset, length);

		this.text = text;
	}

	public String getText()
	{
		return text;
	}
}
