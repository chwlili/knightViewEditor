package org.game.knight.editor.swf;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

public class SwfEditor extends EditorPart
{
	public static final String ID = "org.game.editors.SWF";
	
	private Swf swf;
	
	public SwfEditor()
	{

	}

	@Override
	public void doSave(IProgressMonitor monitor)
	{

	}

	@Override
	public void doSaveAs()
	{

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException
	{
		setInput(input);
		setSite(site);

		setPartName(input.getName());
	}

	@Override
	public boolean isDirty()
	{
		return false;
	}

	@Override
	public boolean isSaveAsAllowed()
	{
		return false;
	}

	@Override
	public void createPartControl(Composite parent)
	{
		FileEditorInput fileInput=(FileEditorInput)getEditorInput();
		
		swf=new Swf(parent, SWT.NONE);
		swf.loadMovie(fileInput.getFile().getLocation().toFile());
	}

	@Override
	public void setFocus()
	{
		swf.setFocus();
	}

}
