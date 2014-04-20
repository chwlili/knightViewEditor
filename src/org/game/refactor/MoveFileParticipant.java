package org.game.refactor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
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
	private IResource file;

	public MoveFileParticipant()
	{
	}

	@Override
	protected boolean initialize(Object element)
	{
		if (element instanceof IResource)
		{
			this.file = (IResource) element;
			return true;
		}
		return false;
	}

	@Override
	public String getName()
	{
		return "更新文件\""+file.getProjectRelativePath().toString()+"\"";
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException
	{
		return RefactoringStatus.create(Status.OK_STATUS);
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException
	{
		return null;
	}
	
	@Override
	public Change createPreChange(IProgressMonitor pm) throws CoreException, OperationCanceledException
	{
		Hashtable<IFile, ArrayList<ReplaceEdit>> file_edits = new Hashtable<IFile, ArrayList<ReplaceEdit>>();

		//移动操作的目标文件夹
		IFolder folder = (IFolder) getArguments().getDestination();
		
		//移动操作涉及的所有文件
		IResource[] fromFiles = new IResource[getProcessor().getElements().length];
		Object[] items = getProcessor().getElements();
		for (int i = 0; i < items.length; i++)
		{
			fromFiles[i] = (IResource) items[i];
		}

		//确定文件变动
		try
		{
			Project.Change[] changes=Project.moveResource((IResource) file, folder, fromFiles, pm);
			for(Project.Change change:changes)
			{
				if (!file_edits.containsKey(change.owner))
				{
					file_edits.put(change.owner, new ArrayList<ReplaceEdit>());
				}

				file_edits.get(change.owner).add(new ReplaceEdit(change.offset, change.length, change.text));
			}
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		//合并文件变动
		CompositeChange root = null;
		for (IFile file : file_edits.keySet())
		{
			TextChange fileEdit=getTextChange(file);
			if(fileEdit==null)
			{
				if(root==null)
				{
					root=new CompositeChange("更新对\""+this.file.getLocation().toString()+"\"的引用");
				}
				
				fileEdit= new TextFileChange("更改文件\""+file.getLocation().toString()+"\"", file);
				fileEdit.setEdit(new MultiTextEdit());
				root.add(fileEdit);
			}
			
			for (ReplaceEdit edit : file_edits.get(file))
			{
				fileEdit.addEdit(edit);
			}
		}
		return root;
	}
}
