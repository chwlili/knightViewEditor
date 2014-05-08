package org.game.knight.editor.xml.design.figure;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
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

	@Override
	protected void paintFigure(Graphics graphics)
	{
		Rectangle rect = super.getBounds();

		IFile file = getFile();
		if (file == null)
		{
			return;
		}

		Image image = null;

		try
		{
			image = new Image(Display.getCurrent(), getFile().getContents());
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}

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

			int drawX = 0;
			int drawY = 0;
			int drawW = 0;
			int drawH = 0;
			int drawMaxX = 0;
			int drawMaxY = 0;

			if (l > 0 && t > 0)
			{
				drawX = rect.x;
				drawY = rect.y;
				graphics.drawImage(image, 0, 0, l, t, drawX, drawY, l, t);
			}
			if (t > 0 && w > 0)
			{
				drawX = rect.x + l;
				drawY = rect.y;
				drawMaxX = rect.x + rect.width - r;
				while (drawX < drawMaxX)
				{
					drawW = Math.min(w, drawMaxX - drawX);
					drawH = t;
					if (drawW > 0 && drawH > 0)
					{
						graphics.drawImage(image, l, 0, drawW, drawH, drawX, drawY, drawW, drawH);
					}
					drawX += w;
				}
			}
			if (r > 0 && t > 0)
			{
				drawX = rect.x + rect.width - r;
				drawY = rect.y;
				graphics.drawImage(image, imgW - r, 0, r, t, drawX, drawY, r, t);
			}

			if (l > 0 && h > 0)
			{
				drawX = rect.x;
				drawY = rect.y + t;
				drawMaxY = rect.y + rect.height - b;
				while (drawY < drawMaxY)
				{
					drawW = l;
					drawH = Math.min(h, drawMaxY - drawY);
					if (drawW > 0 && drawH > 0)
					{
						graphics.drawImage(image, 0, t, drawW, drawH, drawX, drawY, drawW, drawH);
					}
					drawY += h;
				}
			}
			if (w > 0 && h > 0)
			{
				drawX = rect.x + l;
				drawY = rect.y + t;
				drawMaxX = rect.x + rect.width - r;
				drawMaxY = rect.y + rect.height - b;
				while (drawY < drawMaxY)
				{
					while (drawX < drawMaxX)
					{
						drawW = Math.min(w, drawMaxX - drawX);
						drawH = Math.min(h, drawMaxY - drawY);
						if (drawW > 0 && drawH > 0)
						{
							graphics.drawImage(image, l, t, drawW, drawH, drawX, drawY, drawW, drawH);
						}
						drawX += w;
					}
					drawX = rect.x + l;
					drawY += h;
				}
			}
			if (r > 0 && h > 0)
			{
				drawX = rect.x + rect.width - r;
				drawY = rect.y + t;
				drawMaxY = rect.y + rect.height - b;
				while (drawY < drawMaxY)
				{
					drawW = r;
					drawH = Math.min(h, drawMaxY - drawY);
					if (drawW > 0 && drawH > 0)
					{
						graphics.drawImage(image, imgW - r, t, drawW, drawH, drawX, drawY, drawW, drawH);
					}
					drawY += h;
				}
			}

			if (l > 0 && b > 0)
			{
				drawX = rect.x;
				drawY = rect.y + rect.height - b;
				graphics.drawImage(image, 0, imgH - b, l, b, drawX, drawY, l, b);
			}
			if (w > 0 && b > 0)
			{
				drawX = rect.x + l;
				drawY = rect.y + rect.height - b;
				drawMaxX = rect.x + rect.width - r;
				while (drawX < drawMaxX)
				{
					drawW = Math.min(w, drawMaxX - drawX);
					drawH = b;
					if (drawW > 0 && drawH > 0)
					{
						graphics.drawImage(image, l, imgH - b, drawW, drawH, drawX, drawY, drawW, drawH);
					}
					drawX += w;
				}
			}
			if (r > 0 && b > 0)
			{
				drawX = rect.x + rect.width - r;
				drawY = rect.y + rect.height - b;
				graphics.drawImage(image, imgW - r, imgH - b, r, b, drawX, drawY, r, b);
			}
		}

		image.dispose();
		image = null;
	}
}