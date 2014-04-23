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
	 * ��ȡ����б�
	 * 
	 * @return
	 */
	public ArrayList<Token> getTokens()
	{
		return tokens;
	}

	/**
	 * ��ʼ�����ڵ�
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
	private ArrayList<IdDef> bimaps=new ArrayList<IdDef>();
	
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

			TagAttribute srcAttribute = curr.getAttributeToken("src");
			if (srcAttribute == null)
			{
				System.err.println("depend�ڵ�û��src����!");
				continue;
			}

			String srcValue = srcAttribute.getValue();
			if (srcValue.trim().isEmpty())
			{
				System.err.println("depend�ڵ��src���Բ���Ϊ��!");
				continue;
			}

			String destURL = PathUtil.toAbsPath(innerPath, srcValue);
			if (destURL == null)
			{
				System.err.println("depend�ڵ��src�������õ��ļ�δ�ҵ�!");
				continue;
			}

			FileRef fileRef = new FileRef(file, srcValue, srcAttribute.getStartToken().start, srcAttribute.getStopToken().stop, destURL);
			depends.add(fileRef);
			fileRefs.add(fileRef);
		}
	}

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

			TagAttribute idAttribute = curr.getAttributeToken("id");
			if (idAttribute == null)
			{
				System.err.println("bitmap�ڵ�û��id����!");
				continue;
			}

			String idValue = idAttribute.getValue();
			if (idValue.trim().isEmpty())
			{
				System.err.println("bitmap�ڵ��id���Բ���Ϊ��!");
				continue;
			}

			TagAttribute dependAttribute = curr.getAttributeToken("dependId");
			TagAttribute srcAttribute = curr.getAttributeToken("src");
			if (dependAttribute == null && srcAttribute == null)
			{
				System.err.println("bitmap�ڵ�û��src����Ҳû��dependId����!");
				continue;
			}

			IdDef idDef = null;
			IdRef idRef = null;
			FileRef fileRef=null;
			
			if (dependAttribute != null)
			{
				String dependValue = dependAttribute.getValue();
				if (dependValue.trim().isEmpty())
				{
					System.err.println("bitmap�ڵ��dependId���Բ���Ϊ��!");
				}
				idRef = new IdRef(null, IdRef.REF_Bitmap, dependValue, dependAttribute.getStartToken().start, dependAttribute.getStopToken().stop);
				idRefs.add(idRef);
			}
			
			if(srcAttribute!=null)
			{
				String srcValue = srcAttribute.getValue();
				if(srcValue.trim().isEmpty())
				{
					System.err.println("bitmap�ڵ��src���Բ���Ϊ��!");
				}
				String destURL = PathUtil.toAbsPath(innerPath, srcValue);
				
				fileRef = new FileRef(file, srcValue, dependAttribute.getStartToken().start, dependAttribute.getStopToken().stop, destURL);
				fileRefs.add(fileRef);
			}
			
			if(idRef!=null)
			{
				idDef=new IdDef(file, idValue, idAttribute.getStartToken().start, idAttribute.getStopToken().stop, idRef);
			}
			else if(fileRef!=null)
			{
				idDef=new IdDef(file, idValue, idAttribute.getStartToken().start, idAttribute.getStopToken().stop, fileRef);
			}
			
			idDefs.add(idDef);
			bimaps.add(idDef);
		}
	}
	
	private void initalizeSwfs(ComplexTag tag)
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

			TagAttribute idAttribute = curr.getAttributeToken("id");
			if (idAttribute == null)
			{
				System.err.println("bitmap�ڵ�û��id����!");
				continue;
			}

			String idValue = idAttribute.getValue();
			if (idValue.trim().isEmpty())
			{
				System.err.println("bitmap�ڵ��id���Բ���Ϊ��!");
				continue;
			}
		}
	}
}
