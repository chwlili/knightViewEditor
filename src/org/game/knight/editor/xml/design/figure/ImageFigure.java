package org.game.knight.editor.xml.design.figure;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;

public class ImageFigure extends Figure
{
	private SliceImage img;

	public SliceImage getImage()
	{
		return img;
	}
	
	public void setImage(SliceImage img)
	{
		this.img=img;
	}
	
	@Override
	protected void paintFigure(Graphics graphics)
	{
		Rectangle rect = super.getBounds();

		if(img==null)
		{
			return;
		}
		
		if(img.isSlice()==false)
		{
			graphics.drawImage(img.getOriginImage(), 0, 0, img.getOriginImage().getBounds().width, img.getOriginImage().getBounds().height, rect.x, rect.y, rect.width, rect.height);
		}
		else
		{
			int imgW = img.getOriginW();
			int imgH = img.getOriginH();

			int l = img.getLeftLength();
			int r = img.getTopLength();
			int t = img.getRightLength();
			int b = img.getBottomLength();
			int w = imgW - l - r;
			int h = imgH - t - b;

			graphics.pushState();

			try
			{
				graphics.translate(rect.x, rect.y);

				if (l > 0 && t > 0)
				{
					paintPatter(graphics, img.getImageTL(), 0, 0, l, t);
				}
				if (t > 0 && w > 0)
				{
					paintPatter(graphics, img.getImageTC(), l, 0, rect.width - l - r, t);
				}
				if (r > 0 && t > 0)
				{
					paintPatter(graphics, img.getImageTR(), rect.width - r, 0, r, t);
				}

				if (l > 0 && h > 0)
				{
					paintPatter(graphics, img.getImageCL(), 0, t, l, rect.height - t - b);
				}
				if (w > 0 && h > 0)
				{
					paintPatter(graphics, img.getImageCC(), l, t, rect.width - l - r, rect.height - t - b);
				}
				if (r > 0 && h > 0)
				{
					paintPatter(graphics, img.getImageCR(), rect.width - r, t, r, rect.height - t - b);
				}

				if (l > 0 && b > 0)
				{
					paintPatter(graphics, img.getImageBL(), 0, rect.height - b, l, b);
				}
				if (w > 0 && b > 0)
				{
					paintPatter(graphics, img.getImageBC(), l, rect.height - b, rect.width - l - r, b);
				}
				if (r > 0 && b > 0)
				{
					paintPatter(graphics, img.getImageBR(), rect.width - r, rect.height - b, r, b);
				}
			}
			finally
			{
				graphics.popState();
			}
		}
	}

	private void paintPatter(Graphics graphics, Image img, int x, int y, int w, int h)
	{
		if (img != null && w > 0 && h > 0)
		{
			Pattern pattern = new Pattern(Display.getCurrent(), img);

			graphics.pushState();
			graphics.translate(x, y);

			graphics.setBackgroundPattern(pattern);
			graphics.fillRectangle(0, 0, w, h);
			graphics.setBackgroundPattern(null);
			pattern.dispose();

			graphics.popState();
		}
	}
}