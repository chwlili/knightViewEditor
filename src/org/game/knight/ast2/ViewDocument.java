package org.game.knight.ast2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentDescription;

public class ViewDocument
{
	private static final String SOURCE_FOLDER_NAME = "views";
	
	private IFile file;
	private ViewRoot root;
	
	/**
	 * 从文件构建
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

		RootContext antlrNode=parser.root();
		
		for(int i=0;i<antlrNode.getChildCount();i++)
		{
			ParseTree antlrChildNode=antlrNode.getChild(i);
			if(antlrChildNode instanceof SingleNodeContext || antlrChildNode instanceof ComplexNodeContext)
			{
				root=new ViewRoot(this,(ParserRuleContext) antlrNode);
				break;
			}
		}
	}
	
	/**
	 * 文件
	 * @return
	 */
	public IFile getFile()
	{
		return file;
	}
	
	/**
	 * 获取根节点
	 * @return
	 */
	public ViewRoot getRoot()
	{
		return root;
	}

	/**
	 * 引用路径
	 * @param file
	 * @return
	 */
	public String getProjectPath()
	{
		return file.getProjectRelativePath().toString().substring(SOURCE_FOLDER_NAME.length());
	}
	
	/**
	 * 解析路径
	 * @param path
	 * @return
	 */
	public String resolvePath(String refPath)
	{
		String currPath=getProjectPath();
		
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
