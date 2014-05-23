package org.game.knight.editor.xml.design;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.DeviceResourceDescriptor;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.game.knight.ast.DefineControlTag;
import org.game.knight.ast.DefineFormatTag;
import org.game.knight.editor.xml.design.figure.ButtonFigure;
import org.game.knight.editor.xml.design.figure.SliceImage;
import org.game.knight.editor.xml.design.figure.SliceImageDescriptor;

public class Button1Part extends ControlPart
{
	private ButtonFigure view;
	private ArrayList<DeviceResourceDescriptor> resourceList=new ArrayList<DeviceResourceDescriptor>();

	@Override
	public void removeNotify()
	{
		while(resourceList.size()>0)
		{
			getViewer().getResourceManager().destroy(resourceList.remove(0));
		}
		
		super.removeNotify();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected List getModelChildren()
	{
		return new ArrayList<Object>();
	}

	@Override
	protected IFigure createFigure()
	{
		view = new ButtonFigure();
		
		return view;
	}

	@Override
	protected void refreshVisuals()
	{
		super.refreshVisuals();
		
		ArrayList<DeviceResourceDescriptor> old=resourceList;
		
		resourceList=new ArrayList<DeviceResourceDescriptor>();
		
		view.removeAllResource();
		view.setLabel(getTagHelper().findTextByAttribute("label"));

		for (Object child : getTag().getChildren())
		{
			if (child instanceof DefineControlTag)
			{
				DefineControlTag tag = (DefineControlTag) child;
				if (tag.getName().equals("state"))
				{
					TagHelper helper = new TagHelper(tag);

					String frameID = helper.getStringValue("frame");
					if (frameID != null)
					{
						IFile file=helper.findFileByAttribute("back");
						if(file!=null)
						{
							SliceImageDescriptor imageDesc=new SliceImageDescriptor(file, false, 0, 0, 0, 0);
							
							view.addImage(frameID, (SliceImage) getViewer().getResourceManager().create(imageDesc));
							
							resourceList.add(imageDesc);
						}
						
						DefineFormatTag formatTag = helper.findFontByAttribute("format");
						if(formatTag!=null)
						{
							FontData font = new FontData();
							RGB rgb = new RGB(0, 0, 0);
							if (tag != null)
							{
								if (formatTag.getFont() != null && formatTag.getFont().isEmpty() == false)
								{
									font.setName(formatTag.getFont());
								}
								font.setHeight(TagHelper.pxToPoint(formatTag.getSize()));
								font.setStyle((formatTag.isBold() ? SWT.BOLD : 0) | (formatTag.isItalic() ? SWT.ITALIC : 0));
	
								rgb = TagHelper.colorToRGB(formatTag.getColor());
							}
	
							FontDescriptor fontDesc=FontDescriptor.createFrom(font);
							ColorDescriptor colorDesc=ColorDescriptor.createFrom(rgb);
							
							view.addFormat(frameID, getViewer().getResourceManager().createFont(fontDesc));
							view.addColor(frameID, getViewer().getResourceManager().createColor(colorDesc));
							
							resourceList.add(fontDesc);
							resourceList.add(colorDesc);
						}
					}
				}
			}
		}
		
		while(old.size()>0)
		{
			getViewer().getResourceManager().destroy(old.remove(0));
		}
	}
}
