package org.game.knight.ast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.chw.xml.XmlBaseVisitor;
import org.chw.xml.XmlLexer;
import org.chw.xml.XmlParser;
import org.chw.xml.XmlParser.AttributeContext;
import org.chw.xml.XmlParser.AttributeValueContext;
import org.chw.xml.XmlParser.CdataContext;
import org.chw.xml.XmlParser.CommContext;
import org.chw.xml.XmlParser.ComplexNodeContext;
import org.chw.xml.XmlParser.DtdContext;
import org.chw.xml.XmlParser.ProcessContext;
import org.chw.xml.XmlParser.RootContext;
import org.chw.xml.XmlParser.SingleNodeContext;
import org.chw.xml.XmlParser.TextContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

public class AST
{
	private AST ast;
	private IFile file;
	private IDocument document;
	private ASTLinks fileRefs;

	private ArrayList<Token> tokens = new ArrayList<Token>();
	private ArrayList<Object> trees = new ArrayList<Object>();

	/**
	 * 基于文档创建AST
	 * 
	 * @param document
	 */
	public AST(IDocument document)
	{
		this.ast = this;
		this.file = ASTManager.getDocumentFile(document);
		this.document = document;

		XmlLexer lexer = new XmlLexer(new ANTLRInputStream(document.get()));
		XmlParser parser = new XmlParser(new CommonTokenStream(lexer));
		parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

		build(parser.root());

		this.document.addDocumentListener(new IDocumentListener()
		{
			@Override
			public void documentChanged(DocumentEvent event)
			{
				// ..
			}

			@Override
			public void documentAboutToBeChanged(DocumentEvent event)
			{
				// ..
			}
		});
	}

	/**
	 * 基于文件创建AST
	 * 
	 * @param file
	 * @throws CoreException
	 * @throws IOException
	 */
	public AST(IFile file) throws CoreException, IOException
	{
		this.file = file;

		int bomLen = 0;
		IContentDescription desc = file.getContentDescription();
		if (desc != null)
		{
			byte[] bom = (byte[]) desc.getProperty(IContentDescription.BYTE_ORDER_MARK);
			if (bom != null)
			{
				bomLen = bom.length;
			}
		}

		InputStream stream = file.getContents();
		stream.skip(bomLen);
		InputStreamReader reader = new InputStreamReader(stream, file.getCharset());

		XmlLexer lexer = new XmlLexer(new ANTLRInputStream(reader));
		XmlParser parser = new XmlParser(new CommonTokenStream(lexer));
		parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

		build(parser.root());
	}

	/**
	 * 获取相关文件
	 * 
	 * @return
	 */
	public IFile getFile()
	{
		return file;
	}

	/**
	 * 获取Token列表
	 * 
	 * @return
	 */
	public ArrayList<Token> getTokens()
	{
		return tokens;
	}

	public ArrayList<Object> getTrees()
	{
		return trees;
	}

	/**
	 * 按偏移查找标签
	 * @param offset
	 * @return
	 */
	public AbsTag getTagBy(int offset)
	{
		AbsTag result=null;

		ArrayList<Object> nodes = getTrees();
		for (int i = 0; i < nodes.size(); i++)
		{
			Object node = nodes.get(i);
			if (!(node instanceof AbsTag))
			{
				continue;
			}
			
			AbsTag tag = (AbsTag) node;
			if (tag.getOffset() <= offset && offset <= tag.getOffset() + tag.getLength() - 1)
			{
				result=tag;
				
				if(tag.getChildren()!=null)
				{
					nodes = tag.getChildren();
					i = -1;
				}
				else
				{
					break;
				}
			}
		}
		
		return result;
	}

	/**
	 * 获取链接列表
	 * 
	 * @return
	 */
	public ASTLinks getLinks()
	{
		return fileRefs;
	}

	/**
	 * 构建
	 * 
	 * @param root
	 */
	private void build(RootContext root)
	{
		tokens.clear();
		trees.clear();

		root.accept(visitor);

		fileRefs = new ASTLinks(file, trees);
	}

	public static class Token
	{
		private static int id = 1;

		public static final int DEFAULT = id++;
		public static final int ERROR = id++;

