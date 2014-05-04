package org.game.knight.editor.xml.design;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;

public class GefDropListener extends AbstractTransferDropTargetListener
{
	public GefDropListener(EditPartViewer viewer, Transfer xfer)
	{
		super(viewer, xfer);
	}

	public GefDropListener(EditPartViewer viewer)
	{
		super(viewer, FileTransfer.getInstance());
	}

	@Override
	protected void updateTargetRequest()
	{

	}
	
	
	@Override
	public void dragEnter(DropTargetEvent event)
	{
		event.detail=DND.DROP_MOVE;
	}
	
	@Override
	public void drop(DropTargetEvent event)
	{
		event.detail=DND.DROP_MOVE;
	}
	
	@Override
	public void dropAccept(DropTargetEvent event)
	{
		super.dropAccept(event);
	}
	
	@Override
	protected void handleDragOver()
	{
		// TODO 自动生成的方法存根
		super.handleDragOver();
	}
	
	
	
	@Override  
	protected Request createTargetRequest() {  
	    CreateRequest request = new CreateRequest();  
	    request.setFactory(new CreationFactory()
		{
			
			@Override
			public Object getObjectType()
			{
				return String.class;
			}
			
			@Override
			public Object getNewObject()
			{
				return "xx";
			}
		});  
	    return request;  
	}  
}
