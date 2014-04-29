package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.game.knight.ast.DefineTextTag;

public class DefineTextPart extends AbstractGraphicalEditPart
{
	private TextFlow text;

	public DefineTextTag getTag()
	{
		return (DefineTextTag) getModel();
	}

	@Override
	protected IFigure createFigure()
	{
		text = new TextFlow(getTag().getText());
		text.setLayoutManager(new ParagraphTextLayout(text, ParagraphTextLayout.WORD_WRAP_SOFT));
		return text;
	}

	@Override
	protected void createEditPolicies()
	{
	}
	
	@Override
	protected void refreshVisuals()
	{
		text.setText(getTag().getText());
		text.setBounds(text.getParent().getBounds());
	}

}
