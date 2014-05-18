package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.game.knight.ast.DefineFormatTag;
import org.game.knight.editor.xml.design.figure.LabelFigure;

public class LabelPart extends ControlPart
{
	private LabelFigure view = null;

	private FontDescriptor fontDescriptor = null;
	private ColorDescriptor colorDescriptor = null;

	private FontData font = null;
	private RGB rgb = null;

	@Override
	public void removeNotify()
	{
		releaseFont();
		releaseColor();

		super.removeNotify();
	}

	private void releaseFont()
	{
		if (fontDescriptor != null)
		{
			getViewer().getResourceManager().destroyFont(fontDescriptor);
			fontDescriptor = null;
		}
		view.setFont(null);

		font = null;
	}

	private void releaseColor()
	{
		if (colorDescriptor != null)
		{
			getViewer().getResourceManager().destroyColor(colorDescriptor);
			colorDescriptor = null;
		}
		view.setForegroundColor(null);

		rgb = null;
	}

	@Override
	protected IFigure createFigure()
	{
		view = new LabelFigure(getViewer().getResourceManager());
		return view;
	}

	@Override
	protected void refreshVisuals()
	{
		super.refreshVisuals();

		DefineFormatTag tag = getTagHelper().findFontByAttribute("format");

		FontData font = new FontData();
		RGB rgb = new RGB(0, 0, 0);
		if (tag != null)
		{
			if (tag.getFont() != null && tag.getFont().isEmpty() == false)
			{
				font.setName(tag.getFont());
			}
			font.setHeight(TagHelper.pxToPoint(tag.getSize()));
			font.setStyle((tag.isBold() ? SWT.BOLD : 0) | (tag.isItalic() ? SWT.ITALIC : 0));

			rgb = TagHelper.colorToRGB(tag.getColor());
			
			this.view.setHorizontalAligment("center".equals(tag.getAlign()) ? PositionConstants.CENTER : ("right".equals(tag.getAlign()) ? PositionConstants.RIGHT : PositionConstants.LEFT));
		}

		if (!font.equals(this.font))
		{
			releaseFont();
			this.fontDescriptor = FontDescriptor.createFrom(font);
		}
		if (!rgb.equals(this.rgb))
		{
			releaseColor();
			this.colorDescriptor = ColorDescriptor.createFrom(rgb);
		}

		this.view.setForegroundColor(getViewer().getResourceManager().createColor(colorDescriptor));
		this.view.setFont(getViewer().getResourceManager().createFont(fontDescriptor));
		this.view.setText(getTagHelper().findTextByAttribute("text"), "true".equals(getTagHelper().getStringValue("html")));
		
		this.rgb = rgb;
		this.font = font;
	}
}
