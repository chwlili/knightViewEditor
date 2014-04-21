package org.game.knight.editor.xml;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
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
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.graphics.Color;
import org.game.knight.ast.FileAst;
import org.game.knight.ast.FileAstManager;
import org.game.knight.ast.FileRef;
import org.game.knight.ast.IdDef;
import org.game.knight.ast.IdRef;

public class DomManager
{
	private static Hashtable<IDocument, DomManager> doms = new Hashtable<IDocument, DomManager>();
	private static Hashtable<IDocument, IFile> dom_file = new Hashtable<IDocument, IFile>();

	/**
	 * 获取文档管理器
	 * 
	 * @param document
	 * @return
	 */
	public static DomManager getDomManager(IDocument document)
	{
		if (!doms.containsKey(document))
		{
			doms.put(document, new DomManager(document));
		}
		return doms.get(document);
	}

	/**
	 * 链接文档到文件
	 * 
	 * @param document
	 * @param file
	 */
	public static void linkFile(IDocument document, IFile file)
	{
		dom_file.put(document, file);
	}

	/**
	 * 获取文档对应的文件
	 * 
	 * @param document
	 * @return
	 */
	public static IFile getFileByDocument(IDocument document)
	{
		return dom_file.get(document);
	}

	// --------------------------------------------------------------------------
	//
	// 实例方法
	//
	// --------------------------------------------------------------------------

	private IDocument dom;
	private boolean changed = false;
	private ArrayList<DomToken> tokens = new ArrayList<DomToken>();

	private XmlLexer lexer;
	private XmlParser parser;
	private RootContext root;

	private FileAst summay;

	private TextAttribute ProcessColor = new TextAttribute(new Color(null, 255, 0, 0));
	private TextAttribute DtdColor = new TextAttribute(new Color(null, 255, 0, 255));
	private TextAttribute CommentColor = new TextAttribute(new Color(null, 63, 95, 191));
	private TextAttribute CdataColor = new TextAttribute(new Color(null, 0, 0, 0));
	private TextAttribute TagColor = new TextAttribute(new Color(null, 0, 128, 128));
	private TextAttribute AttrColor = new TextAttribute(new Color(null, 127, 0, 127));
	private TextAttribute AttrValueColor = new TextAttribute(new Color(null, 42, 0, 255));
	private TextAttribute DefaultColor = new TextAttribute(new Color(null, 0, 0, 0));

	/**
	 * 构造函数
	 * 
	 * @param dom
	 */
	public DomManager(IDocument dom)
	{
		this.dom = dom;
		this.changed = true;
		this.dom.addDocumentListener(new IDocumentListener()
		{
			@Override
			public void documentChanged(DocumentEvent event)
			{
				changed = true;
			}

			@Override
			public void documentAboutToBeChanged(DocumentEvent event)
			{
			}
		});
	}

	/**
	 * 根椐索引位置查找Token
	 * 
	 * @param offset
	 * @return
	 */
	public DomToken getTokenByOffset(int offset)
	{
		if (changed)
		{
			init();
		}

		for (int i = 0; i < tokens.size(); i++)
		{
			DomToken token = tokens.get(i);
			token.index = i;

			if (token.start <= offset || offset < token.stop)
			{
				return token;
			}
		}
		return null;
	}

	/**
	 * 根椐Token查找下一个Token
	 * 
	 * @param token
	 * @return
	 */
	public DomToken getNextToken(DomToken token)
	{
		if (changed)
		{
			init();
		}

		int index = token.index + 1;

		if (index < tokens.size())
		{
			DomToken next = tokens.get(index);
			next.index = index;

			return next;
		}

		return null;
	}

	/**
	 * 获取文件引用
	 * 
	 * @param offset
	 * @return
	 */
	public FileRef getFileRef(int offset)
	{
		if (summay != null)
		{
			for (FileRef ref : summay.getFileRefs())
			{
				if (offset >= ref.getStart() && offset <= ref.getStop())
				{
					return ref;
				}
			}
		}
		return null;
	}

