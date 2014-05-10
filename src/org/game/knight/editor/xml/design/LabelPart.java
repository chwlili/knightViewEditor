package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.game.knight.ast.DefineFormatTag;
import org.game.knight.editor.xml.design.figure.LabelFigure;

public class LabelPart extends ControlPart
{
	private LabelFigure view = null;

	@Override
	protected IFigure createFigure()
	{
		view = new LabelFigure();
		return view;
	}

	@Override
	protected void refreshVisuals()
	{
		super.refreshVisuals();
		
		DefineFormatTag tag=getTagHelper().findFontByAttribute("format");
		
		FontData fontData=new FontData();
		RGB rgb=new RGB(0,0,0);
		if(tag!=null)
		{
			if(tag.getFont()!=null && tag.getFont().isEmpty()==false)
			{
				fontData.setName(tag.getFont());
			}
			
			fontData.setHeight(TagHelper.pxToPoint(tag.getSize()));
			fontData.setStyle((tag.isBold() ? SWT.BOLD:0)|(tag.isItalic() ? SWT.ITALIC:0));
			rgb=TagHelper.colorToRGB(tag.getColor());
		}
		
		view.setForegroundColor(new Color(Display.getCurrent(),rgb));
		view.setFont(new Font(Display.getCurrent(), fontData));
		view.setText(getTagHelper().findTextByAttribute("text"));
	}
}
