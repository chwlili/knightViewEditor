package org.game.knight.wizard;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.game.knight.builder.ViewProjectNature;

public class NewProjectWizard extends Wizard implements INewWizard
{
	private NewProjectWizardPage page1;

	public NewProjectWizard()
	{
	}

	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
	}

	@Override
	public void addPages()
	{
		setWindowTitle("view ÏîÄ¿");
		setNeedsProgressMonitor(true);
		page1 = new NewProjectWizardPage();

		addPage(page1);
	}

	@Override
	public boolean performFinish()
	{
		final String name = page1.getProjectName();
		final String path = page1.getProjectPath();
		final String source=page1.getSourcePath();
		final String code=page1.getCodePath();
		final String bin=page1.getBinPath();

		try
		{
			getContainer().run(true, false, new IRunnableWithProgress()
			{
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					monitor.beginTask("", 6000); //$NON-NLS-1$
					
					IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
					try
					{
						IProjectDescription desc = ResourcesPlugin.getWorkspace().newProjectDescription(name);
						
						desc.setNatureIds(new String[]{ViewProjectNature.NatureID});
						
						ICommand cmd=desc.newCommand();
						cmd.setBuilderName(ViewProjectNature.BuilderID);
						desc.setBuildSpec(new ICommand[]{cmd});
						
						if (path != null)
						{
							desc.setLocation(new Path(path));
						}
						
						project.create(desc, new SubProgressMonitor(monitor, 1000));
						
						project.open(new SubProgressMonitor(monitor, 1000));
						
						IFolder srcDir=project.getFolder(code);
						srcDir.refreshLocal(IResource.DEPTH_ZERO, null);
						if(!srcDir.exists())
						{
							srcDir.create(false, true, new SubProgressMonitor(monitor, 1000));
						}
						
						IFolder binDir=project.getFolder(bin);
						binDir.refreshLocal(IResource.DEPTH_ZERO, null);
						if(!binDir.exists())
						{
							binDir.create(false, true, new SubProgressMonitor(monitor, 1000));
						}
						
						File outputFile = new File(source);
						if (outputFile.exists() && outputFile.isDirectory())
						{
							IFolder folder = project.getFolder(/*"[source path] " + */outputFile.getName());
							folder.createLink(new Path(outputFile.getPath()), IResource.REPLACE | IResource.BACKGROUND_REFRESH, null);
						}
						else
						{
							IFolder folder = project.getFolder(source);
							folder.refreshLocal(IResource.DEPTH_ZERO, null);
							if (!folder.exists())
							{
								folder.create(true, true, null);
							}
						}
						
						project.setDefaultCharset("UTF-8", new SubProgressMonitor(monitor, 1000)); //$NON-NLS-1$
					}
					catch (CoreException e)
					{
						e.printStackTrace();
					}
					finally
					{
						monitor.done();
					}
				}
			});
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		return true;
	}
}
