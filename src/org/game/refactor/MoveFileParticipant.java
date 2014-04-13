package org.game.refactor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.MoveParticipant;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;

public class MoveFileParticipant extends MoveParticipant
{
	private IFile file;
	public MoveFileParticipant()
	{
	}

	@Override
	protected boolean initialize(Object element)
	{
		this.file=(IFile)element;
		return true;
	}

	@Override
	public String getName()
	{
		return "重构文件引用";
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException
	{
		return RefactoringStatus.create(Status.OK_STATUS);
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException
	{
		// TODO 自动生成的方法存根
		return super.createPreChange(pm);
	}
	@Override
	public Change createPreChange(IProgressMonitor pm) throws CoreException, OperationCanceledException
	{
		CompositeChange root=new CompositeChange("重构文件引用");
		
		IFolder folder=(IFolder)getArguments().getDestination();
		
		IFile file=this.file;
		IFile last=folder.getFile(file.getName());
		try
		{
			String path=Project.getViewURL(last);
			List<FileRef> refs=Project.getFileRefs(file);
			
			for(FileRef ref:refs)
			{
				TextChange old=getTextChange(ref.owner);
				if(old!=null)
				{
					old.addEdit(new ReplaceEdit(ref.start,ref.stop-ref.start+1,path));
				}
				else
				{
					TextFileChange change=new TextFileChange("",ref.owner);
					change.setEdit(new MultiTextEdit());
					change.addEdit(new ReplaceEdit(ref.start,ref.stop-ref.start+1,path));
					root.add(change);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return root;
	}

	public Change createChange111111(IProgressMonitor pm) throws CoreException, OperationCanceledException
	{
		CompositeChange root=new CompositeChange("重构文件引用");
		
		IFolder folder=(IFolder)getArguments().getDestination();
		
		Hashtable<String, ArrayList<Range>> allChange=new Hashtable<String, ArrayList<Range>>();
		
		for(Object element : getProcessor().getElements())
		{
			IFile file=(IFile)element;
			IFile last=folder.getFile(file.getName());
			
			try
			{
				String path=Project.getViewURL(last);
				List<FileRef> refs=Project.getFileRefs(file);
				
				for(FileRef ref:refs)
				{
					Range range=new Range(ref.owner,ref.start,ref.stop-ref.start+1,path);
					
					String key=ref.owner.getLocation().toString();
					ArrayList<Range> list=allChange.containsKey(key) ? allChange.get(key):null;
					if(list==null)
					{
						list=new ArrayList<Range>();
						allChange.put(key, list);
					}
					list.add(range);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		for(ArrayList<Range> list:allChange.values())
		{
			Collections.sort(list, new Comparator<Range>()
			{
				@Override
				public int compare(Range o1, Range o2)
				{
					if(o1.offset<o2.offset)
					{
						return -1;
					}
					else if(o1.offset>o2.offset)
					{
						return 1;
					}
					return 0;
				}
			});
			
			int offset=0;
			for(Range range:list)
			{
				range.offset+=offset;
				
				TextFileChange change=new TextFileChange("haha",range.file);
				change.setEdit(new ReplaceEdit(range.offset,range.length,range.text));
				root.add(change);
				
				offset+=range.text.length()-range.length;
			}
		}
		
		return root;
	}
	
	private class Range
	{
		public IFile file;
		public int offset;
		public int length;
		public String text;
		
		public Range(IFile file,int offset,int length,String text)
		{
			this.file=file;
			this.offset=offset;
			this.length=length;
			this.text=text;
		}
	}
}
