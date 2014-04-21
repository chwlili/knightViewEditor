package org.game.knight.editor.xml.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.game.knight.ast.IdDef;
import org.game.knight.editor.xml.ViewEditor;

public class LookIdAction extends Action
{

	private IdDef idDef;
	
	public LookIdAction(IdDef idDef)
	{
		this.idDef=idDef;
	}
	
	public String getText()
	{
		return "打开定义(&I)";
	}
	
	@Override
	public void run()
	{
		if(idDef!=null)
		{
			IFile file=idDef.getFile();
			try
			{
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				ViewEditor editor=(ViewEditor) page.openEditor(new FileEditorInput(file), ViewEditor.ID);
				
				editor.selectRange(idDef.getOffset(), idDef.getLength());
			}
			catch (PartInitException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "定义缺失", "指不到"+idDef.getText()+"的定义");
		}
	}
}
