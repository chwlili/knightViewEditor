package org.game.xml.search.file;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.IEditorMatchAdapter;
import org.eclipse.search.ui.text.IFileMatchAdapter;

public class FileRefResult extends AbstractTextSearchResult implements ISearchResult
{
	private ISearchQuery query;
	private String searchTarget;
	private String projectName;
	
	public FileRefResult(ISearchQuery query,String searchTarget,String projectName)
	{
		this.query=query;
		this.searchTarget=searchTarget;
		this.projectName=projectName;
	}
	
	@Override
	public String getLabel()
	{
		return String.format("\"%s\" - ÏîÄ¿ \"%s\"",searchTarget,projectName);
	}

	@Override
	public String getTooltip()
	{
		return getLabel();
	}

	@Override
	public ImageDescriptor getImageDescriptor()
	{
		return null;
	}

	@Override
	public ISearchQuery getQuery()
	{
		return query;
	}

	@Override
	public IEditorMatchAdapter getEditorMatchAdapter()
	{
		return null;
	}

	@Override
	public IFileMatchAdapter getFileMatchAdapter()
	{
		return null;
	}

}
