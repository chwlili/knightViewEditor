package org.game.knight.editor.xml.design;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.game.knight.ast.DefineControlTag;

public class ControlPart extends AbstractGraphicalEditPart
{
	private Figure view;
	
	private DefineControlTag getTag()
	{
		return (DefineControlTag)getModel();
	}
	
	@Override
	protected List getModelChildren()
	{
		ArrayList<Object> list=new ArrayList<Object>();
		if(getTag().getChildren()!=null)
		{
			for(Object tag:getTag().getChildren())
			{
				if(tag instanceof DefineControlTag)
				{
					list.add(tag);
				}
			}
		}
		return list;
	}
	
	@Override
	protected IFigure createFigure()
	{
		view=GefFigureFactory.createFigure(getTag());
		
		return view;
	}

	@Override
	protected void createEditPolicies()
	{
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy()
		{
			@Override
			protected Command getCreateCommand(CreateRequest request)
			{
				return null;
			}
			
			@Override
			protected Command getChangeConstraintCommand(ChangeBoundsRequest request)
			{
				return super.getChangeConstraintCommand(request);
			}
		});
	}

	@Override
	protected void refreshVisuals()
	{
		
		int x=getTag().hasAttribute("x") ? Integer.parseInt(getTag().getAttributeValue("x")):getTag().hasAttribute("left") ? Integer.parseInt(getTag().getAttributeValue("left")):0;
		int y=getTag().hasAttribute("y") ? Integer.parseInt(getTag().getAttributeValue("y")):getTag().hasAttribute("top") ? Integer.parseInt(getTag().getAttributeValue("top")):0;
		
		view.setLocation(new Point(x, y));

		int w=getTag().hasAttribute("width") ? Integer.parseInt(getTag().getAttributeValue("width")):0;
		int h=getTag().hasAttribute("height") ? Integer.parseInt(getTag().getAttributeValue("height")):0;
		view.setSize(w, h);
	}
	
	@Override
	protected void refreshChildren()
	{
		super.refreshChildren();
		
		//getChildren()
	}
}
