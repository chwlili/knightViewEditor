package org.game.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.game.refactor.FileRef;
import org.game.refactor.Project;

public class LookFileAction extends Action
{
	private FileRef ref;
	
	public LookFileAction(FileRef ref)
	{
		this.ref=ref;
	}
	
	@Override
	public String getText()
	{
		return "打开文件(&F)";
	}
	
	@Override
	public void run()
	{
		IFile owner=ref.owner;
		
		IFile file=Project.getSourceFile(owner.getProject(), ref.filePath);
		if(file.exists())
		{
			String ext=file.getFileExtension().toLowerCase();
			if(ext.equals("xml"))
			{
				try
				{
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					page.openEditor(new FileEditorInput(file), ViewEditor.ID);
				}
				catch (PartInitException e)
				{
					e.printStackTrace();
				}
			}
			else if(ext.equals("png")||ext.equals("jpg"))
			{
				try
				{
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					page.openEditor(new FileEditorInput(file), ImgEditor.ID);
				}
				catch (PartInitException e)
				{
					e.printStackTrace();
				}
			}
			else if(ext.equals("swf"))
			{
				try
				{
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					page.openEditor(new FileEditorInput(file), SwfEditor.ID);
				}
				catch (PartInitException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "文件未找到！", "\""+ref.text+"\" 未找到");
		}
	}
}
