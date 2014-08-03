package org.game.knight.editor.xml;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.game.knight.ast2.UIBase;
import org.game.knight.ast2.UIViewListNode;
import org.game.knight.ast2.ViewDocument;
import org.game.knight.ast2.ViewDocumentFactory;
import org.game.knight.editor.xml.design.GefFactory;
import org.game.knight.editor.xml.design.GefViewer;
import org.game.knight.editor.xml.design.TagInput;

public class ViewGefEditor extends Composite
{
	private ViewEditor editor;
	private GefViewer viewer;
	
	private Combo editings;
	
	

	public ViewGefEditor(Composite parent, ViewEditor editor)
	{
		super(parent, SWT.NONE);
		
		this.editor=editor;

		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = 1;
		setLayout(layout);

		GridLayout toolbarLayout=new GridLayout();
		toolbarLayout.numColumns=2;
		toolbarLayout.marginWidth=0;
		toolbarLayout.marginHeight=0;
		toolbarLayout.horizontalSpacing=0;
		
		Composite toolbar=new Composite(this, SWT.None);
		toolbar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		toolbar.setLayout(toolbarLayout);
		
		ToolBar tools = new ToolBar(toolbar, SWT.None);
		new ToolItem(tools, SWT.PUSH);
		tools.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		GridLayout editLayout=new GridLayout();
		editLayout.numColumns=2;
		editLayout.marginWidth=3;
		editLayout.marginHeight=3;
		editLayout.horizontalSpacing=0;
		editLayout.horizontalSpacing=5;
		
		Composite edits=new Composite(toolbar, SWT.NONE);
		edits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		edits.setLayout(editLayout);

		//Label label=new Label(edits, SWT.None);
		//label.setText("当前组件:");
		//label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		
		editings=new Combo(edits, SWT.READ_ONLY|SWT.DROP_DOWN);
		editings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		editings.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int index=editings.getSelectionIndex();
				controlID=viewList.get(index).getAttribute("id");
				viewer.setContents(new TagInput(viewList.get(index)));
			}
		});

		Composite viewBox = new Composite(this, SWT.NONE);
		viewBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewBox.setLayout(new FillLayout());

		viewer = new GefViewer();
		viewer.createControl(viewBox);
		viewer.setEditDomain(new DefaultEditDomain(editor));
		viewer.setRootEditPart(new FreeformGraphicalRootEditPart());
		viewer.setEditPartFactory(new GefFactory(editor));
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
	}
	
	@Override
	protected void checkSubclass()
	{
		// super.checkSubclass();
	}

	public void autoShowScroller()
	{
		viewer.autoShowScroller();
	}

	public void offShowScroller()
	{
		viewer.offShowScroller();
	}

	public void onShowScroller()
	{
		viewer.onShowScroller();
	}

	public void setContents(Object contents)
	{
		viewer.setContents(contents);
	}


	private String controlID;
	private UIViewListNode viewList;
	
	public void open()
	{
		editings.removeAll();
		
		try
		{
			ViewDocument document=ViewDocumentFactory.getViewAST(((FileDocument)editor.getDocument()).getFile());

			int index=-1;
			viewList=document.getRoot().getViewList();
			for(int i=0;i<viewList.size();i++)
			{
				UIBase item=viewList.get(i);
				String id=item.getAttribute("id");
				if(id!=null)
				{
					editings.add(id+"("+item.getTagName()+")");
					if(id.equals(controlID))
					{
						index=i;
					}
				}
			}
			
			if(index==-1 && viewList.size()>0)
			{
				index=0;
			}
			
			editings.select(index);
			if(index!=-1)
			{
				controlID=viewList.get(index).getAttribute("id");
				viewer.setContents(new TagInput(viewList.get(index)));
			}
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		
	}
}
