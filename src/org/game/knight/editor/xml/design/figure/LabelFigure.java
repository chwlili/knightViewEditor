package org.game.knight.editor.xml.design.figure;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.DeviceResourceDescriptor;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.game.knight.editor.xml.design.TagHelper;

public class LabelFigure extends FlowPage
{
	private String text;
	private boolean html;

	private ResourceManager manager;
	private ArrayList<DeviceResourceDescriptor> descs = new ArrayList<DeviceResourceDescriptor>();

	public LabelFigure(ResourceManager manager)
	{
		this.manager = manager;
	}

	/**
	 * 获取文字
	 * 
	 * @return
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * 是否html
	 * 
	 * @return
	 */
	public boolean getHtml()
	{
		return html;
	}

	/**
	 * 设置文字
	 * 
	 * @param text
	 */
	public void setText(String text,boolean html)
	{
		boolean changed=false;
		if (text == null)
		{
			text = "";
		}
		if (!text.equals(this.text))
		{
			this.text = text;
			changed=true;
		}
		if (this.html != html)
		{
			this.html = html;
			changed=true;
		}
		if(changed)
		{
			initText();
		}
	}

	/**
	 * 初始化文字
	 */
	private void initText()
	{
		if(descs.size()>0)
		{
			manager.destroy(descs.remove(0));
		}
		
		removeAll();
		
		if(getHtml())
		{
			Format format = null;
			StringBuilder texts = null;
	
			ArrayList<Object> segments = partHTML(this.text);
			while (segments.size() > 0)
			{
				if (segments.get(0) instanceof Format)
				{
					format = (Format) segments.remove(0);
					texts = new StringBuilder();
	
					while (segments.size() > 0 && !(segments.get(0) instanceof Format))
					{
						texts.append((String) segments.remove(0));
					}
	
					FontDescriptor fontDesc = FontDescriptor.createFrom(format.font);
					ColorDescriptor colorDesc = ColorDescriptor.createFrom(format.rgb);
					
					descs.add(fontDesc);
					descs.add(colorDesc);
					
					TextFlow flow = new TextFlow(texts.toString());
					flow.setFont(manager.createFont(fontDesc));
					flow.setForegroundColor(manager.createColor(colorDesc));
					
					add(flow);
				}
			}
		}
		else
		{
			TextFlow flow=new TextFlow(getText());
			flow.setFont(getFont());
			flow.setForegroundColor(getForegroundColor());
			add(flow);
		}
	}

	private static class Format
	{
		public FontData font;
		public RGB rgb;

		public Format(FontData font, RGB rgb)
		{
			this.font = font;
			this.rgb = rgb;
		}
	}

