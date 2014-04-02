package org.game.xml.search.file;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class FileRefSearchAction implements IObjectActionDelegate
{
	private ISelection selection;
	
	public FileRefSearchAction()
	{
	}

	@Override
	public void run(IAction action)
	{
		if(selection!=null)
		{
			if(selection instanceof StructuredSelection)
			{
				StructuredSelection tree=(StructuredSelection)selection;
				Object element=tree.getFirstElement();
				if(element instanceof IFile)
				{
					IFile file=(IFile)element;
					NewSearchUI.activateSearchResultView();
					NewSearchUI.runQueryInBackground(new FileRefSearchQuery(file));
				}
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{
		this.selection=selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		
	}

}
