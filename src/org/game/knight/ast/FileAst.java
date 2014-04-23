package org.game.knight.ast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.chw.xml.XmlLexer;
import org.chw.xml.XmlParser;
import org.chw.xml.XmlParser.AttributeContext;
import org.chw.xml.XmlParser.ComplexNodeContext;
import org.chw.xml.XmlParser.RootContext;
import org.chw.xml.XmlParser.SingleNodeContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentDescription;

public class FileAst
{
	private String innerPath;
	private IFile file;
	private AST ast;

	private ArrayList<FileRef> dependRefs = new ArrayList<FileRef>();
	private ArrayList<FileRef> fileRefs = new ArrayList<FileRef>();
	private ArrayList<IdRef> idRefs = new ArrayList<IdRef>();
	private ArrayList<IdDef> idDefs = new ArrayList<IdDef>();

	private ArrayList<IdDef> bitmapList = new ArrayList<IdDef>();
	private ArrayList<IdDef> swfList = new ArrayList<IdDef>();
	private ArrayList<IdDef> bitmapRendererList = new ArrayList<IdDef>();
	private ArrayList<IdDef> filterList = new ArrayList<IdDef>();
	private ArrayList<IdDef> formatList = new ArrayList<IdDef>();
	private ArrayList<IdDef> textList = new ArrayList<IdDef>();
	private ArrayList<IdDef> controlList = new ArrayList<IdDef>();

	private Hashtable<String, IdDef> bitmapTable = new Hashtable<String, IdDef>();
	private Hashtable<String, IdDef> swfTable = new Hashtable<String, IdDef>();
	private Hashtable<String, IdDef> bitmapRendererTable = new Hashtable<String, IdDef>();
	private Hashtable<String, IdDef> filterTable = new Hashtable<String, IdDef>();
	private Hashtable<String, IdDef> formatTable = new Hashtable<String, IdDef>();
	private Hashtable<String, IdDef> textTable = new Hashtable<String, IdDef>();
	private Hashtable<String, IdDef> controlTable = new Hashtable<String, IdDef>();

	/**
	 * 构造函数
	 * 
	 * @param innerPath
	 * @param file
	 */
	public FileAst(String innerPath, IFile file)
	{
		this.innerPath = innerPath;
		this.file = file;
	}

	/**
	 * 获取文件
	 */
	public IFile getFile()
	{
		return file;
	}

	/**
	 * 路径
	 * 
	 * @return
	 */
	public String getPath()
	{
		return innerPath;
	}
	
	public AST getAST()
	{
		return ast;
	}

	/**
	 * 文件引用列表
	 * 
	 * @return
	 */
	public ArrayList<FileRef> getFileRefs()
	{
		return fileRefs;
	}

	/**
	 * ID引用列表
	 * 
	 * @return
	 */
	public ArrayList<IdRef> getIDRefs()
	{
		return idRefs;
	}

	/**
	 * ID定义列表
	 * 
	 * @return
	 */
	public ArrayList<IdDef> getIDDefs()
	{
		return idDefs;
	}

