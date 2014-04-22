package org.game.knight.ast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.chw.xml.XmlLexer;
import org.chw.xml.XmlParser;
import org.chw.xml.XmlParser.RootContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.jface.text.IDocument;

public class ASTManager
{
	private static Hashtable<IDocument, AST> dom_ast = new Hashtable<IDocument, AST>();
	private static Hashtable<IDocument, IFile> dom_file = new Hashtable<IDocument, IFile>();

	/**
	 * 链接文档和文件
	 * 
	 * @param document
	 * @param file
	 */
	public static void linkDocument(IDocument document, IFile file)
	{
		dom_file.put(document, file);
	}

	/**
	 * 断开链接文档和文件
	 * 
	 * @param document
	 * @param file
	 */
	public static void delinkDocument(IDocument document)
	{
		dom_file.remove(document);
		dom_ast.remove(document);
	}

	/**
	 * 获取文档的文件
	 * 
	 * @param document
	 * @return
	 */
	public static IFile getFile(IDocument document)
	{
		return dom_file.get(document);
	}

	/**
	 * 获取文档的AST
	 * 
	 * @param document
	 * @return
	 */
	public static AST getAST(IDocument document)
	{
		if (!dom_ast.containsKey(document))
		{
			XmlLexer lexer = new XmlLexer(new ANTLRInputStream(document.get()));
			XmlParser parser = new XmlParser(new CommonTokenStream(lexer));
			parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

			dom_ast.put(document, new AST(parser.root()));
		}
		return dom_ast.get(document);
	}

	// ------------------------------------------------------------------------------

	private static final String SOURCE_FOLDER_NAME = "views";
	private static Hashtable<IProject, Hashtable<String, AST>> projectTable = new Hashtable<IProject, Hashtable<String, AST>>();

	/**
	 * 获取视图地址
	 * 
	 * @param file
	 * @return
	 */
	public static String getView(IFile file)
	{
		return file.getProjectRelativePath().toString().substring(SOURCE_FOLDER_NAME.length());
	}

	/**
	 * 查找源文件
	 * 
	 * @param project
	 * @param url
	 * @return
	 */
	public static IFile getSourceFile(IProject project, String url)
	{
		IFolder folder = project.getFolder(SOURCE_FOLDER_NAME);
		return folder.getFile(new Path(url));
	}

	/**
	 * 查找文件内容
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws CoreException
	 */
	public static AST getAST(IFile file) throws CoreException, IOException
	{
		IProject project = file.getProject();

		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, AST>());
		}

		if (file.getName().endsWith(".xml"))
		{
			Hashtable<String, AST> views = projectTable.get(project);

			String viewURL = getView(file);
			if (!views.containsKey(viewURL))
			{
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

				views.put(viewURL, new AST(parser.root()));
			}
			return views.get(viewURL);
		}
		return null;
	}
}
