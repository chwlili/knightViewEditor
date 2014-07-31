package org.game.knight.editor.xml;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Document;

public class FileDocument extends Document
{
	private IFile file;
	
	public IFile getFile()
	{
		return file;
	}
	
	public void setFile(IFile file)
	{
		this.file=file;
	}
}
