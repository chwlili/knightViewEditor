package org.game.knight.editor.xml.design;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.game.knight.ast.ITagListener;
import org.game.knight.ast2.BaseTagNode;
import org.game.knight.editor.xml.ViewEditor;
import org.game.knight.editor.xml.ViewGefEditor;

public abstract class AbsPart extends AbstractGraphicalEditPart implements ITagListener
{
	private ViewEditor editor;
	
	@Override
	public void activate()
	{
		super.activate();
		((BaseTagNode)getModel()).addListener(this);
	}
	
	@Override
	public void deactivate()
	{
		super.deactivate();
		((BaseTagNode)getModel()).removeListener(this);
	}

	public void onTagChanged()
	{
		refreshVisuals();
	}
	
	public ViewEditor getEditor()
	{
		return editor;
	}
	
	public void setEditor(ViewEditor editor)
	{
		this.editor=editor;
	}
}