		public static final int PROCESS = id++;
		public static final int DTD = id++;
		public static final int COMMENT = id++;
		public static final int CDATA_BEGIN = id++;
		public static final int CDATA = id++;
		public static final int CDATA_END = id++;
		public static final int TEXT = id++;
		public static final int SPACE = id++;

		public static final int TAG_L = id++;
		public static final int TAG_R = id++;
		public static final int TAG_SLASH = id++;
		public static final int TAG_NAME = id++;

		public static final int ATT_NAME = id++;
		public static final int ATT_EQUALS = id++;
		public static final int ATT_BEGIN = id++;
		public static final int ATT_VALUE = id++;
		public static final int ATT_END = id++;

		public int type;
		public int start;
		public int stop;
		public String text;

		public Token(int type, int start, int stop, String text)
		{
			this.type = type;
			this.start = start;
			this.stop = stop;
			this.text = text;
		}
	}

	private XmlBaseVisitor<Object> visitor = new XmlBaseVisitor<Object>()
	{
		private int id = 1;
		private int DEFAULT = id++;
		private int DOCUMENT = id++;
		private int ROOT = id++;
		private int DEPENDS = id++;
		private int BITMAPS = id++;
		private int BITMAP_RENDERS = id++;
		private int SWFS = id++;
		private int FILTERS = id++;
		private int FORMATS = id++;
		private int TEXTS = id++;
		private int CONTROLS = id++;
		private int CONTROL = id++;

		private Stack<Integer> stacks = new Stack<Integer>();

		@Override
		public Object visitRoot(RootContext ctx)
		{
			stacks.push(DOCUMENT);

			trees = new ArrayList<Object>();
			List<ParseTree> childNodes = ctx.children;
			for (ParseTree childNode : childNodes)
			{
				trees.add(visit(childNode));
			}

			stacks.pop();

			return null;
		}

		@Override
		public Object visitProcess(ProcessContext ctx)
		{
			Token token = new Token(Token.PROCESS, ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), ctx.getText());
			tokens.add(token);
			return token;
		}

