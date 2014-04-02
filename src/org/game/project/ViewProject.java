package org.game.project;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

public class ViewProject
{
	private IProject project;

	public void open(IProject project) throws CoreException
	{
		this.project = project;

		this.project.accept(new IResourceVisitor()
		{
			@Override
			public boolean visit(IResource resource) throws CoreException
			{
				if (resource instanceof IFile)
				{
					System.out.println("rebuild file:" + resource.getProjectRelativePath().toString());
				}

				return true;
			}
		});
	}

	public void incremental(IResourceDelta delta) throws CoreException
	{
		delta.accept(new IResourceDeltaVisitor()
		{
			@Override
			public boolean visit(IResourceDelta delta) throws CoreException
			{
				IResource resource = delta.getResource();
				if (resource instanceof IFile)
				{
					System.out.println("incremental build file:" + resource.getProjectRelativePath().toString());
				}

				return true;
			}
		});
	}

}
