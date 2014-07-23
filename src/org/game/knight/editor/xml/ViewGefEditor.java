package org.game.knight.editor.xml;

import java.util.ArrayList;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.game.knight.ast.AST;
import org.game.knight.ast.ASTManager;
import org.game.knight.ast.AbsTag;
import org.game.knight.ast.DefineControlTag;
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
				controlID=controls.get(index).getAttributeValue("id");
				viewer.setContents(new TagInput(controls.get(index)));
			}
		});

		Composite viewBox = new Composite(this, SWT.NONE);
		viewBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewBox.setLayout(new FillLayout());

		viewer = new GefViewer();
		viewer.createControl(viewBox);
		viewer.setEditDomain(new DefaultEditDomain(editor));
		viewer.setRootEditPart(new FreeformGraphicalRootEditPart());
		viewer.setEditPartFactory(new GefFactory());
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
	private ArrayList<AbsTag> controls=new ArrayList<AbsTag>();
	
	public void open()
	{
		controls.clear();
		editings.removeAll();
		
		AST ast=ASTManager.getDocumentAST(editor.getDocument());
		ArrayList<Object> list=ast.getTrees();
		for(Object obj:list)
		{
			if(obj instanceof AbsTag)
			{
				AbsTag tag=(AbsTag)obj;
				for(Object listTag:tag.getChildren())
				{
					if(listTag instanceof AbsTag)
					{
						for(Object a:((AbsTag)listTag).getChildren())
						{
							if(a instanceof AbsTag)
							{
								AbsTag last=(AbsTag)a;
								if(last instanceof DefineControlTag)
								{
									controls.add(last);
								}
							}
						}
					}
				}
			}
		}
		
		int index=-1;
		for(int i=0;i<controls.size();i++)
		{
			AbsTag tag=controls.get(i);
			
			String id=tag.getAttributeValue("id");
			if(id!=null)
			{
				String label=tag.getAttributeValue("id")+"("+tag.getName()+")";
				
				editings.add(label);
				if(id.equals(controlID))
				{
					index=i;
				}
			}
		}
		editings.add("添加新的..");
		
		if(index==-1 && controls.size()>0)
		{
			index=0;
		}
		
		editings.select(index);
		if(index!=-1)
		{
			controlID=controls.get(index).getAttributeValue("id");
			viewer.setContents(new TagInput(controls.get(index)));
		}
	}
	
	public void close()
	{
		
	}
}
