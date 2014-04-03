package org.game.views.search;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.game.refactor.Project;

public class SearchFileRefQuery implements ISearchQuery
{
	private IFile file;
	private SearchResult result;
	
	public SearchFileRefQuery(IFile file)
	{
		this.file=file;
		this.result=new SearchResult(this,file.getProjectRelativePath().toString(),file.getProject().getName());
	}
	
	@Override
	public IStatus run(IProgressMonitor monitor) throws OperationCanceledException
	{
		try
		{
			Project.searchFileRef(file, result, monitor);
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			monitor.done();
		}
		return Status.OK_STATUS;
	}

	@Override
	public String getLabel()
	{
		return result.getLabel();
	}

	@Override
	public boolean canRerun()
	{
		return true;
	}

	@Override
	public boolean canRunInBackground()
	{
		return true;
	}

	@Override
	public ISearchResult getSearchResult()
	{
		return result;
	}

}
