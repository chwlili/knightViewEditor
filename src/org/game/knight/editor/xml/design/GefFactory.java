package org.game.knight.editor.xml.design;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.game.knight.ast.DefineControlTag;
import org.game.knight.ast.DefineGridImgTag;
import org.game.knight.ast.DefineImgTag;
import org.game.knight.ast.DefineTextTag;

public class GefFactory implements EditPartFactory
{
	@Override
	public EditPart createEditPart(EditPart context, Object model)
	{
		EditPart part = null;

		if (model instanceof TagInput)
		{
			TagInput box = (TagInput) model;
			if (box.tag instanceof DefineControlTag)
			{
				part = new DefineControlPart();
			}
			else
			{
				model = box.tag;
			}
		}

		if (model instanceof DefineImgTag)
		{
			part = new DefineImgPart();
		}
		else if (model instanceof DefineTextTag)
		{
			part = new DefineTextPart();
		}
		else if (model instanceof DefineGridImgTag)
		{
			part = new DefineGridImgPart();
		}
		else if (model instanceof DefineControlTag)
		{
			DefineControlTag tag = (DefineControlTag) model;
			String tagName = tag.getName();
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
			else if (tagName.equals("box") || tagName.equals("window") || tagName.equals("confirm"))
			{
				part = new BoxPart();
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
			else
			{
				part = new ControlPart();
			}
		}

		if (part != null)
		{
			part.setModel(model);
		}

		return part;
	}

}
