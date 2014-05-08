package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.IFigure;
import org.game.knight.editor.xml.design.figure.ImageFigure;

public class ImagePart extends ControlPart
{
	private ImageFigure view = null;

	@Override
	protected IFigure createFigure()
	{
		view = new ImageFigure();

		return view;
	}

	@Override
	protected void refreshVisuals()
	{
		super.refreshVisuals();

		String grid=getTagHelper().getStringValue("grid");
		if(grid!=null && !grid.isEmpty())
		{
			String[] parts=grid.split(",");
			if(parts.length>=4)
			{
				try
				{
					view.setLeft(Integer.parseInt(parts[0]));
					view.setTop(Integer.parseInt(parts[1]));
					view.setRight(Integer.parseInt(parts[2]));
					view.setBottom(Integer.parseInt(parts[3]));
					view.setSlice(true);
				}
				catch(Exception exception)
				{
					view.setLeft(Double.NaN);
					view.setTop(Double.NaN);
					view.setRight(Double.NaN);
					view.setBottom(Double.NaN);
					view.setSlice(false);
				}
			}
		}
		
		view.setFile(getTagHelper().findFileByAttribute("bmp"));
	}

}
