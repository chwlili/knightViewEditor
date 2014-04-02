package org.game.views;

import java.util.Hashtable;

import org.eclipse.swt.graphics.Image;

public class ResourceManager
{
	private static Hashtable<String, Image> imgs=new Hashtable<String, Image>();
	
	public static Image getIcon(String path)
	{
		if(!imgs.containsKey(path))
		{
			imgs.put(path, new Image(null, ResourceManager.class.getClassLoader().getResourceAsStream(path)));
		}
		
		return imgs.get(path);
	}
}
