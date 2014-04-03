package org.game.views.search;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class SearchFileRefAction implements IObjectActionDelegate
{
	private ISelection selection;
	
	public SearchFileRefAction()
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
					NewSearchUI.runQueryInBackground(new SearchFileRefQuery(file));
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
