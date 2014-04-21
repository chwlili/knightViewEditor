package org.game.knight.refactor;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.internal.core.refactoring.resource.RenameResourceProcessor;
import org.eclipse.ltk.internal.ui.refactoring.RefactoringUIMessages;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.ltk.ui.refactoring.resource.RenameResourceWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RenameFileWizard extends RenameResourceWizard {

	public RenameFileWizard(IResource resource)
	{
		super(resource);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.ui.refactoring.RefactoringWizard#addUserInputPages()
	 */
	protected void addUserInputPages() {
		RenameResourceProcessor processor= (RenameResourceProcessor) getRefactoring().getAdapter(RenameResourceProcessor.class);
		addPage(new RenameResourceRefactoringConfigurationPage(processor));
	}

	private static class RenameResourceRefactoringConfigurationPage extends UserInputWizardPage {

		private final RenameResourceProcessor fRefactoringProcessor;
		private Text fNameField;
		private String name;
		private String ext;

		public RenameResourceRefactoringConfigurationPage(RenameResourceProcessor processor) {
			super("RenameResourceRefactoringInputPage"); //$NON-NLS-1$
			fRefactoringProcessor= processor;
			
			String fileName=fRefactoringProcessor.getNewResourceName();
			int index=fileName.indexOf(".");
			if(index!=-1)
			{
				name=fileName.substring(0,index);
				ext=fileName.substring(index);
			}
			else
			{
				name=fileName;
				ext="";
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
		 */
		public void createControl(Composite parent) {
			Composite composite= new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			composite.setFont(parent.getFont());

			Label label= new Label(composite, SWT.NONE);
			label.setText(RefactoringUIMessages.RenameResourceWizard_name_field_label);
			label.setLayoutData(new GridData());
			
			fNameField= new Text(composite, SWT.BORDER);
			fNameField.setText(name);
			fNameField.setFont(composite.getFont());
			fNameField.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			fNameField.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					validatePage();
				}
			});

			fNameField.selectAll();
			setPageComplete(false);
			setControl(composite);
		}

		public void setVisible(boolean visible) {
			if (visible) {
				fNameField.setFocus();
			}
			super.setVisible(visible);
		}

		protected final void validatePage() {
			String text= fNameField.getText();
			RefactoringStatus status= fRefactoringProcessor.validateNewElementName(text+ext);
			setPageComplete(status);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ltk.ui.refactoring.UserInputWizardPage#performFinish()
		 */
		protected boolean performFinish() {
			initializeRefactoring();
			storeSettings();
			return super.performFinish();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ltk.ui.refactoring.UserInputWizardPage#getNextPage()
		 */
		public IWizardPage getNextPage() {
			initializeRefactoring();
			storeSettings();
			return super.getNextPage();
		}

		private void storeSettings() {
		}

		private void initializeRefactoring() {
			fRefactoringProcessor.setNewResourceName(fNameField.getText()+ext);
		}
	}
}