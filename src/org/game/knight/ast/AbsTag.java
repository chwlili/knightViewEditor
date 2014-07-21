package org.game.knight.ast;

import java.util.ArrayList;
import java.util.Hashtable;

import org.game.knight.ast.AST.Token;

public class AbsTag
{
	private static int id = 1;

	public static final int Depend = id++;
	public static final int Bitmap = id++;
	public static final int BitmapRenderer = id++;
	public static final int Swf = id++;
	public static final int Filter = id++;
	public static final int Format = id++;
	public static final int Text = id++;
	public static final int Control = id++;

	public static final int DependList = id++;
	public static final int BitmapList = id++;
	public static final int BitmapRendererList = id++;
	public static final int SwfList = id++;
	public static final int FilterList = id++;
	public static final int FormatList = id++;
	public static final int TextList = id++;
	public static final int ControlList = id++;

	private AST ast;
	private boolean complex;
	private int type;
	private Token start;
	private Token stop;

	private Token name;
	private ArrayList<Attribute> attributes;
	private ArrayList<Object> children;
	
	private Hashtable<String, Attribute> attributeTable;
	
	private ArrayList<ITagListener> listeners;
	
	/**
	 * 构造函数
	 * 
	 * @param start
	 * @param stop
	 */
	public AbsTag(AST ast, boolean complex, Token start, Token stop, Token name, ArrayList<Attribute> attributes, ArrayList<Object> children)
	{
		this.ast = ast;
		this.complex=complex;
		this.type = type;
		this.start = start;
		this.stop = stop;

		this.name = name;
		this.attributes = attributes;
		this.children = children;
		
		this.attributeTable = new Hashtable<String, Attribute>();
		if (attributes != null)
		{
			for (Attribute attribute : attributes)
			{
				attributeTable.put(attribute.getNameToken().text, attribute);
			}
		}
	}
	
	public void addListener(ITagListener listener)
	{
		if(listeners==null)
		{
			listeners=new ArrayList<ITagListener>();
		}
		listeners.remove(listener);
		listeners.add(listener);
	}
	
	public void removeListener(ITagListener listener)
	{
		if(listeners!=null)
		{
			listeners.remove(listener);
		}
	}
	
	public void fireTagChanged()
	{
		if(listeners!=null)
		{
			for(ITagListener listener:listeners)
			{
				listener.onTagChanged();
			}
		}
	}
	
	public AST getAST()
	{
		return ast;
	}
	
	/**
	 * 开始位置
	 * 
	 * @return
	 */
	public int getOffset()
	{
		return start.start;
	}

	/**
	 * 内容长度
	 * 
	 * @return
	 */
	public int getLength()
	{
		return stop.stop - start.start + 1;
	}

	/**
	 * 获取标记名称
	 * 
	 * @return
	 */
	public String getName()
	{
		return name.text;
	}

	/**
	 * 获取属性列表
	 * 
	 * @return
	 */
	public ArrayList<Attribute> getAttributes()
	{
		return attributes;
	}

	/**
	 * 获取子级列表
	 * 
	 * @return
	 */
	public ArrayList<Object> getChildren()
	{
		return children;
	}

	/**
	 * 是否是指定的属性
	 * 
	 * @param name
	 * @return
	 */
	public boolean hasAttribute(String name)
	{
		return attributeTable.containsKey(name);
	}

	/**
	 * 获取属性
	 * 
	 * @param name
	 * @return
	 */
	public Attribute getAttribute(String name)
	{
		if (attributeTable.containsKey(name))
		{
			return attributeTable.get(name);
		}
		return null;
	}

	/**
	 * 获取属性值
	 * 
	 * @param name
	 * @return
	 */
	public String getAttributeValue(String name)
	{
		if (attributeTable.containsKey(name))
		{
			return attributeTable.get(name).getValue();
		}
		return null;
	}

	/**
	 * 是否为列表
	 * 
	 * @return
	 */
	public boolean isList()
	{
		return DependList <= type && type <= ControlList;
	}

	public boolean isItem()
	{
		return Depend <= type && type <= Control;
	}

	public boolean isDepend()
	{
		return Depend == type;
	}

	public boolean isBitmap()
	{
		return Bitmap == type;
	}

	public boolean isBitmapRenderer()
	{
		return BitmapRenderer == type;
	}

	public boolean isSwf()
	{
		return Swf == type;
	}

	public boolean isFilter()
	{
		return Filter == type;
	}

	public boolean isFormat()
	{
		return Format == type;
	}

	public boolean isText()
	{
		return Text == type;
	}

	public boolean isControl()
	{
		return Control == type;
	}
}
