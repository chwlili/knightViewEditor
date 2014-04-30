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
import org.game.knight.ast.DefineImgTag;

public class DefineImgPart extends AbstractGraphicalEditPart
{
	private Image img;
	private String imgURL;

	private Figure view;
	private ImageBack imgBack;
	private ImageDraw imgView;

	private boolean downed=false;
	private int downX = 0;
	private int downY = 0;
	private int downOffsetX = 0;
	private int downOffsetY = 0;

	public DefineImgTag getTag()
	{
		return (DefineImgTag) getModel();
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
		
		if(img!=null)
		{
			img.dispose();
			img=null;
		}
		
		imgURL=null;
		
		super.deactivate();
	}

	@Override
	protected IFigure createFigure()
	{
		view = new Figure();
		imgBack = new ImageBack();
		imgView = new ImageDraw();

		view.add(imgBack);
		view.add(imgView);

		return view;
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

			imgView.setImage(img);
		}

		view.setBounds(new Rectangle(0, 0, getViewer().getControl().getBounds().width-1, getViewer().getControl().getBounds().height-1));
		imgBack.setBounds(view.getBounds());
		imgView.setBounds(view.getBounds());
	}

	// tool
	private AbstractTool tool = new AbstractTool()
	{
		@Override
		protected String getCommandName()
		{
			return null;
		}
		
		protected boolean handleButtonDown(int button)
		{
			downed=true;
			downX = Display.getCurrent().getCursorLocation().x;
			downY = Display.getCurrent().getCursorLocation().y;
			downOffsetX = imgView.getOffsetX();
			downOffsetY = imgView.getOffsetY();
			
			return true;
		};
		
		protected boolean handleButtonUp(int button) 
		{
			downed=false;
			
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
			if(downed)
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

	// 主控件移到，缩放监视器。
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

	
	//图像绘制组件
	protected class ImageDraw extends Figure
	{
		private Image img;

		private int offsetX;
		private int offsetY;
		private float scale = 1;

		public Image getImage()
		{
			return img;
		}

		public void setImage(Image value)
		{
			img = value;
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
				getUpdateManager().addDirtyRegion(this,getBounds());
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
				getUpdateManager().addDirtyRegion(this,getBounds());
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
				getUpdateManager().addDirtyRegion(this,getBounds());
			}
		}

		@Override
		public void paint(Graphics graphics)
		{
			int maxW = getBounds().width;
			int maxH = getBounds().height;
			
			if (img != null)
			{
				int x = (int) (maxW / 2 - img.getBounds().width / 2 * scale + offsetX);
				int y = (int) (maxH / 2 - img.getBounds().height / 2 * scale + offsetY);
				int w = (int) (img.getBounds().width * scale);
				int h = (int) (img.getBounds().height * scale);

				graphics.drawImage(img, 0, 0, img.getBounds().width, img.getBounds().height, x, y, w, h);
			}
		}
	}
}
