package org.game.knight.editor.xml.design.figure;

import java.util.Hashtable;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.game.knight.ast.DefineFormatTag;

public class ButtonFigure extends ImageFigure
{
	private boolean downed = false;
	private boolean entered = false;

	private String label = "";
	private Hashtable<String, IFile> imageTable = new Hashtable<String, IFile>();
	private Hashtable<String, DefineFormatTag> formatTable = new Hashtable<String, DefineFormatTag>();

	public ButtonFigure()
	{
		addMouseListener(mouseListener);
		addMouseMotionListener(motionListener);
	}

	/**
	 * ��ȡ��ǩ
	 * 
	 * @return
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * ���ñ�ǩ
	 * 
	 * @param value
	 */
	public void setLabel(String value)
	{
		label = value;
	}

	/**
	 * ���ͼ��
	 * 
	 * @param key
	 * @param file
	 */
	public void addImage(String key, IFile file)
	{
		if (key != null)
		{
			imageTable.remove(key);

			if (file != null)
			{
				imageTable.put(key, file);
			}
			repaint();
		}
	}

	/**
	 * ɾ��ͼ��
	 * 
	 * @param key
	 */
	public void delImage(String key)
	{
		if (key != null)
		{
			imageTable.remove(key);
			repaint();
		}
	}

	/**
	 * ɾ������ͼ��
	 */
	public void removeAllImage()
	{
		imageTable.clear();
		repaint();
	}

	/**
	 * �������
	 * 
	 * @param key
	 * @param format
	 */
	public void addFormat(String key, DefineFormatTag format)
	{
		if (key != null)
		{
			formatTable.remove(key);

			if (format != null)
			{
				formatTable.put(key, format);
			}
			repaint();
		}
	}

	/**
	 * ɾ������
	 * 
	 * @param key
	 */
	public void delFormat(String key)
	{
		if (key != null)
		{
			formatTable.remove(key);
			repaint();
		}
	}

	/**
	 * ɾ�����и�ʽ
	 */
	public void removeAllFormat()
	{
		formatTable.clear();
		repaint();
	}

	/**
	 * ������
	 */
	protected void handleMouseEntered()
	{
		entered = true;
		repaint();
	}

	/**
	 * ����˳�
	 */
	protected void handleMouseExit()
	{
		entered = false;
		repaint();
	}

	/**
	 * ��갴��
	 */
	protected void handleMouseDown()
	{
		// downed = true;
		repaint();
	}

	/**
	 * ����ɿ�
	 */
	protected void handleMouseUp()
	{
		// downed = false;
		repaint();
		System.out.println("..");
	}

	/**
	 * ����״̬
	 */
	protected void measureState()
	{
		if (downed)
		{
			setFile(imageTable.get("3") != null ? imageTable.get("3") : imageTable.get("1"));
		}
		else
		{
			if (entered)
			{
				setFile(imageTable.get("2") != null ? imageTable.get("2") : imageTable.get("1"));
			}
			else
			{
				setFile(imageTable.get("1"));
			}
		}
	}

	/**
	 * �ػ�
	 */
	@Override
	public void repaint(int x, int y, int w, int h)
	{
		measureState();
		super.repaint(x, y, w, h);
	}

	@Override
	protected void paintFigure(Graphics graphics)
	{
		super.paintFigure(graphics);

		if (label == null || label.isEmpty())
		{
			return;
		}

		DefineFormatTag tag = null;
		if (downed)
		{
			tag = formatTable.get("3");
		}
		else if (entered)
		{
			tag = formatTable.get("2");
		}

		if (tag == null)
		{
			tag = formatTable.get("1");
		}

		int alignment = SWT.LEFT;
		int indent = 0;
		int leftMargin = 0;
		int rightMargin = 0;
		int leading = 0;
		FontData fontData = new FontData();
		int rgb = 0;
		if (tag != null)
		{
			alignment = tag.getAlign().equals("left") ? SWT.LEFT : (tag.getAlign().equals("center") ? SWT.CENTER : SWT.RIGHT);
			indent = tag.getIndent();
			leftMargin = tag.getLeftMargin();
			rightMargin = tag.getRightMargin();
			leading = tag.getLeading();
			rgb = tag.getColor();

			if (tag.getFont() != null && tag.getFont().isEmpty() == false)
			{
				fontData.setName(tag.getFont());
			}
			else
			{
				//fontData.setName("����");
			}

			fontData.setHeight((int)(tag.getSize()/96f/(1f/72f)));
			fontData.setStyle((tag.isBold() ? SWT.BOLD : 0) | (tag.isItalic() ? SWT.ITALIC : 0));
		}

		graphics.setFont(new Font(Display.getCurrent(), fontData));
		graphics.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));

		// graphics.drawText(getLabel(),getBounds().x,getBounds().y);

		Font font = new Font(Display.getCurrent(), fontData);
		Color color = new Color(Display.getCurrent(), (rgb >>> 16) & 0xFF, (rgb >>> 8) & 0xFF, (rgb >>> 0) & 0xFF);

		TextLayout layout = new TextLayout(Display.getCurrent());
		layout.setWidth(getBounds().width - leftMargin - rightMargin);
		layout.setAlignment(alignment);
		layout.setIndent(indent);
		layout.setFont(font);
		layout.setText(getLabel());
		layout.setStyle(new TextStyle(font, color, null), 0, label.length());
		layout.setSpacing(leading);
		graphics.drawTextLayout(layout, getBounds().x + leftMargin, (int)(getBounds().y+(getBounds().height-fontData.getHeight()*(1f/72f)*96f)/2));

		color.dispose();
		font.dispose();
		layout.dispose();
	}

	/**
	 * ���������
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
	 * �ƶ�������
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
