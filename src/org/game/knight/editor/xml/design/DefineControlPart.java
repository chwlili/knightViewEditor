package org.game.knight.editor.xml.design;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
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
	private Figure view;
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
	
	@Override
	protected List getModelChildren()
	{
		DefineControlTagBox box=(DefineControlTagBox) getModel();
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
	protected IFigure createFigure()
	{
		view=new RectangleFigure();
		
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
		int w=getViewer().getControl().getBounds().width;
		int h=getViewer().getControl().getBounds().height;
		
		view.setSize(w, h);
	}
}
