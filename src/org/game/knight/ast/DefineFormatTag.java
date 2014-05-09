package org.game.knight.ast;

import java.util.ArrayList;

import org.game.knight.ast.AST.Token;

public class DefineFormatTag extends DefineTag
{

	public DefineFormatTag(AST ast, boolean complex, Token start, Token stop, Token name, ArrayList<Attribute> attributes, ArrayList<Object> children)
	{
		super(ast, complex, start, stop, name, attributes, children);
	}

	public String getFont()
	{
		if(hasAttribute("font"))
		{
			return getAttributeValue("font");
		}
		return null;
	}
	
	public int getSize()
	{
		if(hasAttribute("size"))
		{
			try
			{
				return Integer.parseInt(getAttributeValue("size"));
			}
			catch(Exception exception)
			{
			}
		}
		return 12;
	}
	
	public int getColor()
	{
		if(hasAttribute("color"))
		{
			try
			{
				String color_txt=getAttributeValue("color");
				if(color_txt.indexOf("0x")==0)
				{
					color_txt=color_txt.substring(2);
				}
				return Integer.parseInt(color_txt,16);
			}
			catch(Exception exception)
			{
			}
		}
		return 0;
	}
	
	public boolean isBold()
	{
		if(hasAttribute("bold"))
		{
			return "true".equals(getAttributeValue("bold"));
		}
		return false;
	}
	
	public boolean isItalic()
	{
		if(hasAttribute("italic"))
		{
			return "true".equals(getAttributeValue("italic"));
		}
		return false;
	}
	
	public boolean isUnderline()
	{
		if(hasAttribute("underline"))
		{
			return "true".equals(getAttributeValue("underline"));
		}
		return false;
	}
	
	public String getAlign()
	{
		if(hasAttribute("align"))
		{
			String value=getAttributeValue("align");
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
		if(hasAttribute("indent"))
		{
			try
			{
				return Integer.parseInt(getAttributeValue("indent"));
			}
			catch(Exception exception)
			{
			}
		}
		return 0;
	}

	public int getLeftMargin()
	{
		if(hasAttribute("leftMargin"))
		{
			try
			{
				return Integer.parseInt(getAttributeValue("leftMargin"));
			}
			catch(Exception exception)
			{
			}
		}
		return 0;
	}

	public int getRightMargin()
	{
		if(hasAttribute("rightMargin"))
		{
			try
			{
				return Integer.parseInt(getAttributeValue("rightMargin"));
			}
			catch(Exception exception)
			{
			}
		}
		return 0;
	}
	
	public int getLeading()
	{
		if(hasAttribute("leading"))
		{
			try
			{
				return Integer.parseInt(getAttributeValue("leading"));
			}
			catch(Exception exception)
			{
			}
		}
		return 0;
	}
}
