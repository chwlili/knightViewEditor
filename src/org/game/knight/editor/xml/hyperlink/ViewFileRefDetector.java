package org.game.knight.editor.xml.hyperlink;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
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
import org.game.knight.ast.FileRef;
import org.game.knight.editor.img.ImgEditor;
import org.game.knight.editor.swf.SwfEditor;
import org.game.knight.editor.xml.DomManager;
import org.game.knight.editor.xml.ViewEditor;

public class ViewFileRefDetector implements IHyperlinkDetector
{

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks)
	{
		FileRef ref = DomManager.getDomManager(textViewer.getDocument()).getFileRef(region.getOffset());
		if (ref != null)
		{
			return new IHyperlink[] { new InnerHyperLink(ref) };
		}

		return null;
	}

	private class InnerHyperLink implements IHyperlink
	{
		private FileRef ref;
		private IRegion region;

		public InnerHyperLink(FileRef ref)
		{
			this.ref = ref;
			this.region = new Region(ref.getStart(), ref.getLength());
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
			return ref.getText();
		}

		@Override
		public void open()
		{
			IFile owner = ref.getFile();

			IFile file = owner.getProject().getFile(new Path("/views/" + ref.getTargetURL()));
			if (file.exists())
			{
				String ext = file.getFileExtension().toLowerCase();
				if (ext.equals("xml"))
				{
					try
					{
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						page.openEditor(new FileEditorInput(file), ViewEditor.ID);
					}
					catch (PartInitException e)
					{
						e.printStackTrace();
					}
				}
				else if (ext.equals("png") || ext.equals("jpg"))
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
				else if (ext.equals("swf"))
				{
					try
					{
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						page.openEditor(new FileEditorInput(file), SwfEditor.ID);
					}
					catch (PartInitException e)
					{
						e.printStackTrace();
					}
				}
			}
			else
			{
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "文件未找到！", "\"" + ref.getText() + "\" 未找到");
			}
		}

	}

}
