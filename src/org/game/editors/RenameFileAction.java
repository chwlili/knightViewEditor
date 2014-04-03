package org.game.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.ltk.ui.refactoring.resource.RenameResourceWizard;
import org.eclipse.swt.widgets.Display;
import org.game.refactor.FileRef;
import org.game.refactor.Project;

public class RenameFileAction extends Action {

	private FileRef ref;
	
	public RenameFileAction(FileRef fileRef)
	{
		this.ref=fileRef;
	}

	@Override
	public String getText()
	{
		return "������(&R)...";
	}

	@Override
	public void run() 
	{
		if(ref==null)
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "��Ч����", "��Ч�Ĳ���");
			return;
		}
		
		IFile owner=ref.owner;
		
		IFile file=Project.getSourceFile(owner.getProject(), ref.filePath);
		if(file.exists())
		{
			RenameResourceWizard refactoringWizard= new RenameResourceWizard(file);
			RefactoringWizardOpenOperation op= new RefactoringWizardOpenOperation(refactoringWizard);
			
			try 
			{
				op.run(Display.getCurrent().getActiveShell(), "�������ļ�");
			} 
			catch (InterruptedException e) 
			{
				// do nothing
			}
		}
		else
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "�ļ�δ�ҵ���", "\""+ref.text+"\" δ�ҵ�");
		}
	}
}
