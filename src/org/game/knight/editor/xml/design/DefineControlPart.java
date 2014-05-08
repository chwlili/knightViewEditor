package org.game.knight.editor.xml.design;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;

public class DefineControlPart extends AbstractGraphicalEditPart
{
	private int selfW=0;
	private int selfH=0;
	
	
	private ControlListener resizeListener=new ControlListener()
	{
		@Override
		public void controlResized(ControlEvent e)
		{
			refresh();
		}
		
		@Override
		public void controlMoved(ControlEvent e)
		{
		}
	};
	
	@SuppressWarnings("rawtypes")
	@Override
	protected List getModelChildren()
	{
		TagInput box=(TagInput) getModel();
		ArrayList<Object> list=new ArrayList<Object>();
		list.add(box.tag);
		return list;
	}
	
	@Override
	public void activate()
	{
		super.activate();
		getViewer().getControl().addControlListener(resizeListener);
	}
	
	@Override
	public void deactivate()
	{
		getViewer().getControl().removeControlListener(resizeListener);
		
		super.deactivate();
	}
	
	@Override
	protected IFigure createFigure()
	{
		return new RectangleFigure();
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
		selfW=getViewer().getControl().getBounds().width;
		selfH=getViewer().getControl().getBounds().height;
		
		getFigure().setSize(selfW, selfH);
	}
	
	@Override
	protected void refreshChildren()
	{
		super.refreshChildren();
		
		for(Object child:getChildren())
		{
			if(child instanceof ControlPart)
			{
				ControlPart part=(ControlPart)child;
				part.resetParentSize(selfW, selfH);
			}
		}
	}
}
