package org.game.views.priority;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class PrioritySettingAction implements IObjectActionDelegate
{
	private ISelection selection;
	
	@Override
	public void run(IAction action)
	{
		
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
