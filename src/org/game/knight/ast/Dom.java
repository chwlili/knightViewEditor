package org.game.knight.ast;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.game.knight.ast.AST.Token;

public class Dom
{
	private IFile file;
	private String innerPath;
	private ArrayList<Token> tokens;
	private ArrayList<Object> children;
	private ComplexTag root = null;

	public Dom(IFile file, ArrayList<Token> tokens, ArrayList<Object> children)
	{
		this.file = file;
		this.tokens = tokens;
		this.children = children;
		this.innerPath = ASTManager.getFilePath(file);

		initalizeRoot();
		initalizeParts();
	}

	/**
	 * 获取标记列表
	 * 
	 * @return
	 */
	public ArrayList<Token> getTokens()
	{
		return tokens;
	}

	/**
	 * 初始化根节点
	 */
	private void initalizeRoot()
	{
		for (Object obj : children)
		{
			if (obj instanceof Tag)
			{
				return;
			}

			if (obj instanceof ComplexTag)
			{
				root = (ComplexTag) obj;
				break;
			}
		}
	}

	/**
	 * 初始化区段
	 */
	private void initalizeParts()
	{
		if (root == null)
		{
			return;
		}

		for (Object child : root.getChildren())
		{
			if (child instanceof ComplexTag)
			{
				ComplexTag tag = (ComplexTag) child;

				String tagName = tag.getName();
				if (tagName.equals("depends"))
				{
					initalizeDepends(tag);
				}
				else if (tagName.equals("bitmaps"))
				{
					initalizeBitmaps(tag);
				}
				else if (tagName.equals("swfs"))
				{
					initalizeSwfs(tag);
				}
				else if (tagName.equals("bitmapReaders"))
				{
				}
				else if (tagName.equals("filters"))
				{
				}
				else if (tagName.equals("formats"))
				{
				}
				else if (tagName.equals("texts"))
				{
				}
				else if (tagName.equals("controls"))
				{
				}
			}
		}
	}

	private ArrayList<IdDef> idDefs = new ArrayList<IdDef>();
	private ArrayList<IdRef> idRefs = new ArrayList<IdRef>();
	private ArrayList<FileRef> fileRefs = new ArrayList<FileRef>();

	private ArrayList<FileRef> depends = new ArrayList<FileRef>();
	private ArrayList<IdDef> bimaps = new ArrayList<IdDef>();
	private ArrayList<IdDef> swfs = new ArrayList<IdDef>();

	//depends
	private void initalizeDepends(ComplexTag tag)
	{
		for (Object child : tag.getChildren())
		{
			if (!(child instanceof Tag))
			{
				continue;
			}

			Tag curr = (Tag) child;
			if (!curr.getName().equals("depend"))
			{
				continue;
			}

			//取出src属性
			TagAttribute srcAttribute = curr.getAttributeToken("src");
			if (srcAttribute == null)
			{
				System.err.println("depend节点没有src属性!");
				continue;
			}

			//取出src属性值
			String srcValue = srcAttribute.getValue();
			if (srcValue.trim().isEmpty())
			{
				System.err.println("depend节点的src属性不能为空!");
				continue;
			}

			//转换src属性
			String destURL = PathUtil.toAbsPath(innerPath, srcValue);
			if (destURL == null)
			{
				System.err.println("depend节点的src属性引用的文件未找到!");
				continue;
			}
			
			//记录文件引用
			FileRef fileRef = new FileRef(file, srcValue, srcAttribute.getStartToken().start, srcAttribute.getStopToken().stop, destURL);
			fileRefs.add(fileRef);

			//记录depend
			depends.add(fileRef);
		}
	}

