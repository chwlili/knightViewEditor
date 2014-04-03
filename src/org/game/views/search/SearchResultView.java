package org.game.views.search;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.text.AbstractTextSearchViewPage;
import org.eclipse.search.ui.text.Match;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.game.editors.ViewEditor;
import org.game.views.ResourceManager;

public class SearchResultView extends AbstractTextSearchViewPage implements ISearchResultPage
{

	@Override
	protected void elementsChanged(Object[] objects)
	{
		getViewer().refresh();
	}

	@Override
	protected void clear()
	{
		getViewer().refresh();
	}

	private void onDoubleClick(DoubleClickEvent event)
	{
		Object obj=event.getSelection();
		if(obj instanceof IStructuredSelection)
		{
			IStructuredSelection select=(IStructuredSelection)obj;
			Object item=select.getFirstElement();
			if(item instanceof IFile)
			{
				IFile file=(IFile)item;
				try
				{
					getSite().getPage().openEditor(new FileEditorInput(file), ViewEditor.ID);
				}
				catch (PartInitException e)
				{
					e.printStackTrace();
				}
			}
			else if(item instanceof SearchMatch)
			{
				SearchMatch match=(SearchMatch)item;
				IFile owner=(IFile)match.getElement();
				try
				{
					ViewEditor editor=(ViewEditor)(getSite().getPage().openEditor(new FileEditorInput(owner), ViewEditor.ID));
					editor.selectRange(match.getOffset(), match.getLength());
				}
				catch (PartInitException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	protected void configureTreeViewer(TreeViewer viewer)
	{
		viewer.addDoubleClickListener(new IDoubleClickListener()
		{
			@Override
			public void doubleClick(DoubleClickEvent event)
			{
				onDoubleClick(event);
			}
		});
		
		viewer.setContentProvider(new ITreeContentProvider()
		{
			private SearchResult input;
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
			{
				input=(SearchResult) newInput;
			}
			
			@Override
			public void dispose()
			{
				
			}
			
			@Override
			public boolean hasChildren(Object element)
			{
				if(element instanceof SearchResult)
				{
					return true;
				}
				if(element instanceof IFile)
				{
					return true;
				}
				return false;
			}
			
			@Override
			public Object getParent(Object element)
			{
				return null;
			}
			
			@Override
			public Object[] getElements(Object inputElement)
			{
				return ((SearchResult) inputElement).getElements();
			}
			
			@Override
			public Object[] getChildren(Object parentElement)
			{
				if(parentElement instanceof IFile)
				{
					return input.getMatches(parentElement);
				}
				return null;
			}
		});
		
		viewer.setLabelProvider(new ILabelProvider()
		{
			@Override
			public void removeListener(ILabelProviderListener listener)
			{
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property)
			{
				return false;
			}
			
			@Override
			public void dispose()
			{
			}
			
			@Override
			public void addListener(ILabelProviderListener listener)
			{
			}
			
			@Override
			public String getText(Object element)
			{
				if(element instanceof IFile)
				{
					return ((IFile)element).getProjectRelativePath().toString();
				}
				else if(element instanceof Match)
				{
					return ((SearchMatch)element).getText();
				}
				return element.toString();
			}
			
			@Override
			public Image getImage(Object element)
			{
				if(element instanceof SearchMatch)
				{
					return ResourceManager.getIcon("/icons/tag.gif");
				}
				if(element instanceof IFile)
				{
					return ResourceManager.getIcon("/icons/appIcon16.png");
				}
				return null;
			}
		});
	}

	@Override
	protected void configureTableViewer(TableViewer viewer)
	{
		viewer.addDoubleClickListener(new IDoubleClickListener()
		{
			@Override
			public void doubleClick(DoubleClickEvent event)
			{
				onDoubleClick(event);
			}
		});
		
		viewer.setContentProvider(new IStructuredContentProvider()
		{
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
			{
			}
			
			@Override
			public void dispose()
			{
			}
			
			@Override
			public Object[] getElements(Object inputElement)
			{
				ArrayList<Object> all=new ArrayList<Object>();
				
				SearchResult input=(SearchResult) inputElement;
				for(Object element:input.getElements())
				{
					for(Object item:input.getMatches(element))
					{
						all.add(item);
					}
				}
				return all.toArray(new Object[all.size()]);
			}
		});
		

		viewer.setLabelProvider(new ILabelProvider()
		{
			@Override
			public void removeListener(ILabelProviderListener listener)
			{
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property)
			{
				return false;
			}
			
			@Override
			public void dispose()
			{
			}
			
			@Override
			public void addListener(ILabelProviderListener listener)
			{
			}
			
			@Override
			public String getText(Object element)
			{
				if(element instanceof SearchMatch)
				{
					return ((SearchMatch)element).getText();
				}
				return element.toString();
			}
			
			@Override
			public Image getImage(Object element)
			{
				return ResourceManager.getIcon("/icons/tag.gif");
			}
		});
	}

}
