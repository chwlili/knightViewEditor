package org.game.knight.ast2;

import java.util.Hashtable;

import org.antlr.v4.runtime.ParserRuleContext;
import org.chw.xml.XmlParser.ComplexNodeContext;
import org.chw.xml.XmlParser.SingleNodeContext;

public class UIViewListNode extends BaseTagNode
{
	/**
	 * 构造函数
	 * @param antlrNode
	 */
	public UIViewListNode(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}

	/**
	 * 初始化子级
	 */
	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		return createUINode(antlrNode);
	}
	
	
	public int size()
	{
		return getChildren().size();
	}
	
	public UIBase get(int index)
	{
		return (UIBase)getChildren().get(index);
	}
	
	public UIBase find(String id)
	{
		if(id!=null)
		{
			for(int i=size()-1;i>=0;i--)
			{
				if(id.equals(get(i).getAttribute("id")))
				{
					return get(i);
				}
			}
		}
		return null;
	}
	
	//---------------------------------------------------------------------
	
	/**
	 * 创建UI节点
	 * @param antlrNode
	 * @return
	 */
	public static BaseTagNode createUINode(ParserRuleContext antlrNode)
	{
		String tagName="";
		if(antlrNode instanceof SingleNodeContext)
		{
			tagName=((SingleNodeContext)antlrNode).tagName.getText();
		}
		else if(antlrNode instanceof ComplexNodeContext)
		{
			tagName=((ComplexNodeContext)antlrNode).tagName.getText();
		}
		
		if("window".equals(tagName))
		{
			return new UIWindow(antlrNode);
		}
		else if("confirm".equals(tagName))
		{
			return new UIConfirm(antlrNode);
		}
		else if("box".equals(tagName))
		{
			return new UIBase(antlrNode);
		}
		else if("image".equals(tagName))
		{
			return new UIImage(antlrNode);
		}
		else if("bitmap".equals(tagName))
		{
			return new UIBitmap(antlrNode);
		}
		else if("label".equals(tagName))
		{
			return new UILabel(antlrNode);
		}
		else if("button".equals(tagName))
		{
			return new UIButton(antlrNode);
		}
		else if("labelButton".equals(tagName))
		{
			return new UILabelButton(antlrNode);
		}
		else if("toggleButton".equals(tagName))
		{
			return new UIToggleButton(antlrNode);
		}
		else if("labelToggleButton".equals(tagName))
		{
			return new UILabelToggleButton(antlrNode);
		}
		
		return new UIBase(antlrNode);
	}

}
