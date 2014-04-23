package org.game.knight.ast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.chw.xml.XmlLexer;
import org.chw.xml.XmlParser;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.jface.text.IDocument;

public class ASTManager
{
	private static Hashtable<IDocument, AST> dom_ast = new Hashtable<IDocument, AST>();
	private static Hashtable<IDocument, IFile> dom_file = new Hashtable<IDocument, IFile>();
	private static Hashtable<IFile, IDocument> file_dom = new Hashtable<IFile, IDocument>();

	/**
	 * 链接文档和文件
	 * 
	 * @param document
	 * @param file
	 */
	public static void linkDocument(IDocument document, IFile file)
	{
		dom_file.put(document, file);
		file_dom.put(file, document);
	}

	/**
	 * 断开链接文档和文件
	 * 
	 * @param document
	 * @param file
	 */
	public static void delinkDocument(IDocument document)
	{
		dom_ast.remove(document);
		
		dom_file.remove(document);
		file_dom.remove(dom_file.get(document));
	}

	/**
	 * 获取文档的文件
	 * 
	 * @param document
	 * @return
	 */
	public static IFile getDocumentFile(IDocument document)
	{
		return dom_file.get(document);
	}

	/**
	 * 获取文档的AST
	 * 
	 * @param document
	 * @return
	 */
	public static AST getDocumentAST(IDocument document)
	{
		if (!dom_ast.containsKey(document))
		{
			XmlLexer lexer = new XmlLexer(new ANTLRInputStream(document.get()));
			XmlParser parser = new XmlParser(new CommonTokenStream(lexer));
			parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

			dom_ast.put(document, new AST(getDocumentFile(document),parser.root()));
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
	public static String getFilePath(IFile file)
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

			String url = getFilePath(file);
			if (!views.containsKey(url))
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

				views.put(url, new AST(file,parser.root()));
			}
			return views.get(url);
		}
		return null;
	}
	
	
	//----------------------------------------------------------------------------------------------------------------
	//
	// 监视资源变动
	// 1.项目被删除或关闭时，清除地应的AST列表
	// 2.文件变动时，删除对应的AST
	//
	//----------------------------------------------------------------------------------------------------------------
	
	/**
	 * 保持资源更新监视器
	 * 
	 * @param iWorkspace
	 */
	private static void keepResourceChangeListener()
	{
		IWorkspace iWorkspace = ResourcesPlugin.getWorkspace();

		iWorkspace.removeResourceChangeListener(fileListener);

		ArrayList<IProject> projects = new ArrayList<IProject>();
		for (IProject project : projectTable.keySet())
		{
			if (projectTable.get(project).size() == 0)
			{
				projects.add(project);
			}
		}

		for (IProject projec : projects)
		{
			projectTable.remove(projec);
		}

		if (projectTable.size() > 0)
		{
			iWorkspace.addResourceChangeListener(fileListener, IResourceChangeEvent.PRE_DELETE | IResourceChangeEvent.PRE_CLOSE | IResourceChangeEvent.POST_CHANGE);
		}
	}

	/**
	 * 资源更改监视器
	 */
	private static IResourceChangeListener fileListener = new IResourceChangeListener()
	{
		@Override
		public void resourceChanged(IResourceChangeEvent event)
		{
			onResourceChange(event);
		}
	};

	/**
	 * 处理资源更改
	 */
	private static void onResourceChange(IResourceChangeEvent event)
	{
		if (event.getType() == IResourceChangeEvent.PRE_DELETE || event.getType() == IResourceChangeEvent.PRE_CLOSE)
		{
			if (projectTable.containsKey(event.getResource()))
			{
				projectTable.get(event.getResource()).clear();
				projectTable.remove(event.getResource());
				keepResourceChangeListener();
			}
		}
		else if (event.getType() == IResourceChangeEvent.POST_CHANGE)
		{
			try
			{
				event.getDelta().accept(new IResourceDeltaVisitor()
				{
					@Override
					public boolean visit(IResourceDelta delta) throws CoreException
					{
						IProject project = delta.getResource().getProject();
						if (project != null)
						{
							if (projectTable.containsKey(project))
							{
								IResource res = delta.getResource();
								if (res instanceof IFile)
								{
									IFile file = (IFile) res;
									if (file.getName().endsWith(".xml"))
									{
										projectTable.get(project).remove(getFilePath(file));
									}
								}
							}
							else
							{
								return false;
							}
						}
						return true;
					}
				});
			}
			catch (CoreException e)
			{
				e.printStackTrace();
			}

			keepResourceChangeListener();
		}
	}
	
}
