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

			//ȡ��src����
			TagAttribute srcAttribute = curr.getAttributeToken("src");
			if (srcAttribute == null)
			{
				System.err.println("depend�ڵ�û��src����!");
				continue;
			}

			//ȡ��src����ֵ
			String srcValue = srcAttribute.getValue();
			if (srcValue.trim().isEmpty())
			{
				System.err.println("depend�ڵ��src���Բ���Ϊ��!");
				continue;
			}

			//ת��src����
			String destURL = PathUtil.toAbsPath(innerPath, srcValue);
			if (destURL == null)
			{
				System.err.println("depend�ڵ��src�������õ��ļ�δ�ҵ�!");
				continue;
			}
			
			//��¼�ļ�����
			FileRef fileRef = new FileRef(file, srcValue, srcAttribute.getStartToken().start, srcAttribute.getStopToken().stop, destURL);
			fileRefs.add(fileRef);

			//��¼depend
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

			//ȡ��ID����
			String idValue = null;
			TagAttribute idAttribute = curr.getAttributeToken("id");
			if(idAttribute!=null)
			{
				idValue = idAttribute.getValue();
			}
			
			//���ID����
			if (idAttribute == null)
			{
				System.err.println("bitmap�ڵ�û��id����!");
			}
			else if (idValue.trim().isEmpty())
			{
				System.err.println("bitmap�ڵ��id���Բ���Ϊ��!");
			}

			//ȡ��dependId����
			String dependValue = null;
			TagAttribute dependAttribute = curr.getAttributeToken("dependId");
			if(dependAttribute!=null)
			{
				dependValue = dependAttribute.getValue();
			}
			
			//ȡ��src����
			String srcValue = null;
			TagAttribute srcAttribute = curr.getAttributeToken("src");
			if(srcAttribute!=null)
			{
				srcValue = srcAttribute.getValue();
			}
			
			//���dependId��src����
			if(dependAttribute!=null)
			{
				if(dependValue.trim().isEmpty())
				{
					System.err.println("bitmap�ڵ��dependId���Բ���Ϊ��!");
				}
			}
			else if(srcAttribute!=null)
			{
				if(srcValue.trim().isEmpty())
				{
					System.err.println("bitmap�ڵ��src���Բ���Ϊ��!");
				}
			}
			else
			{
				System.err.println("bitmap�ڵ��û��dependId����Ҳû��src����!");
			}
			
			//ת��dependId����
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
					System.err.println("bitmap�ڵ��dependId���Բ���Ϊ��!");
				}
				idRef = new IdRef(null, IdRef.REF_Bitmap, dependValue, dependAttribute.getStartToken().start, dependAttribute.getStopToken().stop);
				idRefs.add(idRef);
			}

			if (srcAttribute != null)
			{
				String srcValue = srcAttribute.getValue();
				if (srcValue.trim().isEmpty())
				{
					System.err.println("bitmap�ڵ��src���Բ���Ϊ��!");
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
			
			//ȡ��id����
			String idValue = null;
			TagAttribute idAttribute = curr.getAttributeToken("id");
			if (idAttribute != null)
			{
				idValue=idAttribute.getValue();
			}

			//���id����
			if(idAttribute==null)
			{
				System.err.println("swf�ڵ�û��id����!");
			}
			else if (idValue.trim().isEmpty())
			{
				System.err.println("swf�ڵ��id���Բ���Ϊ��!");
			}
			
			//ȡ��src����
			String srcValue = null;
			TagAttribute srcAttribute = curr.getAttributeToken("src");
			if (srcAttribute != null)
			{
				srcValue=srcAttribute.getValue();
			}
			
			//���src����
			if(srcAttribute==null)
			{
				System.err.println("swf�ڵ�û��src����!");
			}
			else if (srcValue.trim().isEmpty())
			{
				System.err.println("swf�ڵ��src���Բ���Ϊ��!");
			}

			//ת��src����
			String destURL = null;
			if (srcValue != null)
			{
				destURL=PathUtil.toAbsPath(innerPath, srcValue);
				if(destURL==null)
				{
					System.err.println("swf�ڵ��src�������õ��ļ�δ�ҵ�!");
				}
			}

			//��¼�ļ�����
			FileRef fileRef = null; 
			if(destURL!=null)
			{
				fileRef = new FileRef(file, srcValue, srcAttribute.getStartToken().start, srcAttribute.getStopToken().stop, destURL);
				fileRefs.add(fileRef);
			}

			//��¼ID����
			if(idValue!=null)
			{
				IdDef id = new IdDef(file, idValue, idAttribute.getStartToken().start, idAttribute.getStopToken().stop, fileRef);
				idDefs.add(id);
				
				swfs.add(id);
			}
		}
	}
}
