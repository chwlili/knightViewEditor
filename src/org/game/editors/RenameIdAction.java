package org.game.editors;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.RenameProcessor;
import org.eclipse.ltk.core.refactoring.participants.SharableParticipants;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.text.edits.ReplaceEdit;
import org.game.refactor.FileRef;
import org.game.refactor.FileSummay;
import org.game.refactor.IdDef;
import org.game.refactor.IdRef;
import org.game.refactor.Project;

public class RenameIdAction extends Action
{
	private IdDef idDef;

	public RenameIdAction(IdDef idDef)
	{
		this.idDef = idDef;
	}

	@Override
	public String getText()
	{
		return "重命名(&R)...";
	}

	@Override
	public void run()
	{
		RenameIDWizard wizard = new RenameIDWizard(idDef);
		RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(wizard);

		try
		{
			op.run(Display.getCurrent().getActiveShell(), "XXXXXXXXXXXXXXXXX");
		}
		catch (InterruptedException e)
		{
			// do nothing
		}
	}

	private class RenameIDWizard extends RefactoringWizard
	{
		private IdDef idDef;
		private RenameIdProcessor fProcessor;
		
		public RenameIDWizard(IdDef idDef)
		{
			super(new RenameIdRefactoring(new RenameIdProcessor(idDef)), DIALOG_BASED_USER_INTERFACE);
			
			this.idDef=idDef;
			fProcessor=(RenameIdProcessor) ((RenameIdRefactoring)getRefactoring()).getProcessor();
		}

		@Override
		protected void addUserInputPages()
		{
			addPage(new RenameIDWizardPage(fProcessor,"jjjj"));
		}
	}
	
	private class RenameIdRefactoring extends ProcessorBasedRefactoring
	{
		private RenameIdProcessor fProcessor;
		public RenameIdRefactoring(RenameIdProcessor processor)
		{
			super(processor);
			Assert.isNotNull(processor);
			fProcessor= processor;
		}

		/**
		 * {@inheritDoc}
		 */
		public RefactoringProcessor getProcessor() {
			return fProcessor;
		}
	}
	
	private class RenameIdProcessor extends RenameProcessor
	{
		private IdDef idDef;
		private String name;
		
		public RenameIdProcessor(IdDef idDef)
		{
			this.idDef=idDef;
		}
		
		public void setName(String text)
		{
			this.name=text;
		}
		
		@Override
		public Object[] getElements()
		{
			return new Object[]{idDef};
		}

		@Override
		public String getIdentifier()
		{
			return "org.chw.game.renameIdProcessor";
		}

		@Override
		public String getProcessorName()
		{
			return "重命名ID";
		}

		@Override
		public boolean isApplicable() throws CoreException
		{
			if(idDef==null)
			{
				return false;
			}
			return true;
		}

		@Override
		public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException
		{
			return RefactoringStatus.create(Status.OK_STATUS);
			//return RefactoringStatus.create(Resources.checkInSync(idDef.getFile()));
		}

		@Override
		public RefactoringStatus checkFinalConditions(IProgressMonitor pm, CheckConditionsContext context) throws CoreException, OperationCanceledException
		{
			return RefactoringStatus.create(Status.OK_STATUS);
		}

		@Override
		public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException
		{
			CompositeChange root=new CompositeChange("重构文件引用");
			
			FileSummay dom=Project.getFileSummay(idDef.getFile());
			try
			{
				List<IdRef> refs=Project.getIdRefs(idDef);
				if(refs!=null)
				{
					for(IdRef ref:refs)
					{
						TextFileChange change=new TextFileChange("haha",ref.getOwner());
						change.setEdit(new ReplaceEdit(ref.getOffset(),ref.getLength(),name));
						root.add(change);
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return root;
		}

		@Override
		public RefactoringParticipant[] loadParticipants(RefactoringStatus status, SharableParticipants sharedParticipants) throws CoreException
		{
			return null;
		}
	}

	private class RenameIDWizardPage extends UserInputWizardPage
	{
		private final RenameIdProcessor fRefactoringProcessor;
		private Text fNameField;

		protected RenameIDWizardPage(RenameIdProcessor processor,String name)
		{
			super(name);
			
			fRefactoringProcessor=processor;
		}

		@Override
		public void createControl(Composite parent)
		{
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			composite.setFont(parent.getFont());

			Label label = new Label(composite, SWT.NONE);
			label.setText("新建名称：");
			label.setLayoutData(new GridData());

			fNameField = new Text(composite, SWT.BORDER);
			fNameField.setText("");
			fNameField.setFont(composite.getFont());
			fNameField.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			fNameField.addModifyListener(new ModifyListener()
			{
				public void modifyText(ModifyEvent e)
				{
					validatePage();
				}
			});

			fNameField.selectAll();
			setPageComplete(false);
			setControl(composite);
		}
		
		private void validatePage()
		{
			String text=fNameField.getText();
			if(text!=null && !text.isEmpty())
			{
				fRefactoringProcessor.setName(text);
				setPageComplete(true);
			}
			else
			{
				setPageComplete(false);
			}
		}
	}
}
