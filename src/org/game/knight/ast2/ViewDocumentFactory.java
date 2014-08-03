package org.game.knight.ast2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

public class ViewDocumentFactory
{

	// ------------------------------------------------------------------------------

	private static final String SOURCE_FOLDER_NAME = "views";
	private static Hashtable<IProject, Hashtable<String, ViewDocument>> projectTable = new Hashtable<IProject, Hashtable<String, ViewDocument>>();

	/**
	 * 获取视图地址
	 * 
	 * @param file
	 * @return
	 */
	private static String getFilePath(IFile file)
	{
		return file.getProjectRelativePath().toString().substring(SOURCE_FOLDER_NAME.length());
	}
	
	/**
	 * 获取AST
	 * @param file
	 * @return
	 * @throws CoreException
	 * @throws IOException
	 */
	public static ViewDocument getViewAST(IFile file) throws CoreException, IOException
	{
		IProject project=file.getProject();
		
		if(file.getName().endsWith(".xml"))
		{
			if(!projectTable.containsKey(project))
			{
				projectTable.put(project, new Hashtable<String,ViewDocument>());
			}
			
			Hashtable<String,ViewDocument> documents=projectTable.get(project);
			String url=getFilePath(file);
			if(!documents.containsKey(url))
			{
				documents.put(url, new ViewDocument(file));
				
				keepResourceChangeListener();
			}
			return documents.get(url);
		}
		return null;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	//
	// 监视资源变动
	// 1.项目被删除或关闭时，清除地应的AST列表
	// 2.文件变动时，删除对应的AST
	//
	// ----------------------------------------------------------------------------------------------------------------

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
