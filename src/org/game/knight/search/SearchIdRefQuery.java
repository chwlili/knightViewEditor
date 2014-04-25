package org.game.knight.search;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.game.knight.ast.ASTManager;
import org.game.knight.ast.IdDef;

public class SearchIdRefQuery implements ISearchQuery
{
	private IdDef idDef;
	private SearchResult result;

	public SearchIdRefQuery(IdDef idDef)
	{
		this.idDef = idDef;
		this.result = new SearchResult(this, idDef.getText(), idDef.getFile().getProject().getName());
	}

	@Override
	public IStatus run(IProgressMonitor monitor) throws OperationCanceledException
	{
		try
		{
			ASTManager.searchIdRef(idDef, result, monitor);
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
		return "QUERY£º" + result.getLabel();
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
