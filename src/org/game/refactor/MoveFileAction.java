package org.game.refactor;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.ltk.ui.refactoring.resource.MoveResourcesWizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class MoveFileAction extends Action implements IObjectActionDelegate
{
	private ISelection selection;
	private FileRef ref;

	public MoveFileAction()
	{
		this.ref = null;
	}

	public MoveFileAction(FileRef ref)
	{
		this.ref = ref;
	}

	@Override
	public String getText()
	{
		return "移动(&M)...";
	}

	@Override
	public void run()
	{
		if(ref==null)
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "无效参数", "无效的参数");
			return;
		}
		
		IFile owner = ref.owner;

		IFile file = Project.getSourceFile(owner.getProject(), ref.filePath);
		if (file.exists())
		{
			MoveResourcesWizard refactoringWizard = new MoveResourcesWizard(new IResource[] { file });
			RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(refactoringWizard);

			try
			{
				op.run(Display.getCurrent().getActiveShell(), "移动文件");
			}
			catch (InterruptedException e)
			{
			}
		}
		else
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "文件未找到！", "\"" + ref.text + "\" 未找到");
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
				
				ArrayList<IResource> resources=new ArrayList<IResource>();
				for(Object item:tree.toArray())
				{
					if(item instanceof IResource)
					{
						resources.add((IResource)item);
					}
				}
				
				if(resources.size()>0)
				{
					MoveResourcesWizard refactoringWizard = new MoveResourcesWizard(resources.toArray(new IResource[resources.size()]));
					RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(refactoringWizard);

					try
					{
						op.run(Display.getCurrent().getActiveShell(), "移动文件");
					}
					catch (InterruptedException e)
					{
						ErrorDialog.openError(Display.getCurrent().getActiveShell(), "错误", "移动操作出现错误", new Status(Status.ERROR,e.getMessage(),e.getMessage(),e));
					}
				}
				else
				{
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", "没有选中任何资源!");
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
