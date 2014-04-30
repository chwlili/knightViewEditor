package org.game.knight.editor.xml.design;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.game.knight.ast.DefineImgTag;

public class GefPartFactory implements EditPartFactory
{
	@Override
	public EditPart createEditPart(EditPart context, Object model)
	{
		EditPart part=null;
		
		if(model instanceof DefineImgTag)
		{
			part=new DefineImgPart();
		}
		else if(model instanceof DefineTextPart)
		{
			part=new DefineTextPart();
		}
		
		if(part!=null)
		{
			part.setModel(model);
		}
		
		return part;
	}

}
