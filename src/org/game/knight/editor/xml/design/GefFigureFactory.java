package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.RectangleFigure;
import org.game.knight.ast.DefineControlTag;

public class GefFigureFactory
{
	public static Figure createFigure(DefineControlTag tag)
	{
		Figure figure=null;
		
		if(figure==null)
		{
			figure=new RectangleFigure();
		}
		return figure; 
	}
}
