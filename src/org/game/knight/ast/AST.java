package org.game.knight.ast;

import java.util.ArrayList;
import java.util.Stack;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.chw.xml.XmlBaseVisitor;
import org.chw.xml.XmlParser.AttributeContext;
import org.chw.xml.XmlParser.AttributeValueContext;
import org.chw.xml.XmlParser.CdataContext;
import org.chw.xml.XmlParser.CommContext;
import org.chw.xml.XmlParser.ComplexNodeContext;
import org.chw.xml.XmlParser.DtdContext;
import org.chw.xml.XmlParser.ProcessContext;
import org.chw.xml.XmlParser.RootContext;
import org.chw.xml.XmlParser.SingleNodeContext;
import org.chw.xml.XmlParser.SpaceContext;
import org.chw.xml.XmlParser.TextContext;

public class AST
{
	public AST(RootContext root)
	{
		build(root);
	}

	private void build(RootContext root)
	{
		root.accept(visitor);
	}

	public static class Token
	{
		public static final int PROCESS=1;
		public static final int DTD=2;
		public static final int COMMENT=3;
		public static final int CDATA_BEGIN=4;
		public static final int CDATA=5;
		public static final int CDATA_END=6;
		public static final int TEXT=7;
		public static final int SPACE=8;
		
		public int type;
		public int start;
		public int stop;
		public String text;
		
		public Token(int type,int start,int stop,String text)
		{
			this.type=type;
			this.start=start;
			this.stop=stop;
			this.text=text;
		}
	}
	
	private XmlBaseVisitor<Object> visitor = new XmlBaseVisitor<Object>()
	{
		private static final int Root=1;
		private static final int DependPart=2;
		private static final int BitmpPart=3;
		//private static final int 
		
		private Stack<Integer> stack=new Stack<Integer>();
		private ArrayList<Token> tokens=new ArrayList<Token>();
		
		@Override
		public Object visitRoot(RootContext ctx)
		{
			stack.push(Root);
			super.visitChildren(ctx);
			stack.pop();
			
			return null;
		}
		
		@Override
		public Object visitProcess(ProcessContext ctx)
		{
			tokens.add(new Token(Token.PROCESS, ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), ctx.getText()));
			return null;
		}
		
		@Override
		public Object visitDtd(DtdContext ctx) 
		{
			tokens.add(new Token(Token.DTD,ctx.getStart().getStartIndex(),ctx.getStop().getStopIndex(),ctx.getText()));
			return null;
		};
		
		public Object visitComm(CommContext ctx)
		{
			tokens.add(new Token(Token.COMMENT,ctx.getStart().getStartIndex(),ctx.getStop().getStopIndex(),ctx.getText()));
			return null;
		}
		
		public Object visitCdata(CdataContext ctx)
		{
			int start=ctx.getStart().getStartIndex();
			int stop=ctx.getStop().getStopIndex();
			String text=ctx.getText();
			
			tokens.add(new Token(Token.CDATA_BEGIN,start,start+8,"<![CDATA["));
			tokens.add(new Token(Token.CDATA,start+8,stop-2,text.substring(9, text.length()-3)));
			tokens.add(new Token(Token.CDATA_END,stop-2,stop,"]]>"));
			
			return null;
		}
		
		public Object visitText(TextContext ctx)
		{
			tokens.add(new Token(Token.TEXT,ctx.getStart().getStartIndex(),ctx.getStop().getStopIndex(),ctx.getText()));
			return null;
		}
		
		public Object visitSpace(SpaceContext ctx)
		{
			tokens.add(new Token(Token.SPACE,ctx.getStart().getStartIndex(),ctx.getStop().getStopIndex(),ctx.getText()));
			return null;
		}
		
		public Object visitSingleNode(SingleNodeContext ctx)
		{
			return super.visitSingleNode(ctx);
		}
		
		public Object visitComplexNode(ComplexNodeContext ctx)
		{
			return super.visitComplexNode(ctx);
		}
		
		@Override
		public Object visitAttribute(AttributeContext ctx)
		{
			return super.visitAttribute(ctx);
		}
		
		public Object visitAttributeValue(AttributeValueContext ctx)
		{
			return super.visitAttributeValue(ctx);
		}
		
		@Override
		public Object visitTerminal(TerminalNode node)
		{
			return super.visitTerminal(node);
		}
		
		public Object visitErrorNode(ErrorNode node)
		{
			return super.visitErrorNode(node);
		}
	};
}
