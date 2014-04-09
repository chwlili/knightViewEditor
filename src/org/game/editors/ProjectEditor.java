package org.game.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class ProjectEditor extends EditorPart
{

	public ProjectEditor()
	{
		// TODO 自动生成的构造函数存根
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
		
	}

	@Override
	public void setFocus()
	{
		
	}

}
