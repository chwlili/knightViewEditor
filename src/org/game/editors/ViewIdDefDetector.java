package org.game.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.game.refactor.IdDef;

public class ViewIdDefDetector implements IHyperlinkDetector
{
	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks)
	{
		IdDef def=DomManager.getDomManager(textViewer.getDocument()).getIdDef(region.getOffset());
		if(def!=null)
		{
			IRegion linkRegion=new Region(def.getOffset(),def.getLength());
			
			return new IHyperlink[]{new IDDefHyperlink(def,linkRegion)};
		}
		
		return null;
	}

	private class IDDefHyperlink implements IHyperlink
	{
		private IdDef def;
		private IRegion region;
		
		public IDDefHyperlink(IdDef def,IRegion region)
		{
			this.def=def;
			this.region=region;
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
			return null;
		}

		@Override
		public void open()
		{
			IFile file=def.getFile();
			String ext=file.getFileExtension().toLowerCase();
			if(ext.equals("xml"))
			{
				try
				{
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					ViewEditor editor=(ViewEditor) page.openEditor(new FileEditorInput(file), ViewEditor.ID);
					
					editor.selectRange(def.getOffset(), def.getLength());
				}
				catch (PartInitException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
