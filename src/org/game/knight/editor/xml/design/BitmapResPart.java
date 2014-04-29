package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.game.knight.ast.DefineImgTag;

public class BitmapResPart extends AbstractGraphicalEditPart
{
	private Image img;
	private ImageFigure imgView;
	
	public DefineImgTag getTag()
	{
		return (DefineImgTag)getModel();
	}
	
	@Override
	protected IFigure createFigure()
	{
		imgView=new ImageFigure();
		
		return imgView;
	}

	@Override
	protected void createEditPolicies()
	{
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ResizableEditPolicy());
	}
	
	@Override
	protected void refreshVisuals()
	{
		if(img!=null)
		{
			img.dispose();
		}
		
		img=new Image(Display.getCurrent(), getTag().getImg().getLocation().toOSString());
		
		if(img!=null)
		{
			imgView.setImage(img);
		
			int x=(imgView.getParent().getBounds().width-img.getBounds().width)/2;
			int y=(imgView.getParent().getBounds().height-img.getBounds().height)/2;
			
			imgView.setLocation(new Point(x, y));
			imgView.setSize(img.getBounds().width,img.getBounds().height);
		}
		else
		{
			imgView.setImage(null);
		}
	}
}
