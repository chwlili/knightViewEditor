package org.game.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;

public class ViewFile
{
//	private File file;
//	private Document domcument;
//	
//	
//	public void open(IFile file)
//	{
//		
//	}
//	
//	
//
//	// ---------------------------------------------------------------------------------------------------------------
//	//
//	// ���ļ�,����ʵ������
//	//
//	// ---------------------------------------------------------------------------------------------------------------
//
//	private ArrayList<ViewFile> xmls = new ArrayList<ViewFile>();
//	private Hashtable<ViewFile, Attribute> xmlNodes = new Hashtable<ViewFile, Attribute>();
//	private Hashtable<String, ViewFile> imgs = new Hashtable<String, ViewFile>();
//	private Hashtable<ViewFile, Attribute> imgNodes = new Hashtable<ViewFile, Attribute>();
//	private Hashtable<String, ViewFile> mp3s = new Hashtable<String, ViewFile>();
//	private Hashtable<ViewFile, Attribute> mp3Nodes = new Hashtable<ViewFile, Attribute>();
//	private Hashtable<String, ViewFile> swfs = new Hashtable<String, ViewFile>();
//	private Hashtable<ViewFile, Attribute> swfNodes = new Hashtable<ViewFile, Attribute>();
//	private Hashtable<String, ViewFile> urls = new Hashtable<String, ViewFile>();
//	private Hashtable<ViewFile, Attribute> urlNodes = new Hashtable<ViewFile, Attribute>();
//
//	/**
//	 * ��
//	 */
//	public void open(ViewExport manager)
//	{
//		if (!isCfg)
//		{
//			return;
//		}
//
//		try
//		{
//			domcument = (new SAXReader()).read(file);
//		}
//		catch (DocumentException e)
//		{
//		}
//
//		if (domcument == null)
//		{
//			domcument = DocumentHelper.createDocument();
//			domcument.addElement("xml");
//
//			GamePacker.warning("��ͼ����ʧ��", getInnerPath());
//		}
//
//		String name = domcument.getRootElement().attributeValue("name");
//		if (name != null && name.length() > 0)
//		{
//			bagName = name;
//		}
//		else
//		{
//			bagName = "";
//		}
//
//		String val = domcument.getRootElement().attributeValue("preload");
//		if (val != null)
//		{
//			try
//			{
//				preloadType = Integer.parseInt(val);
//			}
//			catch(Throwable e)
//			{
//				GamePacker.error("preload����ʧ��:"+innerPath);
//			}
//		}
//
//		findXmls(domcument, manager);
//		findImgs(domcument, manager);
//		findMp3s(domcument, manager);
//		findSwfs(domcument, manager);
//		findURLs(domcument, manager);
//		
//		texts=(Element) domcument.getRootElement().selectSingleNode("texts");
//		if(texts!=null)
//		{
//			texts.detach();
//		}
//	}
//
//	/**
//	 * ��������
//	 * 
//	 * @param dom
//	 */
//	private void findXmls(Document dom, ViewExport manager)
//	{
//		@SuppressWarnings("rawtypes")
//		List list = dom.getRootElement().selectNodes("depends/depend");
//
//		for (int i = 0; i < list.size(); i++)
//		{
//			Element node = (Element) list.get(i);
//
//			Attribute att = node.attribute("src");
//			String src = att.getStringValue();
//
//			if (src != null)
//			{
//				ViewFile file = manager.getViewBy(PathUtil.getAbsPath(innerDir, src));
//				if (file == null)
//				{
//					GamePacker.warning("��ͼ������Ч��  " + node.asXML(), getInnerPath());
//					continue;
//				}
//
//				xmls.add(file);
//				xmlNodes.put(file, att);
//			}
//		}
//	}
//
//	/**
//	 * ����ͼ��
//	 * 
//	 * @param dom
//	 */
//	private void findImgs(Document dom, ViewExport manager)
//	{
//		@SuppressWarnings("rawtypes")
//		List list = dom.getRootElement().selectNodes("bitmaps/bitmap");
//
//		for (int i = 0; i < list.size(); i++)
//		{
//			Element node = (Element) list.get(i);
//
//			String id = node.attributeValue("id");
//			Attribute att = node.attribute("src");
//			String ref = att.getStringValue();
//
//			if (id != null && ref != null)
//			{
//				ViewFile file = manager.getViewBy(PathUtil.getAbsPath(innerDir, ref));
//				if (file == null)
//				{
//					GamePacker.warning("IMG��Դ��Ч��  " + node.asXML(), getInnerPath());
//					continue;
//				}
//
//				imgs.put(id, file);
//				imgNodes.put(file, att);
//			}
//		}
//	}
//
//	/**
//	 * ������Ч
//	 * 
//	 * @param dom
//	 */
//	private void findMp3s(Document dom, ViewExport manager)
//	{
//		@SuppressWarnings("rawtypes")
//		List list = dom.getRootElement().selectNodes("sounds/sound");
//
//		for (int i = 0; i < list.size(); i++)
//		{
//			Element node = (Element) list.get(i);
//
//			String id = node.attributeValue("id");
//			Attribute att = node.attribute("src");
//			String ref = att.getStringValue();
//
//			if (id != null && ref != null)
//			{
//				ViewFile file = manager.getViewBy(PathUtil.getAbsPath(innerDir, ref));
//				if (file == null)
//				{
//					GamePacker.warning("MP3��Դ��Ч��  " + node.asXML(), getInnerPath());
//					continue;
//				}
//
//				mp3s.put(id, file);
//				mp3Nodes.put(file, att);
//			}
//		}
//	}
//
//	/**
//	 * ����SWF
//	 * 
//	 * @param dom
//	 */
//	private void findSwfs(Document dom, ViewExport manager)
//	{
//		@SuppressWarnings("rawtypes")
//		List list = dom.getRootElement().selectNodes("swfs/swf");
//
//		for (int i = 0; i < list.size(); i++)
//		{
//			Element node = (Element) list.get(i);
//
//			String id = node.attributeValue("id");
//			Attribute att = node.attribute("src");
//			String ref = att.getStringValue();
//
//			if (id != null && ref != null)
//			{
//				ViewFile file = manager.getViewBy(PathUtil.getAbsPath(innerDir, ref));
//				if (file == null)
//				{
//					GamePacker.warning("SWF��Դ��Ч��  " + node.asXML(), getInnerPath());
//					continue;
//				}
//
//				swfs.put(id, file);
//				swfNodes.put(file, att);
//			}
//		}
//	}
//
//	/**
//	 * ����URL
//	 * 
//	 * @param dom
//	 */
//	private void findURLs(Document dom, ViewExport manager)
//	{
//		@SuppressWarnings("rawtypes")
//		List list = dom.getRootElement().selectNodes("urls/url");
//
//		for (int i = 0; i < list.size(); i++)
//		{
//			Element node = (Element) list.get(i);
//
//			String id = node.attributeValue("id");
//			Attribute att = node.attribute("src");
//			String ref = att.getStringValue();
//
//			if (id != null && ref != null)
//			{
//				ViewFile file = manager.getViewBy(PathUtil.getAbsPath(innerDir, ref));
//				if (file == null)
//				{
//					GamePacker.warning("URL��Դ��Ч��  " + node.asXML(), getInnerPath());
//					continue;
//				}
//
//				urls.put(id, file);
//				urlNodes.put(file, att);
//			}
//		}
//	}
}
