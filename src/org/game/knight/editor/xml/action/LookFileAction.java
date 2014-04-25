package org.game.knight.editor.xml.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.game.knight.ast.ASTManager;
import org.game.knight.ast.FileRef;
import org.game.knight.editor.img.ImgEditor;
import org.game.knight.editor.swf.SwfEditor;
import org.game.knight.editor.xml.ViewEditor;

public class LookFileAction extends Action
{
	private FileRef ref;

	public LookFileAction(FileRef ref)
	{
		this.ref = ref;
	}

	@Override
	public String getText()
	{
		return "打开文件(&F)";
	}

	@Override
	public void run()
	{
		IFile owner = ref.getFile();

		IFile file = ASTManager.getSourceFile(owner.getProject(), ref.getTargetURL());
		if (file.exists())
		{
			String ext = file.getFileExtension().toLowerCase();
			if (ext.equals("xml"))
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
			else if (ext.equals("png") || ext.equals("jpg"))
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
			else if (ext.equals("swf"))
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
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "文件未找到！", "\"" + ref.getText() + "\" 未找到");
		}
	}
}
