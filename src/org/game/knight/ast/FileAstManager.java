package org.game.knight.ast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.game.knight.search.SearchMatch;
import org.game.knight.search.SearchResult;

public class FileAstManager
{
	private static final String SOURCE_FOLDER_NAME = "views";
	private static Hashtable<IProject, Hashtable<String, FileAst>> projectTable = new Hashtable<IProject, Hashtable<String, FileAst>>();

	/**
	 * ��ȡ��ͼ��ַ
	 * 
	 * @param file
	 * @return
	 */
	public static String getViewURL(IFile file)
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
	public static FileAst getFileSummay(IFile file)
	{
		IProject project = file.getProject();

		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, FileAst>());
		}

		if (file.getName().endsWith(".xml"))
		{
			Hashtable<String, FileAst> views = projectTable.get(project);

			String viewURL = getViewURL(file);
			if (!views.containsKey(viewURL))
			{
				FileAst data = new FileAst(viewURL, file);
				views.put(viewURL, data);

				try
				{
					data.parse();
				}
				catch (CoreException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			return views.get(viewURL);
		}
		return null;
	}

	public static List<IdRef> getIdRefs(IdDef idDef) throws CoreException, IOException
	{
		IProject project = idDef.getFile().getProject();
		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, FileAst>());
		}

		final Hashtable<String, FileAst> views = projectTable.get(project);
		final ArrayList<FileAst> addedViews = new ArrayList<FileAst>();

		IFolder folder = project.getFolder(SOURCE_FOLDER_NAME);
		folder.accept(new IResourceVisitor()
		{
			@Override
			public boolean visit(IResource resource) throws CoreException
			{
				if (resource instanceof IFile)
				{
					IFile file = (IFile) resource;
					if (file.getName().endsWith(".xml"))
					{
						String viewURL = getViewURL(file);
						if (!views.containsKey(viewURL))
						{
							FileAst addedView = new FileAst(viewURL, file);
							views.put(viewURL, addedView);
							addedViews.add(addedView);
						}
					}
				}
				return true;
			}
		});

		for (int i = 0; i < addedViews.size(); i++)
		{
			addedViews.get(i).parse();
		}

		ArrayList<IdRef> result = new ArrayList<IdRef>();
		for (FileAst view : views.values())
		{
			for (IdRef ref : view.getIDRefs())
			{
				IdDef curr = ref.getTarget();
				if (curr != null)
				{
					if (curr.getFile().equals(idDef.getFile()) && curr.getStart() == idDef.getStart() && curr.getStop() == idDef.getStop())
					{
						result.add(ref);
					}
				}
			}
		}

		keepResourceChangeListener();

		return result;
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
	public static Change[] reanameResource(IResource from, String newName, IProgressMonitor pm) throws CoreException, IOException
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
			projectTable.put(project, new Hashtable<String, FileAst>());
		}

		Hashtable<String, FileAst> parsedViews = projectTable.get(project);
		ArrayList<FileAst> noParseViews = new ArrayList<FileAst>();

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
					String fileURL = getViewURL(file);
					if (!parsedViews.containsKey(fileURL))
					{
						FileAst noParseView = new FileAst(fileURL, file);
						parsedViews.put(fileURL, noParseView);
						noParseViews.add(noParseView);
					}
				}
			}
		}

		// ������û�н�������ͼ�ļ�
		pm.beginTask("������ͼ�ļ�", noParseViews.size() + parsedViews.size() * froms_dests.size());
		for (int i = 0; i < noParseViews.size(); i++)
		{
			noParseViews.get(i).parse();
			pm.worked(1);
		}

		// ��������
		ArrayList<Change> changes = new ArrayList<FileAstManager.Change>();
		pm.setTaskName("�����ļ�����");
		for (IFile fromFile : froms_dests.keySet())
		{
			String fromPath = getViewURL(fromFile);

			IFile destFile = froms_dests.get(fromFile);
			String destPath = getViewURL(destFile);

			for (FileAst view : parsedViews.values())
			{
				for (FileRef ref : view.getFileRefs())
				{
					if (ref.getTargetURL().equals(fromPath))
					{
						Change change = new Change();
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
				FileAst content = parsedViews.get(getViewURL(fromFile));
				for (FileRef ref : content.getFileRefs())
				{
					IFile file = projectSourceFolder.getFile(new Path(ref.getTargetURL()));
					if (!froms_dests.containsKey(file))
					{
						Change change = new Change();
						change.owner = content.getFile();
						change.offset = ref.getStart();
						change.length = ref.getLength();
						change.text = froms_dests.containsKey(file) ? getViewURL(froms_dests.get(file)) : getViewURL(file);
						changes.add(change);
					}
				}
			}
			pm.worked(1);
		}

		keepResourceChangeListener();

		return changes.toArray(new Change[changes.size()]);
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
	public static Change[] moveResource(IResource from, IFolder dest, IResource[] otherFroms, IProgressMonitor pm) throws CoreException, IOException
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
			projectTable.put(project, new Hashtable<String, FileAst>());
		}

		Hashtable<String, FileAst> parsedViews = projectTable.get(project);
		ArrayList<FileAst> noParseViews = new ArrayList<FileAst>();

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
					String fileURL = getViewURL(file);
					if (!parsedViews.containsKey(fileURL))
					{
						FileAst noParseView = new FileAst(fileURL, file);
						parsedViews.put(fileURL, noParseView);
						noParseViews.add(noParseView);
					}
				}
			}
		}

		// ������û�н�������ͼ�ļ�
		pm.beginTask("������ͼ�ļ�", noParseViews.size() + parsedViews.size() * froms_dests.size());
		for (int i = 0; i < noParseViews.size(); i++)
		{
			noParseViews.get(i).parse();
			pm.worked(1);
		}

		// ��������
		ArrayList<Change> changes = new ArrayList<FileAstManager.Change>();
		pm.setTaskName("�����ļ�����");
		for (IFile fromFile : fromFiles)
		{
			String fromPath = getViewURL(fromFile);

			IFile destFile = froms_dests.get(fromFile);
			String destPath = getViewURL(destFile);

			for (FileAst view : parsedViews.values())
			{
				for (FileRef ref : view.getFileRefs())
				{
					if (ref.getTargetURL().equals(fromPath))
					{
						Change change = new Change();
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
				FileAst content = parsedViews.get(getViewURL(fromFile));
				for (FileRef ref : content.getFileRefs())
				{
					IFile file = projectSourceFolder.getFile(new Path(ref.getTargetURL()));
					if (!froms_dests.containsKey(file))
					{
						Change change = new Change();
						change.owner = content.getFile();
						change.offset = ref.getStart();
						change.length = ref.getLength();
						change.text = froms_dests.containsKey(file) ? getViewURL(froms_dests.get(file)) : getViewURL(file);
						changes.add(change);
					}
				}
			}
			pm.worked(1);
		}

		keepResourceChangeListener();

		return changes.toArray(new Change[changes.size()]);
	}

	public static class Change
	{
		public IFile owner;
		public int offset;
		public int length;
		public String text;
	}

	/**
	 * ��ȡ�ļ�����
	 * 
	 * @param file
	 * @throws CoreException
	 * @throws IOException
	 */
	public static List<FileRef> getFileRefs(IFile file) throws CoreException, IOException
	{
		IProject project = file.getProject();
		String refURL = getViewURL(file);

		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, FileAst>());
		}

		final Hashtable<String, FileAst> views = projectTable.get(project);
		final ArrayList<FileAst> addedViews = new ArrayList<FileAst>();

		IFolder folder = project.getFolder(SOURCE_FOLDER_NAME);
		folder.accept(new IResourceVisitor()
		{
			@Override
			public boolean visit(IResource resource) throws CoreException
			{
				if (resource instanceof IFile)
				{
					IFile file = (IFile) resource;
					if (file.getName().endsWith(".xml"))
					{
						String viewURL = getViewURL(file);
						if (!views.containsKey(viewURL))
						{
							FileAst addedView = new FileAst(viewURL, file);
							views.put(viewURL, addedView);
							addedViews.add(addedView);
						}
					}
				}
				return true;
			}
		});

		for (int i = 0; i < addedViews.size(); i++)
		{
			addedViews.get(i).parse();
		}

		ArrayList<FileRef> result = new ArrayList<FileRef>();
		for (FileAst view : views.values())
		{
			for (FileRef ref : view.getFileRefs())
			{
				if (ref.getTargetURL().equals(refURL))
				{
					result.add(ref);
				}
			}
		}

		keepResourceChangeListener();

		return result;
	}

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
		IProject project = target.getFile().getProject();

		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, FileAst>());
		}

		final Hashtable<String, FileAst> views = projectTable.get(project);
		final ArrayList<FileAst> addedViews = new ArrayList<FileAst>();

		IFolder folder = project.getFolder(SOURCE_FOLDER_NAME);
		folder.accept(new IResourceVisitor()
		{
			@Override
			public boolean visit(IResource resource) throws CoreException
			{
				if (resource instanceof IFile)
				{
					IFile file = (IFile) resource;
					if (file.getName().endsWith(".xml"))
					{
						String viewURL = getViewURL(file);
						if (!views.containsKey(viewURL))
						{
							FileAst addedView = new FileAst(viewURL, file);
							views.put(viewURL, addedView);
							addedViews.add(addedView);
						}
					}
				}
				return true;
			}
		});

		pm.beginTask("��������", addedViews.size() + views.size());
		for (int i = 0; i < addedViews.size(); i++)
		{
			addedViews.get(i).parse();
			pm.worked(1);
		}

		for (FileAst view : views.values())
		{
			for (IdRef ref : view.getIDRefs())
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
	public static void searchFileRef(IFile file, SearchResult result, IProgressMonitor pm) throws CoreException, IOException
	{
		IProject project = file.getProject();
		String refURL = getViewURL(file);

		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, FileAst>());
		}

		final Hashtable<String, FileAst> views = projectTable.get(project);
		final ArrayList<FileAst> addedViews = new ArrayList<FileAst>();

		IFolder folder = project.getFolder(SOURCE_FOLDER_NAME);
		folder.accept(new IResourceVisitor()
		{
			@Override
			public boolean visit(IResource resource) throws CoreException
			{
				if (resource instanceof IFile)
				{
					IFile file = (IFile) resource;
					if (file.getName().endsWith(".xml"))
					{
						String viewURL = getViewURL(file);
						if (!views.containsKey(viewURL))
						{
							FileAst addedView = new FileAst(viewURL, file);
							views.put(viewURL, addedView);
							addedViews.add(addedView);
						}
					}
				}
				return true;
			}
		});

		pm.beginTask("��������", addedViews.size() + views.size());
		for (int i = 0; i < addedViews.size(); i++)
		{
			addedViews.get(i).parse();
			pm.worked(1);
		}

		for (FileAst view : views.values())
		{
			for (FileRef ref : view.getFileRefs())
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
										projectTable.get(project).remove(getViewURL(file));
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
				// Activator.getDefault().getLog().
				e.printStackTrace();
			}

			keepResourceChangeListener();
		}
	}
}