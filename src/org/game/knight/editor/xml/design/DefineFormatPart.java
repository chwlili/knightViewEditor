package org.game.knight.editor.xml.design;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.game.knight.ast.DefineFormatTag;
import org.game.knight.ast.DefineTextTag;
import org.game.knight.editor.xml.design.DefineTextPart.Text;

public class DefineFormatPart extends AbstractGraphicalEditPart
{
	private Text text;

	public DefineFormatTag getTag()
	{
		return (DefineFormatTag) getModel();
	}

	@Override
	protected IFigure createFigure()
	{
		text = new Text("111");
		return text;
	}

	@Override
	protected void createEditPolicies()
	{
	}

	@Override
	protected void refreshVisuals()
	{
		text.setText("xxxxxxxxxxxxxxxxxxxxxxxxx");
		text.setBounds(text.getParent().getBounds());
	}

	protected class Text extends FlowPage
	{
		private TextFlow contents;

		public Text()
		{
			this("");
		}

		public Text(String text)
		{
			contents = new TextFlow();
			contents.setLayoutManager(new ParagraphTextLayout(contents, ParagraphTextLayout.WORD_WRAP_SOFT));
			contents.setText(text);
			add(contents);
		}

		public void setText(String text)
		{
			contents.setText(text);
		}

		public String getText()
		{
			return contents.getText();
		}
	}
}
