package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.game.knight.ast.DefineFormatTag;
import org.game.knight.ast2.FormatNode;
import org.game.knight.ast2.UILabel;
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
	
	@Override
	protected void createEditPolicies()
	{
		//super.createEditPolicies();
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

		UILabel data=(UILabel)getModel();
		
		FormatNode tag=data.getFormat();

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
		this.view.setText(data.getText(), data.isHTML());
		
		this.rgb = rgb;
		this.font = font;
	}
}
