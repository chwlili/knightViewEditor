package org.game.knight.editor.xml.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Display;
import org.game.knight.ast.FileAstManager;
import org.game.knight.ast.FileRef;
import org.game.knight.refactor.RenameFileWizard;

public class RenameFileAction extends Action
{

	private FileRef ref;

	public RenameFileAction(FileRef fileRef)
	{
		this.ref = fileRef;
	}

	@Override
	public String getText()
	{
		return "������(&R)...";
	}

	@Override
	public void run()
	{
		if (ref == null)
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "��Ч����", "��Ч�Ĳ���");
			return;
		}

		IFile owner = ref.getFile();

		IFile file = FileAstManager.getSourceFile(owner.getProject(), ref.getTargetURL());
		if (file.exists())
		{
			RenameFileWizard refactoringWizard = new RenameFileWizard(file);
			RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(refactoringWizard);

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
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "�ļ�δ�ҵ���", "\"" + ref.getText() + "\" δ�ҵ�");
		}
	}
}
