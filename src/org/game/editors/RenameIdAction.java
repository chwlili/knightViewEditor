package org.game.editors;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.resources.IFile;
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
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
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
		return "������(&R)...";
	}

	@Override
	public void run()
	{
		RenameIDWizard wizard = new RenameIDWizard(idDef);
		RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(wizard);

		try
		{
			op.run(Display.getCurrent().getActiveShell(), "������ID");
		}
		catch (InterruptedException e)
		{
		}
	}
	
	/**
	 * ������ID��
	 * @author tt
	 *
	 */
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
			addPage(new RenameIDWizardPage(fProcessor,"������ID",idDef.getID()));
		}
	}

	/**
	 * ������IDҳ��
	 * @author tt
	 *
	 */
	private class RenameIDWizardPage extends UserInputWizardPage
	{
		private final RenameIdProcessor fRefactoringProcessor;
		private Text fNameField;
		private String id;
		
		protected RenameIDWizardPage(RenameIdProcessor processor,String name,String id)
		{
			super(name);
			
			this.id=id;
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
			label.setText("�½����ƣ�");
			label.setLayoutData(new GridData());

			fNameField = new Text(composite, SWT.BORDER);
			fNameField.setText(id!=null ? id:"");
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

	/**
	 * �ع�����
	 * @author tt
	 *
	 */
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
		public RefactoringProcessor getProcessor() 
		{
			return fProcessor;
		}
	}
	
	/**
	 * �ع�������
	 * @author tt
	 *
	 */
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
			return "������ID";
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
		}

		@Override
		public RefactoringStatus checkFinalConditions(IProgressMonitor pm, CheckConditionsContext context) throws CoreException, OperationCanceledException
		{
			return RefactoringStatus.create(Status.OK_STATUS);
		}

		@Override
		public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException
		{
			CompositeChange root=new CompositeChange("�ع��ļ�����");
			try
			{
				Hashtable<IFile, TextFileChange> file_changes=new Hashtable<IFile, TextFileChange>();
				
				List<IdRef> refs=Project.getIdRefs(idDef);
				if(refs!=null)
				{
					for(IdRef ref:refs)
					{
						TextFileChange change=null;
						if(!file_changes.containsKey(ref.getOwner()))
						{
							change = new TextFileChange("",ref.getOwner());
							change.setEdit(new MultiTextEdit());
							root.add(change);
							
							file_changes.put(ref.getOwner(), change);
						}
						
						change=file_changes.get(ref.getOwner());
						change.addEdit(new ReplaceEdit(ref.getOffset(),ref.getLength(),name));
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
}