	/**
	 * 查找ID引用
	 * 
	 * @param id
	 * @return
	 */
	public IdDef findIDDef(IdRef ref)
	{
		ArrayList<IdDef> list = null;
		if (ref.isBitmapRef())
		{
			list = bitmapList;
		}
		else if (ref.isBitmapRenderRef())
		{
			list = bitmapRendererList;
		}
		else if (ref.isFilterRef())
		{
			list = filterList;
		}
		else if (ref.isFormatRef())
		{
			list = formatList;
		}
		else if (ref.isTextRef())
		{
			list = textList;
		}
		else if (ref.isControlRef())
		{
			list = controlList;
		}

		for (int i = list.size() - 1; i >= 0; i--)
		{
			IdDef id = list.get(i);
			if (id.getText().equals(ref.getText()))
			{
				return id;
			}
		}

		for (int i = dependRefs.size() - 1; i >= 0; i--)
		{
			FileRef depend = dependRefs.get(i);
			if (depend.getTargetURL() != null && !depend.getTargetURL().isEmpty())
			{
				IFile dependFile = FileAstManager.getSourceFile(file.getProject(), depend.getTargetURL());
				if (dependFile.exists())
				{
					FileAst data = FileAstManager.getFileSummay(dependFile);
					if (data != null)
					{
						IdDef result = data.findIDDef(ref);
						if (result != null)
						{
							return result;
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * 解析
	 * 
	 * @throws CoreException
	 * @throws IOException
	 */
	public void parse() throws CoreException, IOException
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

		RootContext root = parser.root();

		parse(root);
	}

	/**
	 * 解析
	 * 
	 * @param root
	 */
	public void parse(RootContext root)
	{
		dependRefs.clear();
		fileRefs.clear();
		idRefs.clear();
		idDefs.clear();

		bitmapList.clear();
		swfList.clear();
		bitmapRendererList.clear();
		filterList.clear();
		formatList.clear();
		textList.clear();
		controlList.clear();

		bitmapTable.clear();
		swfTable.clear();
		bitmapRendererTable.clear();
		filterTable.clear();
		formatTable.clear();
		textTable.clear();
		controlTable.clear();

		for (ParseTree child : root.children)
		{
			if (child instanceof ComplexNodeContext)
			{
				parseRoot((ComplexNodeContext) child);
				break;
			}
			else if (child instanceof SingleNodeContext)
			{
				break;
			}
		}
		
		ast=new AST(file,root);
	}

	/**
	 * 解析主节点
	 * 
	 * @param root
	 */
	private void parseRoot(ComplexNodeContext root)
	{
		for (ParseTree child : root.children)
		{
			if (child instanceof ComplexNodeContext)
			{
				ComplexNodeContext node = (ComplexNodeContext) child;
				String tagName = node.tagName.getText();
				if (tagName.equals("depends"))
				{
					parseDepends(node);
				}
				else if (tagName.equals("bitmaps"))
				{
					parseBitmaps(node);
				}
				else if (tagName.equals("swfs"))
				{
					parseSwfs(node);
				}
				else if (tagName.equals("bitmapReaders"))
				{
					parseBitmapRenders(node);
				}
				else if (tagName.equals("filters"))
				{
					parseFilters(node);
				}
				else if (tagName.equals("formats"))
				{
					parseFormats(node);
				}
				else if (tagName.equals("texts"))
				{
					parseTexts(node);
				}
				else if (tagName.equals("controls"))
				{
					parseControls(node);
				}
			}
		}
	}

	/**
	 * 解析Depends
	 * 
	 * @param root
	 */
	private void parseDepends(ComplexNodeContext root)
	{
		for (ParseTree child : root.children)
		{
			String tagName = getNodeName(child);
			if (tagName.equals("depend"))
			{
				List<AttributeContext> attributes = getNodeAttributes(child);
				for (AttributeContext attribute : attributes)
				{
					if (attribute.name.getText().equals("src"))
					{
						int start = attribute.value.start.getStartIndex();
						int stop = attribute.value.stop.getStopIndex();
						String text = attribute.value.getText();
						String value = PathUtil.toAbsPath(innerPath, text);
						if (value != null)
						{
							FileRef ref = new FileRef(file, text, start, stop, value);
							dependRefs.add(ref);
							fileRefs.add(ref);
						}
						break;
					}
				}
			}
		}
	}

	/**
	 * 解析swfs
	 * 
	 * @param root
	 */
	private void parseSwfs(ComplexNodeContext root)
	{
		for (ParseTree child : root.children)
		{
			String tagName = getNodeName(child);
			if (tagName.equals("swf"))
			{
				String bitmapID = null;
				int bitmapIDStart = 0;
				int bitmapIDStop = 0;
				FileRef swfRef = null;

				List<AttributeContext> attributes = getNodeAttributes(child);
				for (AttributeContext attribute : attributes)
				{
					String attributeName = attribute.name.getText();

					String txt = attribute.value.getText();
					int start = attribute.value.start.getStartIndex();
					int stop = attribute.value.stop.getStopIndex();
					if (attributeName.equals("id"))
					{
						bitmapID = txt;
						bitmapIDStart = start;
						bitmapIDStop = stop;
					}
					else if (attributeName.equals("src"))
					{
						String value = PathUtil.toAbsPath(innerPath, txt);
						if (value != null)
						{
							swfRef = new FileRef(file, txt, start, stop, value);
						}
					}
				}

				if (bitmapID != null)
				{
					IdDef id = new IdDef(file, bitmapID, bitmapIDStart, bitmapIDStop, swfRef);

					idDefs.add(id);
					swfList.add(id);
					swfTable.put(bitmapID, id);

					if (swfRef != null)
					{
						fileRefs.add(swfRef);
					}
				}
			}
		}
	}

	/**
	 * 解析Bmps
	 * 
	 * @param root
	 */
	private void parseBitmaps(ComplexNodeContext root)
	{
		for (ParseTree child : root.children)
		{
			String tagName = getNodeName(child);
			if (tagName.equals("bitmap"))
			{
				String bitmapID = null;
				int bitmapIDStart = 0;
				int bitmapIDStop = 0;
				FileRef bitmapRef = null;
				IdRef idBitmaRef = null;

				List<AttributeContext> attributes = getNodeAttributes(child);
				for (AttributeContext attribute : attributes)
				{
					String attributeName = attribute.name.getText();

					String txt = attribute.value.getText();
					int start = attribute.value.start.getStartIndex();
					int stop = attribute.value.stop.getStopIndex();
					if (attributeName.equals("id"))
					{
						bitmapID = txt;
						bitmapIDStart = start;
						bitmapIDStop = stop;
					}
					else if (attributeName.equals("src"))
					{
						String value = PathUtil.toAbsPath(innerPath, txt);
						if (value != null)
						{
							bitmapRef = new FileRef(file, txt, start, stop, value);
						}
					}
					else if (attributeName.equals("dependId"))
					{
						idBitmaRef = new IdRef(this, IdRef.REF_Bitmap, txt, start, stop);
						idRefs.add(idBitmaRef);
					}
				}

				if (bitmapID != null)
				{
					if (bitmapRef != null)
					{
						IdDef id = new IdDef(file, bitmapID, bitmapIDStart, bitmapIDStop, bitmapRef);

						idDefs.add(id);
						bitmapList.add(id);
						bitmapTable.put(bitmapID, id);

						fileRefs.add(bitmapRef);
					}
					else if (idBitmaRef != null)
					{
						IdDef id = new IdDef(file, bitmapID, bitmapIDStart, bitmapIDStop, idBitmaRef);

						idDefs.add(id);
						bitmapList.add(id);
						bitmapTable.put(bitmapID, id);

						idRefs.add(idBitmaRef);
					}
					else
					{
						IdDef id = new IdDef(file, bitmapID, bitmapIDStart, bitmapIDStop);

						idDefs.add(id);
						bitmapList.add(id);
						bitmapTable.put(bitmapID, id);
					}
				}
			}
		}
	}

	/**
	 * 解析BitmapRenders
	 * 
	 * @param root
	 */
	private void parseBitmapRenders(ComplexNodeContext root)
	{
		for (ParseTree child : root.children)
		{
			String tagName = getNodeName(child);
			if (tagName.equals("bitmapReader"))
			{
				String bitmapID = null;
				int bitmapIDStart = 0;
				int bitmapIDStop = 0;
				IdRef bitmapRef = null;

				List<AttributeContext> attributes = getNodeAttributes(child);
				for (AttributeContext attribute : attributes)
				{
					String attributeName = attribute.name.getText();

					String txt = attribute.value.getText();
					int start = attribute.value.start.getStartIndex();
					int stop = attribute.value.stop.getStopIndex();
					if (attributeName.equals("id"))
					{
						bitmapID = txt;
						bitmapIDStart = start;
						bitmapIDStop = stop;
					}
					else if (attributeName.equals("bmp"))
					{
						bitmapRef = new IdRef(this, IdRef.REF_Bitmap, txt, start, stop);
					}
				}

				if (bitmapID != null)
				{
					if (bitmapRef != null)
					{
						IdDef id = new IdDef(file, bitmapID, bitmapIDStart, bitmapIDStop, bitmapRef);

						idDefs.add(id);
						bitmapRendererList.add(id);
						bitmapRendererTable.put(bitmapID, id);

						idRefs.add(bitmapRef);
					}
					else
					{
						IdDef id = new IdDef(file, bitmapID, bitmapIDStart, bitmapIDStop);

						idDefs.add(id);
						bitmapRendererList.add(id);
						bitmapRendererTable.put(bitmapID, id);
					}
				}
			}
		}
	}

	/**
	 * 解析Filters
	 * 
	 * @param root
	 */
	private void parseFilters(ComplexNodeContext root)
	{
		for (ParseTree child : root.children)
		{
			String tagName = getNodeName(child);
			if (tagName.equals("filter"))
			{
				List<AttributeContext> attributes = getNodeAttributes(child);
				for (AttributeContext attribute : attributes)
				{
					String txt = attribute.value.getText();
					int start = attribute.value.start.getStartIndex();
					int stop = attribute.value.stop.getStopIndex();

					if (attribute.name.getText().equals("id"))
					{
						IdDef filterID = new IdDef(file, txt, start, stop);
						idDefs.add(filterID);
						filterList.add(filterID);
						filterTable.put(txt, filterID);
						break;
					}
				}
			}
		}
	}

	/**
	 * 解析Formats
	 * 
	 * @param root
	 */
	private void parseFormats(ComplexNodeContext root)
	{
		for (ParseTree child : root.children)
		{
			String tagName = getNodeName(child);
			if (tagName.equals("format"))
			{
				List<AttributeContext> attributes = getNodeAttributes(child);
				for (AttributeContext attribute : attributes)
				{
					String attributeName = attribute.name.getText();
					int start = attribute.value.start.getStartIndex();
					int stop = attribute.value.stop.getStopIndex();
					String txt = attribute.value.getText();

					if (attributeName.equals("id"))
					{
						IdDef formatID = new IdDef(file, txt, start, stop);
						idDefs.add(formatID);
						formatList.add(formatID);
						formatTable.put(txt, formatID);
					}
					else if (attributeName.equals("filter"))
					{
						idRefs.add(new IdRef(this, IdRef.REF_Filter, txt, start, stop));
					}
				}
			}
		}
	}

	/**
	 * 解析Txts
	 * 
	 * @param root
	 */
	private void parseTexts(ComplexNodeContext root)
	{
		for (ParseTree child : root.children)
		{
			String tagName = getNodeName(child);
			if (tagName.equals("text"))
			{
				List<AttributeContext> attributes = getNodeAttributes(child);
				for (AttributeContext attribute : attributes)
				{
					String txt = attribute.value.getText();
					int start = attribute.value.start.getStartIndex();
					int stop = attribute.value.stop.getStopIndex();

					if (attribute.name.getText().equals("id"))
					{
						IdDef textID = new IdDef(file, txt, start, stop);
						idDefs.add(textID);
						textList.add(textID);
						textTable.put(txt, textID);
						break;
					}
				}
			}
		}
	}

	/**
	 * 解析Controls
	 * 
	 * @param root
	 */
	private void parseControls(ComplexNodeContext root)
	{
		for (ParseTree child : root.children)
		{
			List<AttributeContext> attributes = getNodeAttributes(child);
			for (AttributeContext attribute : attributes)
			{
				if (attribute.name.getText().equals("id"))
				{
					String txt = attribute.value.getText();
					int start = attribute.value.start.getStartIndex();
					int stop = attribute.value.stop.getStopIndex();

					IdDef controlID = new IdDef(file, txt, start, stop);
					idDefs.add(controlID);
					controlList.add(controlID);
					controlTable.put(txt, controlID);
					break;
				}
			}

			if (child instanceof ComplexNodeContext)
			{
				parseControl((ComplexNodeContext) child);
			}
		}
	}

	/**
	 * 解析控件内容
	 * 
	 * @param child
	 */
	private void parseControl(ComplexNodeContext node)
	{

		for (ParseTree child : node.children)
		{
			String tagName = getNodeName(child);
			List<AttributeContext> attributes = getNodeAttributes(child);

			findIdRef(tagName, attributes);

			if (child instanceof ComplexNodeContext)
			{
				parseControl((ComplexNodeContext) child);
			}
		}
	}

	/**
	 * 查找ID引用
	 * 
	 * @param tagName
	 * @param attributes
	 */
	private void findIdRef(String tagName, List<AttributeContext> attributes)
	{
		for (AttributeContext attribute : attributes)
		{
			String attributeName = attribute.name.getText();

			int type = getType(tagName, attributeName);
			if (type != 0)
			{
				String txt = attribute.value.getText();
				int start = attribute.value.start.getStartIndex();
				int stop = attribute.value.stop.getStopIndex();

				idRefs.add(new IdRef(this, type, txt, start, stop));
			}
		}
	}

	private static Hashtable<String, Integer> typeTable = null;

	/**
	 * 获取属性类型
	 * 
	 * @param tagName
	 * @param attributeName
	 * @return
	 */
	private static int getType(String tagName, String attributeName)
	{
		if (typeTable == null)
		{
			typeTable = new Hashtable<String, Integer>();

			typeTable.put("ref.ref", IdRef.Ref_Control);

			typeTable.put("image.bmp", IdRef.REF_Bitmap);
			typeTable.put("filmAnimEffect.bmp", IdRef.REF_Bitmap);
			typeTable.put("progressBar.bmp", IdRef.REF_Bitmap);
			typeTable.put("countImage.bmp", IdRef.REF_Bitmap);

			typeTable.put("text.format", IdRef.REF_Format);

			typeTable.put("label.format", IdRef.REF_Format);
			typeTable.put("label.text", IdRef.REF_Text);

			typeTable.put("labelButton.label", IdRef.REF_Text);
			typeTable.put("labelToggleButton.label", IdRef.REF_Text);

			typeTable.put("state.back", IdRef.REF_Bitmap);
			typeTable.put("state.format", IdRef.REF_Format);

			typeTable.put("box.tip", IdRef.REF_Text);
		}

		String key = tagName + "." + attributeName;
		if (typeTable.containsKey(key))
		{
			return typeTable.get(key);
		}

		return 0;
	}

	/**
	 * 获取节点名称
	 * 
	 * @param child
	 * @return
	 */
	private String getNodeName(ParseTree child)
	{
		if (child instanceof SingleNodeContext)
		{
			return ((SingleNodeContext) child).tagName.getText();
		}
		else if (child instanceof ComplexNodeContext)
		{
			return ((ComplexNodeContext) child).tagName.getText();
		}
		return "";
	}

	/**
	 * 获取节点属性
	 * 
	 * @param child
	 * @return
	 */
	private List<AttributeContext> getNodeAttributes(ParseTree child)
	{
		if (child instanceof SingleNodeContext)
		{
			return ((SingleNodeContext) child).attribute();
		}
		else if (child instanceof ComplexNodeContext)
		{
			return ((ComplexNodeContext) child).attribute();
		}
		return new ArrayList<XmlParser.AttributeContext>();
	}
}
