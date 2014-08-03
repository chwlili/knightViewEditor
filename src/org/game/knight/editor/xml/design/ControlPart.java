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
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.game.knight.ast2.UIBase;

public class ControlPart extends AbsPart
{
	private int parentW = 0;
	private int parentH = 0;
	private int selfW = 0;
	private int selfH = 0;

	/**
	 * 获取模型子级
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected List getModelChildren()
	{
		ArrayList<Object> list = new ArrayList<Object>();
		UIBase base=(UIBase)getModel();
		for(int i=0;i<base.getChildCount();i++)
		{
			list.add(base.getChildAt(i));
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
			protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint)
			{
				return new SetCommand(getEditor().getDocument(),(UIBase)((ControlPart) child).getModel(),(Rectangle)constraint);
			}
			
			@Override
			protected Command getOrphanChildrenCommand(Request request)
			{
				return super.getOrphanChildrenCommand(request);
			}
			@Override
			protected Command getResizeChildrenCommand(ChangeBoundsRequest request)
			{
				return super.getResizeChildrenCommand(request);
			}
			
			@Override
			public Command getCommand(Request request)
			{
				return super.getCommand(request);
			}
		});
	}

	private static class SetCommand extends Command
	{
		private UIBase tag;
		private int oldX;
		private int oldY;
		private int oldW;
		private int oldH;
		private Rectangle rect;
		
		public SetCommand(IDocument dom,UIBase tag,Rectangle rect)
		{
			this.tag=tag;
			this.rect=rect;
			
			oldX=0;
			oldY=0;
			oldW=0;
			oldH=0;
			if(tag.hasAttribute("x"))
			{
				oldX=tag.getX();
			}
			if(tag.hasAttribute("y"))
			{
				oldY=tag.getY();
			}
			if(tag.hasAttribute("width"))
			{
				oldW=tag.getWidth();
			}
			if(tag.hasAttribute("height"))
			{
				oldH=tag.getHeight();
			}
			//dom.replace(offset, length, text);
		}
		@Override
		public String getLabel()
		{
			return "toto";
		}

		@Override
		public void execute()
		{
			redo();
		}

		@Override
		public void redo()
		{
			tag.setX(rect.x);
			tag.setY(rect.y);
			tag.setWidth(rect.width);
			tag.setHeight(rect.height);
			tag.fireTagChanged();
		}

		@Override
		public void undo()
		{
			tag.setX(oldX);
			tag.setY(oldY);
			tag.setWidth(oldW);
			tag.setHeight(oldH);
			tag.fireTagChanged();
		}
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
		UIBase tag=(UIBase)getModel();

		double x = tag.getX();
		double y = tag.getY();
		double w = tag.getWidth();
		double h = tag.getHeight();

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
