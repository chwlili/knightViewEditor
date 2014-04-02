package org.game.editors;

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
import org.game.refactor.FileRef;

public class ViewFileRefDetector implements IHyperlinkDetector
{

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks)
	{
		FileRef ref=DomManager.getDomManager(textViewer.getDocument()).getFileRef(region.getOffset());
		if(ref!=null)
		{
			return new IHyperlink[]{new InnerHyperLink(ref)};
		}
		
		return null;
	}
	
	private class InnerHyperLink implements IHyperlink
	{
		private FileRef ref;
		private IRegion region;
		
		public InnerHyperLink(FileRef ref)
		{
			this.ref=ref;
			this.region=new Region(ref.start, ref.stop-ref.start+1);
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
			return ref.text;
		}

		@Override
		public void open()
		{
			IFile owner=ref.owner;
			
			IFile file=owner.getProject().getFile(new Path("/views/"+ref.filePath));
			if(file.exists())
			{
				String ext=file.getFileExtension().toLowerCase();
				if(ext.equals("xml"))
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
				else if(ext.equals("png")||ext.equals("jpg"))
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
			}
			else
			{
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "�ļ�δ�ҵ���", "\""+ref.text+"\" δ�ҵ�");
			}
		}
		
	}

}
