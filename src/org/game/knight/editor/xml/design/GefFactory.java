package org.game.knight.editor.xml.design;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.game.knight.ast2.UIBase;
import org.game.knight.editor.xml.ViewEditor;
import org.game.knight.editor.xml.ViewGefEditor;

public class GefFactory implements EditPartFactory
{
	private ViewEditor editor;
	
	public GefFactory(ViewEditor editor)
	{
		this.editor=editor;
	}
	
	@Override
	public EditPart createEditPart(EditPart context, Object model)
	{
		EditPart part = null;

		if (model instanceof TagInput)
		{
			part = new DefineControlPart();
		}
		else if (model instanceof UIBase)
		{
			UIBase tag = (UIBase) model;
			String tagName = tag.getTagName();
			if (tagName.equals("image"))
			{
				part = new ImagePart();
			}
			else if (tagName.equals("bitmap"))
			{
				part = new BitmapPart();
			}
			else if (tagName.equals("label"))
			{
				part = new LabelPart();
			}
			else if (tagName.equals("box"))
			{
				part = new BoxPart();
			}
			else if(tagName.equals("window") || tagName.equals("confirm"))
			{
				part=new WindowPart();
			}
			else if (tagName.equals("button"))
			{
				part = new Button1Part();
			}
			else if(tagName.equals("labelButton"))
			{
				part=new Button2Part();
			}
			else if(tagName.equals("toggleButton"))
			{
				part=new Button3Part();
			}
			else if(tagName.equals("labelToggleButton"))
			{
				part=new Button4Part();
			}
			else
			{
				part = new ControlPart();
			}
		}

		if (part != null)
		{
			part.setModel(model);
			if(part instanceof AbsPart)
			{
				((AbsPart)part).setEditor(editor);
			}
		}

		return part;
	}

}
