package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.IFigure;
import org.game.knight.editor.xml.design.figure.BlankFigure;

public class BoxPart extends ControlPart
{
	@Override
	protected IFigure createFigure()
	{
		return new BlankFigure();
	}
}
