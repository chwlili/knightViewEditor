package org.game.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class ImgEditor extends EditorPart
{
	public static final String ID="org.game.editors.IMG";
	
	private Label img_1;
	public ImgEditor()
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
		setSite(site);
		setInput(input);
		
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
		parent.setLayout(new GridLayout(1, false));
		
		img_1 = new Label(parent, SWT.NONE);
		img_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		img_1.setText("");
		
		IFileEditorInput fileInput=(IFileEditorInput)getEditorInput();
		IFile file=fileInput.getFile();
		
		try
		{
			img_1.setImage(new Image(null, new ImageData(file.getContents())));
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void setFocus()
	{
		img_1.getParent().setFocus();
	}

}
