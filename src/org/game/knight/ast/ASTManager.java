package org.game.knight.ast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.game.knight.search.SearchMatch;
import org.game.knight.search.SearchResult;

public class ASTManager
{
	private static Hashtable<IDocument, AST> dom_ast = new Hashtable<IDocument, AST>();
	private static Hashtable<IDocument, IFile> dom_file = new Hashtable<IDocument, IFile>();
	private static Hashtable<IFile, IDocument> file_dom = new Hashtable<IFile, IDocument>();

	/**
	 * �����ĵ����ļ�
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
	 * �Ͽ������ĵ����ļ�
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
	 * ��ȡ�ĵ����ļ�
	 * 
	 * @param document
	 * @return
	 */
	public static IFile getDocumentFile(IDocument document)
	{
		return dom_file.get(document);
	}

	/**
	 * ��ȡ�ĵ���AST
	 * 
	 * @param document
	 * @return
	 */
	public static AST getDocumentAST(IDocument document)
	{
		if (!dom_ast.containsKey(document))
		{
			dom_ast.put(document, new AST(document));
		}
		return dom_ast.get(document);
	}

	// ------------------------------------------------------------------------------

	private static final String SOURCE_FOLDER_NAME = "views";
	private static Hashtable<IProject, Hashtable<String, AST>> projectTable = new Hashtable<IProject, Hashtable<String, AST>>();

	/**
	 * ��ȡ��ͼ��ַ
	 * 
	 * @param file
	 * @return
	 */
	public static String getFilePath(IFile file)
	{
		return file.getProjectRelativePath().toString().substring(SOURCE_FOLDER_NAME.length());
	}

