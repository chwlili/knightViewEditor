package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

public class GefViewer extends ScrollingGraphicalViewer
{
	public void autoShowScroller()
	{
		FigureCanvas canvas=(FigureCanvas) getControl();
		canvas.setHorizontalScrollBarVisibility(FigureCanvas.AUTOMATIC);
		canvas.setVerticalScrollBarVisibility(FigureCanvas.AUTOMATIC);
	}
	
	public void offShowScroller()
	{
		FigureCanvas canvas=(FigureCanvas) getControl();
		canvas.setHorizontalScrollBarVisibility(FigureCanvas.NEVER);
		canvas.setVerticalScrollBarVisibility(FigureCanvas.NEVER);
	}
	
	public void onShowScroller()
	{
		FigureCanvas canvas=(FigureCanvas) getControl();
		canvas.setHorizontalScrollBarVisibility(FigureCanvas.ALWAYS);
		canvas.setVerticalScrollBarVisibility(FigureCanvas.ALWAYS);
	}
}
