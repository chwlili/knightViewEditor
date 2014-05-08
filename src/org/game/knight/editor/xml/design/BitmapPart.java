package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.IFigure;
import org.game.knight.editor.xml.design.figure.ImageFigure;

public class BitmapPart extends ControlPart
{
	private ImageFigure view = null;

	@Override
	protected IFigure createFigure()
	{
		view = new ImageFigure();
		view.setSlice(false);
		
		return view;
	}

	@Override
	protected void refreshVisuals()
	{
		super.refreshVisuals();

		view.setFile(getTagHelper().findFileByAttribute("bitmap"));
	}
}
