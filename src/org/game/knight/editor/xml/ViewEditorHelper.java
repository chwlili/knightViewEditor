package org.game.knight.editor.xml;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.game.knight.ast.ASTManager;

public class ViewEditorHelper extends PresentationReconciler
{
	public final static String XML_COMMENT = "view_xml_comment";

	public final static String XML_PROCCESS = "view_xml_proccess";

	public final static String XML_DOCTYPE = "view_xml_doctype";

	public final static String XML_TAG = "view_xml_tag";

	public final static String XML_CDATA = "view_xml_cdata";

	/**
	 * 创建一个文档提供器
	 * 
	 * @return
	 */
	public static IDocumentProvider createDocumentProvider()
	{
		return new MyDocumentProvider();
	}

	/**
	 * 文档提供器
	 * @author chw
	 *
	 */
	private static class MyDocumentProvider extends FileDocumentProvider
	{
		@Override
		protected IDocument createEmptyDocument()
		{
			return new FileDocument();
		}
		
		protected IDocument createDocument(Object element) throws CoreException
		{
			IDocument document = super.createDocument(element);

			IFileEditorInput input = (IFileEditorInput) element;

			ASTManager.linkDocument(document, input.getFile());

			if (document != null)
			{
				IDocumentPartitioner partitioner = new FastPartitioner(new ViewXmlPartitionScanner(), new String[] { XML_COMMENT, XML_PROCCESS, XML_DOCTYPE, XML_TAG, XML_CDATA });
				partitioner.connect(document);
				document.setDocumentPartitioner(partitioner);
			}
			return document;
		}
		
		@Override
		protected void setupDocument(Object element, IDocument document)
		{
			super.setupDocument(element, document);
			
			FileDocument dom=(FileDocument)document;
			dom.setFile(((IFileEditorInput) element).getFile());
		}
	}

	/**
	 * 分区搜索器
	 * @author chw
	 *
	 */
	private static class ViewXmlPartitionScanner extends RuleBasedPartitionScanner
	{
		public ViewXmlPartitionScanner()
		{
			ArrayList<IPredicateRule> rules = new ArrayList<IPredicateRule>();

			rules.add(new MultiLineRule("<!--", "-->", new Token(XML_COMMENT)));
			rules.add(new MultiLineRule("<?", "?>", new Token(XML_PROCCESS)));
			rules.add(new MultiLineRule("<!DOCTYPE", ">", new Token(XML_DOCTYPE)));
			rules.add(new MultiLineRule("<![CDATA[", "]]>", new Token(XML_CDATA)));
			rules.add(new TagRule(new Token(XML_TAG)));

			this.setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
		}
	}

	/**
	 * Tag搜索规则
	 * @author chw
	 *
	 */
	private static class TagRule extends MultiLineRule
	{
		public TagRule(IToken token)
		{
			super("<", ">", token);
		}

