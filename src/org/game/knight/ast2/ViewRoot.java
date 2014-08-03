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
		initChildren();
		initAttributes();
	}

	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		String tagName = getTagName(antlrNode);
		if ("depends".equals(tagName))
		{
			dependList=new DependListNode(antlrNode);
			return dependList;
		}
		else if ("bitmaps".equals(tagName))
		{
			bitmapList=new BitmapListNode(antlrNode);
			return bitmapList;
		}
		else if ("bitmapReaders".equals(tagName))
		{
			gridBitmapList=new GridBitmapListNode(antlrNode);
			return gridBitmapList;
		}
		else if ("swfs".equals(tagName))
		{
			swfList= new SwfListNode(antlrNode);
			return swfList;
		}
		else if ("filters".equals(tagName))
		{
			filterList= new FilterListNode(antlrNode);
			return filterList;
		}
		else if ("formats".equals(tagName))
		{
			formatList= new FormatListNode(antlrNode);
			return formatList;
		}
		else if ("colors".equals(tagName))
		{
			colorList= new ColorListNode(antlrNode);
			return colorList;
		}
		else if ("texts".equals(tagName))
		{
			textList=new TextListNode(antlrNode);
			return textList;
		}
		else if ("controls".equals(tagName))
		{
			viewList=new UIViewListNode(antlrNode);
			return viewList;
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
	
	public TextListNode getTextList()
	{
		return textList;
	}
	
	public UIViewListNode getViewList()
	{
		return viewList;
	}

	public String getText()
	{
		return antlrNode.getText();
	}
}
