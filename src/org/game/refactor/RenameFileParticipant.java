package org.game.refactor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.game.project.ViewProjectNature;

public class RenameFileParticipant extends RenameParticipant
{
	private IResource file;
	
	public RenameFileParticipant()
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
		if(file instanceof IFolder)
		{
			Hashtable<IFile, ArrayList<ReplaceEdit>> file_edits = new Hashtable<IFile, ArrayList<ReplaceEdit>>();
	
			//移动操作的目标文件夹
			IFolder folder = ((IFolder)file.getParent()).getFolder(getArguments().getNewName());
			
			//移动操作涉及的所有文件
			IResource[] fromFiles = new IResource[]{file};
	
			//确定文件变动
			try
			{
				Project.Change[] changes=Project.findRefactoringFileRef((IResource) file, folder, fromFiles, pm);
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
			CompositeChange root = new CompositeChange("更新文件引用");
			for (IFile file : file_edits.keySet())
			{
				TextChange fileEdit=getTextChange(file);
				if(fileEdit==null)
				{
					fileEdit= new TextFileChange("", file);
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
		else
		{
			return null;
		}
		
//		CompositeChange root=new CompositeChange("重构文件引用");
//		//new CoreException(new Status(IStatus.ERROR));
//		
//		try
//		{
//			List<FileRef> refs=Project.getFileRefs(file);
//			for(FileRef ref:refs)
//			{
//				TextFileChange change=new TextFileChange("haha",ref.owner);
//				change.setEdit(new ReplaceEdit(ref.start,ref.stop-ref.start+1,ref.filePath.substring(0, ref.filePath.lastIndexOf("/"))+"/"+getArguments().getNewName()));
//				root.add(change);
//			}
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		
//		return root;
	}

}
