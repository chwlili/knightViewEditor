package org.game.knight.editor.xml.design;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.game.knight.ast.DefineControlTag;

public class ControlPart extends AbstractGraphicalEditPart
{
	private TagHelper helper;
	
	private int parentW = 0;
	private int parentH = 0;
	private int selfW = 0;
	private int selfH = 0;

	
	/**
	 * ��ȡģ��
	 * @return
	 */
	protected DefineControlTag getTag()
	{
		return (DefineControlTag)getModel();
	}
	
	/**
	 * ��ȡģ�͸�����
	 * 
	 * @return
	 */
	protected TagHelper getTagHelper()
	{
		if (helper == null)
		{
			helper = new TagHelper((DefineControlTag) getModel());
		}
		return helper;
	}
	
	/**
	 * ��ȡģ���Ӽ�
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected List getModelChildren()
	{
		ArrayList<Object> list = new ArrayList<Object>();
		if (getTag().getChildren() != null)
		{
			for (Object tag : getTag().getChildren())
			{
				if (tag instanceof DefineControlTag)
				{
					list.add(tag);
				}
			}
		}
		return list;
	}

	/**
	 * ��װ�༭����
	 */
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

	/**
	 * ������ͼ
	 */
	@Override
	protected IFigure createFigure()
	{
		RectangleFigure rect = new RectangleFigure();
		rect.setAlpha(150);
		rect.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));

		return rect;
	}

	/**
	 * �����С
	 * @param w
	 * @param h
	 */
	public void resetParentSize(int w, int h)
	{
		parentW = w;
		parentH = h;

		refresh();
	}

	/**
	 * ˢ����ͼ
	 */
	@Override
	protected void refreshVisuals()
	{
		TagHelper tag=getTagHelper();
		
		double x = tag.getX();
		double y = tag.getY();
		double w = tag.getW();
		double h = tag.getH();

		if (tag.hasLeft() && tag.hasRight())
		{
			x = tag.getLeft();
			w = parentW - tag.getLeft() - tag.getRight();
			if (w < 0)
			{
				w = 0;
			}
		}
		else
		{
			if (tag.hasCenter())
			{
				x = (parentW - w) / 2 + tag.getCenter();
			}
			else if (tag.hasRight())
			{
				x = parentW - tag.getRight() - w;
			}
			else if (tag.hasLeft())
			{
				x = tag.getLeft();
			}
		}

		if (tag.hasTop() && tag.hasBottom())
		{
			y = tag.getTop();
			h = parentH - tag.getTop() - tag.getBottom();
			if (h < 0)
			{
				h = 0;
			}
		}
		else
		{
			if (tag.hasMiddle())
			{
				y = (parentH - h) / 2 + tag.getMiddle();
			}
			else if (tag.hasBottom())
			{
				y = parentH - tag.getBottom() - h;
			}
			else if (tag.hasTop())
			{
				y = tag.getTop();
			}
		}

		IFigure view = getFigure();
		view.setLocation(new Point(view.getParent().getBounds().x + (int) x, view.getParent().getBounds().y + (int) y));
		view.setSize((int) w, (int) h);

		selfW = (int) w;
		selfH = (int) h;
	}

	/**
	 * ˢ���Ӽ�
	 */
	@Override
	protected void refreshChildren()
	{
		super.refreshChildren();

		for (Object child : getChildren())
		{
			if (child instanceof ControlPart)
			{
				ControlPart part = (ControlPart) child;
				part.resetParentSize(selfW, selfH);
			}
		}
	}
}
