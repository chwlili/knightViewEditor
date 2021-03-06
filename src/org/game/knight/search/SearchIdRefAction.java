package org.game.knight.search;

import org.eclipse.jface.action.Action;
import org.eclipse.search.ui.NewSearchUI;
import org.game.knight.ast.IdDef;

public class SearchIdRefAction extends Action {
	
	private IdDef idDef;
	
	public SearchIdRefAction(IdDef idDef)
	{
		this.idDef=idDef;
	}
	
	public String getText() {
		return "��������";
	};
	
	@Override
	public void run() {
		NewSearchUI.activateSearchResultView();
		NewSearchUI.runQueryInBackground(new SearchIdRefQuery(idDef));
	}
}
