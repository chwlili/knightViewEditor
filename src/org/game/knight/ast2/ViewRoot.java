package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class ViewRoot extends BaseTagNode
{
	private DependListNode dependList;
	private BitmapListNode bitmapList;
	private GridBitmapListNode gridBitmapList;
	private SwfListNode swfList;
	private FilterListNode filterList;
	private FormatListNode formatList;
	private ColorListNode colorList;
	private TextListNode textList;
	private UIViewListNode viewList;
	
	public ViewRoot(ViewDocument document,ParserRuleContext node)
	{
		super(node);
		
		setDocument(document);
	}

	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		if (isTagContext(antlrNode))
		{
			String tagName = getTagName(antlrNode);
			if ("depends".equals(tagName))
			{
				return new DependListNode(antlrNode);
			}
			else if ("bitmaps".equals(tagName))
			{
				return new BitmapListNode(antlrNode);
			}
			else if ("bitmapReaders".equals(tagName))
			{
				return new GridBitmapListNode(antlrNode);
			}
			else if ("swfs".equals(tagName))
			{
				return new SwfListNode(antlrNode);
			}
			else if ("filters".equals(tagName))
			{
				return new FilterListNode(antlrNode);
			}
			else if ("formats".equals(tagName))
			{
				return new FormatListNode(antlrNode);
			}
			else if ("colors".equals(tagName))
			{
				return new ColorListNode(antlrNode);
			}
			else if ("texts".equals(tagName))
			{
				return new TextListNode(antlrNode);
			}
			else if ("controls".equals(tagName))
			{
				return new UIViewListNode(antlrNode);
			}
		}

		return super.initChild(antlrNode);
	}
	
	public DependListNode getDependList()
	{
		return dependList;
	}
	
	public BitmapListNode getBitmapList()
	{
		return bitmapList;
	}

	public GridBitmapListNode getGridBitmapList()
	{
		return gridBitmapList;
	}
	
	public SwfListNode getSwfList()
	{
		return swfList;
	}
	
	public FilterListNode getFilterList()
	{
		return filterList;
	}
	
	public FormatListNode getFormatList()
	{
		return formatList;
	}
	
	public ColorListNode getColorList()
	{
		return colorList;
	}
	
	public UIViewListNode getViewList()
	{
		return viewList;
	}
	
	public TextListNode getTextList()
	{
		return textList;
	}
}
