package org.game.knight.editor.xml.design.figure;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;

public class ImageFigure extends Figure
{
	private IFile file;

	private boolean slice;
	private double left;
	private double top;
	private double right;
	private double bottom;

	public IFile getFile()
	{
		return file;
	}

	public void setFile(IFile file)
	{
		this.file = file;
	}

	public boolean isSlice()
	{
		return slice;
	}

	public void setSlice(boolean value)
	{
		slice = value;
	}

	public double getLeft()
	{
		return left;
	}

	public void setLeft(double value)
	{
		left = value;
	}

	public double getTop()
	{
		return top;
	}

	public void setTop(double value)
	{
		this.top = value;
	}

	public double getRight()
	{
		return right;
	}

	public void setRight(double value)
	{
		this.right = value;
	}

	public double getBottom()
	{
		return bottom;
	}

	public void setBottom(double value)
	{
		bottom = value;
	}

	private static int drawCount = 0;

	@Override
	protected void paintFigure(Graphics graphics)
	{

		Rectangle rect = super.getBounds();

		IFile file = getFile();
		if (file == null)
		{
			return;
		}
		Date begin = new Date();
		System.out.println("ÖØ»æ:" + drawCount);
		System.out.println("    " + getFile().getLocation().toString() + ")");
		drawCount++;

		Image image = null;

		try
		{
			image = new Image(Display.getCurrent(), getFile().getContents());
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
		Date imageOK = new Date();
		System.out.println("    Í¼Ïñ½¨Á¢:" + (imageOK.getTime() - begin.getTime()));
		begin = imageOK;

		if (image == null)
		{
			return;
		}

		if (!slice)
		{
			graphics.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, rect.x, rect.y, rect.width, rect.height);
		}
		else
		{
			int imgW = image.getBounds().width;
			int imgH = image.getBounds().height;

			int l = (int) left;
			int r = (int) right;
			int t = (int) top;
			int b = (int) bottom;
			int w = imgW - l - r;
			int h = imgH - t - b;

			if (w < 0)
			{
				l = l / (l + r) * imgW;
				r = imgW - l;
			}

			if (h < 0)
			{
				t = t / (t + b) * imgH;
				b = imgH - t;
			}

			ImageSliceHelper slice = new ImageSliceHelper(file, l, t, r, b);
			slice.getImageBC();

			Date imageSlice = new Date();
			System.out.println("    Í¼ÏñSlice:" + (imageSlice.getTime() - begin.getTime()));
			begin = imageSlice;

			graphics.pushState();

			try
			{
				graphics.translate(rect.x, rect.y);

				if (l > 0 && t > 0)
				{
					paintPatter(graphics, slice.getImageTL(), 0, 0, l, t);
				}
				if (t > 0 && w > 0)
				{
					paintPatter(graphics, slice.getImageTC(), l, 0, rect.width - l - r, t);
				}
				if (r > 0 && t > 0)
				{
					paintPatter(graphics, slice.getImageTR(), rect.width - r, 0, r, t);
				}

				if (l > 0 && h > 0)
				{
					paintPatter(graphics, slice.getImageCL(), 0, t, l, rect.height - t - b);
				}
				if (w > 0 && h > 0)
				{
					paintPatter(graphics, slice.getImageCC(), l, t, rect.width - l - r, rect.height - t - b);
				}
				if (r > 0 && h > 0)
				{
					paintPatter(graphics, slice.getImageCR(), rect.width - r, t, r, rect.height - t - b);
				}

				if (l > 0 && b > 0)
				{
					paintPatter(graphics, slice.getImageBL(), 0, rect.height - b, l, b);
				}
				if (w > 0 && b > 0)
				{
					paintPatter(graphics, slice.getImageBC(), l, rect.height - b, rect.width - l - r, b);
				}
				if (r > 0 && b > 0)
				{
					paintPatter(graphics, slice.getImageBR(), rect.width - r, rect.height - b, r, b);
				}
			}
			finally
			{
				graphics.popState();
			}
		}

		image.dispose();
		image = null;

		Date drawOK = new Date();
		System.out.println("    Í¼Ïñ»æÖÆ(" + slice + "):" + (drawOK.getTime() - begin.getTime()));
		begin = drawOK;
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