package org.game.knight.editor.xml;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.game.knight.PluginResource;
import org.game.knight.ast.AST;
import org.game.knight.ast.ASTManager;
import org.game.knight.ast.AbsTag;
import org.game.knight.ast.FileRef;
import org.game.knight.ast.IdDef;
import org.game.knight.ast.IdRef;
import org.game.knight.editor.swf.Swf;
import org.game.knight.editor.xml.action.LookFileAction;
import org.game.knight.editor.xml.action.LookIdAction;
import org.game.knight.editor.xml.action.RenameFileAction;
import org.game.knight.editor.xml.action.RenameIdAction;
import org.game.knight.refactor.MoveFileAction;
import org.game.knight.search.SearchFileRefAction;
import org.game.knight.search.SearchIdRefAction;

public class ViewEditor extends TextEditor
{
	public static final String ID = "org.game.editors.XML";

	private CTabFolder folder;
	private ViewGefEditor viewer;

	public ViewEditor()
	{
		super();

		setSourceViewerConfiguration(new ViewEditorConfig());

		setDocumentProvider(ViewEditorHelper.createDocumentProvider());
	}

	/**
	 * 选定文本范围
	 * @param offset
	 * @param length
	 */
	public void selectRange(int offset, int length)
	{
		Point range = getSourceViewer().getSelectedRange();
		if (range.x != offset || range.y != length)
		{
			markInNavigationHistory();

			getSourceViewer().setSelectedRange(offset, length);
			getSourceViewer().revealRange(offset, length);

			markInNavigationHistory();
		}

		folder.setSelection(0);
		setFocus();
	}

	/**
	 * 选定指定的标记
	 * @param tag
	 */
	public void selectTag(AbsTag tag)
	{
		if (folder.getSelectionIndex() == 0)
		{
			selectRange(tag.getOffset(), tag.getLength());
		}
	}

	/**
	 * 获取文档
	 * @return
	 */
	public IDocument getDocument()
	{
		return getSourceViewer().getDocument();
	}

	@Override
	public void createPartControl(Composite parent)
	{
		folder = new CTabFolder(parent, SWT.BOTTOM);
		folder.setTabHeight(25);
		folder.setSelectionBackground(new Color(null, 153, 180, 209));
		folder.setSimple(true);
		folder.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if(folder.getSelectionIndex()==1)
				{
					viewer.offShowScroller();
					viewer.open();
					setFocus();
				}
			}
		});

		Composite sourceBox = new Composite(folder, SWT.None);
		sourceBox.setBackground(PluginResource.getColor(153, 180, 209));
		sourceBox.setLayout(new FillLayout());
		
		Composite designBox = new Composite(folder, SWT.None);
		designBox.setLayout(new FillLayout());
		viewer = new ViewGefEditor(designBox, this);

		Composite previewBox = new Composite(folder, SWT.None);
		previewBox.setLayout(new FillLayout());
		new Swf(previewBox, SWT.NONE);

		CTabItem sourceItem = new CTabItem(folder, SWT.NONE);
		sourceItem.setText("源码视图  ");
		sourceItem.setImage(PluginResource.getIcon("viewIcon_source.gif"));
		sourceItem.setControl(sourceBox);

		CTabItem designItem = new CTabItem(folder, SWT.NONE);
		designItem.setText("设计视图  ");
		designItem.setImage(PluginResource.getIcon("viewIcon_design.gif"));
		designItem.setControl(designBox);

		CTabItem previewItem = new CTabItem(folder, SWT.NONE);
		previewItem.setText("预览视图  ");
		previewItem.setImage(PluginResource.getIcon("fileIcon_swf.gif"));
		previewItem.setControl(previewBox);

		super.createPartControl(sourceBox);

		folder.setSelection(0);
	}

	@Override
	protected void handleCursorPositionChanged()
	{
		super.handleCursorPositionChanged();

		if (outline != null && folder.getSelectionIndex() == 0)
		{
			outline.handleCursorPositionChanged(getSourceViewer().getTextWidget().getSelection().x);
		}
	}
	
	@Override
	public void setFocus()
	{
		if(folder.getSelectionIndex()!=0)
		{
			folder.setFocus();
		}
		else
		{
			super.setFocus();
		}
	}

	@Override
	protected boolean isEditorInputIncludedInContextMenu()
	{
		return false;
	}

	@Override
	protected void editorContextMenuAboutToShow(IMenuManager menu)
	{
		menu.add(new Separator("link"));
		menu.add(new Separator("copy"));
		menu.add(new Separator("edit"));
		menu.add(new Separator("find"));
		menu.add(new Separator("prop"));

		int offset = getSourceViewer().getSelectedRange().x;

		AST manager = ASTManager.getDocumentAST(getSourceViewer().getDocument());

		// ID引用
		IdRef idRef = manager.getLinks().getIdRef(offset);
		// 文件引用
		FileRef fileRef = manager.getLinks().getFileRef(offset);
		// ID定义
		IdDef idDef = manager.getLinks().getIdDef(offset);

		// 聚焦到ID
		if (idRef != null)
		{
			menu.appendToGroup("link", new LookIdAction(idRef.getTarget()));
		}

		// 聚焦到文件
		if (idRef != null && idRef.isBitmapRef())
		{
			FileRef file = null;
			IdDef target = null;

			target = idRef.getTarget();
			if (target != null && (target.getRef() instanceof IdRef))
			{
				idRef = (IdRef) target.getRef();
			}

			target = idRef.getTarget();
			if (target != null && (target.getRef() instanceof FileRef))
			{
				file = (FileRef) target.getRef();
			}

			menu.appendToGroup("link", new LookFileAction(file));
		}
		// 聚焦到文件
		if (fileRef != null)
		{
			menu.appendToGroup("link", new LookFileAction(fileRef));
		}

		// 剪切,复制,粘贴
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

		// 重命名ID
		if (idDef != null)
		{
			menu.appendToGroup("edit", new RenameIdAction(idDef));
		}
		else if (idRef != null)
		{
			IdDef def = idRef.getTarget();
			if (def != null)
			{
				menu.appendToGroup("edit", new RenameIdAction(def));
			}
		}
		// 重命名文件
		if (fileRef != null)
		{
			menu.appendToGroup("edit", new MoveFileAction(fileRef));
			menu.appendToGroup("edit", new RenameFileAction(fileRef));
		}

		// 查找ID引用
		if (idDef != null)
		{
			menu.appendToGroup("find", new SearchIdRefAction(idDef));
		}
		else if (idRef != null)
		{
			IdDef def = idRef.getTarget();
			if (def != null)
			{
				menu.appendToGroup("find", new SearchIdRefAction(def));
			}
		}
		// 查找文件引用
		if (fileRef != null)
		{
			menu.appendToGroup("find", new SearchFileRefAction(fileRef));
		}
	}

	private ViewEditorOutline outline;

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter)
	{
		if (adapter == IContentOutlinePage.class)
		{
			if (outline == null)
			{
				outline = new ViewEditorOutline(this);
			}
			return outline;
		}
		return super.getAdapter(adapter);
	}
}