	/**
	 * 获取ID引用
	 * 
	 * @param offset
	 * @return
	 */
	public IdRef getIdRef(int offset)
	{
		if (summay != null)
		{
			for (IdRef ref : summay.getIDRefs())
			{
				if (offset >= ref.getStart() && offset <= ref.getStop())
				{
					return ref;
				}
			}
		}
		return null;
	}

	/**
	 * 获取ID定义
	 * 
	 * @param offset
	 * @return
	 */
	public IdDef getIdDef(int offset)
	{
		if (summay != null)
		{
			for (IdDef def : summay.getIDDefs())
			{
				if (offset >= def.getStart() && offset <= def.getStop())
				{
					return def;
				}
			}
		}
		return null;
	}

	/**
	 * 重建文档
	 */
	private void init()
	{
		if (!changed)
		{
			return;
		}

		tokens.clear();

		lexer = new XmlLexer(new ANTLRInputStream(dom.get()));

		parser = new XmlParser(new CommonTokenStream(lexer));
		parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

		root = parser.root();

		IFile file = getFileByDocument(dom);
		String path = FileAstManager.getViewURL(file);

		summay = new FileAst(path, file);
		summay.parse(root);

		XmlBaseVisitor<Integer> visitor = new XmlBaseVisitor<Integer>()
		{
			private Stack<TextAttribute> attributes = new Stack<TextAttribute>();

			@Override
			public Integer visitRoot(RootContext ctx)
			{
				attributes.push(DefaultColor);
				super.visitRoot(ctx);
				attributes.pop();
				return 0;
			}

			@Override
			public Integer visitProcess(ProcessContext ctx)
			{
				attributes.push(ProcessColor);
				super.visitProcess(ctx);
				attributes.pop();
				return 0;
			}

			@Override
			public Integer visitDtd(DtdContext ctx)
			{
				attributes.push(DtdColor);
				super.visitDtd(ctx);
				attributes.pop();
				return 0;
			}

			@Override
			public Integer visitComm(CommContext ctx)
			{
				attributes.push(CommentColor);
				super.visitComm(ctx);
				attributes.pop();
				return 0;
			}

			@Override
			public Integer visitCdata(CdataContext ctx)
			{
				int start = ctx.getStart().getStartIndex();
				int stop = ctx.getStop().getStopIndex();

				tokens.add(new DomToken(start, start + 8, TagColor));
				tokens.add(new DomToken(start + 9, stop - 3, CdataColor));
				tokens.add(new DomToken(stop - 2, stop, TagColor));
				return 0;
			}

			@Override
			public Integer visitSingleNode(SingleNodeContext ctx)
			{
				attributes.push(TagColor);
				super.visitSingleNode(ctx);
				attributes.pop();
				return 0;
			}

			@Override
			public Integer visitComplexNode(ComplexNodeContext ctx)
			{
				attributes.push(TagColor);
				super.visitComplexNode(ctx);
				attributes.pop();
				return 0;
			}

			@Override
			public Integer visitAttribute(AttributeContext ctx)
			{
				attributes.push(AttrColor);
				super.visitAttribute(ctx);
				attributes.pop();
				return 0;
			}

			@Override
			public Integer visitAttributeValue(AttributeValueContext ctx)
			{
				attributes.push(AttrValueColor);
				super.visitAttributeValue(ctx);
				attributes.pop();
				return 0;
			}

			@Override
			public Integer visitText(TextContext ctx)
			{
				attributes.push(DefaultColor);
				super.visitText(ctx);
				attributes.pop();
				return 0;
			}

			@Override
			public Integer visitTerminal(TerminalNode node)
			{
				int start = node.getSymbol().getStartIndex();
				int stop = node.getSymbol().getStopIndex();
				tokens.add(new DomToken(start, stop, attributes.lastElement()));
				return 0;
			}
		};

		visitor.visit(root);
		changed = false;
	}
}
