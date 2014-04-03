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
import org.game.refactor.IdDef;
import org.game.refactor.IdRef;
import org.game.refactor.Project;

public class LookIdAction extends Action
{

	private IdDef idDef;
	
	public LookIdAction(IdDef idDef)
	{
		this.idDef=idDef;
	}
	
	public String getText()
	{
		return "�򿪶���(&I)";
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
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "����ȱʧ", "ָ����"+idRef.getText()+"�Ķ���");
		}
	}
	
	private IdRef idRef;
	private boolean findFile;
	public LookIdAction(IdRef idRef,boolean findFile)
	{
		this.idRef=idRef;
		this.findFile=findFile;
	}
	
	//@Override
//	public String getText()
//	{
//		return findFile ? "���ļ�":"�򿪶���";
//	}
	
	//@Override
	public void run1()
	{
		IdDef idDef=idRef.getTarget();
		
		if(idDef!=null)
		{
			if(findFile && idRef.isBitmapRef())
			{
				if(idDef.getRef() instanceof IdRef)
				{
					idDef=((IdRef)idDef.getRef()).getTarget();
				}
			}
			
			if(findFile)
			{
				if(idDef.getRef()!=null)
				{
					if(idDef.getRef() instanceof FileRef)
					{
						FileRef fileRef=(FileRef)idDef.getRef();
						IFile file=Project.getSourceFile(idDef.getFile().getProject(),fileRef.filePath);
						if(file!=null)
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
						else
						{
							MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "�ļ�δ�ҵ���", "\""+fileRef.text+"\" δ�ҵ�");
						}
					}
				}
				else
				{
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "����ȱʧ", "ָ����Ŀ���ļ�");
				}
			}
			else
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
		}
		else
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "����ȱʧ", "ָ����"+idRef.getText()+"�Ķ���");
		}
	}
}
