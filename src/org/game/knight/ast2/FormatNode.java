package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class FormatNode extends BaseTagNode
{

	public FormatNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	public String getID()
	{
		return getAttribute("id");
	}
	
	public String getFont()
	{
		return getAttribute("font");
	}
	
	public int getSize()
	{
		if(hasAttribute("size"))
		{
			return getAttributeByInt("size");
		}
		return 12;
	}
	
	public int getColor()
	{
		if(hasAttribute("color"))
		{
			String txt=getAttribute("color");
			if(txt.startsWith("0x"))
			{
				txt=txt.substring(2);
			}
			
			try
			{
				return Integer.parseInt(txt,16);
			}
			catch(Exception exception)
			{
			}
		}
		return 0;
	}
	
	public boolean isBold()
	{
		return "true".equals(getAttribute("bold"));
	}
	
	public boolean isItalic()
	{
		return "true".equals(getAttribute("italic"));
	}
	
	public boolean isUnderline()
	{
		return "true".equals(getAttribute("underline"));
	}
	
	public String getAlign()
	{
		if(hasAttribute("align"))
		{
			String value=getAttribute("align");
			if("left".equals(value))
			{
				return "left";
			}
			else if("right".equals(value))
			{
				return "right";
			}
			else if("center".equals(value))
			{
				return "center";
			}
		}
		return "left";
	}
	
	public int getIndent()
	{
		return getAttributeByInt("indent");
	}
	
	public int getLeftMargin()
	{
		return getAttributeByInt("leftMargin");
	}
	
	public int getRightMargin()
	{
		return getAttributeByInt("rightMargin");
	}
}
