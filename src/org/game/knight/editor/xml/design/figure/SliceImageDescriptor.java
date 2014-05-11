package org.game.knight.editor.xml.design.figure;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.DeviceResourceDescriptor;
import org.eclipse.swt.graphics.Device;

public class SliceImageDescriptor extends DeviceResourceDescriptor
{
	private IFile file;

	private boolean slice;
	private int left;
	private int top;
	private int right;
	private int bottom;

	public SliceImageDescriptor(IFile file, boolean slice, int l, int t, int r, int b)
	{
		this.file = file;

		this.slice = slice;
		this.left = l;
		this.top = t;
		this.right = r;
		this.bottom = b;
	}

	@Override
	public Object createResource(Device device)
	{
		return new SliceImage(file, slice, left, top, right, bottom);
	}

	@Override
	public void destroyResource(Object previouslyCreatedObject)
	{
		((SliceImage) previouslyCreatedObject).dispose();
	}

	@Override
	public int hashCode()
	{
		if(slice)
		{
			return file.hashCode() ^ (Integer.MAX_VALUE & left ^ top ^ right ^ bottom);
		}
		else
		{
			return file.hashCode() ^ Integer.MIN_VALUE;
		}
	}

	@Override
	public boolean equals(Object arg0)
	{
		if (arg0 instanceof SliceImageDescriptor)
		{
			SliceImageDescriptor desc = (SliceImageDescriptor) arg0;
			if(desc.file.equals(file) && desc.slice==slice)
			{
				if(desc.slice)
				{
					return (desc.left == left) && (desc.top == top) && (desc.right == right) && (desc.bottom == bottom);
				}
				else
				{
					return true;
				}
			}
		}
		return false;
	}

}
