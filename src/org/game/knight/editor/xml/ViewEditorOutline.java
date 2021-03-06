package org.game.knight.editor.xml;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.game.knight.PluginResource;
import org.game.knight.ast.AST;
import org.game.knight.ast.AST.Token;
import org.game.knight.ast.ASTManager;
import org.game.knight.ast.AbsTag;
import org.game.knight.ast.DefineControlTag;
import org.game.knight.ast.DefineFilterTag;
import org.game.knight.ast.DefineFormatTag;
import org.game.knight.ast.DefineGridImgTag;
import org.game.knight.ast.DefineImgTag;
import org.game.knight.ast.DefineSwfTag;
import org.game.knight.ast.DefineTag;
import org.game.knight.ast.DefineTextTag;
import org.game.knight.ast.ImportXmlTag;
import org.game.knight.ast.ResourceSetTag;

public class ViewEditorOutline implements IContentOutlinePage
{
	private ViewEditor editor;
	
	private DefineTag editingTag;

	private TreeViewer tree;

	private int offset;

	private static boolean linked = true;


	private Action collapseAction = new Action()
	{
		@Override
		public String getText()
		{
			return "ȫ���۵�";
		}

		@Override
		public ImageDescriptor getImageDescriptor()
		{
			return ImageDescriptor.createFromFile(ViewEditorOutline.class, "/icons/action_collapse.gif");
		}

		@Override
		public void run()
		{
			tree.collapseAll();
		}
	};

	private Action linkAction = new Action()
	{
		@Override
		public int getStyle()
		{
			return Action.AS_CHECK_BOX;
		}

		@Override
		public ImageDescriptor getImageDescriptor()
		{
			return ImageDescriptor.createFromFile(ViewEditorOutline.class, "/icons/action_link.gif");
		}

		@Override
		public String getText()
		{
			return "��༭������";
		}

		@Override
		public void run()
		{
			linked = !linked;
			handleCursorPositionChanged(offset);
		}
	};
	
	private ISelectionChangedListener treeSelectionListener=new ISelectionChangedListener()
	{
		@Override
		public void selectionChanged(SelectionChangedEvent event)
		{
			Object obj = event.getSelection();
			if (obj instanceof IStructuredSelection)
			{
				IStructuredSelection select = (IStructuredSelection) obj;
				Object item = select.getFirstElement();
				if ((item instanceof AbsTag) && !(item instanceof ResourceSetTag))
				{
					editor.selectTag((AbsTag)item);
				}
			}
		}
	};
	
	private IDoubleClickListener treeDoubleClickListener=new IDoubleClickListener()
	{
		@Override
		public void doubleClick(DoubleClickEvent event)
		{
			Object obj = event.getSelection();
			if (obj instanceof IStructuredSelection)
			{
				IStructuredSelection select = (IStructuredSelection) obj;
				Object item = select.getFirstElement();
				if(item instanceof AbsTag)
				{
					AbsTag tag=(AbsTag)item;
					if(tag.getChildren()!=null && tag.getChildren().size()>0)
					{
						boolean finded=false;
						for(Object node:tree.getExpandedElements())
						{
							if(node==tag)
							{
								finded=true;
								break;
							}
						}
						
						if(finded)
						{
							tree.collapseToLevel(tag, 1);
						}
						else
						{
							tree.expandToLevel(tag, 1);
						}
					}
				}
			}
		}
	};

	public ViewEditorOutline(ViewEditor editor)
	{
		this.editor = editor;
	}

