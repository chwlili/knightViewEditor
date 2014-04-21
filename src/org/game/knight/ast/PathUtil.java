package org.game.knight.ast;

public class PathUtil
{

	public static String toAbsPath(String currPath, String refPath)
	{
		currPath = currPath.replaceAll("\\\\", "/").replaceAll("/+", "/");
		refPath = refPath.replaceAll("\\\\", "/").replaceAll("/+", "/");

		if (refPath.charAt(0) == '/')
		{
			currPath = "/";
			refPath = refPath.substring(1);
		}
		else
		{
			currPath = currPath.substring(0, currPath.lastIndexOf('/'));
		}

		String[] parts = refPath.split("/");
		for (String part : parts)
		{
			if (part.equals("."))
			{
				continue;
			}
			else if (part.equals(".."))
			{
				if (currPath.equals("/"))
				{
					currPath = null;
					break;
				}
				else
				{
					int index = currPath.lastIndexOf("/");
					if (index == 0)
					{
						currPath = "/";
					}
					else
					{
						currPath = currPath.substring(0, index);
					}
				}
			}
			else
			{
				if (currPath.equals("/"))
				{
					currPath = "/" + part;
				}
				else
				{
					currPath += "/" + part;
				}
			}
		}

		return currPath;
	}
}
