package org.game.project;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class ViewProjectBuilder extends IncrementalProjectBuilder
{
	public static Hashtable<IProject, ViewProject> projects = new Hashtable<IProject, ViewProject>();

	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException
	{
		if (!projects.containsKey(getProject()))
		{
			kind = FULL_BUILD;
		}

		if (kind == FULL_BUILD)
		{
			fullBuild(monitor);
		}
		else
		{
			IResourceDelta delta = getDelta(getProject());
			if (delta == null)
			{
				fullBuild(monitor);
			}
			else
			{
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	/**
	 * 完整构建
	 * 
	 * @param monitor
	 * @throws CoreException 
	 */
	private void fullBuild(IProgressMonitor monitor) throws CoreException
	{
		System.out.println("full builder");
		
		IProject project=getProject();
		ViewProject viewProject=new ViewProject();
		
		projects.put(project, viewProject);

		viewProject.open(project);
	}

	/**
	 * 增量构建
	 * 
	 * @param delta
	 * @param monitor
	 * @throws CoreException 
	 */
	private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException
	{
		System.out.println("incremental builder");
		
		ViewProject viewProject=projects.get(getProject());
		
		viewProject.incremental(delta);
	}
}