	@Override
	public void createControl(Composite parent)
	{
		tree = new TreeViewer(parent);
		tree.setLabelProvider(new TreeLabelProvider());
		tree.setContentProvider(new TreeContentProvider());
		tree.addSelectionChangedListener(treeSelectionListener);
		tree.addDoubleClickListener(treeDoubleClickListener);
		tree.setAutoExpandLevel(2);
		tree.setInput(ASTManager.getDocumentAST(editor.getDocument()));
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public Control getControl()
	{
		return tree.getControl();
	}

	@Override
	public void setActionBars(IActionBars actionBars)
	{
		linkAction.setChecked(linked);

		actionBars.getToolBarManager().add(collapseAction);
		actionBars.getToolBarManager().add(linkAction);
	}

	@Override
	public void setFocus()
	{

	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener)
	{

	}

	@Override
	public ISelection getSelection()
	{
		return null;
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener)
	{

	}

	@Override
	public void setSelection(ISelection selection)
	{

	}

	public void handleCursorPositionChanged(int offset)
	{
		tree.removeSelectionChangedListener(treeSelectionListener);
		
		this.offset = offset;
		if (!linked)
		{
			return;
		}

		ArrayList<Object> treeSelection = new ArrayList<Object>();

		ArrayList<Object> nodes = ASTManager.getDocumentAST(editor.getDocument()).getTrees();
		for (int i = 0; i < nodes.size(); i++)
		{
			Object node = nodes.get(i);
			if (!(node instanceof AbsTag))
			{
				continue;
			}
			
			AbsTag tag = (AbsTag) node;
			if (tag.getOffset() <= offset && offset <= tag.getOffset() + tag.getLength() - 1)
			{
				treeSelection.add(tag);
				
				if(tag.getChildren()!=null)
				{
					nodes = tag.getChildren();
					i = -1;
				}
				else
				{
					break;
				}
			}
		}

		if (treeSelection.size() > 0)
		{
			while (treeSelection.size() > 1)
			{
				tree.expandToLevel(treeSelection.remove(0), 1);
			}
			tree.setSelection(new StructuredSelection(treeSelection.remove(0)));
		}
		
		tree.addSelectionChangedListener(treeSelectionListener);
	}

	private class TreeLabelProvider implements ILabelProvider
	{
		@Override
		public void addListener(ILabelProviderListener listener)
		{
		}

		@Override
		public void dispose()
		{
		}

		@Override
		public boolean isLabelProperty(Object element, String property)
		{
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener)
		{
		}

		@Override
		public Image getImage(Object element)
		{
			if(element instanceof ResourceSetTag)
			{
				return PluginResource.getIcon("actionIcon.gif");
			}
			if(element instanceof ImportXmlTag)
			{
				return PluginResource.getIcon("fileIcon_view.gif");
			}
			if(element instanceof DefineImgTag)
			{
				return PluginResource.getIcon("nodeIcon_img.gif");
			}
			if(element instanceof DefineGridImgTag)
			{
				return PluginResource.getIcon("nodeIcon_9scale.gif");
			}
			if(element instanceof DefineSwfTag)
			{
				return PluginResource.getIcon("fileIcon_swf.gif");
			}
			if(element instanceof DefineControlTag)
			{
				DefineControlTag control=(DefineControlTag) element;
				if(control.isRoot())
				{
					return PluginResource.getIcon("viewIcon_design.gif");
				}
			}
			return PluginResource.getIcon("tag.gif");
		}

		@Override
		public String getText(Object element)
		{
			if(element instanceof ImportXmlTag)
			{
				return ((ImportXmlTag)element).getSrc();
			}
			if(element instanceof DefineControlTag)
			{
				DefineControlTag control=(DefineControlTag) element;
				if(control.isRoot())
				{
					return (element==editingTag ? "*":"")+control.getID()+" - "+control.getName();
				}
				else
				{
					return ((AbsTag)element).getName();
				}
			}
			if(element instanceof DefineTag)
			{
				return (element==editingTag ? "*":"")+((DefineTag)element).getID();
			}
			if(element instanceof AbsTag)
			{
				return ((AbsTag)element).getName();
			}
			return element.toString();
		}
	}

	private class TreeContentProvider implements ITreeContentProvider
	{
		@Override
		public void dispose()
		{
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{
		}

		@Override
		public Object getParent(Object element)
		{
			return null;
		}

		@Override
		public boolean hasChildren(Object element)
		{
			if(element instanceof AbsTag)
			{
				AbsTag tag=(AbsTag)element;
				if(tag instanceof ImportXmlTag || tag instanceof DefineImgTag || tag instanceof DefineSwfTag || tag instanceof DefineGridImgTag || tag instanceof DefineFilterTag || tag instanceof DefineFormatTag || tag instanceof DefineTextTag)
				{
					return false;
				}
				if(tag.getChildren()==null)
				{
					return false;
				}
			}
			return true;
		}

		@Override
		public Object[] getElements(Object inputElement)
		{
			return filterChildren(((AST) inputElement).getTrees());
		}

		@Override
		public Object[] getChildren(Object parentElement)
		{
			if(parentElement instanceof AbsTag)
			{
				return filterChildren(((AbsTag)parentElement).getChildren());
			}
			return null;
		}

		private Object[] filterChildren(ArrayList<Object> children)
		{
			ArrayList<Object> objects = new ArrayList<Object>();
			if(children!=null)
			{
				for (Object child : children)
				{
					if (child instanceof Token)
					{
						continue;
					}
	
					objects.add(child);
				}
			}

			return objects.toArray(new Object[objects.size()]);
		}

	}

}