		protected boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed)
		{
			int c = scanner.read();
			if (sequence[0] == '<')
			{
				if (c == '?')
				{
					scanner.unread();
					return false;
				}
				if (c == '!')
				{
					scanner.unread();
					return false;
				}
			}
			else if (sequence[0] == '>')
			{
				scanner.unread();
			}
			return super.sequenceDetected(scanner, sequence, eofAllowed);
		}
	}

	// ------------------------------------------------------------------------------------------------------

	private static TextAttribute ProcessColor = new TextAttribute(new Color(null, 255, 0, 0));
	private static TextAttribute DtdColor = new TextAttribute(new Color(null, 255, 0, 255));
	private static TextAttribute CommentColor = new TextAttribute(new Color(null, 63, 95, 191));
	private static TextAttribute CdataColor = new TextAttribute(new Color(null, 0, 0, 0));
	private static TextAttribute TagColor = new TextAttribute(new Color(null, 0, 128, 128));
	private static TextAttribute AttrColor = new TextAttribute(new Color(null, 127, 0, 127));
	private static TextAttribute AttrValueColor = new TextAttribute(new Color(null, 42, 0, 255));
	private static TextAttribute DefaultColor = new TextAttribute(new Color(null, 0, 0, 0));

	/**
	 * 创建一个呈现恢复器
	 * 
	 * @return
	 */
	public static PresentationReconciler createPresentationReconciler()
	{
		PresentationReconciler presentation = new PresentationReconciler();

		initCommentScaner(presentation);
		
		initProccessScaner(presentation);
		
		initDoctypeScaner(presentation);

		initCDataScanner(presentation);

		initTagScaner(presentation);

		return presentation;
	}

	/**
	 * COMMENT分区
	 * 
	 * @param presentation
	 */
	private static void initCommentScaner(PresentationReconciler presentation)
	{
		RuleBasedScanner scanner = new RuleBasedScanner();
		scanner.setDefaultReturnToken(new Token(CommentColor));
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
		presentation.setDamager(dr, XML_COMMENT);
		presentation.setRepairer(dr, XML_COMMENT);
	}
	
	/**
	 * PROCCESS分区
	 * @param presentation
	 */
	private static void initProccessScaner(PresentationReconciler presentation)
	{
		RuleBasedScanner scanner = new RuleBasedScanner();
		scanner.setDefaultReturnToken(new Token(ProcessColor));
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
		presentation.setDamager(dr, XML_PROCCESS);
		presentation.setRepairer(dr, XML_PROCCESS);
	}
	
	/**
	 * DOCTYPE分区
	 * @param presentation
	 */
	private static void initDoctypeScaner(PresentationReconciler presentation)
	{
		RuleBasedScanner scanner = new RuleBasedScanner();
		scanner.setDefaultReturnToken(new Token(DtdColor));
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
		presentation.setDamager(dr, XML_DOCTYPE);
		presentation.setRepairer(dr, XML_DOCTYPE);
	}

	/**
	 * CDATA分区
	 * 
	 * @param presentation
	 */
	private static void initCDataScanner(PresentationReconciler presentation)
	{
		RuleBasedScanner scanner = new RuleBasedScanner();
		scanner.setDefaultReturnToken(new Token(CdataColor));

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
		presentation.setDamager(dr, XML_CDATA);
		presentation.setRepairer(dr, XML_CDATA);
	}

	/**
	 * Tag分区
	 * 
	 * @param presentation
	 */
	private static void initTagScaner(PresentationReconciler presentation)
	{
		RuleBasedScanner scanner = new RuleBasedScanner();
		scanner.setDefaultReturnToken(new Token(DefaultColor));

		SingleLineRule attValue1 = new SingleLineRule("\"", "\"", new Token(AttrValueColor));
		SingleLineRule attValue2 = new SingleLineRule("'", "'", new Token(AttrValueColor));
		WhitespaceRule space = new WhitespaceRule(new WhitespaceDetector());
		WordRule equals = new WordRule(new CharDetector(new String[] { "=" }), new Token(TagColor));
		WordRule tagBracket = new WordRule(new CharDetector(new String[] { "<", "</", ">", "/>" }), new Token(TagColor));
		TagNameRule tagBegin = new TagNameRule(new Token(TagColor));
		AttNameRule attBegin = new AttNameRule(new Token(AttrColor));
		scanner.setRules(new IRule[] { attValue1, attValue2, space, equals, tagBracket, tagBegin, attBegin });

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
		presentation.setDamager(dr, XML_TAG);
		presentation.setRepairer(dr, XML_TAG);
	}

	/**
	 * 标签名规则
	 * 
	 * @author chw
	 * 
	 */
	private static class TagNameRule implements IRule
	{
		private Token token;

		public TagNameRule(Token token)
		{
			this.token = token;
		}

		@Override
		public IToken evaluate(ICharacterScanner scanner)
		{
			int prev1;
			int prev2;

			scanner.unread();
			prev1 = scanner.read();
			if (prev1 == '<')
			{
				int next = scanner.read();
				while (true)
				{
					if (next == '/' || next == '>' || next == ' ' || next == '\t' || next == '\r' || next == '\n' || next == ICharacterScanner.EOF)
					{
						scanner.unread();
						return token;
					}
					next = scanner.read();
				}
			}
			else if (prev1 == '/')
			{
				scanner.unread();
				scanner.unread();
				prev2 = scanner.read();
				scanner.read();

				if (prev2 == '<')
				{
					int next = scanner.read();
					while (true)
					{
						if (next == '/' || next == '>' || next == ' ' || next == '\t' || next == '\r' || next == '\n' || next == ICharacterScanner.EOF)
						{
							scanner.unread();
							return token;
						}
						next = scanner.read();
					}
				}
			}

			return Token.UNDEFINED;
		}
	}

	/**
	 * 属性名规则
	 * 
	 * @author chw
	 * 
	 */
	private static class AttNameRule implements IRule
	{
		private Token token;

		public AttNameRule(Token token)
		{
			this.token = token;
		}

		@Override
		public IToken evaluate(ICharacterScanner scanner)
		{
			int prev;

			scanner.unread();
			prev = scanner.read();
			if (prev == ' ' || prev == '\r' || prev == '\n')
			{
				int next = scanner.read();
				while (true)
				{
					if (next == ' ' || next == '\t' || next == '\r' || next == '\n' || next == '=' || next == '/' || next == '>' || next == ICharacterScanner.EOF)
					{
						scanner.unread();
						return token;
					}
					next = scanner.read();
				}
			}

			return Token.UNDEFINED;
		}
	}

	/**
	 * 空白检测器
	 * 
	 * @author chw
	 * 
	 */
	private static class WhitespaceDetector implements IWhitespaceDetector
	{
		@Override
		public boolean isWhitespace(char c)
		{
			return (' ' == c || '\t' == c || '\r' == c || '\n' == c);
		}
	}

	/**
	 * 字符检测器
	 * 
	 * @author chw
	 * 
	 */
	private static class CharDetector implements IWordDetector
	{
		private char[][] words;
		private char[] chars;
		private int charIndex;

		public CharDetector(String[] words)
		{
			this.words = new char[words.length][];
			for (int i = 0; i < words.length; i++)
			{
				this.words[i] = words[i].toCharArray();
			}
		}

		@Override
		public boolean isWordStart(char c)
		{
			chars = null;
			charIndex = 0;

			for (int i = 0; i < words.length; i++)
			{
				if (words[i][0] == c)
				{
					chars = words[i];
					charIndex = 1;
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean isWordPart(char c)
		{
			if (chars != null && charIndex > 0)
			{
				if (charIndex < chars.length && chars[charIndex] == c)
				{
					charIndex++;
					return true;
				}
			}
			return false;
		}

	}
}