	/**
	 * 查找下一个非空白字符
	 * 
	 * @param text
	 * @param from
	 * @param end
	 * @return
	 */
	private int findCharIndex(String text, int from, int end)
	{
		for (int i = from; i < end; i++)
		{
			if (Character.isWhitespace(text.charAt(i)) == false)
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * 查找下一个空白字符
	 * 
	 * @param text
	 * @param from
	 * @param end
	 * @return
	 */
	private int findWhitespaceIndex(String text, int from, int end)
	{
		for (int i = from; i < end; i++)
		{
			if (Character.isWhitespace(text.charAt(i)))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * 解析Html
	 * 
	 * @param text
	 * @return
	 */
	private ArrayList<Object> partHTML(String text)
	{
		Stack<FontData> fonts = new Stack<FontData>();
		Stack<RGB> rgbs = new Stack<RGB>();

		ArrayList<Object> segments = new ArrayList<Object>();

		fonts.add(new FontData("", TagHelper.pxToPoint(12), 0));
		rgbs.add(new RGB(0, 0, 0));
		segments.add(new Format(fonts.lastElement(),rgbs.lastElement()));
		
		int from = 0;
		while (from < text.length())
		{
			int tagBegin = text.indexOf("<", from);
			if (tagBegin == -1)
			{
				segments.add(text.substring(from));
				break;
			}
			else
			{
				// 保存 < 之前的字符
				if (tagBegin > from)
				{
					segments.add(text.substring(from, tagBegin));
				}
				tagBegin++;

				//
				String tagName = "";
				Hashtable<String, String> attributes = new Hashtable<String, String>();
				boolean isBeginTag = true;
				boolean isSingleTag = false;

				// 确定开始位置
				if (tagBegin < text.length() && text.charAt(tagBegin) == '/')
				{
					tagBegin++;
					isBeginTag = false;
				}

				// 确定结束位置
				int tagEnd = text.indexOf(">", tagBegin);
				if (tagEnd == -1)
				{
					from = tagBegin + 1;
					continue;
				}
				else if (tagEnd > tagBegin && tagEnd > 0 && text.charAt(tagEnd - 1) == '/')
				{
					isSingleTag = true;
					tagEnd--;
				}

				// 确定标记名称
				int nameBegin = findCharIndex(text, tagBegin, tagEnd);
				if (nameBegin == -1)
				{
					from = tagEnd + (isSingleTag ? 2 : 1);
					continue;
				}
				int nameEnd = findWhitespaceIndex(text, nameBegin, tagEnd);
				if (nameEnd == -1)
				{
					tagName = text.substring(nameBegin, tagEnd);
				}
				else
				{
					tagName = text.substring(nameBegin, nameEnd);
				}

				// 确定属性表
				int charFrom = nameEnd;
				while (charFrom != -1)
				{
					String attName = null;
					String attValue = null;

					charFrom = findCharIndex(text, charFrom, tagEnd);
					if (charFrom == -1)
					{
						break;
					}

					// attName
					int attNameBegin = charFrom;
					int attNameEnd = -1;
					for (int i = charFrom; i < tagEnd; i++)
					{
						if (text.charAt(i) == '=' || Character.isWhitespace(text.charAt(i)))
						{
							attNameEnd = i;
							break;
						}
					}
					if (attNameEnd == -1)
					{
						break;
					}
					attName = text.substring(attNameBegin, attNameEnd);

					// =
					charFrom = findCharIndex(text, attNameEnd, tagEnd);
					if (charFrom == -1)
					{
						break;
					}
					if (text.charAt(charFrom) != '=')
					{
						continue;
					}
					charFrom += 1;

					// attValue
					charFrom = findCharIndex(text, charFrom, tagEnd);
					if (charFrom == -1)
					{
						break;
					}
					int valueBegin = -1;
					int valueEnd = -1;
					if (text.charAt(charFrom) == '\'')
					{
						valueBegin = charFrom + 1;
						valueEnd = text.indexOf('\'', valueBegin);
						if (valueEnd == -1)
						{
							valueEnd = tagEnd;
						}
					}
					else if (text.charAt(charFrom) == '\"')
					{
						valueBegin = charFrom + 1;
						valueEnd = text.indexOf('\"', valueBegin);
						if (valueEnd == -1)
						{
							valueEnd = tagEnd;
						}
					}
					else
					{
						valueBegin = charFrom;
						valueEnd = findWhitespaceIndex(text, valueBegin, tagEnd);
						if (valueEnd == -1)
						{
							valueEnd = tagEnd;
						}
					}
					attValue = text.substring(valueBegin, valueEnd);

					//
					if (attName != null && attValue != null)
					{
						attributes.put(attName, attValue);
					}
				}

				// 分板字体变化
				if (tagName != null)
				{
					if (!isSingleTag)
					{
						if ("font".equals(tagName))
						{
							if (isBeginTag)
							{
								RGB defRGB = rgbs.lastElement();
								FontData defFont = fonts.lastElement();

								RGB currRGB = new RGB(defRGB.red, defRGB.green, defRGB.blue);
								FontData currFont = new FontData(defFont.getName(), defFont.getHeight(), defFont.getStyle());

								if (attributes.containsKey("font"))
								{
									currFont.setName(attributes.get("font"));
								}
								if (attributes.containsKey("size"))
								{
									try
									{
										currFont.setHeight(TagHelper.pxToPoint(Integer.parseInt(attributes.get("size"))));
									}
									catch (Exception exception)
									{
									}
								}
								if (attributes.containsKey("color"))
								{
									try
									{
										int color = Integer.parseInt(attributes.get("color").substring(1),16);
										currRGB.red = (color >>> 16) & 0xFF;
										currRGB.green = (color >>> 8) & 0xFF;
										currRGB.blue = color & 0xFF;
									}
									catch (Exception exception)
									{
									}
								}

								rgbs.add(currRGB);
								fonts.add(currFont);

								segments.add(new Format(currFont, currRGB));
							}
							else
							{
								if(rgbs.size()>1 && fonts.size()>1)
								{
									rgbs.pop();
									fonts.pop();
	
									RGB defRGB = rgbs.lastElement();
									FontData defFont = fonts.lastElement();
	
									segments.add(new Format(defFont, defRGB));
								}
							}
						}
					}
				}

				from = tagEnd + 1;
			}
		}

		return segments;
	}

	private String partHTML1(String text)
	{
		Stack<String> fonts = new Stack<String>();
		Stack<Integer> sizes = new Stack<Integer>();
		Stack<Integer> colors = new Stack<Integer>();

		Stack<Boolean> bolds = new Stack<Boolean>();
		Stack<Boolean> italics = new Stack<Boolean>();

		ArrayList<String> texts = new ArrayList<String>();

		int from = 0;
		while (from < text.length())
		{
			int tagBegin = text.indexOf("<", from);
			if (tagBegin == -1)
			{
				texts.add(text.substring(from));
				break;
			}
			else
			{
				// 保存 < 之前的字符
				if (tagBegin > from)
				{
					texts.add(text.substring(from, tagBegin));
				}
				tagBegin++;

				//
				String tagName = "";
				Hashtable<String, String> attributes = new Hashtable<String, String>();
				boolean isBeginTag = true;
				boolean isSingleTag = false;

				// 确定开始位置
				if (tagBegin < text.length() && text.charAt(tagBegin) == '/')
				{
					tagBegin++;
					isBeginTag = false;
				}

				// 确定结束位置
				int tagEnd = text.indexOf(">", tagBegin);
				if (tagEnd == -1)
				{
					from = tagBegin + 1;
					continue;
				}
				else if (tagEnd > tagBegin && tagEnd > 0 && text.charAt(tagEnd - 1) == '/')
				{
					isSingleTag = true;
					tagEnd--;
				}

				// 确定标记名称
				int nameBegin = findCharIndex(text, tagBegin, tagEnd);
				if (nameBegin == -1)
				{
					from = tagEnd + (isSingleTag ? 2 : 1);
					continue;
				}
				int nameEnd = findWhitespaceIndex(text, nameBegin, tagEnd);
				if (nameEnd == -1)
				{
					tagName = text.substring(nameBegin, tagEnd);
				}
				else
				{
					tagName = text.substring(nameBegin, nameEnd);
				}

				// 确定属性表
				int charFrom = nameEnd;
				while (charFrom != -1)
				{
					String attName = null;
					String attValue = null;

					charFrom = findCharIndex(text, charFrom, tagEnd);
					if (charFrom == -1)
					{
						break;
					}

					// attName
					int attNameBegin = charFrom;
					int attNameEnd = -1;
					for (int i = charFrom; i < tagEnd; i++)
					{
						if (text.charAt(i) == '=' || Character.isWhitespace(text.charAt(i)))
						{
							attNameEnd = i;
							break;
						}
					}
					if (attNameEnd == -1)
					{
						break;
					}
					attName = text.substring(attNameBegin, attNameEnd);

					// =
					charFrom = findCharIndex(text, attNameEnd, tagEnd);
					if (charFrom == -1)
					{
						break;
					}
					if (text.charAt(charFrom) != '=')
					{
						continue;
					}
					charFrom += 1;

					// attValue
					charFrom = findCharIndex(text, charFrom, tagEnd);
					if (charFrom == -1)
					{
						break;
					}
					int valueBegin = -1;
					int valueEnd = -1;
					if (text.charAt(charFrom) == '\'')
					{
						valueBegin = charFrom + 1;
						valueEnd = text.indexOf('\'', valueBegin);
						if (valueEnd == -1)
						{
							valueEnd = tagEnd;
						}
					}
					else if (text.charAt(charFrom) == '\"')
					{
						valueBegin = charFrom + 1;
						valueEnd = text.indexOf('\"', valueBegin);
						if (valueEnd == -1)
						{
							valueEnd = tagEnd;
						}
					}
					else
					{
						valueBegin = charFrom;
						valueEnd = findWhitespaceIndex(text, valueBegin, tagEnd);
						if (valueEnd == -1)
						{
							valueEnd = tagEnd;
						}
					}
					attValue = text.substring(valueBegin, valueEnd);

					//
					if (attName != null && attValue != null)
					{
						attributes.put(attName, attValue);
					}
				}

				from = tagEnd + 1;
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < texts.size(); i++)
		{
			sb.append(texts.get(i));
		}
		return sb.toString();
	}
}
