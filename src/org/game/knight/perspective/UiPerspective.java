package org.game.knight.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class UiPerspective implements IPerspectiveFactory
{

	@Override
	public void createInitialLayout(IPageLayout layout)
	{
		String id=layout.getEditorArea();
		
		IFolderLayout leftFolder=layout.createFolder("leftFolder", IPageLayout.LEFT, 0.25f, id);
		leftFolder.addView("org.game.views.view1");
		leftFolder.addView(IPageLayout.ID_OUTLINE);
		
		IFolderLayout bottomFolder=layout.createFolder("bottomFolder", IPageLayout.BOTTOM, 0.7f, id);
		bottomFolder.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottomFolder.addView(IPageLayout.ID_PROP_SHEET);
		bottomFolder.addPlaceholder("org.eclipse.search.ui.views.SearchView");
		bottomFolder.addView(IPageLayout.ID_PROGRESS_VIEW);
	}

}
