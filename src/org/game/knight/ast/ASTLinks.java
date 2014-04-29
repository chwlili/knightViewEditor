package org.game.knight.ast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class ASTLinks
{
	private IFile file;
	private String innerPath;
	private ArrayList<Object> children;
	private AbsTag root = null;

	private ArrayList<IdDef> idDefs = new ArrayList<IdDef>();
	private ArrayList<IdRef> idRefs = new ArrayList<IdRef>();
	private ArrayList<FileRef> fileRefs = new ArrayList<FileRef>();

	private ArrayList<FileRef> depends = new ArrayList<FileRef>();
	private ArrayList<IdDef> bimaps = new ArrayList<IdDef>();
	private ArrayList<IdDef> bitmapRenders = new ArrayList<IdDef>();
	private ArrayList<IdDef> swfs = new ArrayList<IdDef>();
	private ArrayList<IdDef> filters = new ArrayList<IdDef>();
	private ArrayList<IdDef> formats = new ArrayList<IdDef>();
	private ArrayList<IdDef> texts = new ArrayList<IdDef>();
	private ArrayList<IdDef> controls = new ArrayList<IdDef>();

	public ASTLinks(IFile file, ArrayList<Object> children)
	{
		this.file = file;
		this.children = children;
		this.innerPath = ASTManager.getFilePath(file);

		initalizeRoot();
		initalizeParts();
	}

	/**
	 * ��ȡ�ļ�
	 * 
	 * @return
	 */
	public IFile getFile()
	{
		return file;
	}

	/**
	 * ��ȡ�ļ������б�
	 * 
	 * @return
	 */
	public ArrayList<FileRef> getFileRefs()
	{
		return fileRefs;
	}

	/**
	 * ��ȡID�����б�
	 * 
	 * @return
	 */
	public ArrayList<IdRef> getIdRefs()
	{
		return idRefs;
	}

	/**
	 * ��ȡID����
	 * 
	 * @param offset
	 * @return
	 */
	public IdDef getIdDef(int offset)
	{
		for (IdDef def : idDefs)
		{
			if (def.getStart() <= offset && offset <= def.getStop())
			{
				return def;
			}
		}
		return null;
	}

	/**
	 * ��ȡID����
	 * 
	 * @param offset
	 * @return
	 */
	public IdRef getIdRef(int offset)
	{
		for (IdRef ref : idRefs)
		{
			if (ref.getStart() <= offset && offset <= ref.getStop())
			{
				return ref;
			}
		}
		return null;
	}

	/**
	 * ��ȡ�ļ�����
	 * 
	 * @param offset
	 * @return
	 */
	public FileRef getFileRef(int offset)
	{
		for (FileRef ref : fileRefs)
		{
			if (ref.getStart() <= offset && offset <= ref.getStop())
			{
				return ref;
			}
		}
		return null;
	}

	/**
	 * ����ID����
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 * @throws CoreException
	 */
	public IdDef findIDDef(IdRef ref) throws CoreException, IOException
	{
		ArrayList<IdDef> list = null;
		if (ref.isBitmapRef())
		{
			list = bimaps;
		}
		else if (ref.isBitmapRenderRef())
		{
			list = bitmapRenders;
		}
		else if (ref.isFilterRef())
		{
			list = filters;
		}
		else if (ref.isFormatRef())
		{
			list = formats;
		}
		else if (ref.isTextRef())
		{
			list = texts;
		}
		else if (ref.isControlRef())
		{
			list = controls;
		}

		for (int i = list.size() - 1; i >= 0; i--)
		{
			IdDef id = list.get(i);
			if (id.getText().equals(ref.getText()))
			{
				return id;
			}
		}

		for (int i = depends.size() - 1; i >= 0; i--)
		{
			FileRef depend = depends.get(i);
			if (depend.getTargetURL() != null && !depend.getTargetURL().isEmpty())
			{
				IFile dependFile = ASTManager.getSourceFile(file.getProject(), depend.getTargetURL());
				if (dependFile.exists())
				{
					AST data = ASTManager.getAST(dependFile);
					if (data != null)
					{
						IdDef result = data.getLinks().findIDDef(ref);
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
	 * ��ʼ�����ڵ�
	 */
	private void initalizeRoot()
	{
		for (Object child : children)
		{
			if (((AbsTag) child).getChildren() != null)
			{
				root = (AbsTag) child;
				break;
			}
		}
	}

	/**
	 * ��ʼ������
	 */
	private void initalizeParts()
	{
		if (root == null)
		{
			return;
		}

		for (Object child : root.getChildren())
		{
			if (!(child instanceof AbsTag))
			{
				continue;
			}

			AbsTag tag = (AbsTag) child;
			if (tag.getChildren() != null)
			{
				String tagName = tag.getName();
				if (tagName.equals("depends"))
				{
					initalizeDepends(tag);
				}
				else if (tagName.equals("bitmaps"))
				{
					initalizeBitmaps(tag);
				}
				else if (tagName.equals("bitmapReaders"))
				{
					initalizeBitmapRenders(tag);
				}
				else if (tagName.equals("swfs"))
				{
					initalizeSwfs(tag);
				}
				else if (tagName.equals("filters"))
				{
					initalizeFilters(tag);
				}
				else if (tagName.equals("formats"))
				{
					initalizeFormats(tag);
				}
				else if (tagName.equals("texts"))
				{
					initalizeTexts(tag);
				}
				else if (tagName.equals("controls"))
				{
					initalizeControls(tag);
				}
			}
		}
	}

	// depends
	private void initalizeDepends(AbsTag tag)
	{
		for (Object child : tag.getChildren())
		{
			if (!(child instanceof AbsTag))
			{
				continue;
			}

			AbsTag curr = (AbsTag) child;
			if (!curr.getName().equals("depend"))
			{
				continue;
			}

			// ȡ��src����
			Attribute srcAttribute = curr.getAttribute("src");
			if (srcAttribute == null)
			{
				System.err.println("depend�ڵ�û��src����!");
				continue;
			}

			// ȡ��src����ֵ
			String srcValue = srcAttribute.getValue();
			if (srcValue.trim().isEmpty())
			{
				System.err.println("depend�ڵ��src���Բ���Ϊ��!");
				continue;
			}

			// ת��src����
			String destURL = PathUtil.toAbsPath(innerPath, srcValue);
			if (destURL == null)
			{
				System.err.println("depend�ڵ��src�������õ��ļ�δ�ҵ�!");
				continue;
			}

			// ��¼�ļ�����
			FileRef fileRef = new FileRef(file, srcValue, srcAttribute.getValueToken().start, srcAttribute.getValueToken().stop, destURL);
			fileRefs.add(fileRef);

			// ��¼depend
			depends.add(fileRef);
		}
	}

	// bitmaps
	private void initalizeBitmaps(AbsTag tag)
	{
		for (Object child : tag.getChildren())
		{
			if (!(child instanceof AbsTag))
			{
				continue;
			}

			AbsTag curr = (AbsTag) child;
			if (!curr.getName().equals("bitmap"))
			{
				continue;
			}

			// ȡ��ID����
			String idValue = null;
			Attribute idAttribute = curr.getAttribute("id");
			if (idAttribute != null)
			{
				idValue = idAttribute.getValue();
			}

			// ���ID����
			if (idAttribute == null)
			{
				System.err.println("bitmap�ڵ�û��id����!");
			}
			else if (idValue.trim().isEmpty())
			{
				System.err.println("bitmap�ڵ��id���Բ���Ϊ��!");
			}

			// ȡ��dependId����
			String dependValue = null;
			Attribute dependAttribute = curr.getAttribute("dependId");
			if (dependAttribute != null)
			{
				dependValue = dependAttribute.getValue();
			}

			// ȡ��src����
			String srcValue = null;
			Attribute srcAttribute = curr.getAttribute("src");
			if (srcAttribute != null)
			{
				srcValue = srcAttribute.getValue();
			}

			// ���dependId��src����
			if (dependAttribute != null)
			{
				if (dependValue.trim().isEmpty())
				{
					System.err.println("bitmap�ڵ��dependId���Բ���Ϊ��!");
				}
			}
			else if (srcAttribute != null)
			{
				if (srcValue.trim().isEmpty())
				{
					System.err.println("bitmap�ڵ��src���Բ���Ϊ��!");
				}
			}
			else
			{
				System.err.println("bitmap�ڵ��û��dependId����Ҳû��src����!");
			}

			// ת��dependId����
			String dependURL = null;
			IdRef dependID = null;
			if (dependValue != null)
			{
				dependURL = PathUtil.toAbsPath(innerPath, dependValue);
				if (dependURL == null || dependURL.trim().isEmpty())
				{
					System.err.println("bitmap�ڵ��û��dependId���������˲����ڵ��ļ�!");
				}
				else
				{
					dependID = new IdRef(this, IdRef.REF_Bitmap, dependValue, dependAttribute.getValueToken().start, dependAttribute.getValueToken().stop);
					idRefs.add(dependID);
				}
			}

			// ת��src����
			String srcURL = null;
			FileRef dependFile = null;
			if (srcValue != null)
			{
				srcURL = PathUtil.toAbsPath(innerPath, srcValue);
				if (srcURL == null || srcURL.trim().isEmpty())
				{
					System.err.println("bitmap�ڵ��û��src���������˲����ڵ��ļ�!");
				}
				else
				{
					dependFile = new FileRef(file, srcValue, srcAttribute.getValueToken().start, srcAttribute.getValueToken().stop, srcURL);
					fileRefs.add(dependFile);
				}
			}

			if (idValue != null)
			{
				IdDef def = null;
				if (dependID != null)
				{
					def = new IdDef(file, idValue, idAttribute.getValueToken().start, idAttribute.getValueToken().stop, dependID);
				}
				else if (dependFile != null)
				{
					def = new IdDef(file, idValue, idAttribute.getValueToken().start, idAttribute.getValueToken().stop, dependFile);
				}
				else
				{
					def = new IdDef(file, idValue, idAttribute.getValueToken().start, idAttribute.getValueToken().stop);
				}

				idDefs.add(def);
				bimaps.add(def);
			}
		}
	}

	// bitmapRenders
	private void initalizeBitmapRenders(AbsTag tag)
	{
		for (Object child : tag.getChildren())
		{
			if (!(child instanceof AbsTag))
			{
				continue;
			}

			AbsTag curr = (AbsTag) child;
			if (!curr.getName().equals("bitmapReader"))
			{
				continue;
			}

			// ȡ��id����
			String idValue = null;
			Attribute idAttribute = curr.getAttribute("id");
			if (idAttribute != null)
			{
				idValue = idAttribute.getValue();
			}

			// ���id����
			if (idAttribute == null)
			{
				System.err.println("bitmapReader�ڵ�û��id����!");
			}
			else if (idValue.trim().isEmpty())
			{
				System.err.println("bitmapReader�ڵ��id���Բ���Ϊ��!");
			}

			// ȡ��bmp����
			String bmpValue = null;
			Attribute bmpAttribute = curr.getAttribute("bmp");
			if (bmpAttribute != null)
			{
				bmpValue = bmpAttribute.getValue();
			}

			// ���bmp����
			if (bmpAttribute == null)
			{
				System.err.println("bitmapReader�ڵ�û��bmp����!");
			}
			else if (bmpValue.trim().isEmpty())
			{
				System.err.println("bitmapReader�ڵ��bmp���Բ���Ϊ��!");
			}

			// ��¼ID����
			IdRef bmpRef = null;
			if (bmpValue != null)
			{
				bmpRef = new IdRef(this, IdRef.REF_Bitmap, bmpValue, bmpAttribute.getValueToken().start, bmpAttribute.getValueToken().stop);
				idRefs.add(bmpRef);
			}

			// ��¼ID����
			if (idValue != null)
			{
				IdDef id = new IdDef(file, idValue, idAttribute.getValueToken().start, idAttribute.getValueToken().stop, bmpRef);
				idDefs.add(id);
				bitmapRenders.add(id);
			}
		}
	}

	// swfs
	private void initalizeSwfs(AbsTag tag)
	{
		for (Object child : tag.getChildren())
		{
			if (!(child instanceof AbsTag))
			{
				continue;
			}

			AbsTag curr = (AbsTag) child;
			if (!curr.getName().equals("swf"))
			{
				continue;
			}

			// ȡ��id����
			String idValue = null;
			Attribute idAttribute = curr.getAttribute("id");
			if (idAttribute != null)
			{
				idValue = idAttribute.getValue();
			}

			// ���id����
			if (idAttribute == null)
			{
				System.err.println("swf�ڵ�û��id����!");
			}
			else if (idValue.trim().isEmpty())
			{
				System.err.println("swf�ڵ��id���Բ���Ϊ��!");
			}

			// ȡ��src����
			String srcValue = null;
			Attribute srcAttribute = curr.getAttribute("src");
			if (srcAttribute != null)
			{
				srcValue = srcAttribute.getValue();
			}

			// ���src����
			if (srcAttribute == null)
			{
				System.err.println("swf�ڵ�û��src����!");
			}
			else if (srcValue.trim().isEmpty())
			{
				System.err.println("swf�ڵ��src���Բ���Ϊ��!");
			}

			// ת��src����
			String destURL = null;
			if (srcValue != null)
			{
				destURL = PathUtil.toAbsPath(innerPath, srcValue);
				if (destURL == null)
				{
					System.err.println("swf�ڵ��src�������õ��ļ�δ�ҵ�!");
				}
			}

			// ��¼�ļ�����
			FileRef fileRef = null;
			if (destURL != null)
			{
				fileRef = new FileRef(file, srcValue, srcAttribute.getValueToken().start, srcAttribute.getValueToken().stop, destURL);
				fileRefs.add(fileRef);
			}

			// ��¼ID����
			if (idValue != null)
			{
				IdDef id = new IdDef(file, idValue, idAttribute.getValueToken().start, idAttribute.getValueToken().stop, fileRef);
				idDefs.add(id);

				swfs.add(id);
			}
		}
	}

	// filters
	private void initalizeFilters(AbsTag tag)
	{
		for (Object child : tag.getChildren())
		{
			if (!(child instanceof AbsTag))
			{
				continue;
			}

			AbsTag curr = (AbsTag) child;
			if (!curr.getName().equals("filter"))
			{
				continue;
			}

			// ȡ��id����
			String idValue = null;
			Attribute idAttribute = curr.getAttribute("id");
			if (idAttribute != null)
			{
				idValue = idAttribute.getValue();
			}

			// ���id����
			if (idAttribute == null)
			{
				System.err.println("filter�ڵ�û��id����!");
			}
			else if (idValue.trim().isEmpty())
			{
				System.err.println("filter�ڵ��id���Բ���Ϊ��!");
			}

			// ��¼ID����
			if (idValue != null)
			{
				IdDef id = new IdDef(file, idValue, idAttribute.getValueToken().start, idAttribute.getValueToken().stop);
				idDefs.add(id);
				filters.add(id);
			}
		}
	}

	// formats
	private void initalizeFormats(AbsTag tag)
	{
		for (Object child : tag.getChildren())
		{
			if (!(child instanceof AbsTag))
			{
				continue;
			}

			AbsTag curr = (AbsTag) child;
			if (!curr.getName().equals("format"))
			{
				continue;
			}

			// ȡ��id����
			String idValue = null;
			Attribute idAttribute = curr.getAttribute("id");
			if (idAttribute != null)
			{
				idValue = idAttribute.getValue();
			}

			// ���id����
			if (idAttribute == null)
			{
				System.err.println("format�ڵ�û��id����!");
			}
			else if (idValue.trim().isEmpty())
			{
				System.err.println("format�ڵ��id���Բ���Ϊ��!");
			}

			// ȡ��filter����
			String filterValue = null;
			Attribute filterAttribute = curr.getAttribute("filter");
			if (filterAttribute != null)
			{
				filterValue = filterAttribute.getValue();
			}

			// ���filter����
			if (filterAttribute != null && filterValue.trim().isEmpty())
			{
				System.err.println("format�ڵ��filter���Բ���Ϊ��!");
			}

			// ��¼ID����
			IdRef filterRef = null;
			if (filterValue != null)
			{
				filterRef = new IdRef(this, IdRef.REF_Filter, filterValue, filterAttribute.getValueToken().start, filterAttribute.getValueToken().stop);
				idRefs.add(filterRef);
			}

			// ��¼ID����
			if (idValue != null)
			{
				IdDef id = new IdDef(file, idValue, idAttribute.getValueToken().start, idAttribute.getValueToken().stop, filterRef);
				idDefs.add(id);
				formats.add(id);
			}
		}
	}

	// texts
	private void initalizeTexts(AbsTag tag)
	{
		for (Object child : tag.getChildren())
		{
			if (!(child instanceof AbsTag))
			{
				continue;
			}

			AbsTag curr = (AbsTag) child;
			if (!curr.getName().equals("text"))
			{
				continue;
			}

			// ȡ��id����
			String idValue = null;
			Attribute idAttribute = curr.getAttribute("id");
			if (idAttribute != null)
			{
				idValue = idAttribute.getValue();
			}

			// ���id����
			if (idAttribute == null)
			{
				System.err.println("text�ڵ�û��id����!");
			}
			else if (idValue.trim().isEmpty())
			{
				System.err.println("text�ڵ��id���Բ���Ϊ��!");
			}

			// ��¼ID����
			if (idValue != null)
			{
				IdDef id = new IdDef(file, idValue, idAttribute.getValueToken().start, idAttribute.getValueToken().stop);
				idDefs.add(id);
				texts.add(id);
			}
		}
	}

	// controls
	private void initalizeControls(AbsTag tag)
	{
		for (Object child : tag.getChildren())
		{
			if (!(child instanceof AbsTag))
			{
				continue;
			}

			AbsTag curr = (AbsTag) child;

			// ȡ��id����
			String idValue = null;
			Attribute idAttribute = curr.getAttribute("id");
			if (idAttribute != null)
			{
				idValue = idAttribute.getValue();
			}

			// ���id����
			if (idAttribute == null)
			{
				System.err.println(curr.getName() + "�ڵ�û��id����!");
			}
			else if (idValue.trim().isEmpty())
			{
				System.err.println(curr.getName() + "�ڵ��id���Բ���Ϊ��!");
			}

			// ��¼ID����
			if (idValue != null)
			{
				IdDef id = new IdDef(file, idValue, idAttribute.getValueToken().start, idAttribute.getValueToken().stop);
				idDefs.add(id);
				controls.add(id);
			}

			findIdRef(curr);
		}
	}

	// ����ID����
	private void findIdRef(AbsTag tag)
	{
		for (Attribute attribute : tag.getAttributes())
		{
			String tagName = tag.getName();
			String attName = attribute.getName();
			int refType = getType(tagName, attName);
			if (refType != 0)
			{
				idRefs.add(new IdRef(this, refType, attribute.getValue(), attribute.getValueToken().start, attribute.getValueToken().stop));
			}
		}

		if (tag instanceof AbsTag)
		{
			AbsTag box = (AbsTag) tag;
			if (box.getChildren() != null)
			{
				for (Object child : box.getChildren())
				{
					if (child instanceof AbsTag)
					{
						findIdRef((AbsTag) child);
					}
				}
			}
		}
	}

	private static Hashtable<String, Integer> typeTable = null;

	/**
	 * ��ȡ��������
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
}
