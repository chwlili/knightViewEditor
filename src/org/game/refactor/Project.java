package org.game.refactor;

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
import org.game.views.search.SearchMatch;
import org.game.views.search.SearchResult;

public class Project
{
	private static final String SOURCE_FOLDER_NAME = "views";
	private static Hashtable<IProject, Hashtable<String, FileSummay>> projectTable = new Hashtable<IProject, Hashtable<String, FileSummay>>();

	/**
	 * 获取视图地址
	 * 
	 * @param file
	 * @return
	 */
	public static String getViewURL(IFile file)
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
	public static FileSummay getFileSummay(IFile file)
	{
		IProject project = file.getProject();

		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, FileSummay>());
		}

		if (file.getName().endsWith(".xml"))
		{
			Hashtable<String, FileSummay> views = projectTable.get(project);

			String viewURL = getViewURL(file);
			if (!views.containsKey(viewURL))
			{
				FileSummay data = new FileSummay(viewURL, file);
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
			projectTable.put(project, new Hashtable<String, FileSummay>());
		}

		final Hashtable<String, FileSummay> views = projectTable.get(project);
		final ArrayList<FileSummay> addedViews = new ArrayList<FileSummay>();

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
							FileSummay addedView = new FileSummay(viewURL, file);
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
		for (FileSummay view : views.values())
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

	public static Change[] findRefactoringFileRef(IResource from, IFolder dest, IResource[] others, IProgressMonitor pm) throws CoreException, IOException
	{
		ArrayList<IFile> fromFiles = new ArrayList<IFile>();
		Hashtable<IFile, IFile> froms_dests = new Hashtable<IFile, IFile>();
		ArrayList<Change> changes = new ArrayList<Project.Change>();

		// 列出所有文件移动前后的对应关系
		ArrayList<IResource> resources = new ArrayList<IResource>();
		for (IResource other : others)
		{
			boolean isCurr = other.equals(from);
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
					if (isCurr)
					{
						fromFiles.add(fromFile);
					}
				}
			}
		}

		// 列出项目中所有视图文件（已解析的、未解析的）
		IProject project = from.getProject();
		IFolder projectSourceFolder = project.getFolder(SOURCE_FOLDER_NAME);
		ArrayList<IResource> projectSourceFiles = new ArrayList<IResource>();

		if (!projectTable.containsKey(project))
		{
			projectTable.put(project, new Hashtable<String, FileSummay>());
		}

		Hashtable<String, FileSummay> parsedViews = projectTable.get(project);
		ArrayList<FileSummay> noParseViews = new ArrayList<FileSummay>();

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
						FileSummay noParseView = new FileSummay(fileURL, file);
						parsedViews.put(fileURL, noParseView);
						noParseViews.add(noParseView);
					}
				}
			}
		}

		// 解析还没有解析的视图文件
		pm.beginTask("解析视图文件", noParseViews.size() + parsedViews.size() * froms_dests.size());
		for (int i = 0; i < noParseViews.size(); i++)
		{
			noParseViews.get(i).parse();
			pm.worked(1);
		}

		// 处理引用
		System.out.println(">> 处理移动:" + from.getLocation().toString());
		pm.setTaskName("查找文件引用");
		for (IFile fromFile : fromFiles)
		{
			String fromPath = getViewURL(fromFile);

			IFile destFile = froms_dests.get(fromFile);
			String destPath = getViewURL(destFile);

			System.out.println("   处理别人对" + from.getLocation().toString() + "的引用");
			for (FileSummay view : parsedViews.values())
			{
				for (FileRef ref : view.getFileRefs())
				{
					if (ref.filePath.equals(fromPath))
					{
						Change change = new Change();
						change.owner = view.getFile();
						// change.owner =
						// froms_dests.containsKey(view.getFile()) ?
						// froms_dests.get(view.getFile()) : view.getFile();
						change.offset = ref.start;
						change.length = ref.stop - ref.start + 1;
						change.text = destPath;
						changes.add(change);

						System.out.println("   add range:" + change.offset + "," + change.length + "," + change.text);
					}
				}
			}

			System.out.println("   处理" + from.getLocation().toString() + "对别人的引用");
			if (fromFile.getName().toLowerCase().endsWith(".xml"))
			{
				FileSummay content = parsedViews.get(getViewURL(fromFile));
				for (FileRef ref : content.getFileRefs())
				{
					IFile file = projectSourceFolder.getFile(new Path(ref.filePath));
					if (!froms_dests.containsKey(file))
					{
						Change change = new Change();
						change.owner = content.getFile();
						//change.owner = destFile;
						change.offset = ref.start;
						change.length = ref.stop - ref.start + 1;
						change.text = froms_dests.containsKey(file) ? getViewURL(froms_dests.get(file)) : getViewURL(file);
						changes.add(change);
						System.out.println("   add range:" + change.offset + "," + change.length + "," + change.text);
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
	 * 获取文件引用
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
			projectTable.put(project, new Hashtable<String, FileSummay>());
		}

		final Hashtable<String, FileSummay> views = projectTable.get(project);
		final ArrayList<FileSummay> addedViews = new ArrayList<FileSummay>();

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
							FileSummay addedView = new FileSummay(viewURL, file);
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
		for (FileSummay view : views.values())
		{
			for (FileRef ref : view.getFileRefs())
			{
				if (ref.filePath.equals(refURL))
				{
					result.add(ref);
				}
			}
		}

		keepResourceChangeListener();

		return result;
	}

	/**
	 * 搜索ID引用
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
			projectTable.put(project, new Hashtable<String, FileSummay>());
		}

		final Hashtable<String, FileSummay> views = projectTable.get(project);
		final ArrayList<FileSummay> addedViews = new ArrayList<FileSummay>();

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
							FileSummay addedView = new FileSummay(viewURL, file);
							views.put(viewURL, addedView);
							addedViews.add(addedView);
						}
					}
				}
				return true;
			}
		});

		pm.beginTask("查找引用", addedViews.size() + views.size());
		for (int i = 0; i < addedViews.size(); i++)
		{
			addedViews.get(i).parse();
			pm.worked(1);
		}

		for (FileSummay view : views.values())
		{
			for (IdRef ref : view.getIDRefs())
			{
				IdDef curr = ref.getTarget();
				if (curr != null)
				{
					if (curr.getFile().equals(target.getFile()) && curr.getStart() == target.getStart() && curr.getStop() == target.getStop())
					{
						result.addMatch(new SearchMatch(ref.getOwner(), ref.getText(), ref.getStart(), ref.getStop() - ref.getStart() + 1));
						pm.worked(1);
					}
				}
			}
		}

		keepResourceChangeListener();
	}

	/**
	 * 搜索文件引用
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
			projectTable.put(project, new Hashtable<String, FileSummay>());
		}

		final Hashtable<String, FileSummay> views = projectTable.get(project);
		final ArrayList<FileSummay> addedViews = new ArrayList<FileSummay>();

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
							FileSummay addedView = new FileSummay(viewURL, file);
							views.put(viewURL, addedView);
							addedViews.add(addedView);
						}
					}
				}
				return true;
			}
		});

		pm.beginTask("查找引用", addedViews.size() + views.size());
		for (int i = 0; i < addedViews.size(); i++)
		{
			addedViews.get(i).parse();
			pm.worked(1);
		}

		for (FileSummay view : views.values())
		{
			for (FileRef ref : view.getFileRefs())
			{
				if (ref.filePath.equals(refURL))
				{
					result.addMatch(new SearchMatch(ref.owner, ref.text, ref.start, ref.stop - ref.start + 1));
					pm.worked(1);
				}
			}
		}

		keepResourceChangeListener();
	}

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
