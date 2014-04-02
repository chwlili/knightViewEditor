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
import org.game.xml.search.file.FileRefMatch;
import org.game.xml.search.file.FileRefResult;

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
			for(IdRef ref : view.getIDRefs())
			{
				IdDef curr=ref.getTarget();
				if(curr!=null)
				{
					if(curr.getFile().equals(idDef.getFile()) && curr.getStart()==idDef.getStart() && curr.getStop()==idDef.getStop())
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
	 * @param target
	 * @param result
	 * @param pm
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void searchIdRef(IdDef target, FileRefResult result, IProgressMonitor pm) throws CoreException, IOException
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
				IdDef curr=ref.getTarget();
				if(curr!=null)
				{
					if(curr.getFile().equals(target.getFile()) && curr.getStart()==target.getStart() && curr.getStop()==target.getStop())
					{
						result.addMatch(new FileRefMatch(ref.getOwner(), ref.getText(), ref.getStart(), ref.getStop() - ref.getStart() + 1));
						pm.worked(1);
					}
				}
			}
		}

		keepResourceChangeListener();
	}

	/**
	 * 搜索文件引用
	 * @param file
	 * @param result
	 * @param pm
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void searchFileRef(IFile file, FileRefResult result, IProgressMonitor pm) throws CoreException, IOException
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
					result.addMatch(new FileRefMatch(ref.owner, ref.text, ref.start, ref.stop - ref.start + 1));
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
