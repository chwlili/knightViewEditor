package org.game.knight.editor.xml;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlinkPresenter;
import org.eclipse.jface.text.hyperlink.MultipleHyperlinkPresenter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.RGB;
import org.game.knight.editor.xml.hyperlink.ViewFileRefDetector;
import org.game.knight.editor.xml.hyperlink.ViewIdDefDetector;
import org.game.knight.editor.xml.hyperlink.ViewIdRefDetector;

public class ViewEditorConfig extends SourceViewerConfiguration
{
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer)
	{
		PresentationReconciler reconciler=new PresentationReconciler();
		DefaultDamagerRepairer dr=new DefaultDamagerRepairer(new ViewXmlScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		return reconciler;
	}
	
	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType)
	{
		return super.getTextHover(sourceViewer, contentType);
	}
	
	@Override
	public IHyperlinkPresenter getHyperlinkPresenter(ISourceViewer sourceViewer)
	{
		return new MultipleHyperlinkPresenter(new RGB(0,0,255));
	}
	
	@Override
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer)
	{
		return new IHyperlinkDetector[]{new ViewFileRefDetector(),new ViewIdRefDetector(),new ViewIdDefDetector()};
	}
}
