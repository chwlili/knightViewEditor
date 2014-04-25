package org.game.knight.editor.xml;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.game.knight.ast.ASTManager;

public class ViewXmlProvider extends FileDocumentProvider
{

	protected IDocument createDocument(Object element) throws CoreException
	{
		IDocument document = super.createDocument(element);
		
		IFileEditorInput input=(IFileEditorInput)element;
		
		ASTManager.linkDocument(document, input.getFile());
		
//		if (document != null)
//		{
//			IDocumentPartitioner partitioner = new FastPartitioner(new XMLPartitionScanner(), new String[] { XMLPartitionScanner.XML_TAG, XMLPartitionScanner.XML_COMMENT });
//			partitioner.connect(document);
//			document.setDocumentPartitioner(partitioner);
//		}
		return document;
	}
}
