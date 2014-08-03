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
	 * ���캯��
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
	 * ��ȡ�ĵ�
	 * @return
	 */
	public ViewDocument getDocument()
	{
		return dom;
	}
	
	/**
	 * �����ĵ�
	 * @param dom
	 */
	public void setDocument(ViewDocument dom)
	{
		this.dom=dom;
	}
	
	/**
	 * ��ȡ��ǩ����
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
	 * ��ȡ�Ӽ�����
	 * @return
	 */
	public int getChildCount()
	{
		return getChildren().size();
	}
	
	/**
	 * ��������ȡ�Ӽ�
	 * @param index
	 * @return
	 */
	public BaseTagNode getChildAt(int index)
	{
		return getChildren().get(index);
	}

	/**
	 * ��ʼ����
	 * 
	 * @return
	 */
	public int getStartIndex()
	{
		return ((ParserRuleContext) antlrNode).start.getStartIndex();
	}

	/**
	 * ��������
	 * 
	 * @return
	 */
	public int getStopIndex()
	{
		return ((ParserRuleContext) antlrNode).stop.getStopIndex();
	}
	
	/**
	 * �Ƿ�Ϊ���������
	 * @param tree
	 * @return
	 */
	protected boolean isTagContext(ParseTree antlrNode)
	{
		return (antlrNode instanceof SingleNodeContext)||(antlrNode instanceof ComplexNodeContext);
	}
	
	/**
	 * ��ȡ�������
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
	 * ��ʼ������
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
	 * �Ƿ���ָ������
	 * @param name
	 * @return
	 */
	public boolean hasAttribute(String name)
	{
		initAttributes();
		return attributeHash.containsKey(name);
	}
	
	/**
	 * ��ȡ����ֵ
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
	 * ��������ֵ
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
	 * ��ȡ����Intֵ
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
	 * ��������Intֵ
	 * @param name
	 * @return
	 */
	protected void setAttributeByInt(String name,int value)
	{
		setAttribute(name, value+"");
	}
	
	/**
	 * ��ȡ����Floatֵ
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
	 * ��������Floatֵ
	 * @param name
	 * @return
	 */
	protected void setAttributeByFloat(String name,float value)
	{
		setAttribute(name, value+"");
	}
	
	/**
	 * ��ʼ���Ӽ�
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
	 * �����Ӽ�
	 * @param antlrNode
	 * @return
	 */
	protected BaseTagNode initChild(ParserRuleContext antlrNode)
	{
		return null;
	}
}
