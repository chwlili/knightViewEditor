package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class UIBase extends BaseTagNode
{
	/**
	 * 构造函数
	 * @param antlrNode
	 */
	public UIBase(ParserRuleContext antlrNode)
	{
		super(antlrNode);
	}
	
	@Override
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		return UIViewListNode.createUINode(antlrNode);
	}
	
	/**
	 * 获取名称
	 * @return
	 */
	public String getName()
	{
		return getAttribute("name");
	}
	
	/**
	 * 设置名称
	 * @param value
	 */
	public void setName(String value)
	{
		setAttribute("name", value);
	}
	
	/**
	 * 获取X坐标
	 * @return
	 */
	public int getX()
	{
		return getAttributeByInt("x");
	}
	
	/**
	 * 设置X坐标
	 * @param value
	 */
	public void setX(int value)
	{
		setAttributeByInt("x",value);
	}

	/**
	 * 获取Y坐标
	 * @return
	 */
	public int getY()
	{
		return getAttributeByInt("y");
	}
	
	/**
	 * 设置Y坐标
	 * @param value
	 */
	public void setY(int value)
	{
		setAttributeByInt("y",value);
	}

	/**
	 * 获取宽度
	 * @return
	 */
	public int getWidth()
	{
		return getAttributeByInt("width");
	}
	
	/**
	 * 设置宽度
	 * @param value
	 */
	public void setWidth(int value)
	{
		setAttributeByInt("width",value);
	}

	/**
	 * 获取高度
	 * @return
	 */
	public int getHeight()
	{
		return getAttributeByInt("height");
	}
	
	/**
	 * 设置高度
	 * @param value
	 */
	public void setHeight(int value)
	{
		setAttributeByInt("height",value);
	}
	
	/**
	 * 获取left
	 * @return
	 */
	public int getLeft()
	{
		return getAttributeByInt("left");
	}
	
	/**
	 * 设置left
	 * @param value
	 */
	public void setLeft(int value)
	{
		setAttributeByInt("left",value);
	}
	
	/**
	 * 获取center
	 * @return
	 */
	public int getCenter()
	{
		return getAttributeByInt("center");
	}
	
	/**
	 * 设置center
	 * @param value
	 */
	public void setCenter(int value)
	{
		setAttributeByInt("center",value);
	}
	
	/**
	 * 获取right
	 * @return
	 */
	public int getRight()
	{
		return getAttributeByInt("right");
	}
	
	/**
	 * 设置right
	 * @param value
	 */
	public void setRight(int value)
	{
		setAttributeByInt("right",value);
	}
	
	/**
	 * 获取top
	 * @return
	 */
	public int getTop()
	{
		return getAttributeByInt("top");
	}
	
	/**
	 * 设置top
	 * @param value
	 */
	public void setTop(int value)
	{
		setAttributeByInt("top",value);
	}
	
	/**
	 * 获取middle
	 * @return
	 */
	public int getMiddle()
	{
		return getAttributeByInt("middle");
	}
	
	/**
	 * 设置middle
	 * @param value
	 */
	public void setMiddle(int value)
	{
		setAttributeByInt("middle",value);
	}
	
	/**
	 * 获取bottom
	 * @return
	 */
	public int getBottom()
	{
		return getAttributeByInt("bottom");
	}
	
	/**
	 * 设置bottom
	 * @param value
	 */
	public void setBottom(int value)
	{
		setAttributeByInt("bottom",value);
	}
	
	/**
	 * 获取旋转度
	 * @return
	 */
	public int getRotation()
	{
		return getAttributeByInt("rotation");
	}
	
	/**
	 * 设置旋转度
	 * @return
	 */
	public void setRotation(int value)
	{
		setAttributeByInt("rotation",value);
	}
	
	/**
	 * 获取透明度
	 * @return
	 */
	public float getAlpha()
	{
		return getAttributeByFloat("alpha");
	}
	
	/**
	 * 设置透明度
	 * @param value
	 */
	public void setAlpha(float value)
	{
		setAttributeByFloat("alpha", value);
	}
	
	/**
	 * 获取可见性
	 * @return
	 */
	public boolean getVisible()
	{
		return "true".equals(getAttribute("visible"));
	}
	
	/**
	 * 设置可见性
	 * @param value
	 */
	public void setVisible(boolean value)
	{
		setAttribute("visible", value ? "true":"false");
	}
	
	/**
	 * 获取鼠标响应性
	 * @return
	 */
	public boolean getMouseEnabled()
	{
		return "true".equals(getAttribute("mouseEnabled"));
	}
	
	/**
	 * 设置鼠标响应性
	 * @return
	 */
	public void setMouseEnabled(boolean value)
	{
		setAttribute("mouseEnabled",value ? "true":"false");
	}
	
	/**
	 * 获取按钮模式
	 * @return
	 */
	public boolean getButtonMode()
	{
		return "true".equals(getAttribute("buttonMode"));
	}
	
	/**
	 * 设置按钮模式
	 * @param value
	 */
	public void setButtonMode(boolean value)
	{
		setAttribute("buttonMode", value ? "true":"false");
	}
}
