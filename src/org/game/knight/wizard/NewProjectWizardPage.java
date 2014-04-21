package org.game.knight.wizard;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewProjectWizardPage extends WizardPage
{
	private Text projectNameInput;
	private Text pathInput;
	private Button useDefaultCheck;
	private Label pathLabel;
	private Button pathSelector;

	private String curstomPath = ""; //$NON-NLS-1$
	private Label codeLabel;
	private Text codeInput;
	private Text destInput;
	private Composite composite;
	private Composite composite_1;
	private Text sourceInput;
	private Button sourceSelector;
	private Label sourceLabel;
	private Label destLabel;

	/**
	 * Create the wizard.
	 */
	public NewProjectWizardPage()
	{
		super("wizardPage"); //$NON-NLS-1$
		setTitle("��ͼ��Ŀ");
		setDescription("����һ����ͼ��Ŀ");
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));

		composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_1.setLayout(new GridLayout(2, false));

		Label projectNameLabel = new Label(composite_1, SWT.NONE);
		projectNameLabel.setText("��Ŀ����");

		projectNameInput = new Text(composite_1, SWT.BORDER);
		projectNameInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		projectNameInput.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				checkProjectName();
			}
		});

		composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(3, false));

		useDefaultCheck = new Button(composite, SWT.CHECK);
		useDefaultCheck.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		useDefaultCheck.setSelection(true);
		useDefaultCheck.setText("ʹ��ȱʡλ��");
		useDefaultCheck.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				boolean pathEnabled = !useDefaultCheck.getSelection();

				if (pathEnabled)
				{
					pathInput.setText(curstomPath);
				}

				pathLabel.setEnabled(pathEnabled);
				pathInput.setEnabled(pathEnabled);
				pathSelector.setEnabled(pathEnabled);

				if (!pathEnabled)
				{
					pathInput.setText(getWorkspacePath() + File.separatorChar + projectNameInput.getText());
				}

				checkProjectName();
				checkProjectPath();
			}
		});

		pathLabel = new Label(composite, SWT.NONE);
		pathLabel.setEnabled(false);
		pathLabel.setText("λ�ã�");

		pathInput = new Text(composite, SWT.BORDER);
		pathInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		pathInput.setEnabled(false);
		pathInput.setText(getWorkspacePath());
		pathInput.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				if (pathInput.isEnabled())
				{
					curstomPath = pathInput.getText();
					checkProjectPath();
				}
			}
		});

		pathSelector = new Button(composite, SWT.NONE);
		pathSelector.setEnabled(false);
		pathSelector.setText("  ���...  ");
		pathSelector.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				DirectoryDialog selectDialog = new DirectoryDialog(getShell());
				String path = selectDialog.open();
				if (path != null && !path.isEmpty())
				{
					pathInput.setText(path);
				}
			}
		});

		Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		group.setText("��Ŀ����");
		GridLayout gl_group = new GridLayout(3, false);
		gl_group.marginBottom = 10;
		gl_group.marginWidth = 10;
		group.setLayout(gl_group);

		sourceLabel = new Label(group, SWT.NONE);
		sourceLabel.setText("Э���ļ��У�");

		sourceInput = new Text(group, SWT.BORDER);
		sourceInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		sourceInput.setText("");

		sourceSelector = new Button(group, SWT.NONE);
		sourceSelector.setText("  ���...  ");
		sourceSelector.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				DirectoryDialog selectDialog = new DirectoryDialog(getShell());
				String path = selectDialog.open();
				if (path != null && !path.isEmpty())
				{
					sourceInput.setText(path);
				}
			}
		});

		codeLabel = new Label(group, SWT.NONE);
		codeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		codeLabel.setText("�����ļ��У�");

		codeInput = new Text(group, SWT.BORDER);
		codeInput.setText("");
		codeInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button codeSelector = new Button(group, SWT.NONE);
		codeSelector.setText("  ���...  ");
		codeSelector.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				DirectoryDialog selectDialog = new DirectoryDialog(getShell());
				String path = selectDialog.open();
				if (path != null && !path.isEmpty())
				{
					codeInput.setText(path);
				}
			}
		});

		destLabel = new Label(group, SWT.NONE);
		destLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		destLabel.setText("����ļ��У�");

		destInput = new Text(group, SWT.BORDER);
		destInput.setText("");
		destInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button destSelector = new Button(group, SWT.NONE);
		destSelector.setText("  ���...  ");
		destSelector.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				DirectoryDialog selectDialog = new DirectoryDialog(getShell());
				String path = selectDialog.open();
				if (path != null && !path.isEmpty())
				{
					destInput.setText(path);
				}
			}
		});
		
		setPageComplete(false);
	}

	/**
	 * ��ȡ��Ŀ����
	 * 
	 * @return
	 */
	public String getProjectName()
	{
		return projectNameInput.getText();
	}

	/**
	 * ��ȡ��Ŀ·��
	 * 
	 * @return
	 */
	public String getProjectPath()
	{
		if (useDefaultCheck.getSelection())
		{
			return null;
		}
		return pathInput.getText();
	}
	
	/**
	 * ��ȡЭ��Ŀ¼
	 * @return
	 */
	public String getSourcePath()
	{
		return sourceInput.getText();
	}
	
	/**
	 * ��ȡ����Ŀ¼
	 * @return
	 */
	public String getCodePath()
	{
		return codeInput.getText();
	}
	
	/**
	 * ��ȡ���Ŀ¼
	 * @return
	 */
	public String getBinPath()
	{
		return destInput.getText();
	}

	/**
	 * ��ȡ�����ռ�Ŀ¼
	 * 
	 * @return
	 */
	private String getWorkspacePath()
	{
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
	}

	/**
	 * �����Ŀ����
	 * 
	 * @return
	 */
	private void checkProjectName()
	{
		String projectName = projectNameInput.getText();

		if (projectName == null || projectName.isEmpty())
		{
			setErrorMessage(null);
			setPageComplete(false);
			return;
		}
		else
		{
			String errorChars = "\\/:*?\"<>|"; //$NON-NLS-1$
			for (int i = 0; i < errorChars.length(); i++)
			{
				if (projectName.indexOf(errorChars.charAt(i)) != -1)
				{
					setErrorMessage("��Ŀ���Ʋ��ܰ���" + errorChars.charAt(i));
					setPageComplete(false);
					return;
				}
			}

			if (ResourcesPlugin.getWorkspace().getRoot().getProject(projectName).exists())
			{
				setErrorMessage("�����ռ����Ѵ��ھ��и����Ƶ���Ŀ��");
				setPageComplete(false);
				return;
			}
		}

		if (useDefaultCheck.getSelection())
		{
			pathInput.setText(getWorkspacePath() + File.separatorChar + projectName);
		}

		setErrorMessage(null);
		setPageComplete(true);
	}

	/**
	 * �����Ŀ·��
	 */
	private void checkProjectPath()
	{
		String projectPath = pathInput.getText();

		if (projectPath == null || projectPath.isEmpty())
		{
			setErrorMessage("����ָ����Ŀλ��Ŀ¼");
			setPageComplete(false);
			return;
		}
		else
		{
			File dir = new File(projectPath);
			if (dir.exists() && !dir.isDirectory())
			{
				setErrorMessage("\"" + projectPath + "\" " + "������Ч��Ŀ¼��"); //$NON-NLS-1$ //$NON-NLS-2$
				setPageComplete(false);
				return;
			}

			File file = dir;
			while (file != null && !file.exists())
			{
				file = file.getParentFile();
			}

			if (file == null)
			{
				setErrorMessage("\"" + projectPath + "\" " + "������Ч��λ�á�"); //$NON-NLS-1$ //$NON-NLS-2$
				setPageComplete(false);
				return;
			}
		}

		setErrorMessage(null);
		setPageComplete(true);
	}
}
