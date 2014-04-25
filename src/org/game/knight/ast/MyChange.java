package org.game.knight.ast;

import org.eclipse.core.resources.IFile;

public class MyChange
{
	public IFile owner;
	public int offset;
	public int length;
	public String text;
}