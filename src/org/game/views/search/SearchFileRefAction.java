package org.game.views.search;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.game.refactor.FileRef;
import org.game.refactor.Project;

public class SearchFileRefAction extends Action implements IObjectActionDelegate
{
	private ISelection selection;
	private FileRef ref;

	public SearchFileRefAction()
	{
		this.ref = null;
	}

	public SearchFileRefAction(FileRef ref)
	{
		this.ref = ref;
	}

	@Override
	public String getText()
	{
		return "查找引用";
	}

	@Override
	public void run()
	{
		if (ref == null)
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "无效参数", "无效的参数");
			return;
		}

		IFile owner = ref.owner;

		IFile file = Project.getSourceFile(owner.getProject(), ref.filePath);
		if (file.exists())
		{
			NewSearchUI.activateSearchResultView();
			NewSearchUI.runQueryInBackground(new SearchFileRefQuery(file));
		}
		else
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "文件未找到！", "\"" + ref.text + "\" 不存在");
		}
	}

	@Override
	public void run(IAction action)
	{
		if (selection != null)
		{
			if (selection instanceof StructuredSelection)
			{
				StructuredSelection tree = (StructuredSelection) selection;
				Object element = tree.getFirstElement();
				if (element instanceof IFile)
				{
					IFile file = (IFile) element;
					NewSearchUI.activateSearchResultView();
					NewSearchUI.runQueryInBackground(new SearchFileRefQuery(file));
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
