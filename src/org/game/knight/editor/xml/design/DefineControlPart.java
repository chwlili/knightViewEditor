package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.game.knight.PluginResource;
import org.game.knight.ast.AbsTag;

public class DefineControlPart extends AbstractGraphicalEditPart
{
	private Figure view;
	
	private AbsTag getTag()
	{
		return (AbsTag)getModel();
	}
	
	@Override
	protected IFigure createFigure()
	{
		view=new RectangleFigure();
		view.setBackgroundColor(PluginResource.getColor(255,0,0));
		
		
		return view;
	}

	@Override
	protected void createEditPolicies()
	{
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new NonResizableEditPolicy());
	}

	@Override
	protected void refreshVisuals()
	{
		int w=Integer.parseInt(getTag().getAttributeValue("width"));
		int h=Integer.parseInt(getTag().getAttributeValue("height"));
		
		view.setSize(w, h);
	}
}
