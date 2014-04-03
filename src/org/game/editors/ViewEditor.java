package org.game.editors;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.game.refactor.FileRef;
import org.game.refactor.IdDef;
import org.game.refactor.IdRef;
import org.game.views.search.SearchIdRefAction;

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
		Point range=getSourceViewer().getSelectedRange();
		if(range.x!=offset || range.y!=length)
		{
			markInNavigationHistory();
			
			getSourceViewer().setSelectedRange(offset, length);
			getSourceViewer().revealRange(offset, length);
		}
	}
	
	@Override
	protected void editorContextMenuAboutToShow(IMenuManager menu)
	{
		int offset=getSourceViewer().getSelectedRange().x;
		
		DomManager manager=DomManager.getDomManager(getSourceViewer().getDocument());

		//ID引用
		IdRef idRef=manager.getIdRef(offset);
		//文件引用
		FileRef fileRef=manager.getFileRef(offset);
		//ID定义
		IdDef idDef=manager.getIdDef(offset);
		
		//打开目标声明动作
		if(idRef!=null)
		{
			menu.add(new LookIdAction(idRef.getTarget()));
		}
		
		//打开目标文件动作
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
			
			menu.add(new LookFileAction(file));
		}

		//打开目标文件动作
		if(fileRef!=null)
		{
			menu.add(new LookFileAction(fileRef));
		}
		
		super.editorContextMenuAboutToShow(menu);
		
		menu.remove(ITextEditorActionConstants.UNDO);
		menu.remove(ITextEditorActionConstants.SAVE);
		menu.remove(ITextEditorActionConstants.REVERT);
		//menu.remove(ITextEditorActionConstants.SHOW_IN);
		menu.remove(ITextEditorActionConstants.SHIFT_LEFT);
		menu.remove(ITextEditorActionConstants.SHIFT_RIGHT);
		
		//重命名ID
		if(idDef!=null)
		{
			menu.appendToGroup(ITextEditorActionConstants.GROUP_COPY, new RenameIdAction(idDef));
		}
		else
		{
			IdDef def=idRef.getTarget();
			if(def!=null)
			{
				menu.appendToGroup(ITextEditorActionConstants.GROUP_COPY, new RenameIdAction(def));
			}
		}
		
		//查找ID引用
		if(idDef!=null)
		{
			menu.appendToGroup(ITextEditorActionConstants.GROUP_COPY, new SearchIdRefAction(idDef));
		}
	}
}
