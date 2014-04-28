package org.game.knight.editor.xml;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
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
import org.game.knight.ast.ComplexTag;
import org.game.knight.ast.SingleTag;

public class ViewEditorOutline implements IContentOutlinePage
{
	private ViewEditor editor;
	private IDocument document;
	
	private AbsTag editingTag;

	private TreeViewer tree;

	private int offset;

	private static boolean linked = true;


	private Action collapseAction = new Action()
	{
		@Override
		public String getText()
		{
			return "全部折叠";
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
			return "与编辑器链接";
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
				if (item instanceof AbsTag)
				{
					editor.selectTag((AbsTag)item);
					editor.setFocus();
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
				if (item instanceof AbsTag)
				{
					AbsTag tag=(AbsTag)item;
					if(tag.isItem())
					{
						if(editingTag!=tag)
						{
							editingTag=tag;
							tree.refresh();
							
							editor.editTag(tag);
							editor.setFocus();
						}
					}
				}
			}
		}
	};

	public ViewEditorOutline(ViewEditor editor, IDocument document)
	{
		this.editor = editor;
		this.document = document;
	}

	@Override
	public void createControl(Composite parent)
	{
		tree = new TreeViewer(parent);
		tree.setLabelProvider(new TreeLabelProvider());
		tree.setContentProvider(new TreeContentProvider());
		tree.addSelectionChangedListener(treeSelectionListener);
		tree.addDoubleClickListener(treeDoubleClickListener);
		tree.setInput(ASTManager.getDocumentAST(document));
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

		ArrayList<Object> nodes = ASTManager.getDocumentAST(document).getTrees();
		for (int i = 0; i < nodes.size(); i++)
		{
			Object node = nodes.get(i);
			if (node instanceof AbsTag)
			{
				AbsTag tag = (AbsTag) node;
				if (tag.getOffset() <= offset && offset <= tag.getOffset() + tag.getLength() - 1)
				{
					treeSelection.add(tag);

					if (tag instanceof ComplexTag)
					{
						nodes = ((ComplexTag) tag).getChildren();
						i = -1;
					}
					else
					{
						break;
					}
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
			if (element instanceof AbsTag)
			{
				AbsTag tag = (AbsTag) element;
				if (tag.isList())
				{
					return PluginResource.getIcon("actionIcon.gif");
				}
				else if (tag.isDepend())
				{
					return PluginResource.getIcon("fileIcon_view.gif");
				}
				else if (tag.isBitmap())
				{
					return PluginResource.getIcon("nodeIcon_img.gif");
				}
				else if (tag.isBitmapRenderer())
				{
					return PluginResource.getIcon("nodeIcon_9scale.gif");
				}
				else if (tag.isSwf())
				{
					return PluginResource.getIcon("fileIcon_swf.gif");
				}
				else if (tag.isControl())
				{
					return PluginResource.getIcon("viewIcon_design.gif");
				}
			}
			return PluginResource.getIcon("tag.gif");
		}

		@Override
		public String getText(Object element)
		{
			if (element instanceof SingleTag)
			{
				SingleTag tag = (SingleTag) element;
				if (tag.isList())
				{
					return tag.getName();
				}
				else if (tag.isDepend())
				{
					return tag.getAttributeValue("src");
				}
				else if (tag.isItem())
				{
					String prefix=tag==editingTag ? "*":"";
					if (tag.isControl())
					{
						return prefix+tag.getAttributeValue("id") + " - " + tag.getName();
					}
					else
					{
						return prefix+tag.getAttributeValue("id");
					}
				}
				else
				{
					return tag.getName();
				}
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
			if (element instanceof ComplexTag)
			{
				ComplexTag tag = (ComplexTag) element;
				if (tag.isItem() && tag.isControl() == false)
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			return false;
		}

		@Override
		public Object[] getElements(Object inputElement)
		{
			return filterChildren(((AST) inputElement).getTrees());
		}

		@Override
		public Object[] getChildren(Object parentElement)
		{
			if (parentElement instanceof ComplexTag)
			{
				return filterChildren(((ComplexTag) parentElement).getChildren());
			}
			return null;
		}

		private Object[] filterChildren(ArrayList<Object> children)
		{
			ArrayList<Object> objects = new ArrayList<Object>();
			for (Object child : children)
			{
				if (child instanceof Token)
				{
					continue;
				}

				objects.add(child);
			}

			return objects.toArray(new Object[objects.size()]);
		}

	}

}
