package org.game.editors;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.game.refactor.FileRef;
import org.game.refactor.IdDef;
import org.game.refactor.IdRef;
import org.game.refactor.MoveFileAction;
import org.game.views.search.SearchFileRefAction;
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

			markInNavigationHistory();
		}
	}
	
	@Override
	protected void editorContextMenuAboutToShow(IMenuManager menu)
	{
		menu.add(new Separator("link"));
		menu.add(new Separator("copy"));
		menu.add(new Separator("edit"));
		menu.add(new Separator("find"));
		menu.add(new Separator("prop"));
		
		
		int offset=getSourceViewer().getSelectedRange().x;
		
		DomManager manager=DomManager.getDomManager(getSourceViewer().getDocument());

		//ID����
		IdRef idRef=manager.getIdRef(offset);
		//�ļ�����
		FileRef fileRef=manager.getFileRef(offset);
		//ID����
		IdDef idDef=manager.getIdDef(offset);
		
		//�۽���ID
		if(idRef!=null)
		{
			menu.appendToGroup("link",new LookIdAction(idRef.getTarget()));
		}
		
		//�۽����ļ�
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

			menu.appendToGroup("link",new LookFileAction(file));
		}
		//�۽����ļ�
		if(fileRef!=null)
		{
			menu.appendToGroup("link",new LookFileAction(fileRef));
		}
		

		//����,����,ճ��
		if (isEditable()) 
		{
			addAction(menu, "copy", ITextEditorActionConstants.CUT);
			addAction(menu, "copy", ITextEditorActionConstants.COPY);
			addAction(menu, "copy", ITextEditorActionConstants.PASTE);
		} 
		else 
		{
			addAction(menu, "copy", ITextEditorActionConstants.COPY);
		}
		
		//������ID
		if(idDef!=null)
		{
			menu.appendToGroup("edit", new RenameIdAction(idDef));
		}
		else if(idRef!=null)
		{
			IdDef def=idRef.getTarget();
			if(def!=null)
			{
				menu.appendToGroup("edit", new RenameIdAction(def));
			}
		}
		//�������ļ�
		if(fileRef!=null)
		{
			menu.appendToGroup("edit", new MoveFileAction(fileRef));
			menu.appendToGroup("edit", new RenameFileAction(fileRef));
		}
		
		//����ID����
		if(idDef!=null)
		{
			menu.appendToGroup("find", new SearchIdRefAction(idDef));
		}
		else if(idRef!=null)
		{
			IdDef def=idRef.getTarget();
			if(def!=null)
			{
				menu.appendToGroup("find", new SearchIdRefAction(def));
			}
		}
		//�����ļ�����
		if(fileRef!=null)
		{
			menu.appendToGroup("find", new SearchFileRefAction(fileRef));
		}
	}
}
