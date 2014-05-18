package org.game.knight.editor.xml.design.figure;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import org.eclipse.jface.resource.DeviceResourceDescriptor;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Device;

public class HtmlTextDescriptor extends DeviceResourceDescriptor
{
	private ResourceManager manager;
	private String html;

	/**
	 * 构造函数
	 * 
	 * @param html
	 */
	public HtmlTextDescriptor(ResourceManager manager, String html)
	{
		this.manager = manager;
		this.html = html;
	}

	/**
	 * 创建资源
	 */
	@Override
	public Object createResource(Device device)
	{
		return new HtmlText(manager, html);
	}

	/**
	 * 销毁资源
	 */
	@Override
	public void destroyResource(Object previouslyCreatedObject)
	{
		((HtmlText) previouslyCreatedObject).dispose();
	}
}
