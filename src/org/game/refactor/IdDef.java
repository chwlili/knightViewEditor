package org.game.refactor;

import org.eclipse.core.resources.IFile;

public class IdDef
{
	private IFile file;
	private String id;
	private int start;
	private int stop;
	private Object ref;
	
	public IdDef(IFile file,String id,int start,int stop)
	{
		this.file=file;
		this.id=id;
		this.start=start;
		this.stop=stop;
		this.ref=null;
	}
	
	public IdDef(IFile file,String id,int start,int stop,Object ref)
	{
		this(file,id,start,stop);
		this.ref=ref;
	}
	
	public IFile getFile()
	{
		return file;
	}
	
	public String getID()
	{
		return id;
	}
	
	public int getStart()
	{
		return start;
	}
	
	public int getStop()
	{
		return stop;
	}
	
	public int getOffset()
	{
		return start;
	}
	
	public int getLength()
	{
		return stop-start+1;
	}
	
	public Object getRef()
	{
		return ref;
	}
}
