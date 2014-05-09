package org.game.knight.editor.xml.design.figure;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageSliceHelper
{
	private IFile file;
	private int left;
	private int top;
	private int right;
	private int bottom;

	private boolean sliced = false;

	private Image img_tl;
	private Image img_tc;
	private Image img_tr;
	private Image img_cl;
	private Image img_cc;
	private Image img_cr;
	private Image img_bl;
	private Image img_bc;
	private Image img_br;

	public ImageSliceHelper(IFile file, int l, int t, int r, int b)
	{
		this.file = file;
		this.left = l;
		this.top = t;
		this.right = r;
		this.bottom = b;
	}

	public Image getImageTL()
	{
		slice();
		return img_tl;
	}

	public Image getImageTC()
	{
		slice();
		return img_tc;
	}

	public Image getImageTR()
	{
		slice();
		return img_tr;
	}

	public Image getImageCL()
	{
		slice();
		return img_cl;
	}

	public Image getImageCC()
	{
		slice();
		return img_cc;
	}

	public Image getImageCR()
	{
		slice();
		return img_cr;
	}

	public Image getImageBL()
	{
		slice();
		return img_bl;
	}

	public Image getImageBC()
	{
		slice();
		return img_bc;
	}

	public Image getImageBR()
	{
		slice();
		return img_br;
	}

	public void dispose()
	{
		file = null;

		if (img_tl != null)
		{
			img_tl.dispose();
			img_tl = null;
		}
		if (img_tc != null)
		{
			img_tc.dispose();
			img_tc = null;
		}
		if (img_tr != null)
		{
			img_tr.dispose();
			img_tr = null;
		}

		if (img_cl != null)
		{
			img_cl.dispose();
			img_cl = null;
		}
		if (img_cc != null)
		{
			img_cc.dispose();
			img_cc = null;
		}
		if (img_cr != null)
		{
			img_cr.dispose();
			img_cr = null;
		}

		if (img_bl != null)
		{
			img_bl.dispose();
			img_bl = null;
		}
		if (img_bc != null)
		{
			img_bc.dispose();
			img_bc = null;
		}
		if (img_br != null)
		{
			img_br.dispose();
			img_br = null;
		}
	}

	private void slice()
	{
		if (sliced)
		{
			return;
		}

		sliced = true;

		try
		{
			BufferedImage image = ImageIO.read(file.getContents());

			int imgW = image.getWidth();
			int imgH = image.getHeight();

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

			BufferedImage tempImg = null;
			Graphics tempGc = null;
			ByteArrayOutputStream tempStream = null;

			// tl
			if (l > 0 && t > 0)
			{
				tempImg = new BufferedImage(l, t, BufferedImage.TYPE_4BYTE_ABGR);
				tempGc = tempImg.getGraphics();
				tempGc.drawImage(image, 0, 0, l, t, 0, 0, l, t, null);
				tempGc.dispose();
				tempStream = new ByteArrayOutputStream();

				ImageIO.write(tempImg, "png", tempStream);

				img_tl = new Image(Display.getCurrent(), new ByteArrayInputStream(tempStream.toByteArray()));
			}

			// tc
			if (w > 0 && t > 0)
			{
				tempImg = new BufferedImage(w, t, BufferedImage.TYPE_4BYTE_ABGR);
				tempGc = tempImg.getGraphics();
				tempGc.drawImage(image, 0, 0, w, t, l, 0, l + w, t, null);
				tempGc.dispose();
				tempStream = new ByteArrayOutputStream();

				ImageIO.write(tempImg, "png", tempStream);

				img_tc = new Image(Display.getCurrent(), new ByteArrayInputStream(tempStream.toByteArray()));
			}

			// tr
			if (r > 0 && t > 0)
			{
				tempImg = new BufferedImage(r, t, BufferedImage.TYPE_4BYTE_ABGR);
				tempGc = tempImg.getGraphics();
				tempGc.drawImage(image, 0, 0, r, t, imgW - r, 0, imgW, t, null);
				tempGc.dispose();
				tempStream = new ByteArrayOutputStream();

				ImageIO.write(tempImg, "png", tempStream);

				img_tr = new Image(Display.getCurrent(), new ByteArrayInputStream(tempStream.toByteArray()));
			}

			// cl
			if (l > 0 && h > 0)
			{
				tempImg = new BufferedImage(l, h, BufferedImage.TYPE_4BYTE_ABGR);
				tempGc = tempImg.getGraphics();
				tempGc.drawImage(image, 0, 0, l, h, 0, t, l, t + h, null);
				tempGc.dispose();
				tempStream = new ByteArrayOutputStream();

				ImageIO.write(tempImg, "png", tempStream);

				img_cl = new Image(Display.getCurrent(), new ByteArrayInputStream(tempStream.toByteArray()));
			}

			// cc
			if (w > 0 && h > 0)
			{
				tempImg = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
				tempGc = tempImg.getGraphics();
				tempGc.drawImage(image, 0, 0, w, h, l, t, l + w, t + h, null);
				tempGc.dispose();
				tempStream = new ByteArrayOutputStream();

				ImageIO.write(tempImg, "png", tempStream);

				img_cc = new Image(Display.getCurrent(), new ByteArrayInputStream(tempStream.toByteArray()));
			}

			// cr
			if (r > 0 && h > 0)
			{
				tempImg = new BufferedImage(r, h, BufferedImage.TYPE_4BYTE_ABGR);
				tempGc = tempImg.getGraphics();
				tempGc.drawImage(image, 0, 0, r, h, l + w, t, l + w + r, t + h, null);
				tempGc.dispose();
				tempStream = new ByteArrayOutputStream();

				ImageIO.write(tempImg, "png", tempStream);

				img_cr = new Image(Display.getCurrent(), new ByteArrayInputStream(tempStream.toByteArray()));
			}

			// bl
			if (l > 0 && b > 0)
			{
				tempImg = new BufferedImage(l, b, BufferedImage.TYPE_4BYTE_ABGR);
				tempGc = tempImg.getGraphics();
				tempGc.drawImage(image, 0, 0, l, b, 0, t + h, l, t + h + b, null);
				tempGc.dispose();
				tempStream = new ByteArrayOutputStream();

				ImageIO.write(tempImg, "png", tempStream);

				img_bl = new Image(Display.getCurrent(), new ByteArrayInputStream(tempStream.toByteArray()));
			}

			// bc
			if (w > 0 && b > 0)
			{
				tempImg = new BufferedImage(w, b, BufferedImage.TYPE_4BYTE_ABGR);
				tempGc = tempImg.getGraphics();
				tempGc.drawImage(image, 0, 0, w, b, l, t + h, l + w, t + h + b, null);
				tempGc.dispose();
				tempStream = new ByteArrayOutputStream();

				ImageIO.write(tempImg, "png", tempStream);

				img_bc = new Image(Display.getCurrent(), new ByteArrayInputStream(tempStream.toByteArray()));
			}

			// br
			if (r > 0 && b > 0)
			{
				tempImg = new BufferedImage(r, b, BufferedImage.TYPE_4BYTE_ABGR);
				tempGc = tempImg.getGraphics();
				tempGc.drawImage(image, 0, 0, r, b, l + w, t + h, l + w + r, t + h + b, null);
				tempGc.dispose();
				tempStream = new ByteArrayOutputStream();

				ImageIO.write(tempImg, "png", tempStream);

				img_br = new Image(Display.getCurrent(), new ByteArrayInputStream(tempStream.toByteArray()));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
	}
}
