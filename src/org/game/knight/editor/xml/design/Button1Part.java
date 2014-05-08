package org.game.knight.editor.xml.design;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.game.knight.ast.DefineControlTag;
import org.game.knight.editor.xml.design.figure.ButtonFigure;

public class Button1Part extends ControlPart
{
	private ButtonFigure view;

	@SuppressWarnings("rawtypes")
	@Override
	protected List getModelChildren()
	{
		return new ArrayList<Object>();
	}

	@Override
	protected IFigure createFigure()
	{
		view = new ButtonFigure();

		return view;
	}

	@Override
	protected void refreshVisuals()
	{
		super.refreshVisuals();

		view.setLabel(getTagHelper().findTextByAttribute("label"));
		view.removeAllImage();
		view.removeAllFormat();

		for (Object child : getTag().getChildren())
		{
			if (child instanceof DefineControlTag)
			{
				DefineControlTag tag = (DefineControlTag) child;
				if (tag.getName().equals("state"))
				{
					TagHelper helper = new TagHelper(tag);

					String frameID = helper.getStringValue("frame");
					if (frameID != null)
					{
						view.addImage(frameID, helper.findFileByAttribute("back"));
						view.addFormat(frameID, helper.findFontByAttribute("format"));
					}
				}
			}
		}
	}
}