	/**
	 * ����Դ�ļ�
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
	 * �����ļ�����
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws CoreException
	 */
	public static AST getAST(IFile file) throws CoreException, IOException
	{
		if (file_dom.containsKey(file))
		{
			return getDocumentAST(file_dom.get(file));
		}

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
				views.put(url, new AST(file));
			}
			return views.get(url);
		}
		return null;
	}

	// ----------------------------------------------------------------------------------------------------------------
	//
	// ����
	// 1.����ID����
	// 2.�����ļ�����
	//
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * ����ID����
	 * 
	 * @param target
	 * @param result
	 * @param pm
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void searchIdRef(IdDef target, SearchResult result, IProgressMonitor pm) throws CoreException, IOException
	{
		// �г���Ŀ��������ͼ�ļ����ѽ����ġ�δ�����ģ�
		IProject project = target.getFile().getProject();
		IFolder projectSourceFolder = project.getFolder(SOURCE_FOLDER_NAME);
		ArrayList<IResource> projectSourceFiles = new ArrayList<IResource>();

		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, AST>());
		}

		Hashtable<String, AST> parsedViews = projectTable.get(project);
		ArrayList<IFile> noParseFiles = new ArrayList<IFile>();

		projectSourceFiles.add(projectSourceFolder);
		while (projectSourceFiles.size() > 0)
		{
			IResource resource = projectSourceFiles.remove(0);
			if (resource instanceof IFolder)
			{
				for (IResource child : ((IFolder) resource).members())
				{
					projectSourceFiles.add(child);
				}
			}
			else if (resource instanceof IFile)
			{
				if (resource.getName().toLowerCase().endsWith(".xml"))
				{
					IFile file = (IFile) resource;
					String fileURL = getFilePath(file);
					if (!parsedViews.containsKey(fileURL))
					{
						noParseFiles.add(file);

						AST noParseView = new AST(file);
						parsedViews.put(fileURL, noParseView);
					}
				}
			}
		}

		// ������û�н�������ͼ�ļ�
		pm.beginTask("������ͼ�ļ�", noParseFiles.size() + parsedViews.size());
		for (int i = 0; i < noParseFiles.size(); i++)
		{
			IFile file = noParseFiles.get(i);
			String fileURL = getFilePath(file);

			parsedViews.put(fileURL, new AST(file));
			pm.worked(1);
		}

		// ����ID����
		pm.setTaskName("��������");
		for (AST view : parsedViews.values())
		{
			for (IdRef ref : view.getLinks().getIdRefs())
			{
				IdDef curr = ref.getTarget();
				if (curr != null)
				{
					if (curr.getFile().equals(target.getFile()) && curr.getStart() == target.getStart() && curr.getStop() == target.getStop())
					{
						result.addMatch(new SearchMatch(ref.getFile(), ref.getText(), ref.getOffset(), ref.getLength()));
						pm.worked(1);
					}
				}
			}
		}

		keepResourceChangeListener();
	}

	/**
	 * �����ļ�����
	 * 
	 * @param file
	 * @param result
	 * @param pm
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void searchFileRef(IFile target, SearchResult result, IProgressMonitor pm) throws CoreException, IOException
	{
		// �г���Ŀ��������ͼ�ļ����ѽ����ġ�δ�����ģ�
		IProject project = target.getProject();
		IFolder projectSourceFolder = project.getFolder(SOURCE_FOLDER_NAME);
		ArrayList<IResource> projectSourceFiles = new ArrayList<IResource>();

		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, AST>());
		}

		Hashtable<String, AST> parsedViews = projectTable.get(project);
		ArrayList<IFile> noParseFiles = new ArrayList<IFile>();

		projectSourceFiles.add(projectSourceFolder);
		while (projectSourceFiles.size() > 0)
		{
			IResource resource = projectSourceFiles.remove(0);
			if (resource instanceof IFolder)
			{
				for (IResource child : ((IFolder) resource).members())
				{
					projectSourceFiles.add(child);
				}
			}
			else if (resource instanceof IFile)
			{
				if (resource.getName().toLowerCase().endsWith(".xml"))
				{
					IFile file = (IFile) resource;
					String fileURL = getFilePath(file);
					if (!parsedViews.containsKey(fileURL))
					{
						noParseFiles.add(file);

						AST noParseView = new AST(file);
						parsedViews.put(fileURL, noParseView);
					}
				}
			}
		}

		// ������û�н�������ͼ�ļ�
		pm.beginTask("������ͼ�ļ�", noParseFiles.size() + parsedViews.size());
		for (int i = 0; i < noParseFiles.size(); i++)
		{
			IFile file = noParseFiles.get(i);
			String fileURL = getFilePath(file);

			parsedViews.put(fileURL, new AST(file));
			pm.worked(1);
		}

		// �����ļ�����
		pm.setTaskName("��������");
		String refURL = getFilePath(target);
		for (AST view : parsedViews.values())
		{
			for (FileRef ref : view.getLinks().getFileRefs())
			{
				if (ref.getTargetURL().equals(refURL))
				{
					result.addMatch(new SearchMatch(ref.getFile(), ref.getText(), ref.getStart(), ref.getLength()));
					pm.worked(1);
				}
			}
		}

		keepResourceChangeListener();
	}

	// ----------------------------------------------------------------------------------------------------------------
	//
	// �ع�
	// 1.������Դ
	// 2.�ƶ���Դ
	//
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * ������ID
	 * 
	 * @param idDef
	 * @return
	 * @throws CoreException
	 * @throws IOException
	 */
	public static MyChange[] renameID(IdDef idDef, String newID, IProgressMonitor pm) throws CoreException, IOException
	{
		// �г���Ŀ��������ͼ�ļ����ѽ����ġ�δ�����ģ�
		IProject project = idDef.getFile().getProject();
		IFolder projectSourceFolder = project.getFolder(SOURCE_FOLDER_NAME);
		ArrayList<IResource> projectSourceFiles = new ArrayList<IResource>();

		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, AST>());
		}

		Hashtable<String, AST> parsedViews = projectTable.get(project);
		ArrayList<IFile> noParseFiles = new ArrayList<IFile>();

		projectSourceFiles.add(projectSourceFolder);
		while (projectSourceFiles.size() > 0)
		{
			IResource resource = projectSourceFiles.remove(0);
			if (resource instanceof IFolder)
			{
				for (IResource child : ((IFolder) resource).members())
				{
					projectSourceFiles.add(child);
				}
			}
			else if (resource instanceof IFile)
			{
				if (resource.getName().toLowerCase().endsWith(".xml"))
				{
					IFile file = (IFile) resource;
					String fileURL = getFilePath(file);
					if (!parsedViews.containsKey(fileURL))
					{
						noParseFiles.add(file);

						AST noParseView = new AST(file);
						parsedViews.put(fileURL, noParseView);
					}
				}
			}
		}

		// ������û�н�������ͼ�ļ�
		pm.beginTask("������ͼ�ļ�", noParseFiles.size() + parsedViews.size());
		for (int i = 0; i < noParseFiles.size(); i++)
		{
			IFile file = noParseFiles.get(i);
			String fileURL = getFilePath(file);

			parsedViews.put(fileURL, new AST(file));
			pm.worked(1);
		}

		// �����Ķ�
		pm.setTaskName("����ID����");
		ArrayList<MyChange> changes = new ArrayList<MyChange>();
		ArrayList<IdRef> result = new ArrayList<IdRef>();
		for (AST view : parsedViews.values())
		{
			for (IdRef ref : view.getLinks().getIdRefs())
			{
				IdDef curr = ref.getTarget();
				if (curr != null)
				{
					if (curr.getFile().equals(idDef.getFile()) && curr.getStart() == idDef.getStart() && curr.getStop() == idDef.getStop())
					{
						result.add(ref);

						MyChange change = new MyChange();
						change.owner = ref.getFile();
						change.offset = ref.getStart();
						change.length = ref.getLength();
						change.text = newID;
						changes.add(change);
					}
				}
			}
		}

		MyChange idChange = new MyChange();
		idChange.owner = idDef.getFile();
		idChange.offset = idDef.getStart();
		idChange.length = idDef.getLength();
		idChange.text = newID;
		changes.add(idChange);

		keepResourceChangeListener();

		return changes.toArray(new MyChange[changes.size()]);
	}

	/**
	 * ��������Դ
	 * 
	 * @param from
	 * @param newName
	 * @param pm
	 * @return
	 * @throws CoreException
	 * @throws IOException
	 */
	public static MyChange[] reanameResource(IResource from, String newName, IProgressMonitor pm) throws CoreException, IOException
	{
		Hashtable<IFile, IFile> froms_dests = new Hashtable<IFile, IFile>();

		if (from instanceof IFile)
		{
			froms_dests.put((IFile) from, ((IFolder) from.getParent()).getFile(newName));
		}
		else if (from instanceof IFolder)
		{
			IFolder dest = ((IFolder) from.getParent()).getFolder(newName);

			ArrayList<IResource> resources = new ArrayList<IResource>();
			resources.add(from);

			while (resources.size() > 0)
			{
				IResource resource = resources.remove(0);
				if (resource instanceof IFolder)
				{
					IFolder folder = (IFolder) resource;
					for (IResource child : folder.members())
					{
						resources.add(child);
					}
				}
				else if (resource instanceof IFile)
				{
					IFile file = (IFile) resource;
					IFile newFile = dest.getFile(file.getLocation().makeRelativeTo(from.getLocation()));

					froms_dests.put(file, newFile);
				}
			}
		}

		// �г���Ŀ��������ͼ�ļ����ѽ����ġ�δ�����ģ�
		IProject project = from.getProject();
		IFolder projectSourceFolder = project.getFolder(SOURCE_FOLDER_NAME);
		ArrayList<IResource> projectSourceFiles = new ArrayList<IResource>();

		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, AST>());
		}

		Hashtable<String, AST> parsedViews = projectTable.get(project);
		ArrayList<IFile> noParseFiles = new ArrayList<IFile>();

		projectSourceFiles.add(projectSourceFolder);
		while (projectSourceFiles.size() > 0)
		{
			IResource resource = projectSourceFiles.remove(0);
			if (resource instanceof IFolder)
			{
				for (IResource child : ((IFolder) resource).members())
				{
					projectSourceFiles.add(child);
				}
			}
			else if (resource instanceof IFile)
			{
				if (resource.getName().toLowerCase().endsWith(".xml"))
				{
					IFile file = (IFile) resource;
					String fileURL = getFilePath(file);
					if (!parsedViews.containsKey(fileURL))
					{
						noParseFiles.add(file);

						AST noParseView = new AST(file);
						parsedViews.put(fileURL, noParseView);
					}
				}
			}
		}

		// ������û�н�������ͼ�ļ�
		pm.beginTask("������ͼ�ļ�", noParseFiles.size() + parsedViews.size() * froms_dests.size());
		for (int i = 0; i < noParseFiles.size(); i++)
		{
			IFile file = noParseFiles.get(i);
			String fileURL = getFilePath(file);

			parsedViews.put(fileURL, new AST(file));
			pm.worked(1);
		}

		// ��������
		ArrayList<MyChange> changes = new ArrayList<MyChange>();
		pm.setTaskName("�����ļ�����");
		for (IFile fromFile : froms_dests.keySet())
		{
			String fromPath = getFilePath(fromFile);

			IFile destFile = froms_dests.get(fromFile);
			String destPath = getFilePath(destFile);

			for (AST view : parsedViews.values())
			{
				for (FileRef ref : view.getLinks().getFileRefs())
				{
					if (ref.getTargetURL().equals(fromPath))
					{
						MyChange change = new MyChange();
						change.owner = view.getFile();
						change.offset = ref.getStart();
						change.length = ref.getLength();
						change.text = destPath;
						changes.add(change);
					}
				}
			}

			if (fromFile.getName().toLowerCase().endsWith(".xml"))
			{
				AST content = parsedViews.get(getFilePath(fromFile));
				for (FileRef ref : content.getLinks().getFileRefs())
				{
					IFile file = projectSourceFolder.getFile(new Path(ref.getTargetURL()));
					if (!froms_dests.containsKey(file))
					{
						MyChange change = new MyChange();
						change.owner = content.getFile();
						change.offset = ref.getStart();
						change.length = ref.getLength();
						change.text = froms_dests.containsKey(file) ? getFilePath(froms_dests.get(file)) : getFilePath(file);
						changes.add(change);
					}
				}
			}
			pm.worked(1);
		}

		keepResourceChangeListener();

		return changes.toArray(new MyChange[changes.size()]);
	}

	/**
	 * �ƶ���Դ
	 * 
	 * @param from
	 * @param dest
	 * @param otherFroms
	 * @param pm
	 * @return
	 * @throws CoreException
	 * @throws IOException
	 */
	public static MyChange[] moveResource(IResource from, IFolder dest, IResource[] otherFroms, IProgressMonitor pm) throws CoreException, IOException
	{
		ArrayList<IFile> fromFiles = new ArrayList<IFile>();
		Hashtable<IFile, IFile> froms_dests = new Hashtable<IFile, IFile>();

		// ����Դ��Դ�б�
		ArrayList<IResource> froms = new ArrayList<IResource>();
		boolean added = false;
		for (IResource item : otherFroms)
		{
			froms.add(item);
			if (item.equals(from))
			{
				added = true;
			}
		}
		if (!added)
		{
			froms.add(from);
		}

		// ��������Դ�ļ��������ƶ�ǰ��λ��
		ArrayList<IResource> resources = new ArrayList<IResource>();
		for (IResource other : froms)
		{
			boolean isFrom = other.equals(from);
			resources.add(other);
			while (resources.size() > 0)
			{
				IResource resource = resources.remove(0);
				if (resource instanceof IFolder)
				{
					IFolder curr = (IFolder) resource;
					for (IResource child : curr.members())
					{
						resources.add(child);
					}
				}
				else if (resource instanceof IFile)
				{
					IFile fromFile = (IFile) resource;
					IFile destFile = dest.getFile(fromFile.getLocation().makeRelativeTo(other.getParent().getLocation()));
					froms_dests.put(fromFile, destFile);
					if (isFrom)
					{
						fromFiles.add(fromFile);
					}
				}
			}
		}

		// �г���Ŀ��������ͼ�ļ����ѽ����ġ�δ�����ģ�
		IProject project = from.getProject();
		IFolder projectSourceFolder = project.getFolder(SOURCE_FOLDER_NAME);
		ArrayList<IResource> projectSourceFiles = new ArrayList<IResource>();

		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, AST>());
		}

		Hashtable<String, AST> parsedViews = projectTable.get(project);
		ArrayList<IFile> noParseFiles = new ArrayList<IFile>();

		projectSourceFiles.add(projectSourceFolder);
		while (projectSourceFiles.size() > 0)
		{
			IResource resource = projectSourceFiles.remove(0);
			if (resource instanceof IFolder)
			{
				for (IResource child : ((IFolder) resource).members())
				{
					projectSourceFiles.add(child);
				}
			}
			else if (resource instanceof IFile)
			{
				if (resource.getName().toLowerCase().endsWith(".xml"))
				{
					IFile file = (IFile) resource;
					String fileURL = getFilePath(file);
					if (!parsedViews.containsKey(fileURL))
					{
						noParseFiles.add(file);
					}
				}
			}
		}

		// ������û�н�������ͼ�ļ�
		pm.beginTask("������ͼ�ļ�", noParseFiles.size() + parsedViews.size() * froms_dests.size());
		for (int i = 0; i < noParseFiles.size(); i++)
		{
			IFile file = noParseFiles.get(i);
			String fileURL = getFilePath(file);

			parsedViews.put(fileURL, new AST(file));
			pm.worked(1);
		}

		// ��������
		ArrayList<MyChange> changes = new ArrayList<MyChange>();
		pm.setTaskName("�����ļ�����");
		for (IFile fromFile : fromFiles)
		{
			String fromPath = getFilePath(fromFile);

			IFile destFile = froms_dests.get(fromFile);
			String destPath = getFilePath(destFile);

			for (AST view : parsedViews.values())
			{
				for (FileRef ref : view.getLinks().getFileRefs())
				{
					if (ref.getTargetURL().equals(fromPath))
					{
						MyChange change = new MyChange();
						change.owner = view.getFile();
						change.offset = ref.getStart();
						change.length = ref.getLength();
						change.text = destPath;
						changes.add(change);
					}
				}
			}

			if (fromFile.getName().toLowerCase().endsWith(".xml"))
			{
				AST content = parsedViews.get(getFilePath(fromFile));
				for (FileRef ref : content.getLinks().getFileRefs())
				{
					IFile file = projectSourceFolder.getFile(new Path(ref.getTargetURL()));
					if (!froms_dests.containsKey(file))
					{
						MyChange change = new MyChange();
						change.owner = content.getFile();
						change.offset = ref.getStart();
						change.length = ref.getLength();
						change.text = froms_dests.containsKey(file) ? getFilePath(froms_dests.get(file)) : getFilePath(file);
						changes.add(change);
					}
				}
			}
			pm.worked(1);
		}

		keepResourceChangeListener();

		return changes.toArray(new MyChange[changes.size()]);
	}

	// ----------------------------------------------------------------------------------------------------------------
	//
	// ������Դ�䶯
	// 1.��Ŀ��ɾ����ر�ʱ�������Ӧ��AST�б�
	// 2.�ļ��䶯ʱ��ɾ����Ӧ��AST
	//
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * ������Դ���¼�����
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
	 * ��Դ���ļ�����
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
	 * ������Դ����
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
