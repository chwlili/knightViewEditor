package org.game.knight.editor.xml.hyperlink;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.game.knight.ast.FileAstManager;
import org.game.knight.ast.FileRef;
import org.game.knight.ast.IdDef;
import org.game.knight.ast.IdRef;
import org.game.knight.editor.img.ImgEditor;
import org.game.knight.editor.xml.DomManager;
import org.game.knight.editor.xml.ViewEditor;

public class ViewIdRefDetector implements IHyperlinkDetector
{

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks)
	{
		IdRef ref = DomManager.getDomManager(textViewer.getDocument()).getIdRef(region.getOffset());
		if (ref != null)
		{
			IRegion linkRegion = new Region(ref.getOffset(), ref.getLength());

			if (ref.isBitmapRef())
			{
				return new IHyperlink[] { new IDRefHyperlink(ref, linkRegion, 1), new IDRefHyperlink(ref, linkRegion, 2) };
			}
			else
			{
				return new IHyperlink[] { new IDRefHyperlink(ref, linkRegion, 1) };
			}
		}

		return null;
	}

	private class IDRefHyperlink implements IHyperlink
	{
		private IdRef ref;
		private IRegion region;
		private int type;

		public IDRefHyperlink(IdRef ref, IRegion region, int type)
		{
			this.ref = ref;
			this.region = region;
			this.type = type;
		}

		@Override
		public IRegion getHyperlinkRegion()
		{
			return region;
		}

		@Override
		public String getTypeLabel()
		{
			return null;
		}

		@Override
		public String getHyperlinkText()
		{
			if (type == 2)
			{
				return "打开文件";
			}
			return "打开定义";
		}

		@Override
		public void open()
		{
			IdDef idDef = ref.getTarget();
			if (idDef == null)
			{
				return;
			}

			if (type == 1)
			{
				IFile file = idDef.getFile();
				String ext = file.getFileExtension().toLowerCase();
				if (ext.equals("xml"))
				{
					try
					{
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						ViewEditor editor = (ViewEditor) page.openEditor(new FileEditorInput(file), ViewEditor.ID);

						editor.selectRange(idDef.getOffset(), idDef.getLength());
					}
					catch (PartInitException e)
					{
						e.printStackTrace();
					}
				}
				return;
			}

			if (type == 2 && idDef.getRef() != null)
			{
				if (idDef.getRef() instanceof IdRef)
				{
					IdRef idRef = (IdRef) idDef.getRef();

					idDef = idRef.getTarget();
				}

				if (idDef.getRef() != null)
				{
					if (idDef.getRef() instanceof FileRef)
					{
						FileRef fileRef = (FileRef) idDef.getRef();
						IFile file = FileAstManager.getSourceFile(idDef.getFile().getProject(), fileRef.getTargetURL());
						if (file != null)
						{
							try
							{
								IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
								page.openEditor(new FileEditorInput(file), ImgEditor.ID);
							}
							catch (PartInitException e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "文件未找到！", "\"" + fileRef.getText() + "\" 未找到");
						}
					}
				}
				else
				{
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "文件未找到！", "文件未找到！");
				}
			}
		}
	}

}
