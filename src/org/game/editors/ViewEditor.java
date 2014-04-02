package org.game.editors;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.game.refactor.FileRef;
import org.game.refactor.IdDef;
import org.game.refactor.IdRef;

public class ViewEditor extends TextEditor
{
	public static final String ID="org.game.editors.XML";
	
	public ViewEditor()
	{
		super();
		
		setSourceViewerConfiguration(new ViewEditorConfig());
		
		setDocumentProvider(new ViewXmlProvider());
	}

	public void dispose()
	{
		super.dispose();
	}
	
	public void selectRange(int offset,int length)
	{
		getSourceViewer().setSelectedRange(offset, length);
		getSourceViewer().revealRange(offset, length);
	}
	
	@Override
	protected void editorContextMenuAboutToShow(IMenuManager menu)
	{
		int offset=getSourceViewer().getSelectedRange().x;
		
		DomManager manager=DomManager.getDomManager(getSourceViewer().getDocument());
		
		//����ID����
		IdRef idRef=manager.getIdRef(offset);
		if(idRef!=null)
		{
			menu.add(new LockToIdAction(idRef.getTarget()));
		}
		
		//����ͼ������ID���õ�ͼ���ļ�
		if(idRef!=null && idRef.isBitmapRef())
		{
			FileRef file=null;
			IdDef target=null;
			
			target=idRef.getTarget();
			if(target!=null && (target.getRef() instanceof IdRef))
			{
				idRef=(IdRef)target.getRef();
			}
			
			target=idRef.getTarget();
			if(target!=null && (target.getRef() instanceof FileRef))
			{
				file=(FileRef)target.getRef();
			}
			
			menu.add(new LockToFileAction(file));
		}

		//�����ļ�����
		FileRef fileRef=manager.getFileRef(offset);
		if(fileRef!=null)
		{
			menu.add(new LockToFileAction(fileRef));
		}
		
		super.editorContextMenuAboutToShow(menu);
		
		menu.remove(ITextEditorActionConstants.UNDO);
		menu.remove(ITextEditorActionConstants.SAVE);
		menu.remove(ITextEditorActionConstants.REVERT);
		//menu.remove(ITextEditorActionConstants.SHOW_IN);
		menu.remove(ITextEditorActionConstants.SHIFT_LEFT);
		menu.remove(ITextEditorActionConstants.SHIFT_RIGHT);
		
		//������ID
		IdDef idDef=manager.getIdDef(offset);
		if(idDef!=null)
		{
			menu.appendToGroup(ITextEditorActionConstants.GROUP_COPY, new RenameIdAction(idDef));
		}
		
		//����ID����
		if(idDef!=null)
		{
			menu.appendToGroup(ITextEditorActionConstants.GROUP_COPY, new SearchIdAction(idDef));
		}
	}
}
