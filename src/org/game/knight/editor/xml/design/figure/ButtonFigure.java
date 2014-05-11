package org.game.knight.editor.xml.design.figure;

import java.util.Hashtable;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public class ButtonFigure extends Figure
{
	private ImageFigure backView;
	private Label labelView;

	private boolean downed = false;
	private boolean entered = false;

	private String label = "";
	private Hashtable<String, SliceImage> imageTable = new Hashtable<String, SliceImage>();
	private Hashtable<String, Font> fontTable = new Hashtable<String, Font>();
	private Hashtable<String, Color> colorTable = new Hashtable<String, Color>();

	public ButtonFigure()
	{
		backView = new ImageFigure();
		labelView = new Label();
		add(backView);
		add(labelView);

		addMouseListener(mouseListener);
		addMouseMotionListener(motionListener);
	}

	@Override
	public void setBounds(Rectangle rect)
	{
		super.setBounds(rect);

		refresh();
	}

	/**
	 * 获取标签
	 * 
	 * @return
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * 设置标签
	 * 
	 * @param value
	 */
	public void setLabel(String value)
	{
		if (value == null)
		{
			value = "";
		}
		if (!value.equals(label))
		{
			label = value;
			labelView.setText(label);
			refresh();
		}
	}

	/**
	 * 添加图像
	 * 
	 * @param key
	 * @param file
	 */
	public void addImage(String key, SliceImage file)
	{
		if (key != null)
		{
			imageTable.remove(key);

			if (file != null)
			{
				imageTable.put(key, file);
			}
			refresh();
		}
	}

	/**
	 * 添加字体
	 * 
	 * @param key
	 * @param format
	 */
	public void addFormat(String key, Font format)
	{
		if (key != null)
		{
			fontTable.remove(key);

			if (format != null)
			{
				fontTable.put(key, format);
			}
			refresh();
		}
	}

	/**
	 * 添加颜色
	 * 
	 * @param key
	 * @param color
	 */
	public void addColor(String key, Color color)
	{
		if (key != null)
		{
			colorTable.remove(key);

			if (color != null)
			{
				colorTable.put(key, color);
			}
			refresh();
		}
	}

	/**
	 * 删除所有格式
	 */
	public void removeAllResource()
	{
		imageTable.clear();
		fontTable.clear();
		colorTable.clear();
		refresh();
	}

	/**
	 * 鼠标进入
	 */
	protected void handleMouseEntered()
	{
		entered = true;
		refresh();
	}

	/**
	 * 鼠标退出
	 */
	protected void handleMouseExit()
	{
		entered = false;
		refresh();
	}

	/**
	 * 鼠标按下
	 */
	protected void handleMouseDown()
	{
		// downed = true;
		refresh();
	}

	/**
	 * 鼠标松开
	 */
	protected void handleMouseUp()
	{
		// downed = false;
		refresh();
	}

	/**
	 * 计算状态
	 */
	protected void refresh()
	{
		SliceImage img = null;
		Font font = null;
		Color color = null;

		if (downed)
		{
			img = imageTable.get("3");
			font = fontTable.get("3");
			color = colorTable.get("3");
		}
		else
		{
			if (entered)
			{
				img = imageTable.get("2");
				font = fontTable.get("2");
				color = colorTable.get("2");
			}
		}

		if (img == null)
		{
			img = imageTable.get("1");
		}

		if (font == null)
		{
			font = fontTable.get("1");
		}

		if (color == null)
		{
			color = colorTable.get("1");
		}

		backView.setImage(img);
		labelView.setFont(font);
		labelView.setForegroundColor(color);

		Rectangle rect = getBounds();
		backView.setBounds(rect);
		labelView.setBounds(rect);
		if(label!= null && labelView.getFont()!=null)
		{
			Dimension size = FigureUtilities.getTextExtents(label, labelView.getFont());
			labelView.setBounds(new Rectangle(rect.x, rect.y+(rect.height-size.height)/2, rect.width, size.height));
		}

		backView.repaint();
		labelView.repaint();
	}

	/**
	 * 点击处理器
	 */
	private MouseListener mouseListener = new MouseListener.Stub()
	{
		@Override
		public void mousePressed(MouseEvent me)
		{
			if (me.button == 1)
			{
				handleMouseDown();
			}
		}

		@Override
		public void mouseReleased(MouseEvent me)
		{
			if (me.button == 1)
			{
				handleMouseUp();
			}
		}
	};

	/**
	 * 移动处理器
	 */
	private MouseMotionListener motionListener = new MouseMotionListener.Stub()
	{
		@Override
		public void mouseEntered(MouseEvent me)
		{
			handleMouseEntered();
		}

		@Override
		public void mouseExited(MouseEvent me)
		{
			handleMouseExit();
		}
	};
}
