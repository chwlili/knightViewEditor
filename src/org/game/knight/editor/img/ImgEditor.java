package org.game.knight.editor.img;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.game.knight.PluginResource;

public class ImgEditor extends EditorPart implements IMenuListener, PaintListener, MouseListener, MouseMoveListener, MouseWheelListener
{
	public static final String ID = "org.game.editors.IMG";

	private Canvas canvas;
	private Image image;
	private int back = 3;

	private float scale = 1;
	private float offsetX = 0;
	private float offsetY = 0;
	private float beginOffsetX = 0;
	private float beginOffsetY = 0;
	private boolean downed = false;
	private int downX = 0;
	private int downY = 0;

	public ImgEditor()
	{
	}

	@Override
	public void doSave(IProgressMonitor monitor)
	{
	}

	@Override
	public void doSaveAs()
	{
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException
	{
		setSite(site);
		setInput(input);

		setPartName(input.getName());
	}

	@Override
	public boolean isDirty()
	{
		return false;
	}

	@Override
	public boolean isSaveAsAllowed()
	{
		return false;
	}

	@Override
	public void createPartControl(Composite parent)
	{
		parent.setLayout(new FillLayout());

		canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.addMouseListener(this);
		canvas.addMouseMoveListener(this);
		canvas.addMouseWheelListener(this);
		canvas.addPaintListener(this);

		IFileEditorInput fileInput = (IFileEditorInput) getEditorInput();
		IFile file = fileInput.getFile();
		try
		{
			image = new Image(canvas.getDisplay(), new ImageData(file.getContents()));
		}
		catch (CoreException err)
		{
		}

		MenuManager manager = new MenuManager("", "");
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(this);

		canvas.setMenu(manager.createContextMenu(canvas));
	}

	@Override
	public void setFocus()
	{
		canvas.setFocus();
	}

	@Override
	public void dispose()
	{
		if (image == null)
		{
			return;
		}

		image.dispose();
		super.dispose();
	}

	@Override
	public void menuAboutToShow(IMenuManager manager)
	{
		manager.add(new ScaleAction(0,"¸´Î»"));
		manager.add(new Separator());
		manager.add(new ScaleAction(1,"·Å´ó"));
		manager.add(new ScaleAction(2,"ËõÐ¡"));
		manager.add(new Separator());
		manager.add(new BackAction(1, "°×É«±³¾°", back == 1));
		manager.add(new BackAction(2, "ºÚÉ«±³¾°", back == 2));
		manager.add(new BackAction(3, "ÆåÅÌ¸ñ±³¾°", back == 3));
	}

	private class ScaleAction extends Action
	{
		private int type;

		public ScaleAction(int type, String text)
		{
			this.type = type;
			setText(text);
		}
		
		@Override
		public void run()
		{
			if(type==0)
			{
				scale=1;
				offsetX=0;
				offsetY=0;
			}
			else if(type==1)
			{
				scale+=.1f;
			}
			else if(type==2)
			{
				scale-=.1f;
			}
			
			if(scale<0)
			{
				scale=0;
			}
			canvas.redraw();
		}
	}
	
	private class BackAction extends Action
	{
		private int type;

		public BackAction(int type, String text, boolean selected)
		{
			this.type = type;
			setText(text);
			setChecked(selected);
		}

		@Override
		public void run()
		{
			if (back != type)
			{
				back = type;
				canvas.redraw();
			}
		}
	}

	@Override
	public void paintControl(PaintEvent e)
	{
		if (image == null)
		{
			return;
		}
		
		if(scale<0)
		{
			scale=0;
		}

		Rectangle imageRect = image.getBounds();
		Rectangle canvasRect = canvas.getBounds();

		float drawX = canvasRect.width / 2 - imageRect.width / 2 * scale + offsetX;
		float drawY = canvasRect.height / 2 - imageRect.height / 2 * scale + offsetY;
		float drawW = imageRect.width * scale;
		float drawH = imageRect.height * scale;
		
		
		if (back == 1)
		{
			e.gc.setBackground(PluginResource.getColor(255, 255, 255));
			e.gc.fillRectangle(canvasRect);
		}
		else if (back == 2)
		{
			e.gc.setBackground(PluginResource.getColor(0, 0, 0));
			e.gc.fillRectangle(canvasRect);
		}
		else
		{
			e.gc.setBackground(PluginResource.getColor(204, 204, 204));
			
			int cellSize=15;
			int rowIndex=0;
			int x=0;
			int y=0;
			while(x<canvasRect.width)
			{
				if(rowIndex%2==0)
				{
					e.gc.fillRectangle(x, y, cellSize, cellSize);
				}
				else
				{
					e.gc.fillRectangle(x+cellSize, y, cellSize, cellSize);
				}
				
				x+=cellSize*2;
				
				if(x>canvasRect.width)
				{
					x=0;
					y+=cellSize;
					rowIndex++;
					
					if(y>canvasRect.height)
					{
						break;
					}
				}
			}
		}

		e.gc.drawImage(image, 0, 0, imageRect.width, imageRect.height, (int) drawX, (int) drawY, (int) drawW, (int) drawH);
		e.gc.dispose();
	}

	@Override
	public void mouseDown(MouseEvent e)
	{
		beginOffsetX = offsetX;
		beginOffsetY = offsetY;
		downed = true;
		downX = e.x;
		downY = e.y;
		canvas.setCapture(true);
		canvas.setCursor(canvas.getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL));
	}

	@Override
	public void mouseMove(MouseEvent e)
	{
		if (downed)
		{
			offsetX = beginOffsetX + (e.x - downX);
			offsetY = beginOffsetY + (e.y - downY);
			canvas.redraw();
		}
	}

	@Override
	public void mouseUp(MouseEvent e)
	{
		downed = false;
		canvas.setCapture(false);
		canvas.setCursor(canvas.getDisplay().getSystemCursor(SWT.DEFAULT));
	}

	@Override
	public void mouseDoubleClick(MouseEvent e)
	{
		offsetX = 0;
		offsetY = 0;
		scale = 1;
		canvas.redraw();
	}

	@Override
	public void mouseScrolled(MouseEvent e)
	{
		scale += (e.count > 0 ? 1f / 10f : -1f / 10f);
		if (scale < 0)
		{
			scale = 0;
		}

		canvas.redraw();
	}

}
