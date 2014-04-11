package org.game.refactor;

import java.io.IOException;
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
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.MoveParticipant;
import org.eclipse.text.edits.ReplaceEdit;

public class MoveFileParticipant extends MoveParticipant
{
	public MoveFileParticipant()
	{
	}

	@Override
	protected boolean initialize(Object element)
	{
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
		CompositeChange root=new CompositeChange("重构文件引用");
		
		IFolder folder=(IFolder)getArguments().getDestination();
		
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
					TextFileChange change=new TextFileChange("haha",ref.owner);
					change.setEdit(new ReplaceEdit(ref.start,ref.stop-ref.start+1,path));
					root.add(change);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return root;
	}

}
