package org.game.refactor;

import org.eclipse.core.resources.IFile;

public class FileRef
{
	public IFile owner;
	public String text;
	public int start;
	public int stop;
	public String filePath;
	
	public FileRef(IFile owner,String text,int start,int stop,String filePath)
	{
		this.owner=owner;
		this.text=text;
		this.start=start;
		this.stop=stop;
		this.filePath=filePath;
	}
}
