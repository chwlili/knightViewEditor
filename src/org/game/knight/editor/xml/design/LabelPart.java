package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

public class LabelPart extends ControlPart
{
	private Label view = null;

	@Override
	protected IFigure createFigure()
	{
		view = new Label();
		return view;
	}

	@Override
	protected void refreshVisuals()
	{
		super.refreshVisuals();
		view.setText(getTagHelper().findTextByAttribute("text"));
	}
}
