package org.game.editors;

import org.eclipse.jface.action.Action;
import org.eclipse.search.ui.NewSearchUI;
import org.game.refactor.IdDef;
import org.game.xml.search.file.IdRefSearchQuery;

public class SearchIdAction extends Action
{
	private IdDef idDef;
	
	public SearchIdAction(IdDef idDef)
	{
		this.idDef=idDef;
	}
	
	@Override
	public String getText()
	{
		return "≤È’““˝”√";
	}
	
	@Override
	public void run()
	{
		NewSearchUI.activateSearchResultView();
		NewSearchUI.runQueryInBackground(new IdRefSearchQuery(idDef));
	}
}
