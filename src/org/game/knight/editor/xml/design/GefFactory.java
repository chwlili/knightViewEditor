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
		
		if(model instanceof DefineControlTagBox)
		{
			DefineControlTagBox box=(DefineControlTagBox)model;
			if(box.tag instanceof DefineControlTag)
			{
				part=new DefineControlPart();
			}
			else
			{
				model=box.tag;
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
		else if(model instanceof DefineControlTag)
		{
			part=new ControlPart();
		}

		if (part != null)
		{
			part.setModel(model);
		}

		return part;
	}

}
