package org.game.knight.editor.xml.design;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.game.knight.ast.AST;
import org.game.knight.ast.ASTManager;
import org.game.knight.ast.AbsTag;
import org.game.knight.ast.Attribute;
import org.game.knight.ast.DefineControlTag;
import org.game.knight.ast.DefineFormatTag;
import org.game.knight.ast.DefineTextTag;
import org.game.knight.ast.FileRef;
import org.game.knight.ast.IdDef;
import org.game.knight.ast.IdRef;

public class TagHelper
{
	private DefineControlTag tag;
	
	/**
	 * ���캯��
	 * @param tag
	 */
	public TagHelper(DefineControlTag tag)
	{
		this.tag=tag;
	}
	
	/**
	 * ��ȡintֵ
	 * 
	 * @param name
	 * @param defaultVal
	 * @return
	 */
	public int getIntValue(String name)
	{
		if (tag.hasAttribute(name))
		{
			try
			{
				return Integer.parseInt(tag.getAttributeValue(name));
			}
			catch (Exception exception)
			{
			}
		}
		return getAttributeIntDefaultValue(name);
	}

	/**
	 * ��ȡint���Ե�Ĭ��ֵ
	 * 
	 * @param name
	 * @return
	 */
	private int getAttributeIntDefaultValue(String name)
	{
		return 0;
	}

	/**
	 * ��ȡ����ֵ
	 * 
	 * @param name
	 * @return
	 */
	public double getDoubleValue(String name)
	{
		if (tag.hasAttribute(name))
		{
			try
			{
				return Double.parseDouble(tag.getAttributeValue(name));
			}
			catch (Exception exception)
			{
			}
		}
		return getAttributeDoubleDefaultValue(name);
	}

	/**
	 * ��ȡ�������Ե�Ĭ��ֵ
	 * 
	 * @param name
	 * @return
	 */
	private double getAttributeDoubleDefaultValue(String name)
	{
		return Double.NaN;
	}

	/**
	 * ��ȡ�ַ���ֵ
	 * 
	 * @param name
	 * @return
	 */
	public String getStringValue(String name)
	{
		if (tag.hasAttribute(name))
		{
			return tag.getAttributeValue(name);
		}
		return null;
	}

	/**
	 * �����Բ��������ļ�
	 * @param name
	 * @return
	 */
	public IFile findFileByAttribute(String name)
	{
		Attribute attribute = tag.getAttribute(name);
		if (attribute != null)
		{
			IdRef ref = tag.getAST().getLinks().getIdRef(attribute.getValueToken().start);
			if (ref != null)
			{
				IdDef def = ref.getTarget();
				if (def != null && def.getRef() instanceof IdRef)
				{
					def = ((IdRef) def.getRef()).getTarget();
				}

				if (def != null)
				{
					FileRef fileRef = (FileRef) def.getRef();
					return ASTManager.getSourceFile(fileRef.getFile().getProject(), fileRef.getTargetURL());
				}
			}
		}
		return null;
	}

	/**
	 * �����Բ����ı�����
	 * @param name
	 * @return
	 */
	public String findTextByAttribute(String name)
	{
		Attribute attribute = tag.getAttribute(name);
		if (attribute != null)
		{
			IdRef ref = tag.getAST().getLinks().getIdRef(attribute.getValueToken().start);
			if (ref != null)
			{
				IdDef def = ref.getTarget();
				if (def != null)
				{
					try
					{
						AST ast = ASTManager.getAST(def.getFile());
						AbsTag tag = ast.getTagBy(def.getOffset());
						if(tag instanceof DefineTextTag)
						{
							DefineTextTag txt=(DefineTextTag)tag;
							return txt.getText();
						}
					}
					catch (CoreException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		return "";
	}
	
	/**
	 * �����Բ�����������
	 * @param name
	 * @return
	 */
	public DefineFormatTag findFontByAttribute(String name)
	{
		Attribute attribute = tag.getAttribute(name);
		if (attribute != null)
		{
			IdRef ref = tag.getAST().getLinks().getIdRef(attribute.getValueToken().start);
			if (ref != null)
			{
				IdDef def = ref.getTarget();
				if (def != null)
				{
					try
					{
						AST ast = ASTManager.getAST(def.getFile());
						AbsTag tag = ast.getTagBy(def.getOffset());
						if(tag instanceof DefineFormatTag)
						{
							return (DefineFormatTag)tag;
						}
					}
					catch (CoreException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	//----------------------------------------------------------------------
	//
	// ������λ����
	//
	//----------------------------------------------------------------------
	
	/**
	 * X����
	 * @return
	 */
	public int getX()
	{
		return getIntValue("x");
	}

	/**
	 * Y����
	 * @return
	 */
	public int getY()
	{
		return getIntValue("y");
	}

	/**
	 * ���
	 * @return
	 */
	public int getW()
	{
		return getIntValue("width");
	}
	
	/**
	 * �߶�
	 * @return
	 */
	public int getH()
	{
		return getIntValue("height");
	}

	//----------------------------------------------------------------------
	//
	// ��չ��λ����
	//
	//----------------------------------------------------------------------

	/**
	 * �Ƿ���left
	 * @return
	 */
	public boolean hasLeft()
	{
		return !Double.isNaN(getLeft());
	}

	/**
	 * left
	 * @return
	 */
	public double getLeft()
	{
		return getDoubleValue("left");
	}

	/**
	 * �Ƿ���right
	 * @return
	 */
	public boolean hasRight()
	{
		return !Double.isNaN(getRight());
	}

	/**
	 * right
	 * @return
	 */
	public double getRight()
	{
		return getDoubleValue("right");
	}

	/**
	 * �Ƿ���top 
	 * @return
	 */
	public boolean hasTop()
	{
		return !Double.isNaN(getTop());
	}

	/**
	 * top
	 * @return
	 */
	public double getTop()
	{
		return getDoubleValue("top");
	}

	/**
	 * �Ƿ���bottom
	 * @return
	 */
	public boolean hasBottom()
	{
		return !Double.isNaN(getBottom());
	}

	/**
	 * bottom
	 * @return
	 */
	public double getBottom()
	{
		return getDoubleValue("bottom");
	}

	/**
	 * �Ƿ���center
	 * @return
	 */
	public boolean hasCenter()
	{
		return !Double.isNaN(getCenter());
	}

	/**
	 * center
	 * @return
	 */
	public double getCenter()
	{
		return getDoubleValue("center");
	}

	/**
	 * �Ƿ���middle
	 * @return
	 */
	public boolean hasMiddle()
	{
		return !Double.isNaN(getMiddle());
	}

	/**
	 * middle
	 * @return
	 */
	public double getMiddle()
	{
		return getDoubleValue("middle");
	}
}
