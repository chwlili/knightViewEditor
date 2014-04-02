package org.game.refactor;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.eclipse.text.edits.ReplaceEdit;
import org.game.project.ViewProjectNature;

public class RenameFileParticipant extends RenameParticipant
{
	private IFile file;
	
	public RenameFileParticipant()
	{
	}

	@Override
	protected boolean initialize(Object element)
	{
		if(element instanceof IFile)
		{
			IFile file=(IFile)element;
			IProject project=file.getProject();
			try
			{
				if(project.hasNature(ViewProjectNature.NatureID))
				{
					this.file=file;
					return true;
				}
			}
			catch (CoreException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public String getName()
	{
		return "xx";
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException
	{
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException
	{
		CompositeChange root=new CompositeChange("重构文件引用");
		//new CoreException(new Status(IStatus.ERROR));
		
		try
		{
			List<FileRef> refs=Project.getFileRefs(file);
			for(FileRef ref:refs)
			{
				TextFileChange change=new TextFileChange("haha",ref.owner);
				change.setEdit(new ReplaceEdit(ref.start,ref.stop-ref.start+1,ref.filePath.substring(0, ref.filePath.lastIndexOf("/"))+"/"+getArguments().getNewName()));
				root.add(change);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return root;
	}

}
