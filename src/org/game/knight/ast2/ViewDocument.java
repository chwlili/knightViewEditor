package org.game.knight.ast2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.chw.xml.XmlLexer;
import org.chw.xml.XmlParser;
import org.chw.xml.XmlParser.ComplexNodeContext;
import org.chw.xml.XmlParser.RootContext;
import org.chw.xml.XmlParser.SingleNodeContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentDescription;

public class ViewDocument
{
	private static final String SOURCE_FOLDER_NAME = "views";

	private IFile file;
	private ViewRoot root;

	/**
	 * 从文件构建
	 * 
	 * @param file
	 * @throws CoreException
	 * @throws IOException
	 */
	public ViewDocument(IFile file) throws CoreException, IOException
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

		RootContext antlrNode = parser.root();

		for (int i = 0; i < antlrNode.getChildCount(); i++)
		{
			ParseTree antlrChildNode = antlrNode.getChild(i);
			if (antlrChildNode instanceof SingleNodeContext || antlrChildNode instanceof ComplexNodeContext)
			{
				root = new ViewRoot(this, (ParserRuleContext) antlrChildNode);
				break;
			}
		}
	}

	/**
	 * 文件
	 * 
	 * @return
	 */
	public IFile getFile()
	{
		return file;
	}

	/**
	 * 获取根节点
	 * 
	 * @return
	 */
	public ViewRoot getRoot()
	{
		return root;
	}

	/**
	 * 查找位图
	 * 
	 * @param id
	 * @return
	 */
	public BitmapNode findBitmap(String id)
	{
		return (BitmapNode) find(BitmapNode.class, id);
	}

	/**
	 * 查找九宫图
	 * 
	 * @param id
	 * @return
	 */
	public GridBitmapNode findGridBitmap(String id)
	{
		return (GridBitmapNode) find(GridBitmapNode.class, id);
	}

	/**
	 * 查找SWF
	 * 
	 * @param id
	 * @return
	 */
	public SwfNode findSwf(String id)
	{
		return (SwfNode) find(SwfNode.class, id);
	}

	/**
	 * 查找滤镜
	 * 
	 * @param id
	 * @return
	 */
	public FilterNode findFilter(String id)
	{
		return (FilterNode) find(FilterNode.class, id);
	}

	/**
	 * 查找格式
	 * 
	 * @param id
	 * @return
	 */
	public FormatNode findFormat(String id)
	{
		return (FormatNode) find(FormatNode.class, id);
	}

	/**
	 * 查找颜色
	 * 
	 * @param id
	 * @return
	 */
	public ColorNode findColor(String id)
	{
		return (ColorNode) find(ColorNode.class, id);
	}

	/**
	 * 查找文字
	 * 
	 * @param id
	 * @return
	 */
	public TextNode findText(String id)
	{
		return (TextNode) find(TextNode.class, id);
	}

	/**
	 * 查找视图
	 * 
	 * @param id
	 * @return
	 */
	public UIBase findView(String id)
	{
		return (UIBase) find(UIBase.class, id);
	}

	/**
	 * 查找入口函数
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	private Object find(@SuppressWarnings("rawtypes") Class type, String id)
	{
		HashSet<ViewDocument> list = new HashSet<ViewDocument>();
		try
		{
			return find(type, id, list);
		}
		finally
		{
			list.clear();
		}
	}

	/**
	 * 查找递归函数
	 * 
	 * @param type
	 * @param id
	 * @param findedTable
	 * @return
	 */
	private Object find(@SuppressWarnings("rawtypes") Class type, String id, HashSet<ViewDocument> findedTable)
	{
		findedTable.add(this);

		if (root == null || id == null || id.trim().isEmpty())
		{
			return null;
		}

		if (type == BitmapNode.class && root.getBitmapList() != null)
		{
			return root.getBitmapList().find(id);
		}
		else if (type == GridBitmapNode.class && root.getGridBitmapList() != null)
		{
			return root.getGridBitmapList().find(id);
		}
		else if (type == SwfNode.class && root.getSwfList() != null)
		{
			return root.getSwfList().find(id);
		}
		else if (type == FilterNode.class && root.getFilterList() != null)
		{
			return root.getFilterList().find(id);
		}
		else if (type == FormatNode.class && root.getFormatList() != null)
		{
			return root.getFormatList().find(id);
		}
		else if (type == ColorNode.class && root.getColorList() != null)
		{
			return root.getColorList().find(id);
		}
		else if (type == TextNode.class && root.getTextList() != null)
		{
			return root.getTextList().find(id);
		}
		else if (type == UIBase.class)
		{
			return root.getViewList().find(id);
		}

		if (root.getDependList() == null)
		{
			return null;
		}

		for (int i = root.getDependList().size() - 1; i >= 0; i--)
		{
			DependNode depend = root.getDependList().get(i);
			if (depend != null)
			{
				ViewDocument dom = depend.getDependDocument();
				if (dom != null && findedTable.contains(dom) == false)
				{
					Object result = dom.find(type, id, findedTable);
					if (result != null)
					{
						return result;
					}
				}
			}
		}

		return null;
	}
	
	/**
	 * 引用路径
	 * 
	 * @param file
	 * @return
	 */
	public String getProjectPath()
	{
		return file.getProjectRelativePath().toString().substring(SOURCE_FOLDER_NAME.length());
	}

	/**
	 * 解析文件
	 * 
	 * @param url
	 * @return
	 */
	public IFile resolveFile(String url)
	{
		url = resolvePath(url);

		IResource res = file.getProject().getFolder(SOURCE_FOLDER_NAME).findMember(url);
		if (res instanceof IFile)
		{
			return (IFile) res;
		}

		return null;
	}

	/**
	 * 解析路径
	 * 
	 * @param path
	 * @return
	 */
	private String resolvePath(String refPath)
	{
		String currPath = getProjectPath();

		currPath = currPath.replaceAll("\\\\", "/").replaceAll("/+", "/");
		refPath = refPath.replaceAll("\\\\", "/").replaceAll("/+", "/");

		if (refPath.charAt(0) == '/')
		{
			currPath = "/";
			refPath = refPath.substring(1);
		}
		else
		{
			currPath = currPath.substring(0, currPath.lastIndexOf('/'));
		}

		String[] parts = refPath.split("/");
		for (String part : parts)
		{
			if (part.equals("."))
			{
				continue;
			}
			else if (part.equals(".."))
			{
				if (currPath.equals("/"))
				{
					currPath = null;
					break;
				}
				else
				{
					int index = currPath.lastIndexOf("/");
					if (index == 0)
					{
						currPath = "/";
					}
					else
					{
						currPath = currPath.substring(0, index);
					}
				}
			}
			else
			{
				if (currPath.equals("/"))
				{
					currPath = "/" + part;
				}
				else
				{
					currPath += "/" + part;
				}
			}
		}

		return currPath;
	}

}
