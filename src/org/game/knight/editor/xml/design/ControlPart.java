package org.game.knight.editor.xml.design;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
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
	 * 获取模型
	 * 
	 * @return
	 */
	protected DefineControlTag getTag()
	{
		return (DefineControlTag) getModel();
	}

	/**
	 * 获取模型辅助器
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
	 * 获取模型子级
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
	 * 安装编辑策略
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

			@Override
			protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint)
			{
				Rectangle rect_child=((ControlPart)child).getFigure().getBounds();
				Rectangle rect_parent=((ControlPart)child).getFigure().getParent().getBounds();
				Rectangle rect_new=(Rectangle)constraint;
				
				DefineControlTag model=(DefineControlTag) ((ControlPart)child).getModel();
				if(model.hasAttribute("width"))
				{
					model.getAttribute("width").setValue(rect_new.width+"");
				}
				if(model.hasAttribute("height"))
				{
					model.getAttribute("height").setValue(rect_new.height+"");
				}
				child.refresh();
				return super.createChangeConstraintCommand(request, child, constraint);
			}
		});
	}

	/**
	 * 创建视图
	 */
	@Override
	protected IFigure createFigure()
	{
		RectangleFigure rect = new RectangleFigure();
		rect.setLayoutManager(new XYLayout());
		rect.setAlpha(150);
		rect.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));

		return rect;
	}

	/**
	 * 重设大小
	 * 
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
	 * 刷新视图
	 */
	@Override
	protected void refreshVisuals()
	{
		TagHelper tag = getTagHelper();

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
	 * 刷新子级
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
