package org.game.refactor;

import org.eclipse.core.resources.IFile;

public class Ref
{
	public IFile file;
	
	public int start;
	
	public int stop;
	
	public String url;
	
	public Ref(IFile file,int start,int stop,String url)
	{
		this.file=file;
		this.start=start;
		this.stop=stop;
		this.url=url;
	}
}