		@Override
		public Object visitDtd(DtdContext ctx)
		{
			Token token = new Token(Token.DTD, ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), ctx.getText());
			tokens.add(token);
			return token;
		}

		@Override
		public Object visitComm(CommContext ctx)
		{
			Token token = new Token(Token.COMMENT, ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), ctx.getText());
			tokens.add(token);
			return token;
		}

		@Override
		public Object visitCdata(CdataContext ctx)
		{
			int start = ctx.getStart().getStartIndex();
			int stop = ctx.getStop().getStopIndex();
			String text = ctx.getText();

			Token first = new Token(Token.CDATA_BEGIN, start, start + 8, "<![CDATA[");
			Token content = new Token(Token.CDATA, start + 9, stop - 3, text.substring(9, text.length() - 3));
			Token last = new Token(Token.CDATA_END, stop - 2, stop, "]]>");

			tokens.add(first);
			tokens.add(content);
			tokens.add(last);

			return new CDataTag(first, last, content);
		}

		@Override
		public Object visitText(TextContext ctx)
		{
			Token token = new Token(Token.TEXT, ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), ctx.getText());
			tokens.add(token);
			return token;
		}

		@Override
		public Object visitSingleNode(SingleNodeContext ctx)
		{
			Token name = null;
			Token first = null;
			Token last = null;
			ArrayList<Attribute> attributes = new ArrayList<Attribute>();

			List<ParseTree> childNodes = ctx.children;
			for (ParseTree childNode : childNodes)
			{
				if (childNode instanceof TerminalNode)
				{
					TerminalNode terminal = (TerminalNode) childNode;

					Token token = null;
					int start = terminal.getSymbol().getStartIndex();
					int stop = terminal.getSymbol().getStopIndex();
					String text = terminal.getText();

					if (terminal.getSymbol() == ctx.begin)
					{
						token = first = new Token(Token.TAG_L, start, stop, text);
					}
					else if (terminal.getSymbol() == ctx.tagName)
					{
						token = name = new Token(Token.TAG_NAME, start, stop, text);
					}
					else if (terminal.getSymbol() == ctx.slash)
					{
						token = new Token(Token.TAG_SLASH, start, stop, text);
					}
					else if (terminal.getSymbol() == ctx.end)
					{
						token = last = new Token(Token.TAG_R, start, stop, text);
					}
					else
					{
						if (terminal.getSymbol().getType() == XmlParser.WS)
						{
							token = new Token(Token.SPACE, start, stop, text);
						}
						else
						{
							token = new Token(Token.DEFAULT, start, stop, text);
						}
					}
					tokens.add(token);
				}
				else if (childNode instanceof AttributeContext)
				{
					attributes.add((Attribute) visitAttribute((AttributeContext) childNode));
				}
			}

			return createTag(false, name, first, last, attributes, null);
		}

		@Override
		public Object visitComplexNode(ComplexNodeContext ctx)
		{
			int stack = stacks.lastElement();
			if (stack == DOCUMENT)
			{
				stacks.push(ROOT);
			}
			else if (stack == ROOT)
			{
				String partName = ctx.tagName.getText();
				if (partName.equals("depends"))
				{
					stacks.push(DEPENDS);
				}
				else if (partName.equals("bitmaps"))
				{
					stacks.push(BITMAPS);
				}
				else if (partName.equals("bitmapReaders"))
				{
					stacks.push(BITMAP_RENDERS);
				}
				else if (partName.equals("swfs"))
				{
					stacks.push(SWFS);
				}
				else if (partName.equals("filters"))
				{
					stacks.push(FILTERS);
				}
				else if (partName.equals("formats"))
				{
					stacks.push(FORMATS);
				}
				else if (partName.equals("texts"))
				{
					stacks.push(TEXTS);
				}
				else if (partName.equals("controls"))
				{
					stacks.push(CONTROLS);
				}
				else
				{
					stacks.push(DEFAULT);
				}
			}
			else
			{
				if (stack == CONTROLS || stack == CONTROL)
				{
					stacks.push(CONTROL);
				}
				else
				{
					stacks.push(DEFAULT);
				}
			}

			Token first = null;
			Token last = null;
			Token name = null;
			ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			ArrayList<Object> children = new ArrayList<Object>();

			List<ParseTree> childNodes = ctx.children;
			for (ParseTree childNode : childNodes)
			{
				if (childNode instanceof TerminalNode)
				{
					TerminalNode terminal = (TerminalNode) childNode;

					Token token = null;
					int start = terminal.getSymbol().getStartIndex();
					int stop = terminal.getSymbol().getStopIndex();
					String text = terminal.getText();

					if (terminal.getSymbol() == ctx.beginL)
					{
						token = first = new Token(Token.TAG_L, start, stop, text);
					}
					else if (terminal.getSymbol() == ctx.tagName)
					{
						token = name = new Token(Token.TAG_NAME, start, stop, text);
					}
					else if (terminal.getSymbol() == ctx.beginR)
					{
						token = new Token(Token.TAG_R, start, stop, text);
					}
					else if (terminal.getSymbol() == ctx.endL)
					{
						token = new Token(Token.TAG_L, start, stop, text);
					}
					else if (terminal.getSymbol() == ctx.endSlash)
					{
						token = new Token(Token.TAG_SLASH, start, stop, text);
					}
					else if (terminal.getSymbol() == ctx.endName)
					{
						token = new Token(Token.TAG_NAME, start, stop, text);
					}
					else if (terminal.getSymbol() == ctx.endR)
					{
						token = last = new Token(Token.TAG_R, start, stop, text);
					}
					else
					{
						if (terminal.getSymbol().getType() == XmlParser.WS)
						{
							token = new Token(Token.SPACE, start, stop, text);
						}
						else
						{
							token = new Token(Token.DEFAULT, start, stop, text);
						}
					}
					tokens.add(token);
				}
				else if (childNode instanceof AttributeContext)
				{
					attributes.add((Attribute) visitAttribute((AttributeContext) childNode));
				}
				else
				{
					children.add(visit(childNode));
				}
			}

			stacks.pop();

			return createTag(true, name, first, last, attributes, children);
		}

		/**
		 * 创建标签
		 * 
		 * @param complex
		 * @param name
		 * @param first
		 * @param last
		 * @param attributes
		 * @param children
		 * @return
		 */
		private AbsTag createTag(boolean complex, Token name, Token first, Token last, ArrayList<Attribute> attributes, ArrayList<Object> children)
		{
			String tagName = name.text;

			int type = stacks.lastElement();
			if (type == ROOT && tagName.equals("depends"))
			{
				return new ResourceSetTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == ROOT && tagName.equals("bitmaps"))
			{
				return new ResourceSetTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == ROOT && tagName.equals("bitmapReaders"))
			{
				return new ResourceSetTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == ROOT && tagName.equals("swfs"))
			{
				return new ResourceSetTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == ROOT && tagName.equals("filters"))
			{
				return new ResourceSetTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == ROOT && tagName.equals("formats"))
			{
				return new ResourceSetTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == ROOT && tagName.equals("texts"))
			{
				return new ResourceSetTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == ROOT && tagName.equals("controls"))
			{
				return new ResourceSetTag(ast, complex, first, last, name, attributes, children);
			}

			if (type == DEPENDS && tagName.equals("depend"))
			{
				return new ImportXmlTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == BITMAPS && tagName.equals("bitmap"))
			{
				return new DefineImgTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == SWFS && tagName.equals("swf"))
			{
				return new DefineSwfTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == BITMAP_RENDERS && tagName.equals("bitmapReader"))
			{
				return new DefineGridImgTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == FILTERS && tagName.equals("filter"))
			{
				return new DefineFilterTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == FORMATS && tagName.equals("format"))
			{
				return new DefineFormatTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == TEXTS && tagName.equals("text"))
			{
				return new DefineTextTag(ast, complex, first, last, name, attributes, children);
			}
			if (type == CONTROLS)
			{
				return new DefineControlTag(ast, complex, first, last, name, attributes, children, true);
			}
			if (type == CONTROL)
			{
				return new DefineControlTag(ast, complex, first, last, name, attributes, children, false);
			}

			return new AbsTag(ast, complex, first, last, name, attributes, children);
		}

		@Override
		public Object visitAttribute(AttributeContext ctx)
		{
			Token first = null;
			Token last = null;
			Token name = null;
			Token value = null;

			List<ParseTree> childNodes = ctx.children;
			for (ParseTree childNode : childNodes)
			{
				if (childNode instanceof TerminalNode)
				{
					TerminalNode terminal = (TerminalNode) childNode;

					Token token = null;
					int start = terminal.getSymbol().getStartIndex();
					int stop = terminal.getSymbol().getStopIndex();
					String text = terminal.getText();

					if (terminal.getSymbol() == ctx.space)
					{
						token = first = new Token(Token.SPACE, start, stop, text);
					}
					if (terminal.getSymbol() == ctx.name)
					{
						token = name = new Token(Token.ATT_NAME, start, stop, text);
					}
					else if (terminal.getSymbol() == ctx.equals)
					{
						token = new Token(Token.ATT_EQUALS, start, stop, text);
					}
					else if (terminal.getSymbol() == ctx.begin)
					{
						token = new Token(Token.ATT_BEGIN, start, stop, text);
					}
					else if (terminal.getSymbol() == ctx.end)
					{
						token = last = new Token(Token.ATT_END, start, stop, text);
					}
					else
					{
						if (terminal.getSymbol().getType() == XmlParser.WS)
						{
							token = new Token(Token.SPACE, start, stop, text);
						}
						else
						{
							token = new Token(Token.DEFAULT, start, stop, text);
						}
					}
					tokens.add(token);
				}
				else if (childNode instanceof AttributeValueContext)
				{
					AttributeValueContext attributeValue = (AttributeValueContext) childNode;

					Token token = new Token(Token.ATT_VALUE, attributeValue.getStart().getStartIndex(), attributeValue.getStop().getStopIndex(), attributeValue.getText());
					tokens.add(token);
					value = token;
				}
			}

			return new Attribute(first, last, name, value);
		}

		@Override
		public Object visitTerminal(TerminalNode node)
		{
			Token token = new Token(Token.DEFAULT, node.getSymbol().getStartIndex(), node.getSymbol().getStopIndex(), node.getText());
			tokens.add(token);
			return token;
		}

		@Override
		public Object visitErrorNode(ErrorNode node)
		{
			Token token = new Token(Token.ERROR, node.getSymbol().getStartIndex(), node.getSymbol().getStopIndex(), node.getText());
			tokens.add(token);
			return token;
		}
	};
}
