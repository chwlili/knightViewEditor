package org.game.knight;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

public class PluginResource
{
	public static Image getIcon(String name)
	{
		name="/icons/"+name;
		
		ImageRegistry imgs=JFaceResources.getImageRegistry();
		if(imgs.get(name)==null)
		{
			imgs.put(name, ImageDescriptor.createFromFile(PluginResource.class, name));
		}
		return imgs.get(name);
	}
	
	public static Color getColor(int r,int g,int b)
	{
		String key=r+","+g+","+b;
		
		ColorRegistry colors=JFaceResources.getColorRegistry();
		if(colors.get(key)==null)
		{
			colors.put(key, new RGB(r, g, b));
		}
		return colors.get(key);
	}
}
