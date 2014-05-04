package org.game.knight.editor.xml.design;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.game.knight.PluginResource;
import org.game.knight.ast.DefineGridImgTag;

public class DefineGridImgPart extends AbstractGraphicalEditPart
{
	private Image img;
	private String imgURL;
	private ImageDraw imgView;

	private boolean downed = false;
	private int downX = 0;
	private int downY = 0;
	private int downOffsetX = 0;
	private int downOffsetY = 0;

	public DefineGridImgTag getTag()
	{
		return (DefineGridImgTag) getModel();
	}

	@Override
	public void activate()
	{
		super.activate();

		getViewer().getControl().addControlListener(controlListener);
		getViewer().getEditDomain().setActiveTool(tool);
	}

	@Override
	public void deactivate()
	{
		getViewer().getControl().removeControlListener(controlListener);
		getViewer().getEditDomain().setActiveTool(getViewer().getEditDomain().getDefaultTool());

		if (img != null)
		{
			img.dispose();
			img = null;
		}

		imgURL = null;

		super.deactivate();
	}

	@Override
	protected IFigure createFigure()
	{
		return imgView = new ImageDraw();
	}

	@Override
	protected void createEditPolicies()
	{
		// installEditPolicy(EditPolicy.LAYOUT_ROLE, new ResizableEditPolicy());
	}

	@Override
	protected void refreshVisuals()
	{
		String url = null;

		IFile file = getTag().getImg();
		if (file != null)
		{
			url = file.getLocation().toOSString();
		}

		if (url != imgURL)
		{
			imgURL = url;

			if (img != null)
			{
				img.dispose();
				img = null;
			}

			if (imgURL != null)
			{
				img = new Image(Display.getCurrent(), imgURL);
			}
			imgView.setURL(getTag().getSrc());
			imgView.setImage(img);
		}

		imgView.setBounds(new Rectangle(0, 0, getViewer().getControl().getBounds().width, getViewer().getControl().getBounds().height));
	}

	// tool
	private AbstractTool tool = new AbstractTool()
	{
		@Override
		protected String getCommandName()
		{
			return null;
		}

		protected boolean handleNativeDragFinished(org.eclipse.swt.dnd.DragSourceEvent event) {
			return true;
		};
		protected boolean handleButtonDown(int button)
		{
			downed = true;
			downX = Display.getCurrent().getCursorLocation().x;
			downY = Display.getCurrent().getCursorLocation().y;
			downOffsetX = imgView.getOffsetX();
			downOffsetY = imgView.getOffsetY();

			return true;
		};

		protected boolean handleButtonUp(int button)
		{
			downed = false;

			return true;
		};

		protected boolean handleDoubleClick(int button)
		{
			imgView.setOffsetX(0);
			imgView.setOffsetY(0);
			imgView.setScale(1);

			return true;
		};

		protected boolean handleDragInProgress()
		{
			if (downed)
			{
				imgView.setOffsetX(downOffsetX + (Display.getCurrent().getCursorLocation().x - downX));
				imgView.setOffsetY(downOffsetY + (Display.getCurrent().getCursorLocation().y - downY));
			}
			return true;
		};

		protected void performViewerMouseWheel(org.eclipse.swt.widgets.Event event, org.eclipse.gef.EditPartViewer viewer)
		{
			float scale = imgView.getScale();

			scale += event.count / 10f;
			if (scale < 0)
			{
				scale = 0;
			}

			imgView.setScale(scale);
		};
	};

	// Ö÷¿Ø¼þÒÆµ½£¬Ëõ·Å¼àÊÓÆ÷¡£
	private ControlListener controlListener = new ControlListener()
	{
		@Override
		public void controlResized(ControlEvent e)
		{
			refreshVisuals();
		}

		@Override
		public void controlMoved(ControlEvent e)
		{
		}
	};

	// Í¼Ïñ»æÖÆ×é¼þ
	protected class ImageDraw extends Figure
	{
		private Image img;
		private String url;

		private int back;
		private int offsetX;
		private int offsetY;
		private float scale = 1;

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

		public Image getImage()
		{
			return img;
		}

		public void setImage(Image value)
		{
			img = value;
			invalidate();
		}

		public String setURL()
		{
			return url;
		}

		public void setURL(String value)
		{
			url = value;
			invalidate();
		}

		public int getOffsetX()
		{
			return offsetX;
		}

		public void setOffsetX(int value)
		{
			if (offsetX != value)
			{
				this.offsetX = value;
				getUpdateManager().addDirtyRegion(this, getBounds());
			}
		}

		public int getOffsetY()
		{
			return offsetY;
		}

		public void setOffsetY(int value)
		{
			if (offsetY != value)
			{
				offsetY = value;
				getUpdateManager().addDirtyRegion(this, getBounds());
			}
		}

		public float getScale()
		{
			return scale;
		}

		public void setScale(float value)
		{
			if (scale != value)
			{
				scale = value;
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

			if (img != null)
			{
				// »æÖÆÍ¼Ïñ
				int x = (int) (maxW / 2 - img.getBounds().width / 2 * scale + offsetX);
				int y = (int) (maxH / 2 - img.getBounds().height / 2 * scale + offsetY);
				int w = (int) (img.getBounds().width * scale);
				int h = (int) (img.getBounds().height * scale);

				graphics.drawImage(img, 0, 0, img.getBounds().width, img.getBounds().height, x, y, w, h);
			}

			// ×´Ì¬ÐÅÏ¢
			graphics.setForegroundColor(PluginResource.getColor(64, 112, 204));
			graphics.drawText(String.format("Â·¾¶£º%s\nËõ·Å£º%s\nÆ«ÒÆ£º%s,%s", url != null ? url : "", (int) (getScale() * 100) + "%", getOffsetX(), getOffsetY()), 3, 3);
		}
	}
}
