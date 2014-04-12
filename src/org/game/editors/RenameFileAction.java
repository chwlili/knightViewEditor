package org.game.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Display;
import org.game.refactor.FileRef;
import org.game.refactor.Project;
import org.game.refactor.RenameFileWizard;

public class RenameFileAction extends Action {

	private FileRef ref;
	
	public RenameFileAction(FileRef fileRef)
	{
		this.ref=fileRef;
	}

	@Override
	public String getText()
	{
		return "重命名(&R)...";
	}

	@Override
	public void run() 
	{
		if(ref==null)
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "无效参数", "无效的参数");
			return;
		}
		
		IFile owner=ref.owner;
		
		IFile file=Project.getSourceFile(owner.getProject(), ref.filePath);
		if(file.exists())
		{
			RenameFileWizard refactoringWizard= new RenameFileWizard(file);
			RefactoringWizardOpenOperation op= new RefactoringWizardOpenOperation(refactoringWizard);
			
			try 
			{
				op.run(Display.getCurrent().getActiveShell(), "重命名文件");
			} 
			catch (InterruptedException e) 
			{
				// do nothing
			}
		}
		else
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "文件未找到！", "\""+ref.text+"\" 未找到");
		}
	}
}
