package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.game.knight.PluginResource;

public class ImageBack extends Figure
{
	private int back;

	public int getBackType()
	{
		return back;
	}

	public void setBackType(int value)
	{
		if (back != value)
		{
			back = value;
			getUpdateManager().addDirtyRegion(this, getBounds());
		}
	}

	@Override
	public void paint(Graphics graphics)
	{
		int maxW = getBounds().width;
		int maxH = getBounds().height;

		if (back == 1)
		{
			// »æÖÆºÚÉ«±³¾°
			graphics.setBackgroundColor(PluginResource.getColor(0, 0, 0));
			graphics.fillRectangle(0, 0, maxW, maxH);
		}
		else if (back == 2)
		{
			// »æÖÆ°×É«±³¾°
			graphics.setBackgroundColor(PluginResource.getColor(255, 255, 255));
			graphics.fillRectangle(0, 0, maxW, maxH);
		}
		else
		{
			// »æÖÆÆåÅÌ¸ñ±³¾°
			graphics.setBackgroundColor(PluginResource.getColor(255, 255, 255));
			graphics.fillRectangle(0, 0, maxW, maxH);

			graphics.setBackgroundColor(PluginResource.getColor(204, 204, 204));

			int cellSize = 15;
			int rowIndex = 0;
			int x = 0;
			int y = 0;
			while (x < maxW)
			{
				if (rowIndex % 2 == 0)
				{
					graphics.fillRectangle(x, y, cellSize, cellSize);
				}
				else
				{
					graphics.fillRectangle(x + cellSize, y, cellSize, cellSize);
				}

				x += cellSize * 2;

				if (x >= maxW)
				{
					x = 0;
					y += cellSize;
					rowIndex++;

					if (y > maxH)
					{
						break;
					}
				}
			}
		}
	}
}
