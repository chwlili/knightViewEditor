package org.game.knight.editor.xml.design;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.IFigure;
import org.game.knight.ast2.UIImage;
import org.game.knight.editor.xml.design.figure.ImageFigure;
import org.game.knight.editor.xml.design.figure.SliceImage;
import org.game.knight.editor.xml.design.figure.SliceImageDescriptor;

public class ImagePart extends ControlPart
{
	private ImageFigure view = null;
	
	private SliceImageDescriptor imageDescriptor=null;
	
	private IFile file;
	private boolean slice;
	private int left;
	private int top;
	private int right;
	private int bottom;
	

	@Override
	public void removeNotify()
	{
		releaseSliceImage();
		super.removeNotify();
	}
	
	private void releaseSliceImage()
	{
		if(imageDescriptor!=null)
		{
			getViewer().getResourceManager().destroy(imageDescriptor);
			imageDescriptor=null;
		}
		view.setImage(null);
		
		file=null;
		slice=false;
		left=0;
		top=0;
		right=0;
		bottom=0;
	}
	
	@Override
	protected IFigure createFigure()
	{
		view = new ImageFigure();

		return view;
	}
	
	@Override
	protected void createEditPolicies()
	{
		//super.createEditPolicies();
	}

	@Override
	protected void refreshVisuals()
	{
		super.refreshVisuals();
		
		UIImage imageData=(UIImage)getModel();
		
		IFile file=imageData.getBitmapFile();
		boolean slice=imageData.hasGrid();
		int left=imageData.getGridLeft();
		int top=imageData.getGridTop();
		int right=imageData.getGridRight();
		int bottom=imageData.getGridBottom();
		
		if(file!=null)
		{
			if(!file.equals(this.file) || slice!=this.slice || left!=this.left || top!=this.top || right!=this.right || bottom!=this.bottom)
			{
				releaseSliceImage();
				imageDescriptor=new SliceImageDescriptor(file, slice, left, top, right, bottom);
			}
			
			view.setImage((SliceImage)(getViewer().getResourceManager().create(imageDescriptor)));
		}
		else
		{
			releaseSliceImage();
		}
		
		this.slice=slice;
		this.left=left;
		this.top=top;
		this.right=right;
		this.bottom=bottom;
	}

}
