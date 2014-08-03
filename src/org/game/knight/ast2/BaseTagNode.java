package org.game.knight.ast2;

import java.util.ArrayList;
import java.util.Hashtable;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.chw.xml.XmlLexer;
import org.chw.xml.XmlParser;
import org.chw.xml.XmlParser.AttributeContext;
import org.chw.xml.XmlParser.ComplexNodeContext;
import org.chw.xml.XmlParser.SingleNodeContext;
import org.game.knight.ast.ITagListener;

public class BaseTagNode
{
	protected ViewDocument dom;
	protected ParseTree antlrNode;
	private ArrayList<BaseTagNode> children;
	protected ArrayList<AttributeContext> attributeList;
	protected Hashtable<String, AttributeContext> attributeHash;
	
	private ArrayList<ITagListener> listeners;

	/**
	 * 构造函数
	 * 
	 * @param node
	 */
	public BaseTagNode(ParserRuleContext antlrNode)
	{
		this.antlrNode = antlrNode;
	}
	
	protected ArrayList<BaseTagNode> getChildren()
	{
		if(children==null)
		{
			initChildren();
		}
		return children;
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
	
	/**
	 * 获取文档
	 * @return
	 */
	public ViewDocument getDocument()
	{
		return dom;
	}
	
	/**
	 * 设置文档
	 * @param dom
	 */
	public void setDocument(ViewDocument dom)
	{
		this.dom=dom;
	}
	
	/**
	 * 获取标签名称
	 * @return
	 */
	public String getTagName()
	{
		if(antlrNode instanceof SingleNodeContext)
		{
			return ((SingleNodeContext)antlrNode).tagName.getText();
		}
		else if(antlrNode instanceof ComplexNodeContext)
		{
			return ((ComplexNodeContext)antlrNode).tagName.getText();
		}
		return "";
	}
	
	/**
	 * 获取子级个数
	 * @return
	 */
	public int getChildCount()
	{
		return getChildren().size();
	}
	
	/**
	 * 按索引获取子级
	 * @param index
	 * @return
	 */
	public BaseTagNode getChildAt(int index)
	{
		return getChildren().get(index);
	}

	/**
	 * 开始索引
	 * 
	 * @return
	 */
	public int getStartIndex()
	{
		return ((ParserRuleContext) antlrNode).start.getStartIndex();
	}

	/**
	 * 结束索引
	 * 
	 * @return
	 */
	public int getStopIndex()
	{
		return ((ParserRuleContext) antlrNode).stop.getStopIndex();
	}
	
	/**
	 * 是否为标记上下文
	 * @param tree
	 * @return
	 */
	protected boolean isTagContext(ParseTree antlrNode)
	{
		return (antlrNode instanceof SingleNodeContext)||(antlrNode instanceof ComplexNodeContext);
	}
	
	/**
	 * 获取标记名称
	 * @param tree
	 * @return
	 */
	protected String getTagName(ParseTree antlrNode)
	{
		if(antlrNode instanceof SingleNodeContext)
		{
			return ((SingleNodeContext)antlrNode).tagName.getText();
		}
		else if(antlrNode instanceof ComplexNodeContext)
		{
			return ((ComplexNodeContext)antlrNode).tagName.getText();
		}
		return null;
	}

	/**
	 * 初始化属性
	 */
	protected void initAttributes()
	{
		if (attributeList == null)
		{
			attributeList=new ArrayList<AttributeContext>();
			attributeHash=new Hashtable<String, AttributeContext>();
			
			for(int i=0;i<antlrNode.getChildCount();i++)
			{
				ParseTree antlrChildNode=antlrNode.getChild(i);
				if(antlrChildNode instanceof AttributeContext)
				{
					AttributeContext attributeContext=(AttributeContext)antlrChildNode;
					attributeList.add(attributeContext);
					attributeHash.put(attributeContext.name.getText(), attributeContext);
				}
			}
		}
	}
	
	/**
	 * 是否有指定属性
	 * @param name
	 * @return
	 */
	public boolean hasAttribute(String name)
	{
		initAttributes();
		return attributeHash.containsKey(name);
	}
	
	/**
	 * 获取属性值
	 * @param name
	 * @return
	 */
	public String getAttribute(String name)
	{
		initAttributes();
		if(hasAttribute(name))
		{
			return attributeHash.get(name).value.getText();
		}
		return null;
	}
	
	/**
	 * 设置属性值
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name,String value)
	{
		initAttributes();
		if(value==null)
		{
			value="";
		}
		
		if(hasAttribute(name))
		{
			if(!attributeHash.get(name).value.getText().equals(value))
			{
				attributeHash.get(name).value.children.clear();
				attributeHash.get(name).value.children.add(new TerminalNodeImpl(new CommonToken(XmlParser.NAME,value)));
			}
		}
		else
		{
			XmlLexer lexer = new XmlLexer(new ANTLRInputStream(" "+name+"=\""+value+"\""));
			XmlParser parser = new XmlParser(new CommonTokenStream(lexer));
			parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

			AttributeContext attributeContext=parser.attribute();
			attributeList.add(attributeContext);
			attributeHash.put(attributeContext.name.getText(), attributeContext);
			
			//ParserRuleContext nativeNode=(ParserRuleContext)antlrNode;
		}
	}
	
	/**
	 * 获取属性Int值
	 * @param name
	 * @return
	 */
	protected int getAttributeByInt(String name)
	{
		String value=getAttribute(name);
		if(value!=null)
		{
			try
			{
				return Integer.parseInt(value);
			}
			catch(Error err)
			{
			}
		}
		return 0;
	}
	
	/**
	 * 设置属性Int值
	 * @param name
	 * @return
	 */
	protected void setAttributeByInt(String name,int value)
	{
		setAttribute(name, value+"");
	}
	
	/**
	 * 获取属性Float值
	 * @param name
	 * @return
	 */
	protected float getAttributeByFloat(String name)
	{
		String value=getAttribute(name);
		if(value!=null)
		{
			try
			{
				return Float.parseFloat(value);
			}
			catch(Error err)
			{
			}
		}
		return 0f;
	}
	
	/**
	 * 设置属性Float值
	 * @param name
	 * @return
	 */
	protected void setAttributeByFloat(String name,float value)
	{
		setAttribute(name, value+"");
	}
	
	/**
	 * 初始化子级
	 */
	protected void initChildren()
	{
		if (children == null)
		{
			children = new ArrayList<BaseTagNode>();
			
			for(int i=0;i<antlrNode.getChildCount();i++)
			{
				ParseTree antlrChildNode=antlrNode.getChild(i);
				if(isTagContext(antlrChildNode))
				{
					BaseTagNode node=initChild((ParserRuleContext)antlrChildNode);
					if(node!=null)
					{
						node.setDocument(getDocument());
						children.add(node);
					}
				}
			}
		}
	}
	
	/**
	 * 创建子级
	 * @param antlrNode
	 * @return
	 */
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		return null;
	}
}