	//bitmaps
	private void initalizeBitmaps(ComplexTag tag)
	{
		for (Object child : tag.getChildren())
		{
			if (!(child instanceof Tag))
			{
				continue;
			}

			Tag curr = (Tag) child;
			if (!curr.getName().equals("bitmap"))
			{
				continue;
			}

			//取出ID属性
			String idValue = null;
			TagAttribute idAttribute = curr.getAttributeToken("id");
			if(idAttribute!=null)
			{
				idValue = idAttribute.getValue();
			}
			
			//检查ID属性
			if (idAttribute == null)
			{
				System.err.println("bitmap节点没有id属性!");
			}
			else if (idValue.trim().isEmpty())
			{
				System.err.println("bitmap节点的id属性不能为空!");
			}

			//取出dependId属性
			String dependValue = null;
			TagAttribute dependAttribute = curr.getAttributeToken("dependId");
			if(dependAttribute!=null)
			{
				dependValue = dependAttribute.getValue();
			}
			
			//取出src属性
			String srcValue = null;
			TagAttribute srcAttribute = curr.getAttributeToken("src");
			if(srcAttribute!=null)
			{
				srcValue = srcAttribute.getValue();
			}
			
			//检查dependId和src属性
			if(dependAttribute!=null)
			{
				if(dependValue.trim().isEmpty())
				{
					System.err.println("bitmap节点的dependId属性不能为空!");
				}
			}
			else if(srcAttribute!=null)
			{
				if(srcValue.trim().isEmpty())
				{
					System.err.println("bitmap节点的src属性不能为空!");
				}
			}
			else
			{
				System.err.println("bitmap节点的没有dependId属性也没有src属性!");
			}
			
			//转换dependId属性
			String dependURL=null;
			if(dependValue!=null)
			{
				
			}
			
			FileRef fileRef = null; 
			if(destURL!=null)
			{
				fileRef = new FileRef(file, srcValue, srcAttribute.getStartToken().start, srcAttribute.getStopToken().stop, destURL);
				fileRefs.add(fileRef);
			}
			
			IdDef idDef = null;
			IdRef idRef = null;
			FileRef fileRef = null;

			if (dependAttribute != null)
			{
				dependValue = dependAttribute.getValue();
				if (dependValue.trim().isEmpty())
				{
					System.err.println("bitmap节点的dependId属性不能为空!");
				}
				idRef = new IdRef(null, IdRef.REF_Bitmap, dependValue, dependAttribute.getStartToken().start, dependAttribute.getStopToken().stop);
				idRefs.add(idRef);
			}

			if (srcAttribute != null)
			{
				String srcValue = srcAttribute.getValue();
				if (srcValue.trim().isEmpty())
				{
					System.err.println("bitmap节点的src属性不能为空!");
				}
				String destURL = PathUtil.toAbsPath(innerPath, srcValue);

				fileRef = new FileRef(file, srcValue, dependAttribute.getStartToken().start, dependAttribute.getStopToken().stop, destURL);
				fileRefs.add(fileRef);
			}

			if (idRef != null)
			{
				idDef = new IdDef(file, idValue, idAttribute.getStartToken().start, idAttribute.getStopToken().stop, idRef);
			}
			else if (fileRef != null)
			{
				idDef = new IdDef(file, idValue, idAttribute.getStartToken().start, idAttribute.getStopToken().stop, fileRef);
			}
			else
			{
				idDef = new IdDef(file, idValue, idAttribute.getStartToken().start, idAttribute.getStopToken().stop);
			}

			idDefs.add(idDef);
			bimaps.add(idDef);
		}
	}

	//swfs
	private void initalizeSwfs(ComplexTag tag)
	{
		for (Object child : tag.getChildren())
		{
			if (!(child instanceof Tag))
			{
				continue;
			}

			Tag curr = (Tag) child;
			if (!curr.getName().equals("swf"))
			{
				continue;
			}
			
			//取出id属性
			String idValue = null;
			TagAttribute idAttribute = curr.getAttributeToken("id");
			if (idAttribute != null)
			{
				idValue=idAttribute.getValue();
			}

			//检查id属性
			if(idAttribute==null)
			{
				System.err.println("swf节点没有id属性!");
			}
			else if (idValue.trim().isEmpty())
			{
				System.err.println("swf节点的id属性不能为空!");
			}
			
			//取出src属性
			String srcValue = null;
			TagAttribute srcAttribute = curr.getAttributeToken("src");
			if (srcAttribute != null)
			{
				srcValue=srcAttribute.getValue();
			}
			
			//检查src属性
			if(srcAttribute==null)
			{
				System.err.println("swf节点没有src属性!");
			}
			else if (srcValue.trim().isEmpty())
			{
				System.err.println("swf节点的src属性不能为空!");
			}

			//转换src属性
			String destURL = null;
			if (srcValue != null)
			{
				destURL=PathUtil.toAbsPath(innerPath, srcValue);
				if(destURL==null)
				{
					System.err.println("swf节点的src属性引用的文件未找到!");
				}
			}

			//记录文件引用
			FileRef fileRef = null; 
			if(destURL!=null)
			{
				fileRef = new FileRef(file, srcValue, srcAttribute.getStartToken().start, srcAttribute.getStopToken().stop, destURL);
				fileRefs.add(fileRef);
			}

			//记录ID定义
			if(idValue!=null)
			{
				IdDef id = new IdDef(file, idValue, idAttribute.getStartToken().start, idAttribute.getStopToken().stop, fileRef);
				idDefs.add(id);
				
				swfs.add(id);
			}
		}
	}
}
