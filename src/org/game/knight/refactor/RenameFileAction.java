package org.game.knight.refactor;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class RenameFileAction implements IObjectActionDelegate
{
	private ISelection selection;

	@Override
	public void run(IAction action)
	{
		if (selection != null)
		{
			if (selection instanceof StructuredSelection)
			{
				StructuredSelection tree = (StructuredSelection) selection;
				Object element = tree.getFirstElement();
				if (element instanceof IResource)
				{
					IResource file = (IResource) element;
					RenameFileWizard renameResourceWizard = new RenameFileWizard(file);
					RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(renameResourceWizard);
					
					try
					{
						op.run(Display.getCurrent().getActiveShell(), "XXXXXXXXXXXXXXXXX");
					}
					catch (InterruptedException e)
					{
						// do nothing
					}
				}
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		
	}

}
