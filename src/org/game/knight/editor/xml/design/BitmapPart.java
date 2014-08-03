package org.game.knight.editor.xml.design;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.IFigure;
import org.game.knight.ast2.UIBitmap;
import org.game.knight.editor.xml.design.figure.ImageFigure;
import org.game.knight.editor.xml.design.figure.SliceImage;
import org.game.knight.editor.xml.design.figure.SliceImageDescriptor;

public class BitmapPart extends ControlPart
{
	private ImageFigure view = null;
	
	private SliceImageDescriptor imageDescriptor=null;

	private IFile file=null;
	
	@Override
	public void removeNotify()
	{
		releaseImage();
		super.removeNotify();
	}
	
	private void releaseImage()
	{
		if(imageDescriptor!=null)
		{
			getViewer().getResourceManager().destroy(imageDescriptor);
			imageDescriptor=null;
		}
		view.setImage(null);
		
		file=null;
	}
	
	@Override
	protected IFigure createFigure()
	{
		view = new ImageFigure();
		
		return view;
	}

	@Override
	protected void refreshVisuals()
	{
		super.refreshVisuals();

		IFile file=((UIBitmap)getModel()).getBitmapFile();
		if(file!=null)
		{
			if(!file.equals(this.file))
			{
				releaseImage();
				imageDescriptor=new SliceImageDescriptor(file, false, 0, 0, 0, 0);
			}
			
			view.setImage((SliceImage) getViewer().getResourceManager().get(imageDescriptor));
		}
		else
		{
			releaseImage();
			view.setImage(null);
		}
		
		this.file=file;
	}
}
