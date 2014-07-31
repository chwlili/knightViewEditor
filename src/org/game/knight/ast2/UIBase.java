package org.game.knight.ast2;

import org.antlr.v4.runtime.ParserRuleContext;

public class UIBase extends BaseTagNode
{
	/**
	 * ���캯��
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
	 * ��ȡ����
	 * @return
	 */
	public String getName()
	{
		return getAttribute("name");
	}
	
	/**
	 * ��������
	 * @param value
	 */
	public void setName(String value)
	{
		setAttribute("name", value);
	}
	
	/**
	 * ��ȡX����
	 * @return
	 */
	public int getX()
	{
		return getAttributeByInt("x");
	}
	
	/**
	 * ����X����
	 * @param value
	 */
	public void setX(int value)
	{
		setAttributeByInt("x",value);
	}

	/**
	 * ��ȡY����
	 * @return
	 */
	public int getY()
	{
		return getAttributeByInt("y");
	}
	
	/**
	 * ����Y����
	 * @param value
	 */
	public void setY(int value)
	{
		setAttributeByInt("y",value);
	}

	/**
	 * ��ȡ���
	 * @return
	 */
	public int getWidth()
	{
		return getAttributeByInt("width");
	}
	
	/**
	 * ���ÿ��
	 * @param value
	 */
	public void setWidth(int value)
	{
		setAttributeByInt("width",value);
	}

	/**
	 * ��ȡ�߶�
	 * @return
	 */
	public int getHeight()
	{
		return getAttributeByInt("height");
	}
	
	/**
	 * ���ø߶�
	 * @param value
	 */
	public void setHeight(int value)
	{
		setAttributeByInt("height",value);
	}
	
	/**
	 * ��ȡleft
	 * @return
	 */
	public int getLeft()
	{
		return getAttributeByInt("left");
	}
	
	/**
	 * ����left
	 * @param value
	 */
	public void setLeft(int value)
	{
		setAttributeByInt("left",value);
	}
	
	/**
	 * ��ȡcenter
	 * @return
	 */
	public int getCenter()
	{
		return getAttributeByInt("center");
	}
	
	/**
	 * ����center
	 * @param value
	 */
	public void setCenter(int value)
	{
		setAttributeByInt("center",value);
	}
	
	/**
	 * ��ȡright
	 * @return
	 */
	public int getRight()
	{
		return getAttributeByInt("right");
	}
	
	/**
	 * ����right
	 * @param value
	 */
	public void setRight(int value)
	{
		setAttributeByInt("right",value);
	}
	
	/**
	 * ��ȡtop
	 * @return
	 */
	public int getTop()
	{
		return getAttributeByInt("top");
	}
	
	/**
	 * ����top
	 * @param value
	 */
	public void setTop(int value)
	{
		setAttributeByInt("top",value);
	}
	
	/**
	 * ��ȡmiddle
	 * @return
	 */
	public int getMiddle()
	{
		return getAttributeByInt("middle");
	}
	
	/**
	 * ����middle
	 * @param value
	 */
	public void setMiddle(int value)
	{
		setAttributeByInt("middle",value);
	}
	
	/**
	 * ��ȡbottom
	 * @return
	 */
	public int getBottom()
	{
		return getAttributeByInt("bottom");
	}
	
	/**
	 * ����bottom
	 * @param value
	 */
	public void setBottom(int value)
	{
		setAttributeByInt("bottom",value);
	}
	
	/**
	 * ��ȡ��ת��
	 * @return
	 */
	public int getRotation()
	{
		return getAttributeByInt("rotation");
	}
	
	/**
	 * ������ת��
	 * @return
	 */
	public void setRotation(int value)
	{
		setAttributeByInt("rotation",value);
	}
	
	/**
	 * ��ȡ͸����
	 * @return
	 */
	public float getAlpha()
	{
		return getAttributeByFloat("alpha");
	}
	
	/**
	 * ����͸����
	 * @param value
	 */
	public void setAlpha(float value)
	{
		setAttributeByFloat("alpha", value);
	}
	
	/**
	 * ��ȡ�ɼ���
	 * @return
	 */
	public boolean getVisible()
	{
		return "true".equals(getAttribute("visible"));
	}
	
	/**
	 * ���ÿɼ���
	 * @param value
	 */
	public void setVisible(boolean value)
	{
		setAttribute("visible", value ? "true":"false");
	}
	
	/**
	 * ��ȡ�����Ӧ��
	 * @return
	 */
	public boolean getMouseEnabled()
	{
		return "true".equals(getAttribute("mouseEnabled"));
	}
	
	/**
	 * ���������Ӧ��
	 * @return
	 */
	public void setMouseEnabled(boolean value)
	{
		setAttribute("mouseEnabled",value ? "true":"false");
	}
	
	/**
	 * ��ȡ��ťģʽ
	 * @return
	 */
	public boolean getButtonMode()
	{
		return "true".equals(getAttribute("buttonMode"));
	}
	
	/**
	 * ���ð�ťģʽ
	 * @param value
	 */
	public void setButtonMode(boolean value)
	{
		setAttribute("buttonMode", value ? "true":"false");
	}
}
